import domain.Spectacol;
import domain.Locatie;

import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args){
        LocalDateTime time = LocalDateTime.now();
        Spectacol a = new Spectacol("Smiley", time, new Locatie("Romania", "Cluj"), 100, 1234);
        a.setId(221L);
        System.out.println(a);
    }

}
