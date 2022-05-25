import festival.model.Employee;
import festival.network.utils.AbstractServer;
import festival.network.utils.FestivalRpcConcurrentServer;
import festival.network.utils.ServerException;
import festival.persistence.*;
import festival.persistence.orm.EmployeesHibernarteRepository;
import festival.server.FestivalServicesImpl;
import festival.services.IFestivalServices;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
//import org.hibernate.SessionFactory;
//import org.hibernate.boot.MetadataSources;
//import org.hibernate.boot.registry.StandardServiceRegistry;
//import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.IOException;
import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort = 55555;
    static SessionFactory sessionFactory;
    static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.err.println("Exception "+e);
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    static void close(){
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }

    }

    public static void main(String[] args) {

        Properties serverProps = new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/festivalserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find festivalserver.properties " + e);
            return;
        }
        System.out.println("-------------------------------------------");
        System.out.println(serverProps);
        System.out.println("-------------------------------------------");
        EmployeesRepository employeesRepository = new EmployeesDataBaseRepository(serverProps);
        LocationsRepository locationsRepository = new LocationsDataBaseRepository(serverProps);
        ShowsRepository showsRepository = new ShowsDataBaseRepository(serverProps);
        TicketsRepository ticketsRepository = new TicketsDataBaseRepository(serverProps);


        //initialize();
        //EmployeesRepository employeesHibernarteRepository = new EmployeesHibernarteRepository(sessionFactory);


        IFestivalServices festivalServiceImpl = new FestivalServicesImpl(employeesRepository, showsRepository, ticketsRepository);
        //IFestivalServices festivalServiceImpl = new FestivalServicesImpl(employeesHibernarteRepository, showsRepository, ticketsRepository);



        int chatServerPort = defaultPort;
        try {
            chatServerPort = Integer.parseInt(serverProps.getProperty("festival.server.port"));
        } catch (NumberFormatException nef) {
            System.err.println("Wrong  Port Number" + nef.getMessage());
            System.err.println("Using default port " + defaultPort);
        }
        System.out.println("Starting server on port: " + chatServerPort);
        AbstractServer server = new FestivalRpcConcurrentServer(chatServerPort, festivalServiceImpl);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        } finally {
            try {
                server.stop();
            } catch (ServerException e) {
                System.err.println("Error stopping server " + e.getMessage());
            }
        }
    }
}
