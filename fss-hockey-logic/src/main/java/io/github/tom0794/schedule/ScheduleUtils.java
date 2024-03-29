package io.github.tom0794.schedule;

import io.github.tom0794.objects.Game;
import io.github.tom0794.objects.Season;
import io.github.tom0794.objects.Team;

import java.time.DayOfWeek;
import java.time.LocalDate;
import static java.time.temporal.TemporalAdjusters.firstInMonth;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class ScheduleUtils {
    private static final int interDivisionGames = 6;
    private static final int interConferenceGames = 4;
    private static final int intraConferenceGames = 2;

    private static final int offDayMaxGames = 6;

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

    // TODO: sort list of game days by applying constraints/off days; add dates
    public static Season createSeason(int year, String yearString) throws Exception {
        Season season = new Season(yearString);

        List<Day> gameDays = createGameDays(createSeasonGames(getTeamList(), 2000 - year));
        List<Day> sortedGameDays = new ArrayList<Day>();

        LocalDate date = LocalDate.of(year, 10, 1).with(firstInMonth(DayOfWeek.SUNDAY));

        String[] offDayPattern = {"OFF", "OFF", "ON", "OFF", "ON", "OFF", "ON"};
        int offDayPointer = 0;
        int totalNumberOfGameDays = gameDays.size();
        HashMap<String, Queue<Boolean>> teamRecentGames = new HashMap<>();
        for (Team t : getTeamList()) {
            teamRecentGames.put(t.getAbbreviation(), new ArrayBlockingQueue<>(5));
            teamRecentGames.get(t.getAbbreviation()).add(false);
        }

        Collections.shuffle(gameDays);
        while (sortedGameDays.size() < totalNumberOfGameDays) {
            boolean gameAdded = false;
            int gamesPointer = 0;
            while (!gameAdded) {
                Day gameDay = gameDays.get(gamesPointer);
                // Check if the number of games in the game day is suitable. OFF day = 8 games or less,
                // ON day = more than 8 games. If yes proceed, if not skip to next game day
                if ((offDayPattern[offDayPointer].equals("OFF") && (gameDay.getGames().size() > offDayMaxGames)) ||
                        (offDayPattern[offDayPointer].equals("ON") && (gameDay.getGames().size() <= offDayMaxGames)) ) {
                    gamesPointer++;
                    continue;
                }

                boolean constraintViolated = false;
                List<String> teamsPlaying = new ArrayList<>();
                for (Game game : gameDay.getGames()) {
                    constraintViolated = teamConstraintViolated(teamRecentGames.get(game.getHomeTeam().getAbbreviation())) &&
                        teamConstraintViolated(teamRecentGames.get(game.getRoadTeam().getAbbreviation()));
                    if (!constraintViolated) {
                        teamsPlaying.add(game.getRoadTeam().getAbbreviation());
                        teamsPlaying.add(game.getHomeTeam().getAbbreviation());
                    }
                }
                if (!constraintViolated) {
                    for (Team team : getTeamList()) {
                        if(!teamRecentGames.get(team.getAbbreviation()).offer(teamsPlaying.contains(team.getAbbreviation()))) {
                            teamRecentGames.get(team.getAbbreviation()).poll();
                            teamRecentGames.get(team.getAbbreviation()).offer(teamsPlaying.contains(team.getAbbreviation()));
                        }
                    }
                    gameDay.setDate(date);
                    date = date.plusDays(1);
                    sortedGameDays.add(gameDay);
                    gameDays.remove(gameDay);
                    offDayPointer = (offDayPointer + 1) % offDayPattern.length;
                    gameAdded = true;
                } else {
                    gamesPointer++;
                }
            }
        }

        season.setDays(sortedGameDays);

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

    public static boolean teamConstraintViolated(Queue<Boolean> teamPreviousDays) {
        if (teamPreviousDays == null) {
            return false;
        }
        Object[] previousDays = teamPreviousDays.toArray();
        // If team has played the two previous days
        if (previousDays.length >= 2 && (boolean) previousDays[previousDays.length - 1] && (boolean) previousDays[previousDays.length - 2]) {
            return true;
        }

        // If team has played 3 times in the last 4 days
        int timesPlayed = 0;
        for (int i = previousDays.length - 1; i >= 1; i--) {
            if ((boolean) previousDays[i]) {
                timesPlayed++;
            }
        }
        if (timesPlayed >= 3) {
            return true;
        }

        return false;
    }

    public static List<Day> createGameDays(List<Game> games) {
        Collections.shuffle(games);
        List<Day> gameDays = new ArrayList<Day>();
        HashMap<Integer, Integer> gameDayQuantities = getGameDayQuantities();

        ArrayList<Integer> reversed = new ArrayList<>();
        for (int i = gameDayQuantities.keySet().size() - 1; i >= 0; i--) {
            reversed.add((Integer) gameDayQuantities.keySet().toArray()[i]);
        }

        for (int i : reversed) {
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
        // TODO: if there are unscheduled games, add to 0 game day(s)
//        while (!games.isEmpty()) {
//
//        }
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

    public static List<Team> getTeamList() {
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
        return teamList;
    }
}
