package festival.persistence;


import festival.model.Employee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EmployeesDataBaseRepository implements EmployeesRepository {

    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public EmployeesDataBaseRepository(Properties props) {
        logger.info("Initializing EmployeesDataBaseRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public void add(Employee elem) {
        logger.traceEntry("Saving employee {}", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into Employees (first_name, last_name, mail, password) values(?, ?, ?, ?)")) {
            preStmt.setString(1, elem.getFirstName());
            preStmt.setString(2, elem.getLastName());
            preStmt.setString(3, elem.getMail());
            preStmt.setString(4, elem.getPassword());

            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instance", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Long aLong, Employee elem) {
        logger.traceEntry("Updating employee with {}", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("update Employees set first_name = ?, last_name = ?, mail = ?, password = ? where id_employee = ?")) {
            preStmt.setString(1, elem.getFirstName());
            preStmt.setString(2, elem.getLastName());
            preStmt.setString(3, elem.getMail());
            preStmt.setString(4, elem.getPassword());

            preStmt.setInt(5, aLong.intValue());

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
        logger.traceEntry("Deleting employee with id {}", aLong);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("delete from Employees where id_employee = ?")) {
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
    public Iterable<Employee> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Employee> employees = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Employees")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    long id = result.getInt("id_employee");
                    String firstName = result.getString("first_name");
                    String lastName = result.getString("last_name");
                    String mail = result.getString("mail");
                    String password = result.getString("password");
                    Employee employee = new Employee(firstName, lastName, mail, password);
                    employee.setId(id);
                    employees.add(employee);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return employees;
    }

    @Override
    public Employee findOneEmployee(String email) {
        logger.traceEntry();
        Employee employee=new Employee("","","","");
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Employees where mail=?")) {
            preStmt.setString(1, email);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    long id = result.getInt("id_employee");
                    String firstName = result.getString("first_name");
                    String lastName = result.getString("last_name");
                    String mail = result.getString("mail");
                    String password = result.getString("password");
                    employee = new Employee(firstName, lastName, mail, password);
                    employee.setId(id);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();

        if(employee.getId() == null)
            return null;
        else
            return employee;

    }
}
