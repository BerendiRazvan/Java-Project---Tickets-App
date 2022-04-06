package festival.model;


import java.io.Serializable;

public class Location implements Entity<Long>, Serializable {
    private String country;
    private String city;
    private Long id;

    public Location(String country, String city) {
        this.country = country;
        this.city = city;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long aLong) {
        this.id = aLong;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return country + ", " + city;

    }
}
