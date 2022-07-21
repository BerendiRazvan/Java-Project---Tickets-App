package festival.server;

import festival.model.Employee;
import festival.model.Show;
import festival.model.Ticket;
import festival.persistence.EmployeesRepository;
import festival.persistence.ShowsRepository;
import festival.persistence.TicketsRepository;
import festival.services.FestivalException;
import festival.services.IFestivalObserver;
import festival.services.IFestivalServices;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FestivalServicesImpl implements IFestivalServices {
    private EmployeesRepository employeesRepository;
    private ShowsRepository showsRepository;
    private TicketsRepository ticketsRepository;

    private Map<String, IFestivalObserver> loggedClients;

    public FestivalServicesImpl(EmployeesRepository employeesRepository, ShowsRepository showsRepository, TicketsRepository ticketsRepository) {
        this.employeesRepository = employeesRepository;
        this.showsRepository = showsRepository;
        this.ticketsRepository = ticketsRepository;

        loggedClients = new ConcurrentHashMap<>();
    }


    @Override
    public void login(Employee employee, IFestivalObserver client) throws FestivalException {
        Employee employeeTry = employeesRepository.findOneEmployee(employee.getMail());
        if (employeeTry!=null) {
            if (loggedClients.get(employee.getMail()) != null) {
                throw new FestivalException("User already logged in!");
            }
            else
                throw new FestivalException("Invalid password!\n");
        } else {
            throw new FestivalException("Authentication failed!");
        }
    }

    @Override
    public void logout(Employee employee, IFestivalObserver client) throws FestivalException {
        IFestivalObserver localClient = loggedClients.remove(employee.getMail());
        if (localClient == null) {
            throw new FestivalException("User " + employee.getMail() + " was not logged in!");
        }
    }



    @Override
    public List<String> getAllArtists() {
        return showsRepository.findAllArtists();
    }

    @Override
    public List<Show> getArtistShows(String artistName) {
        return showsRepository.findAllArtistShows(artistName);
    }

    @Override
    public List<Show> getArtistShowsInADay(String artistName, LocalDateTime showDay) {
        return showsRepository.findAllArtistShowsInADay(artistName, showDay);
    }

    @Override
    public void sellTicket(String name, int tickets, Show show) throws FestivalException {
        if(tickets>show.getTicketsAvailable()){
            throw new FestivalException("Not enough tickets for this show!\n");
        }

        show.setTicketsAvailable(show.getTicketsAvailable()-tickets);
        show.setTicketsSold(show.getTicketsSold()+tickets);
        showsRepository.update(show.getId(), show);

        Ticket ticket = new Ticket(name,tickets,show);
        ticketsRepository.add(ticket);

        notifyEmployeeBuyTicket(ticket);
    }

    private final int defaultThreadsNo=5;
    private void notifyEmployeeBuyTicket(Ticket ticket){
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for(Map.Entry<String, IFestivalObserver> elem : loggedClients.entrySet()){
            String mail = elem.getKey();
            IFestivalObserver client = elem.getValue();
            executor.execute(() -> {
                try {
                    System.out.println("Notifying [" + mail + "] that a ticket was sold");
                    client.soldTicket(ticket);
                } catch (FestivalException e) {
                    System.err.println("Error notifying friend " + e);
                }
            });
        }
        executor.shutdown();
    }

}
