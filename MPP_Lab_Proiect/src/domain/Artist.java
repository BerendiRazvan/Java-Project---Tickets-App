package domain;

import java.time.LocalDateTime;

public class Artist extends Entity<Long> {
    private String nume;
    private LocalDateTime data;
    private Locatie locatie;
    private int bileteDisponibile;
    private int bileteVandute;

    public Artist(String nume, LocalDateTime data, Locatie locatie, int locuriDisponibile, int locuriVandute) {
        this.nume = nume;
        this.data = data;
        this.locatie = locatie;
        this.bileteDisponibile = locuriDisponibile;
        this.bileteVandute = locuriVandute;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Locatie getLocatie() {
        return locatie;
    }

    public void setLocatie(Locatie locatie) {
        this.locatie = locatie;
    }

    public int getBileteDisponibile() {
        return bileteDisponibile;
    }

    public void setBileteDisponibile(int bileteDisponibile) {
        this.bileteDisponibile = bileteDisponibile;
    }

    public int getBileteVandute() {
        return bileteVandute;
    }

    public void setBileteVandute(int bileteVandute) {
        this.bileteVandute = bileteVandute;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", data=" + data +
                ", locatie=" + locatie +
                ", bileteDisponibile=" + bileteDisponibile +
                ", bileteVandute=" + bileteVandute +
                '}';
    }
}
