package io.github.tom0794;

import io.github.tom0794.objects.Game;
import io.github.tom0794.objects.Team;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    private static final Logger logger = LoggerFactory.getLogger("fss-hockey-spring");

    @GetMapping("/retrieveDay/{date}")
    public ResponseEntity<Object> retrieveDay(@PathVariable String date) throws IOException {
        HashMap<Object, Object> entity = new HashMap<>();
        entity.put("day", Day.retrieveDayWithColumn("date", date));
        return new ResponseEntity<Object>(entity, HttpStatus.OK);
    }

    @GetMapping("/getGamesOnDate/{date}")
    public ResponseEntity<Object> getGamesOnDate(@PathVariable String date) throws IOException {
        Map<String, Object> entity = new HashMap<>();
        Day day = Day.retrieveDayWithColumn("date", date);
        if (day == null) {
            ApiResponse<Map<String, Object>> response = new ApiResponse<>(404, entity);
            entity.put("message", "Day not found");
            logger.info(response.toString());
            return ResponseEntity.ok(response);
        }
        List<Game> games = Game.getGamesWithDayId(day.getDayId());
        ApiResponse<Map<String, Object>> response = new ApiResponse<>(200, entity);
        if (games.isEmpty()) {
            entity.put("message", "No games scheduled");
        } else {
            entity.put("day", Day.retrieveDayWithColumn("date", date));
            HashMap<Integer, Team> teamMap = Team.retrieveTeamMap();
            for (Game game : games) {
                game.setRoadTeam(teamMap.get(game.getRoadTeamId()));
                game.setHomeTeam(teamMap.get(game.getHomeTeamId()));
            }
            entity.put("games", games);
            logger.info(response.toString());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/retrieveTeam/{teamId}")
    public ResponseEntity<Object> retrieveTeam(@PathVariable Integer teamId) throws IOException {
        HashMap<Object, Object> entity = new HashMap<>();
        entity.put("team", Team.retrieveTeam(teamId));
        return new ResponseEntity<Object>(entity, HttpStatus.OK);
    }

    @GetMapping("/retrieveTeams")
    public ResponseEntity<Object> retrieveTeams() throws IOException {
        HashMap<Object, Object> entity = new HashMap<>();
        entity.put("teams", Team.retrieveAllTeams());
        return new ResponseEntity<Object>(entity, HttpStatus.OK);
    }
}