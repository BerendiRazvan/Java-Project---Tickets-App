package domain;

public class Bilet {
    private String numeCumparator;
    private int locuri;
    private Spectacol spectacol;


    public Bilet(String numeCumparator, int locuri, Spectacol spectacol) {
        this.numeCumparator = numeCumparator;
        this.locuri = locuri;
        this.spectacol = spectacol;
    }

    public Spectacol getSpectacol() {
        return spectacol;
    }

    public void setSpectacol(Spectacol spectacol) {
        this.spectacol = spectacol;
    }

    public String getNumeCumparator() {
        return numeCumparator;
    }

    public void setNumeCumparator(String numeCumparator) {
        this.numeCumparator = numeCumparator;
    }

    public int getLocuri() {
        return locuri;
    }

    public void setLocuri(int locuri) {
        this.locuri = locuri;
    }

    @Override
    public String toString() {
        return "Bilet{" +
                "numeCumparator='" + numeCumparator + '\'' +
                ", locuri=" + locuri +
                ", spectacol=" + spectacol +
                '}';
    }
}
