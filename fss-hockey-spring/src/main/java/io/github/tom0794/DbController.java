package io.github.tom0794;

import io.github.tom0794.database.DbOperations;
import io.github.tom0794.objects.Skater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/db")
public class DbController {
    private static final Logger logger = LoggerFactory.getLogger("fss-hockey-spring");

    @GetMapping(path = "/dbController", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> index() {
        HashMap<Object, Object> entity = new HashMap<>();
        entity.put("status", 200);
        entity.put("message", "DbController is running!");
        return new ResponseEntity<Object>(entity, HttpStatus.OK);
    }

    @GetMapping("/createDatabase")
    public String createDatabase() throws SQLException {
        DbOperations.createDatabase();
        DbSeeding.createTables();
        DbSeeding.seedLeague();
        return "Created database";
    }

    @GetMapping("/createSeason")
    public String createSeason() throws SQLException {
        DbSeeding.createSeason();
        return "Created season";
    }

    @GetMapping("/dropDatabase")
    public String dropDatabase() {
        DbOperations.dropDatabase();
        return "Dropped database";
    }

    @GetMapping("/createSkater")
    // TODO: parameterize
    public ResponseEntity<Object> createSkater() {
        HashMap<Object, Object> entity = new HashMap<>();
        Skater s1 = new Skater();
        s1.createSkater();
        entity.put("skater", s1);
        return new ResponseEntity<Object>(entity, HttpStatus.OK);
    }

    @GetMapping("/retrieveSkater/{id}")
    public ResponseEntity<Object> retrieveSkater(@PathVariable Integer id) throws IOException {
        HashMap<Object, Object> entity = new HashMap<>();
        entity.put("skater", Skater.retrieveSkater(id));
        return new ResponseEntity<Object>(entity, HttpStatus.OK);
    }

    @GetMapping("/deleteSkater/{id}")
    public ResponseEntity<Object> deleteSkater(@PathVariable Integer id) {
        HashMap<Object, Object> entity = new HashMap<>();
        try {
            Skater.deleteSkater(id);
            entity.put("status", "Skater with id " + id + " deleted");
        } catch (Exception e) {
            entity.put("status", "Error deleting skater with id " + id + ": " + e.getMessage());
        }
        return new ResponseEntity<Object>(entity, HttpStatus.OK);
    }

    @GetMapping("/updateSkater/{id}")
    public ResponseEntity<Object> updateSkater(@PathVariable Integer id) throws IOException {
        HashMap<Object, Object> entity = new HashMap<>();
        Skater s1 = Skater.retrieveSkater(id);
        s1.setPuckHandling(99);
        s1.updateSkater();
        entity.put("skater", s1);
        return new ResponseEntity<Object>(entity, HttpStatus.OK);
    }
}
