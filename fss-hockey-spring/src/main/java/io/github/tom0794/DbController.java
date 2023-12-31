package io.github.tom0794;

import io.github.tom0794.database.DbOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DbController {
    private static final Logger logger = LoggerFactory.getLogger("fss-hockey-spring");
    @GetMapping("/dbController")
    public String index() {
        return "DbController is running!";
    }

    @GetMapping("/v1/createDatabase")
    public String createDatabase() {
        DbOperations.createDatabase();
        return "Created database";
    }

    @GetMapping("/v1/dropDatabase")
    public String dropDatabase() {
        DbOperations.dropDatabase();
        return "Dropped database";
    }
}
