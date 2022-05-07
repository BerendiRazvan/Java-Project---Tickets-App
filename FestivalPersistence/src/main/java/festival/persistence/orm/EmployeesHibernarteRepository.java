package festival.persistence.orm;

import festival.model.Employee;
import festival.persistence.EmployeesRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;
import java.util.Properties;

public class EmployeesHibernarteRepository implements EmployeesRepository {
    static SessionFactory sessionFactory;


    public EmployeesHibernarteRepository(SessionFactory f) {

        sessionFactory = f;
        //System.out.println("HibernateUserRepository() with properties " + properties);
    }

    static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            System.err.println("Exception initializare " + e);
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    static void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }

    }


    @Override
    public Employee findOneEmployee(String email) {
        System.out.println("MAIL :" + email);
//        initialize();
        try (Session session = sessionFactory.openSession()) {
            try {
                Query query = session.createQuery("from Employee E  WHERE E.mail = :email");
                query.setParameter("email", email);

                Employee employee = (Employee) query.uniqueResult();
                System.out.println(query.toString());
                System.out.println(employee);
                return employee;
            } catch (RuntimeException ex) {
                ex.printStackTrace();
            } finally {
                close();
            }
            return null;
        }
    }

    @Override
    public void add(Employee elem) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.save(elem);
                tx.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la inserare " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }
    }

    @Override
    public void update(Long aLong, Employee elem) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                //Employee employee = session.load(Employee.class, aLong);


                elem.setId(aLong);
                session.update(elem);
                tx.commit();

            } catch (RuntimeException ex) {
                System.err.println("Eroare la update " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }
    }

    @Override
    public void delete(Long aLong) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                //Employee crit = session.load(Employee.class, aLong);
                //System.err.println("Stergem angajatul cu id-ul " + crit.getId());
                Employee c = new Employee("","","","");
                c.setId(aLong);
                session.delete(c);
                tx.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la stergere " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }
    }

    @Override
    public Iterable<Employee> findAll() {
        //initialize();
        try (Session session = sessionFactory.openSession()) {
            try {
                String queryString;
                queryString = "from Employee";
                List<Employee> employeeList =
                        session.createQuery(queryString, Employee.class)
                                .list();
                System.out.println(employeeList.size() + " employees founded");
                return employeeList;
            } catch (RuntimeException ex) {
                System.out.println("\n\n\n---------------------------");
                System.out.println(ex.getMessage());
                System.out.println("\n\n\n---------------------------");
                return null;
            } finally {
                close();
            }
        }
    }
}
