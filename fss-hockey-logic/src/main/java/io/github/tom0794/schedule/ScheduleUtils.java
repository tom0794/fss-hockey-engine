package io.github.tom0794.schedule;

import io.github.tom0794.objects.Game;
import io.github.tom0794.objects.Season;
import io.github.tom0794.objects.Team;

import java.util.*;

public class ScheduleUtils {
    private static final int interDivisionGames = 6;
    private static final int interConferenceGames = 4;
    private static final int intraConferenceGames = 2;

    private static final HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> divisionMatchupMappings = new HashMap<Integer, HashMap<Integer, ArrayList<Integer>>>();

    public static List<Game> createSeasonGames(List<Team> teams, int year) {
        createDivisionMatchupMappings();
        List<Game> games = new ArrayList<>();
        HashMap<Integer, ArrayList<Integer>> divisionMapping = divisionMatchupMappings.get(getDivisionMatchupMapping(year));

        for (int i = 0; i < teams.size() - 1; i++) {
            Team currentTeam = teams.get(i);
            for (int j = i + 1; j < teams.size(); j++) {
                if (currentTeam.getDivisionId() == teams.get(j).getDivisionId()) {
                    for (int k = 0; k < interDivisionGames / 2; k++) {
                        games.add(new Game(currentTeam, teams.get(j)));
                        games.add(new Game(teams.get(j), currentTeam));
                    }
                    continue;
                }

                // TODO: Better way to find conference
                if (currentTeam.getDivisionId() <= 4 && teams.get(j).getDivisionId() <= 4) {
                    for (int k = 0; k < interConferenceGames / 2; k++) {
                        games.add(new Game(currentTeam, teams.get(j)));
                        games.add(new Game(teams.get(j), currentTeam));
                    }
                    continue;
                } else if (currentTeam.getDivisionId() >= 5 && teams.get(j).getDivisionId() >= 5) {
                    for (int k = 0; k < interConferenceGames / 2; k++) {
                        games.add(new Game(teams.get(i), teams.get(j)));
                        games.add(new Game(teams.get(j), teams.get(i)));
                    }
                    continue;
                }

                ArrayList<Integer> homeGameAgainstDivisions = divisionMapping.get(currentTeam.getDivisionId());
                if (homeGameAgainstDivisions.contains(teams.get(j).getDivisionId())) {
                    games.add(new Game(currentTeam, teams.get(j)));
                } else {
                    games.add(new Game(teams.get(j), currentTeam));
                }
            }
        }

        return games;
    }

    public static void createDivisionMatchupMappings() {
        divisionMatchupMappings.put(0, new HashMap<>() {{
            put(1, new ArrayList<>() {{ add(5); add(6); }});
            put(2, new ArrayList<>() {{ add(6); add(7); }});
            put(3, new ArrayList<>() {{ add(7); add(8); }});
            put(4, new ArrayList<>() {{ add(8); add(5); }});
        }});
        divisionMatchupMappings.put(1, new HashMap<>() {{
            put(1, new ArrayList<>() {{ add(6); add(7); }});
            put(2, new ArrayList<>() {{ add(7); add(8); }});
            put(3, new ArrayList<>() {{ add(8); add(5); }});
            put(4, new ArrayList<>() {{ add(5); add(6); }});
        }});
        divisionMatchupMappings.put(2, new HashMap<>() {{
            put(1, new ArrayList<>() {{ add(7); add(8); }});
            put(2, new ArrayList<>() {{ add(8); add(5); }});
            put(3, new ArrayList<>() {{ add(5); add(6); }});
            put(4, new ArrayList<>() {{ add(6); add(7); }});
        }});
        divisionMatchupMappings.put(3, new HashMap<>() {{
            put(1, new ArrayList<>() {{ add(8); add(5); }});
            put(2, new ArrayList<>() {{ add(5); add(6); }});
            put(3, new ArrayList<>() {{ add(6); add(7); }});
            put(4, new ArrayList<>() {{ add(7); add(8); }});
        }});
    }

    public static int getDivisionMatchupMapping(int year) {
        int frequency = divisionMatchupMappings.size();
        return (year - (frequency * (year / frequency)));
    }

    public Season createSeason(List<Game> unscheduledGames) {
        Season season = new Season("2024-2025");

        // for loop for each day of the season (0 to 181)
        // check day of the week
        //


        // game quantities
        /*
        * 16 - 3
        * 15 - 2
        * 14 - 4
        * 13 - 5
        * 12 - 5
        * 11 - 5
        * 10 - 5
        * 6 - 4
        * 5 - 5
        * 4 - 5
        * 3 - 4
        * 2 - 4
        * 1 - 3
        * 0 -
        *
        * */


        // Constraints
        // season is ~182 days
        // 26 weeks
        // 50-51 games per week
        // Tue, Thu, Sat should have 12-16 games
        // Mon, Wed, Fri, Sun should have 0-5 games
        // Teams can only have one game per day
        // Teams cannot play games more than 2 days consecutively
        // Teams can play at most 3 games in 5 days
        // Teams cannot play the same opponent more than 2 times in 10 days
        return season;
    }

    public static List<Day> createGameDays(List<Game> games) {
        Collections.shuffle(games);
        List<Day> gameDays = new ArrayList<Day>();
        HashMap<Integer, Integer> gameDayQuantities = getGameDayQuantities();

        for (int i : gameDayQuantities.keySet()) {
            int gameQuantity = gameDayQuantities.get(i);
            for (int k = 0; k < gameQuantity; k++) {
                ArrayList<Team> teamsPlaying = new ArrayList<Team>();
                Day day = new Day();
                for (int j = 0; j < i; j++) {
                    boolean gameAdded = false;
                    int gameListPointer = 0;
                    while (!gameAdded && gameListPointer < games.size()) {
                        Game g = games.get(gameListPointer);
                        if (!teamsPlaying.contains(g.getHomeTeam()) && !teamsPlaying.contains(g.getRoadTeam())) {
                            day.addGame(g);
                            teamsPlaying.add(g.getHomeTeam());
                            teamsPlaying.add(g.getRoadTeam());
                            gameAdded = true;
                            games.remove(gameListPointer);
                        } else {
                            gameListPointer++;
                        }
                    }
                }
                gameDays.add(day);
            }
        }
        System.out.println(games);
        return gameDays;
    }

    private static HashMap<Integer, Integer> getGameDayQuantities() {
        HashMap<Integer, Integer> gameDayQuantities = new HashMap<Integer, Integer>();
        gameDayQuantities.put(16, 8);
        gameDayQuantities.put(15, 10);
        gameDayQuantities.put(14, 10);
        gameDayQuantities.put(13, 10);
        gameDayQuantities.put(12, 10);
        gameDayQuantities.put(11, 10);
        gameDayQuantities.put(10, 10);
        gameDayQuantities.put(8, 10);

        gameDayQuantities.put(6, 12);
        gameDayQuantities.put(5, 22);
        gameDayQuantities.put(4, 18);
        gameDayQuantities.put(3, 18);
        gameDayQuantities.put(2, 20);
        gameDayQuantities.put(1, 6);
        gameDayQuantities.put(0, 8);
        return gameDayQuantities;
    }
}
