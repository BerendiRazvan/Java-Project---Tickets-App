package festival.network.utils;

import festival.model.Show;

import java.io.Serializable;

public class TicketDTO implements Serializable {
    private String name;
    private int tickets;
    private Show show;

    public TicketDTO(String name, int tickets, Show show) {
        this.name = name;
        this.tickets = tickets;
        this.show = show;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTickets() {
        return tickets;
    }

    public void setTickets(int tickets) {
        this.tickets = tickets;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }
}
