package io.github.tom0794;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.tom0794.objects.Game;
import io.github.tom0794.objects.Season;
import io.github.tom0794.objects.Skater;
import io.github.tom0794.objects.Team;
import io.github.tom0794.schedule.Day;
import io.github.tom0794.schedule.ScheduleUtils;

import java.time.LocalDate;
import java.util.*;

import static io.github.tom0794.players.RosterSeeding.seedRosters;

public class Main {
    public static void main(String[] args) throws Exception {
//        System.out.println("Hello world!");
//        Skater s1 = new Skater();
//        System.out.println("Created player: " + s1 + " age " + s1.getAge());
//        testScheduleUtils();
        testRosterSeeding();
    }

    public static void testRosterSeeding() throws JsonProcessingException {
        seedRosters();
    }

    public static void testScheduleUtils() throws Exception {

        //List<Game> games = ScheduleUtils.createSeasonGames(ScheduleUtils.getTeamList(), 25);
        //System.out.println("There are this many games: " + games.size());

        LocalDate startDate = LocalDate.of(2025, 10, 11);
        Season s = ScheduleUtils.createSeason(2025, "2025-26", startDate);
        String[] days = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
        int offDayPointer = 0;
        for (int i = 0; i < s.getDays().size(); i++) {
            if (i % 7 == 0) {
                System.out.println();
            }
            System.out.println(s.getDays().get(i).getDate() + " --- " + s.getDays().get(i).getGames().size() + " --- " + s.getDays().get(i).getGames());
            offDayPointer = (offDayPointer + 1) % days.length;
        }
        System.out.println("Season length is " + s.getDays().size() + " days");
    }
}