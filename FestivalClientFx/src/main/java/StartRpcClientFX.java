import festival.client.HomeController;
import festival.client.LoginController;
import festival.network.FestivalServicesRpcProxy;
import festival.services.IFestivalServices;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;

public class StartRpcClientFX extends Application {
    private Stage primaryStage;

    private static int defaultTravelPart = 55555;
    private static String defaultServer = "localhost";

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartRpcClientFX.class.getResourceAsStream("/festivalclient.properties"));
            System.out.println("Client properties set.");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find festivalclient.properties " + e);
            return;
        }


        String serverIP = clientProps.getProperty("festival.server.host", defaultServer);
        int serverPort = defaultTravelPart;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("festival.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultTravelPart);
        }

        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        IFestivalServices server = new FestivalServicesRpcProxy(serverIP, serverPort);

        System.out.println(getClass().getClassLoader().getResource("views/login-view.fxml"));

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("views/login-view.fxml"));
        Parent root = fxmlLoader.load();


        LoginController logInController = fxmlLoader.getController();
        logInController.setServer(server);

        FXMLLoader cloader = new FXMLLoader(getClass().getClassLoader().getResource("views/home-view.fxml"));
        Parent croot = cloader.load();
        HomeController homeController = cloader.<HomeController>getController();


        logInController.setFestivalController(homeController);
        logInController.setMainFestivalParent(croot);

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("MPP App - Log In");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
