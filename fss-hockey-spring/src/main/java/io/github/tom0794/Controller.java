package io.github.tom0794;

import io.github.tom0794.database.DbConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.SQLException;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class Controller {
    private static final Logger logger = LoggerFactory.getLogger("fss-hockey-spring");
    @GetMapping("/")
    public String index() {
        return "{\"text\":\"fss-hockey-engine is running!\"}";
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
