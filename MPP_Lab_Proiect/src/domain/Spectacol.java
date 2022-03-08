package domain;

import java.time.LocalDateTime;

public class Spectacol extends Entity<Long> {
    private String numeArtist;
    private LocalDateTime data;
    private Locatie locatie;
    private int bileteDisponibile;
    private int bileteVandute;

    public Spectacol(String numeArtist, LocalDateTime data, Locatie locatie, int locuriDisponibile, int locuriVandute) {
        this.numeArtist = numeArtist;
        this.data = data;
        this.locatie = locatie;
        this.bileteDisponibile = locuriDisponibile;
        this.bileteVandute = locuriVandute;
    }

    public String getNume() {
        return numeArtist;
    }

    public void setNume(String nume) {
        this.numeArtist = nume;
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
        return "Spectacol{" +
                "id=" + id +
                ", numeArtist='" + numeArtist + '\'' +
                ", data=" + data +
                ", locatie=" + locatie +
                ", bileteDisponibile=" + bileteDisponibile +
                ", bileteVandute=" + bileteVandute +
                '}';
    }
}
