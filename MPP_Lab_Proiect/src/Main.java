import domain.Artist;
import domain.Locatie;

import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args){
        LocalDateTime time = LocalDateTime.now();
        Artist a = new Artist("Smiley", time, new Locatie("Romania", "Cluj"), 100, 1234);
        a.setId(221L);
        System.out.println(a);
    }

}
