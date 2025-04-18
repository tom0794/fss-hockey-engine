package io.github.tom0794;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import io.github.tom0794.database.DbOperations;
import io.github.tom0794.objects.*;
import io.github.tom0794.schedule.Day;
import io.github.tom0794.schedule.ScheduleUtils;
import io.github.tom0794.services.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class DbSeeding {
    private final GameService gameService;
    private static final Logger logger = LoggerFactory.getLogger("fss-hockey-database");

    public DbSeeding(GameService gameService) {
        this.gameService = gameService;
    }

    public static void createTables() throws SQLException {
        DbOperations.createTablePosition();
        DbOperations.createTableCountry();
        DbOperations.createTableConference();
        DbOperations.createTableDivision();
        DbOperations.createTableTeam();
        DbOperations.createTablePlayer();
        DbOperations.createTableSeason();
        DbOperations.createTableDay();
        DbOperations.createTableGame();
    }

    public static void seedLeague() {
        Conference east = new Conference("East", "E");
        Conference west = new Conference("West", "W");
        east.createConference();
        west.createConference();

        ArrayList<Division> divisionList = new ArrayList<Division>();
        divisionList.add(new Division(east.getConferenceId(), "1", "1"));
        divisionList.add(new Division(east.getConferenceId(), "2", "2"));
        divisionList.add(new Division(east.getConferenceId(), "3", "3"));
        divisionList.add(new Division(east.getConferenceId(), "4", "4"));
        divisionList.add(new Division(west.getConferenceId(), "5", "5"));
        divisionList.add(new Division(west.getConferenceId(), "6", "6"));
        divisionList.add(new Division(west.getConferenceId(), "7", "7"));
        divisionList.add(new Division(west.getConferenceId(), "8", "8"));
        for (Division division : divisionList) {
            division.createDivision();
        }

        for (Team team : ScheduleUtils.getTeamList()) {
            team.createTeam();
            // Save the teamIds somewhere?
        }
    }

    // parameterize with year
    public void createSeason() throws JsonProcessingException {
        LocalDate startDate = LocalDate.of(2025, 10, 11);
        Season season = ScheduleUtils.createSeason(2025, "2025-26", startDate);

        season.createSeason();
        for (Day day : season.getDays()) {
            day.setSeasonId(season.getSeasonId());
            day.createDay();
            for (Game game : day.getGames()) {
                game.setDayId(day.dayId);
                game.setHomeTeamId(game.getHomeTeamId());
                game.setRoadTeamId(game.getRoadTeamId());
                this.gameService.createGame(game);
            }
        }

        // add games/days/season to db
    }

//    public static void seedRosters() {
//        Faker f = new Faker(new Locale("en-CA"));
////        Skater s = new Skater(1, 1, f.name().firstName(), f.name().lastName(), height, weight, number, dob, seccondpos, thirdpos, skating, shooting, passing, phys, faceoff, def, puchandle, forward)
//        Skater s = new Skater(
//            1,
//            1,
//            f.name().firstName(),
//            f.name().lastName(),
//            height,
//            weight,
//            number,
//            dob,
//            seccondpos,
//            thirdpos,
//            skating, shooting, passing, phys, faceoff, def, puchandle,
//            forward
//        );
//    }
}
