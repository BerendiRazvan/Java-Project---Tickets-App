package rest.client;

import festival.model.Show;
import festival.services.rest.ServiceException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;

public class ShowsClient extends RuntimeException {
    public static final String URL = "http://localhost:8080/festival/shows";

    private RestTemplate restTemplate = new RestTemplate();

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (ResourceAccessException | HttpClientErrorException e) { // server down, resource exception
            throw new ServiceException(e);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public Show[] getAll() {
        return execute(() -> restTemplate.getForObject(URL, Show[].class));
    }

    public Show getById(String id) {
        return execute(() -> restTemplate.getForObject(String.format("%s/%s", URL, id), Show.class));
    }

    public Show create(Show show) {
        return execute(() -> restTemplate.postForObject(URL, show, Show.class));
    }

    public void update(Show show) {
        execute(() -> {
            restTemplate.put(String.format("%s/%s", URL, show.getId()), show);
            return null;
        });
    }

    public void delete(String id) {
        execute(() -> {
            restTemplate.delete(String.format("%s/%s", URL, id));
            return null;
        });
    }

}
