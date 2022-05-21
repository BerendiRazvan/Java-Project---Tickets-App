package festival.persistence.mock;

import festival.model.Employee;
import festival.model.Location;
import festival.model.Show;
import festival.persistence.EmployeesRepository;
import festival.persistence.JdbcUtils;
import festival.persistence.RepositoryException;
import festival.persistence.ShowsRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;


@Component
public class ShowsRepositoryMock implements ShowsRepository {
    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public ShowsRepositoryMock() {
        Properties props = new Properties();

        //{festival.server.port=7899, jdbc.url=jdbc:sqlite:C:\Users\Razvan\IdeaProjects\MPP_P2_FestivalMuzica\FestivalDB, jdbc.driver=org.sqlite.JDBC}

        props.put("festival.server.port",7899);
        props.put("jdbc.url","jdbc:sqlite:C:\\Users\\Razvan\\IdeaProjects\\MPP_P2_FestivalMuzica\\FestivalDB");
        props.put("jdbc.driver","org.sqlite.JDBC");


        logger.info("Initializing ShowsDataBaseRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }


    @Override
    public void add(Show elem) {
        logger.traceEntry("Saving show {}", elem);

        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into Shows (artist_name, date_and_time, tickets_available, tickets_sold, location) values (?, ?, ?, ?, ?)")) {
            preStmt.setString(1, elem.getArtistName());
            preStmt.setTimestamp(2, Timestamp.valueOf(elem.getDataAndTime()));
            preStmt.setInt(3, elem.getTicketsAvailable());
            preStmt.setInt(4, elem.getTicketsSold());
            preStmt.setInt(5, elem.getLocation().getId().intValue());

            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instance", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();

        
    }

    @Override
    public void update(Long aLong, Show elem) {
        logger.traceEntry("Updating show with {}", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("update Shows set artist_name = ?, date_and_time = ?, tickets_available = ?, tickets_sold = ?, location = ? where id_show = ?")) {
            preStmt.setString(1, elem.getArtistName());
            preStmt.setTimestamp(2, Timestamp.valueOf(elem.getDataAndTime()));
            preStmt.setInt(3, elem.getTicketsAvailable());
            preStmt.setInt(4, elem.getTicketsSold());
            preStmt.setInt(5, elem.getLocation().getId().intValue());

            preStmt.setInt(6, aLong.intValue());

            int result = preStmt.executeUpdate();
            logger.trace("Update {} instance", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Long aLong) throws Exception {
        logger.traceEntry("Deleting show with id {}", aLong);
        Connection con = dbUtils.getConnection();
        boolean ok = false;
        for(var i:findAll()){
            if(i.getId().equals(aLong))
                ok=true;
        }
        if(!ok)
            throw new Exception("INVALID ID");
        try (PreparedStatement preStmt = con.prepareStatement("delete from Shows where id_show = ?")) {
            preStmt.setInt(1, aLong.intValue());
            int result = preStmt.executeUpdate();
            logger.trace("Delete {} instance", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public Iterable<Show> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Show> shows = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Shows inner join Locations L on L.id_location = Shows.location")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    long id = result.getInt("id_show");
                    String artistName = result.getString("artist_name");
                    LocalDateTime dateAndTime = result.getTimestamp("date_and_time").toLocalDateTime();
                    Integer available = result.getInt("tickets_available");
                    Integer sold = result.getInt("tickets_sold");
                    long idLocation = result.getInt("id_location");
                    String country = result.getString("country");
                    String city = result.getString("city");
                    Location location = new Location(country, city);
                    location.setId(idLocation);
                    Show show = new Show(artistName, dateAndTime, location, available, sold);
                    show.setId(id);
                    shows.add(show);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return shows;
    }

    @Override
    public List<String> findAllArtists() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<String> artists = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select artist_name\n" +
                "from Shows\n" +
                "group by artist_name\n" +
                "order by artist_name")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    String artistName = result.getString("artist_name");
                    artists.add(artistName);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return artists;
    }

    @Override
    public List<Show> findAllArtistShows(String artistName) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Show> shows = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Shows inner join Locations L on L.id_location = Shows.location\n" +
                "where artist_name = ? and DATETIME(date_and_time/1000, 'unixepoch') > datetime('now')" +
                "order by date_and_time asc")) {
            preStmt.setString(1, artistName);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    long id = result.getInt("id_show");
                    LocalDateTime dateAndTime = result.getTimestamp("date_and_time").toLocalDateTime();
                    Integer available = result.getInt("tickets_available");
                    Integer sold = result.getInt("tickets_sold");
                    long idLocation = result.getInt("id_location");
                    String country = result.getString("country");
                    String city = result.getString("city");
                    Location location = new Location(country, city);
                    location.setId(idLocation);
                    Show show = new Show(artistName, dateAndTime, location, available, sold);
                    show.setId(id);

                    shows.add(show);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return shows;
    }

    @Override
    public List<Show> findAllArtistShowsInADay(String artistName, LocalDateTime showDay) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Show> shows = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Shows inner join Locations L on L.id_location = Shows.location\n" +
                "where artist_name = ? and DATETIME(date_and_time/1000, 'unixepoch') > datetime('now')" +
                "order by date_and_time asc")) {
            preStmt.setString(1, artistName);

            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    long id = result.getInt("id_show");
                    LocalDateTime dateAndTime = result.getTimestamp("date_and_time").toLocalDateTime();
                    Integer available = result.getInt("tickets_available");
                    Integer sold = result.getInt("tickets_sold");
                    long idLocation = result.getInt("id_location");
                    String country = result.getString("country");
                    String city = result.getString("city");
                    Location location = new Location(country, city);
                    location.setId(idLocation);
                    Show show = new Show(artistName, dateAndTime, location, available, sold);
                    show.setId(id);
                    if(dateAndTime.isAfter(showDay) && dateAndTime.isBefore(showDay.plusDays(1)))
                        shows.add(show);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return shows;
    }
}
