package festival.services;

import festival.model.Employee;
import festival.model.Show;

import java.time.LocalDateTime;
import java.util.List;

public interface IFestivalServices {

    //Employee
    void login(Employee employee, IFestivalObserver client) throws FestivalException;
    void logout(Employee employee, IFestivalObserver client) throws FestivalException;


    //Show
    List<String> getAllArtists() throws FestivalException;
    List<Show> getArtistShows(String artistName) throws FestivalException;
    List<Show> getArtistShowsInADay(String artistName, LocalDateTime showDay) throws FestivalException;

    //Ticket
    void sellTicket(String name, int tickets, Show show) throws FestivalException;

}
