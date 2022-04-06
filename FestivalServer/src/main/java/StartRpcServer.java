import festival.network.utils.AbstractServer;
import festival.network.utils.FestivalRpcConcurrentServer;
import festival.network.utils.ServerException;
import festival.persistence.*;
import festival.server.FestivalServicesImpl;
import festival.services.IFestivalServices;

import java.io.IOException;
import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort = 55555;

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

        System.out.println(serverProps.toString());

        EmployeesRepository employeesRepository = new EmployeesDataBaseRepository(serverProps);
        LocationsRepository locationsRepository = new LocationsDataBaseRepository(serverProps);
        ShowsRepository showsRepository = new ShowsDataBaseRepository(serverProps);
        TicketsRepository ticketsRepository = new TicketsDataBaseRepository(serverProps);


        IFestivalServices festivalServiceImpl = new FestivalServicesImpl(employeesRepository, showsRepository, ticketsRepository);



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
