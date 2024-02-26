package io.github.tom0794;

import io.github.tom0794.objects.Game;
import io.github.tom0794.objects.Skater;
import io.github.tom0794.objects.Team;
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
        ArrayList<Team> teamList = new ArrayList<Team>();
        teamList.add(new Team(1, "Montreal", "Canadiens", "MTL", "000000", "000000"));
        teamList.add(new Team(1, "Ottawa", "Senators", "OTT", "000000", "000000"));
        teamList.add(new Team(1, "Boston", "Bruins", "BOS", "000000", "000000"));
        teamList.add(new Team(1, "New York", "Rangers", "NYR", "000000", "000000"));
        teamList.add(new Team(2, "Columbus", "Blue Jackets", "CBJ", "000000", "000000"));
        teamList.add(new Team(2, "Carolina", "Hurricanes", "CAR", "000000", "000000"));
        teamList.add(new Team(2, "Florida", "Panthers", "FLA", "000000", "000000"));
        teamList.add(new Team(2, "Tampa Bay", "Lightning", "TBL", "000000", "000000"));
        teamList.add(new Team(3, "Pittsburgh", "Penguins", "PIT", "000000", "000000"));
        teamList.add(new Team(3, "Philadelphia", "Flyers", "PHI", "000000", "000000"));
        teamList.add(new Team(3, "New Jersey", "Devils", "NJD", "000000", "000000"));
        teamList.add(new Team(3, "Washington", "Capitals", "WSH", "000000", "000000"));
        teamList.add(new Team(4, "Detroit", "Red Wings", "DET", "000000", "000000"));
        teamList.add(new Team(4, "Toronto", "Maple Leafs", "TOR", "000000", "000000"));
        teamList.add(new Team(4, "Buffalo", "Sabres", "BUF", "000000", "000000"));
        teamList.add(new Team(4, "New York", "Islanders", "NYI", "000000", "000000"));

        teamList.add(new Team(5, "Seattle", "Kraken", "SEA", "000000", "000000"));
        teamList.add(new Team(5, "Vancouver", "Canucks", "VAN", "000000", "000000"));
        teamList.add(new Team(5, "Calgary", "Flames", "CGY", "000000", "000000"));
        teamList.add(new Team(5, "Edmonton", "Oilers", "EDM", "000000", "000000"));
        teamList.add(new Team(6, "Colorado", "Avalanche", "COL", "000000", "000000"));
        teamList.add(new Team(6, "Dallas", "Stars", "DAL", "000000", "000000"));
        teamList.add(new Team(6, "Nashville", "Predators", "NSH", "000000", "000000"));
        teamList.add(new Team(6, "Salt Lake City", "Zephyrs", "SLC", "000000", "000000"));
        teamList.add(new Team(7, "San Jose", "Sharks", "SJS", "000000", "000000"));
        teamList.add(new Team(7, "Los Angeles", "Kings", "LAK", "000000", "000000"));
        teamList.add(new Team(7, "Anaheim", "Ducks", "ANA", "000000", "000000"));
        teamList.add(new Team(7, "Las Vegas", "Golden Knights", "VGK", "000000", "000000"));
        teamList.add(new Team(8, "Chicago", "Blackhawks", "CHI", "000000", "000000"));
        teamList.add(new Team(8, "Winnipeg", "Jets", "WPG", "000000", "000000"));
        teamList.add(new Team(8, "St. Louis", "Blues", "STL", "000000", "000000"));
        teamList.add(new Team(8, "Minnesota", "Wild", "MIN", "000000", "000000"));

        List<Game> games = ScheduleUtils.createSeasonGames(teamList, 24);
        System.out.println("There are this many games: " + games.size());
        for (Game g : games) {
            if (g.getHomeTeam().getAbbreviation() == "MIN" || g.getRoadTeam().getAbbreviation() == "MIN") {
                System.out.println("Game: " + g.getRoadTeam().getAbbreviation() + " @ " + g.getHomeTeam().getAbbreviation());
            }
        }
    }
}