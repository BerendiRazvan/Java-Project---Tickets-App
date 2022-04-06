package festival.network.utils;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ShowDTO implements Serializable {
    private String artistName;
    private LocalDateTime showDay;

    public ShowDTO(String artistName, LocalDateTime showDay) {
        this.artistName = artistName;
        this.showDay = showDay;
    }


    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public LocalDateTime getShowDay() {
        return showDay;
    }

    public void setShowDay(LocalDateTime showDay) {
        this.showDay = showDay;
    }
}
