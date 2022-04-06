package festival.services;


import festival.model.Ticket;

public interface IFestivalObserver {
    void soldTicket(Ticket ticket) throws FestivalException;
}
