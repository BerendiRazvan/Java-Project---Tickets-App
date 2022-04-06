package festival.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Show implements  Entity<Long>, Serializable {
    private String artistName;
    private LocalDateTime dataAndTime;
    private Location location;
    private int ticketsAvailable;
    private int ticketsSold;
    private Long id;

    public Show(String artist_name, LocalDateTime dataAndTime, Location location, int tickets_available, int tickets_sold) {
        this.artistName = artist_name;
        this.dataAndTime = dataAndTime;
        this.location = location;
        this.ticketsAvailable = tickets_available;
        this.ticketsSold = tickets_sold;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long aLong) {
        this.id = aLong;
    }
    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public LocalDateTime getDataAndTime() {
        return dataAndTime;
    }

    public void setDataAndTime(LocalDateTime dataAndTime) {
        this.dataAndTime = dataAndTime;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getTicketsAvailable() {
        return ticketsAvailable;
    }

    public void setTicketsAvailable(int ticketsAvailable) {
        this.ticketsAvailable = ticketsAvailable;
    }

    public int getTicketsSold() {
        return ticketsSold;
    }

    public void setTicketsSold(int ticketsSold) {
        this.ticketsSold = ticketsSold;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String formattedDateTime = dataAndTime.format(formatter); // "1986-04-08 12:30"
        return artistName + "\n" + formattedDateTime + "\n" + location;
    }
}
