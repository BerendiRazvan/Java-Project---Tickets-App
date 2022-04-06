package festival.model;


import java.io.Serializable;

public class Ticket implements Entity<Long>, Serializable {
    private String buyerName;
    private int seats;
    private Show show;
    private Long id;


    public Ticket(String buyerName, int seats, Show show) {
        this.buyerName = buyerName;
        this.seats = seats;
        this.show = show;
    }
    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long aLong) {
        this.id = aLong;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", buyerName='" + buyerName + '\'' +
                ", seats=" + seats +
                ", show=" + show +
                '}';
    }
}
