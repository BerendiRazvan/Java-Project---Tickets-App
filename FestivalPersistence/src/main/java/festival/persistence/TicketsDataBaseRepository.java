package festival.persistence;


import festival.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TicketsDataBaseRepository implements TicketsRepository {

    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public TicketsDataBaseRepository(Properties props) {
        logger.info("Initializing TicketsDataBaseRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public void add(Ticket elem) {
        logger.traceEntry("Saving ticket {}", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into Tickets (buyer_name, seats, show) values (?, ?, ?)")) {
            preStmt.setString(1, elem.getBuyerName());
            preStmt.setInt(2, elem.getSeats());
            preStmt.setInt(3, elem.getShow().getId().intValue());

            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instance", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Long aLong, Ticket elem) {
        logger.traceEntry("Updating ticket with {}", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("update Tickets set buyer_name = ?, seats = ?, show = ? where id_ticket = ?")) {
            preStmt.setString(1, elem.getBuyerName());
            preStmt.setInt(2, elem.getSeats());
            preStmt.setInt(3, elem.getShow().getId().intValue());

            preStmt.setInt(4, aLong.intValue());

            int result = preStmt.executeUpdate();
            logger.trace("Update {} instance", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Long aLong) {
        logger.traceEntry("Deleting ticket with id {}", aLong);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("delete from Tickets where id_ticket = ?")) {
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
    public Iterable<Ticket> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Ticket> tickets = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Tickets inner join Shows S on S.id_show = Tickets.show inner join Locations L on L.id_location = S.location\n")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    long id = result.getInt("id_ticket");
                    String buyerName = result.getString("buyer_name");
                    Integer seats = result.getInt("seats");

                    long idShow = result.getInt("id_show");
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
                    show.setId(idShow);

                    Ticket ticket = new Ticket(buyerName, seats, show);
                    ticket.setId(id);
                    tickets.add(ticket);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return tickets;
    }
}
