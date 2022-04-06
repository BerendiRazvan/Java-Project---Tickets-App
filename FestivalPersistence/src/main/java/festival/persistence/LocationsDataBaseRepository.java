package festival.persistence;

import festival.model.Location;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class LocationsDataBaseRepository implements LocationsRepository {

    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public LocationsDataBaseRepository(Properties props) {
        logger.info("Initializing LocationsDataBaseRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public void add(Location elem) {
        logger.traceEntry("Saving location {}", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into Locations (country, city) values (?, ?)")) {
            preStmt.setString(1, elem.getCountry());
            preStmt.setString(2, elem.getCity());

            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instance", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Long aLong, Location elem) {
        logger.traceEntry("Updating location with {}", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("update Locations set country = ?, city = ? where id_location = ?")) {
            preStmt.setString(1, elem.getCountry());
            preStmt.setString(2, elem.getCity());
            preStmt.setInt(3, aLong.intValue());

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
        logger.traceEntry("Deleting location with id {}", aLong);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("delete from Locations where id_location = ?")) {
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
    public Iterable<Location> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Location> locations = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Locations")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    long id = result.getInt("id_location");
                    String country = result.getString("country");
                    String city = result.getString("city");
                    Location location = new Location(country, city);
                    location.setId(id);
                    locations.add(location);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return locations;
    }
}
