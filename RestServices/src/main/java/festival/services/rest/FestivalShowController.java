package festival.services.rest;

import festival.model.Show;
import festival.persistence.RepositoryException;
import festival.persistence.ShowsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/festival/shows")
public class FestivalShowController {
    private static final String template = "Hello, %s!";

    @Autowired
    private ShowsRepository showsRepository;

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format(template, name);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Show> getAll() {
        System.out.println("Getting all shows ...");
        List<Show> shows = new ArrayList<>();
        for (var s : showsRepository.findAll()) {
            shows.add(s);
        }
        return shows;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Show create(@RequestBody Show show) {
        System.out.println("Creating show ...");
        showsRepository.add(show);
        long max = 0;
        for (var i:showsRepository.findAll())
            if(max<i.getId())
                max=i.getId();
        show.setId(max);
        return show;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Show update(@RequestBody Show show) {
        System.out.println("Updating show ...");
        showsRepository.update(show.getId(), show);
        return show;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        System.out.println("Deleting show with id="+ id+" ... ");
        try {
            showsRepository.delete(id);
            return new ResponseEntity<Show>(HttpStatus.OK);
        } catch (RepositoryException ex) {
            System.out.println("Ctrl Delete show exception");
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            System.out.println("Show not found");
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable Long id) {
        System.out.println("Getting show with id=" + id + " ... ");
        Show show = null;
        for (var s : showsRepository.findAll()) {
            if (s.getId().equals(id))
                show = s;
        }
        Show result = show;
        if (result == null)
            return new ResponseEntity<String>("Show not found", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Show>(result, HttpStatus.OK);
    }

    @ExceptionHandler(RepositoryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userError(RepositoryException e) {
        return e.getMessage();
    }
}
