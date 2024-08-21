package restcontroller;


import Domeniu.Cursa;
import Repository.CursaDBRepository;
import Repository.Interface.ICursaRepository;
import Repository.RepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("restcontroller/cursa")
public class CursaRestController
{
    @Autowired
    private ICursaRepository cursaDBRepository;

    @GetMapping("/test")
    public String test(@RequestParam(value="name", defaultValue="Hello") String name)    {
        return name.toUpperCase();
    }

    @RequestMapping(
            method = {RequestMethod.POST}
    )
        public Cursa add(@RequestBody Cursa cursa){
        System.out.println("Creating cursa");
        this.cursaDBRepository.add(cursa);
        Cursa cursa1 = cursaDBRepository.findByAll(cursa.getDestinatie(), cursa.getData(), cursa.getOra(), cursa.getLocuriDisponibile());
        System.out.println("ASTA ESTE CURSA 1 TE ROG SA MEARGA" + cursa1);
        return cursa1;
    }

    @RequestMapping(
            value = {"/{id}"},
            method = {RequestMethod.GET}
    )
    public Cursa getById(@PathVariable Integer id){
        System.out.println("Get by id "+id);
        return cursaDBRepository.findOne(id);
    }

    @RequestMapping(
            method = {RequestMethod.GET}
    )
    public Iterable<Cursa> getAll(){
        System.out.println("Getting all cursa");
        return cursaDBRepository.getAll();
    }

    @RequestMapping(
            value = {"/{id}"},
            method = {RequestMethod.PUT}
    )
    public void update(@PathVariable Integer id, @RequestBody Cursa cursa){
        System.out.println("Updating cursa");
        cursaDBRepository.update(id, cursa);
    }

    @RequestMapping(
            value = {"/{id}"},
            method = {RequestMethod.DELETE}
    )
    public ResponseEntity<?> delete(@PathVariable Integer id){
        System.out.println("Deleting cursa ... " + cursaDBRepository.findOne(id));

        try {
            this.cursaDBRepository.remove(id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (RepositoryException var3) {
            System.out.println("Ctrl Delete user exception");
            return new ResponseEntity(var3.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler({RepositoryException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userError(RepositoryException e) {
        return e.getMessage();
    }
}
