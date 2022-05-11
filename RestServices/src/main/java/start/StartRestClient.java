package start;

import festival.model.Location;
import festival.model.Show;
import festival.services.rest.ServiceException;
import org.springframework.web.client.RestClientException;
import rest.client.ShowsClient;

import java.time.LocalDateTime;

public class StartRestClient {

    private final static ShowsClient SHOWS_CLIENT = new ShowsClient();

    public static void main(String[] args) {
        Show showT = new Show("t1", LocalDateTime.now(), new Location("ro", "cj"), 5, 5);
        try {
            show(() -> System.out.println(SHOWS_CLIENT.create(showT)));
            show(() -> {
                Show[] res = SHOWS_CLIENT.getAll();
                for (Show u : res) {
                    System.out.println(u.getId() + ": " + u.getArtistName());
                }
            });
        } catch (RestClientException ex) {
            System.out.println("Exception ... " + ex.getMessage());
        }

    }


    private static void show(Runnable task) {
        try {
            task.run();
        } catch (ServiceException e) {
            System.out.println("Service exception" + e);
        }
    }
}
