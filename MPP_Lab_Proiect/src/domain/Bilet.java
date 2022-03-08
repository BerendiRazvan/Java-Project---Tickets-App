package domain;

public class Bilet {
    private String numeCumparator;
    private int locuri;

    public Bilet(String numeCumparator, int locuri) {
        this.numeCumparator = numeCumparator;
        this.locuri = locuri;
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
                '}';
    }
}
