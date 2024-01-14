package io.github.tom0794;

import io.github.tom0794.database.DbConnection;
import io.github.tom0794.objects.Skater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class Controller {
    private static final Logger logger = LoggerFactory.getLogger("fss-hockey-spring");
    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> index() {
        //return "{\"text\":\"fss-hockey-engine is running!\"}";
        //return new ResponseEntity<>("fss-hockey-engine is running!", HttpStatus.OK);


        HashMap<Object, Object> entity = new HashMap<>();
        entity.put("aa", "bb");
        entity.put("data", 1);
        entity.put("response", 200);
        LocalDate dob = LocalDate.of(1994, 7, 19);
        Skater s1 = new Skater(
                1, 1,1, "John", "Johnson", 180, 180, 19, dob
        );
        entity.put("skater", s1);

        return new ResponseEntity<Object>(entity, HttpStatus.OK);
    }

    @GetMapping("/v1/teams")
    public String[] teams() {
        String[] teams = new String[]{
                "Buffalo Bills",
                "Miami Dolphins"
        };
        return teams;
    }

    @GetMapping("/v1/db")
    public String databaseTest() {
        try (Connection connect = DbConnection.connect("")) {
            logger.info(connect.getCatalog());
            logger.info(connect.getSchema());
            return connect.getSchema();
        }
        catch (SQLException e) {
            logger.error(e.getMessage());
            return "Error connecting to database!";
        }
    }
}
