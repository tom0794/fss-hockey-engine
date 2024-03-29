package io.github.tom0794;

import io.github.tom0794.objects.Game;
import io.github.tom0794.objects.Skater;
import io.github.tom0794.objects.Team;
import io.github.tom0794.schedule.Day;
import io.github.tom0794.schedule.ScheduleUtils;

import java.time.LocalDate;
import java.util.*;

public class Main {
    public static void main(String[] args) {
//        System.out.println("Hello world!");
//        Skater s1 = new Skater();
//        System.out.println("Created player: " + s1 + " age " + s1.getAge());
        testScheduleUtils();
    }

    public static void testScheduleUtils() {

        List<Game> games = ScheduleUtils.createSeasonGames(ScheduleUtils.getTeamList(), 24);
        System.out.println("There are this many games: " + games.size());
//        for (Game g : games) {
//            if (g.getHomeTeam().getAbbreviation() == "MIN" || g.getRoadTeam().getAbbreviation() == "MIN") {
//                System.out.println("Game: " + g.getRoadTeam().getAbbreviation() + " @ " + g.getHomeTeam().getAbbreviation());
//            }
//        }

        List<Day> gameDays = ScheduleUtils.createGameDays(games);
        System.out.println("There are this many game days: " + gameDays.size());
        for (int i = 0; i < gameDays.size(); i++) {
            System.out.println("Game day [" + i + "] : " + Arrays.toString(gameDays.get(i).getGames().toArray()));
        }
    }
}