package festival.persistence;

import festival.model.Show;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowsRepository extends Repository<Long, Show> {
    List<String> findAllArtists();
    List<Show> findAllArtistShows(String artistName);
    List<Show> findAllArtistShowsInADay(String artistName, LocalDateTime showDay);
}
