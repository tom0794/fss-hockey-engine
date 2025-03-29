package io.github.tom0794;

import io.github.tom0794.objects.Game;
import io.github.tom0794.objects.Season;
import io.github.tom0794.objects.Skater;
import io.github.tom0794.objects.Team;
import io.github.tom0794.schedule.Day;
import io.github.tom0794.schedule.ScheduleUtils;

import java.time.LocalDate;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
//        System.out.println("Hello world!");
//        Skater s1 = new Skater();
//        System.out.println("Created player: " + s1 + " age " + s1.getAge());
        testScheduleUtils();
    }

    public static void testScheduleUtils() throws Exception {

        List<Game> games = ScheduleUtils.createSeasonGames(ScheduleUtils.getTeamList(), 24);
        System.out.println("There are this many games: " + games.size());
//        for (Game g : games) {
//            if (g.getHomeTeam().getAbbreviation() == "MIN" || g.getRoadTeam().getAbbreviation() == "MIN") {
//                System.out.println("Game: " + g.getRoadTeam().getAbbreviation() + " @ " + g.getHomeTeam().getAbbreviation());
//            }
//        }

//        List<Day> gameDays = ScheduleUtils.createGameDays(games);
//        System.out.println("There are this many game days: " + gameDays.size());
//        for (int i = 0; i < gameDays.size(); i++) {
//            System.out.println("Game day [" + i + "] : " + Arrays.toString(gameDays.get(i).getGames().toArray()));
//        }
//        Season s = ScheduleUtils.createSeason(2024, "2024-25");
//        for (int i = 0; i < s.getDays().size(); i++) {
//            if (i % 7 == 0) {
//                System.out.println();
//            }
//            System.out.println(s.getDays().get(i).getDate() + " --- " + s.getDays().get(i).getGames());
//        }

        Season s = ScheduleUtils.createSeason2(2024, "2024-25");
        System.out.println("Season length is " + s.getDays().size() + " days");
        for (int i = 0; i < s.getDays().size(); i++) {
            if (i % 7 == 0) {
                System.out.println();
            }
            System.out.println(s.getDays().get(i).getGames().size() + " --- " + s.getDays().get(i).getGames());
        }
    }
}