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
        System.out.println("\nJava REST Client started");

        Location lT = new Location("Romania", "Cluj");
        lT.setId(1L);
        Show showT = new Show("ShowTestJava", LocalDateTime.now(), lT, 999, 999);
        showT.setId(0L);

        try {
            System.out.println("\n---------------------------------------------Create---------------------------------------------\n");

            show(() -> System.out.println(SHOWS_CLIENT.create(showT)));

            System.out.println("\n---------------------------------------------GetAll---------------------------------------------\n");

            show(() -> {
                Show[] res = SHOWS_CLIENT.getAll();
                for (Show s : res) {
                    System.out.println("\n" + s);
                    if (showT.getId() < s.getId())
                        showT.setId(s.getId());
                }
            });

            System.out.println("\n---------------------------------------------GetById---------------------------------------------\n");

            show(() -> {
                Show s = SHOWS_CLIENT.getById(showT.getId().toString());
                System.out.println("\n" + s);
            });

            System.out.println("\n---------------------------------------------Update---------------------------------------------\n");

            showT.setArtistName("Updated");
            show(() -> {
                SHOWS_CLIENT.update(showT);
            });
            System.out.println("Event updated");

            System.out.println("\n---------------------------------------------Delete---------------------------------------------\n");

            show(() -> {
                SHOWS_CLIENT.delete(showT.getId().toString());
            });

            System.out.println("Event deleted");

            System.out.println("\n------------------------------------------------------------------------------------------\n");
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
