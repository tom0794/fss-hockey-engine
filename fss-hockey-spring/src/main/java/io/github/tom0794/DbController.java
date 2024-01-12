package io.github.tom0794;

import io.github.tom0794.database.DbOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class DbController {
    private static final Logger logger = LoggerFactory.getLogger("fss-hockey-spring");
    @GetMapping("/dbController")
    public String index() {
        return "DbController is running!";
    }

    @GetMapping("/v1/db/createDatabase")
    public String createDatabase() throws SQLException {
        DbOperations.createDatabase();
        DbOperations.createTablePosition();
        DbOperations.createTableCountry();
        DbOperations.createTableTeam();
        DbOperations.createTablePlayer();
        return "Created database";
    }

    @GetMapping("/v1/db/dropDatabase")
    public String dropDatabase() {
        DbOperations.dropDatabase();
        return "Dropped database";
    }
}
