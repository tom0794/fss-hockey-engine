package io.github.tom0794;

import io.github.tom0794.schedule.Day;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    private static final Logger logger = LoggerFactory.getLogger("fss-hockey-spring");

    @GetMapping("/retrieveDay/{date}")
    public ResponseEntity<Object> retrieveSkater(@PathVariable String date) throws IOException {
        HashMap<Object, Object> entity = new HashMap<>();
        entity.put("day", Day.retrieveDayWithColumn("date", "2024-11-11"));
        return new ResponseEntity<Object>(entity, HttpStatus.OK);
    }
}