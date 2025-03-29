package io.github.tom0794.schedule;

import io.github.tom0794.objects.Game;
import io.github.tom0794.objects.Season;
import io.github.tom0794.objects.Team;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.time.LocalDate;
import static java.time.temporal.TemporalAdjusters.firstInMonth;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class ScheduleUtils {

    private static final Object lock = new Object();
    private static final Logger logger = LoggerFactory.getLogger("fss-hockey-logic");
    private static final int interDivisionGames = 6;
    private static final int interConferenceGames = 4;
    private static final int intraConferenceGames = 2;

    private static final int offDayMaxGames = 6;

    private static List<Day> globalSortedDays;
    private static long timeoutTimeMillis;

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

    // pseudocode
    // createSeason
    //
    /*
    * Call createSeasonGames to get list of 1312 games -> This is the game pool
    * Shuffle it
    * Add games that don't violate constraints to current day until number of games is 16 or no more games can be added
    * New constraint: at least 1 game per day
    * you have game days so far, and game pool
    * game days so far will always be compliant with constraints
    * because it will only add games to a day if no constraints are violated
    * if you call the add a game day function with days so far and remaining pool, at there are no games in the pool
    *   that can be added without violating a constraint, then that is a dead end, and it should go back a day
    * for each day, do we need to create every possible day? non-constraint violating day?
    *
    * current day: use current game pool to get every possible day
    *   - for each possible day, add it to sorted, call create season on sorted and remaining game pool
    * */
    // recursion: call valid season on current list of games and remaining pool

    /*
    * Minigame: given a pool of 1312 games, calculate every possible 16 game day
    * apply constraints:
    * - no team can play more than one game in the same day
    * - no three days with games in a row for any team
    * - Teams can play at most 3 games in 5 days
    * Then the same thing for 15, 14, 13...1 game days
    * - eliminate games from the pool that involve teams that violate a constraint
    * - first thing is to apply constraints to the pool -> create filtered pool
    * - no games in filtered pool => dead end
    * */

    public static Season createSeason2(int year, String yearString) {
        Season season = new Season(yearString);

        List<Game> initialGamePool = createSeasonGames(getTeamList(), 2000 - year);
        HashMap<String, ArrayList<Boolean>> teamGameHistory = new HashMap<>();
        for (Team t : getTeamList()) {
            teamGameHistory.put(t.getAbbreviation(), new ArrayList<>());
        }

        season = createSeasonInternal(season, new ArrayList<Day>(), initialGamePool, teamGameHistory);

        return season;
    }

    public static Season createSeasonInternal(Season season, List<Day> days, List<Game> gamePool, HashMap<String, ArrayList<Boolean>> teamGameHistory) {
        if (gamePool.isEmpty()) {
            return season;
        }
        List<Game> filteredGamePool = getFilteredGamePool(gamePool, teamGameHistory);
        List<String> ineligibleTeams = new ArrayList<String>();
        Day newGameDay = new Day();
        int filteredGamePoolSentinel = 0;
        while (newGameDay.getGames().size() < 16 && filteredGamePoolSentinel < filteredGamePool.size()) {
            Game candidateGame = filteredGamePool.get(filteredGamePoolSentinel);
            if (!ineligibleTeams.contains(candidateGame.getHomeTeam().getAbbreviation()) &&
                    !ineligibleTeams.contains(candidateGame.getRoadTeam().getAbbreviation())) {
                newGameDay.addGame(candidateGame);
                gamePool.remove(candidateGame);
                ineligibleTeams.add(candidateGame.getHomeTeam().getAbbreviation());
                ineligibleTeams.add(candidateGame.getRoadTeam().getAbbreviation());
            }
            filteredGamePoolSentinel++;
        }

        return null;
    }

    public static boolean isSeasonValid(List<Day> days, List<Game> gamePool, HashMap<String, ArrayList<Boolean>> teamGameHistory) {

        return true;
    }

    /*
    * Consumes a game pool and team history map and returns a filtered game pool. The filtered game pool will not contain
    * any teams that are ineligible to play.
    * @param inputGamePool
    * @parameter: teamGameHistory
    * */
    public static List<Game> getFilteredGamePool(List<Game> inputGamePool, HashMap<String, ArrayList<Boolean>> teamGameHistory) {
        List<Game> filteredGamePool = new ArrayList<Game>();

        List<String> ineligibleTeams = new ArrayList<String>();
        for (String team : teamGameHistory.keySet()) {
            if (teamConstraintViolated(teamGameHistory.get(team))) {
                ineligibleTeams.add(team);
            }
        }

        for (Game game : inputGamePool) {
            Team homeTeam = game.getHomeTeam();
            Team roadTeam = game.getRoadTeam();
            if (ineligibleTeams.contains(homeTeam.getAbbreviation()) || ineligibleTeams.contains(roadTeam.getAbbreviation())) {
                continue;
            }
            filteredGamePool.add(game);
        }

        return filteredGamePool;
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
        HashMap<String, ArrayList<Boolean>> teamRecentGames = new HashMap<>();
        for (Team t : getTeamList()) {
            teamRecentGames.put(t.getAbbreviation(), new ArrayList<>());
        }

//        int attempts = 30;
//        for (int i = 0; i < attempts; i++) {
            timeoutTimeMillis = System.currentTimeMillis() + 3000;
            gameDays = createGameDays(createSeasonGames(getTeamList(), 2000 - year));
            Collections.shuffle(gameDays);
            try {
                validSeason(gameDays, sortedGameDays, teamRecentGames, offDayPointer, offDayPattern);
//                break;
            } catch (Exception e) {
                //logger.info("Timeout reached on attempt {}", i + 1);
            }
//        }


        season.setDays(globalSortedDays);
        for (Day day : season.getDays()) {
            logger.info("Day {}", day.getGames());
        }

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

    public static boolean teamConstraintViolated(ArrayList<Boolean> teamPreviousDays) {
        Object[] previousDays = teamPreviousDays.toArray();
        // If team has played the two previous days
        if (previousDays.length >= 2 && (boolean) previousDays[previousDays.length - 1] && (boolean) previousDays[previousDays.length - 2]) {
            return true;
        }

        // If team has played 3 times in the last 4 days
        int timesPlayed = 0;
        for (int i = previousDays.length - 1; i >= previousDays.length - Math.min(4, previousDays.length); i--) {
            if ((boolean) previousDays[i]) {
                timesPlayed++;
            }
        }
        if (timesPlayed >= 3) {
            return true;
        }

        return false;
    }

    public static boolean validSeason(
            List<Day> unsortedDays,
            List<Day> sortedDays,
            HashMap<String, ArrayList<Boolean>> teamGameHistory,
            int offDayPointer,
            String[] offDayPattern
    ) throws Exception {
        if (System.currentTimeMillis() > timeoutTimeMillis) {
            throw new Exception("Timeout reached");
        }
//        List<Day> newUnsortedDays = new ArrayList<>();
//        for (Day d : unsortedDays) {
//            newUnsortedDays.add(new Day(d));
//        }
//        List<Day> newSortedDays = new ArrayList<>();
//        for (Day d : sortedDays) {
//            newSortedDays.add(new Day(d));
//        }
//        HashMap<String, ArrayList<Boolean>> newTeamGameHistory = new HashMap<>();
//        for (String key : teamGameHistory.keySet()) {
//            newTeamGameHistory.put(key, new ArrayList<>());
//            for (boolean value : teamGameHistory.get(key)) {
//                newTeamGameHistory.get(key).add(value);
//            }
//        }
//        String[] newOffDayPattern = Arrays.copyOf(offDayPattern, offDayPattern.length);

        if (unsortedDays.size() == 1) {
            logger.info("Reached unsorted days size 1");
            boolean constraintViolated = false;
            for (Game game : unsortedDays.get(0).getGames()) {
                constraintViolated = teamConstraintViolated(teamGameHistory.get(game.getHomeTeam().getAbbreviation())) ||
                        teamConstraintViolated(teamGameHistory.get(game.getRoadTeam().getAbbreviation()));
                if (constraintViolated) {
                    break;
                }
            }
            if (!constraintViolated) {
                globalSortedDays = sortedDays;
                globalSortedDays.add(unsortedDays.get(0));
                return true;
            }
            return false;
        }

        for (Day day : unsortedDays) {
//            if ((offDayPattern[offDayPointer].equals("OFF") && (day.getGames().size() > offDayMaxGames)) ||
//                    (offDayPattern[offDayPointer].equals("ON") && (day.getGames().size() <= offDayMaxGames))) {
//                continue;
//            }

            List<String> teamsPlaying = new ArrayList<>();
            boolean constraintViolated = false;

            HashMap<String, ArrayList<Boolean>> newTeamGameHistory = new HashMap<>();
            for (String key : teamGameHistory.keySet()) {
                newTeamGameHistory.put(key, new ArrayList<>());
                for (boolean value : teamGameHistory.get(key)) {
                    newTeamGameHistory.get(key).add(value);
                }
            }

            for (Game game : day.getGames()) {
                constraintViolated = teamConstraintViolated(newTeamGameHistory.get(game.getHomeTeam().getAbbreviation())) ||
                        teamConstraintViolated(newTeamGameHistory.get(game.getRoadTeam().getAbbreviation()));
                if (constraintViolated) {
                    break;
                }
                teamsPlaying.add(game.getHomeTeam().getAbbreviation());
                teamsPlaying.add(game.getRoadTeam().getAbbreviation());
            }
            if (constraintViolated) {
                continue;
            }
            for (Team team : getTeamList()) {
                newTeamGameHistory.get(team.getAbbreviation()).add(teamsPlaying.contains(team.getAbbreviation()));
            }

            offDayPointer = (offDayPointer + 1) % offDayPattern.length;
            List<Day> newUnsortedDays = new ArrayList<>();
            for (Day d : unsortedDays) {
                newUnsortedDays.add(d);
            }
            List<Day> newSortedDays = new ArrayList<>();
            for (Day d : sortedDays) {
                newSortedDays.add(d);
            }
            newUnsortedDays.remove(day);
            newSortedDays.add(day);

            if (validSeason(newUnsortedDays, newSortedDays, newTeamGameHistory, offDayPointer, offDayPattern)) {
                return true;
            }
        }
        logger.info("dead end, unsorted: {} --- sorted: {}", unsortedDays.size(), sortedDays.size());
        return false;
    }

    public static List<Day> createGameDays(List<Game> games) {
        Collections.shuffle(games);
        List<Day> gameDays = new ArrayList<Day>();
        // would the schedule end up balanced if game day quantities were removed?
        // seems like it would naturally end up with off days
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
//        gameDayQuantities.put(16, 8);
//        gameDayQuantities.put(15, 10);
//        gameDayQuantities.put(14, 10);
//        gameDayQuantities.put(13, 10);
//        gameDayQuantities.put(12, 10);
//        gameDayQuantities.put(11, 10);
//        gameDayQuantities.put(10, 10);
//        gameDayQuantities.put(8, 10); // 78 on days
//
//        gameDayQuantities.put(6, 12); // 104 off days
//        gameDayQuantities.put(5, 22);
//        gameDayQuantities.put(4, 18);
//        gameDayQuantities.put(3, 18);
//        gameDayQuantities.put(2, 20);
//        gameDayQuantities.put(1, 6);
//        gameDayQuantities.put(0, 8);

//        gameDayQuantities.put(16, 4);
//        gameDayQuantities.put(15, 6);
//        gameDayQuantities.put(14, 4);
//        gameDayQuantities.put(13, 6);
//        gameDayQuantities.put(12, 16);
//        gameDayQuantities.put(11, 10);
//        gameDayQuantities.put(10, 10);
//        gameDayQuantities.put(9, 11);
//        gameDayQuantities.put(8, 11); // 78 on days
//
//        gameDayQuantities.put(7, 20);
//        gameDayQuantities.put(6, 20); // 104 off days
//        gameDayQuantities.put(5, 9);
//        gameDayQuantities.put(4, 18);
//        gameDayQuantities.put(3, 8);
//        gameDayQuantities.put(2, 14);
//        gameDayQuantities.put(1, 6);
//        gameDayQuantities.put(0, 9);

        // nhl, off day = 7 games or less
        gameDayQuantities.put(16, 2);
        gameDayQuantities.put(15, 4);
        gameDayQuantities.put(14, 8);
        gameDayQuantities.put(13, 15);
        gameDayQuantities.put(12, 13);
        gameDayQuantities.put(11, 9);
        gameDayQuantities.put(10, 9);
        gameDayQuantities.put(9, 13);
        gameDayQuantities.put(8, 11); // 78 on days

        gameDayQuantities.put(7, 6);
        gameDayQuantities.put(6, 12); // 104 off days
        gameDayQuantities.put(5, 12);
        gameDayQuantities.put(4, 20);
        gameDayQuantities.put(3, 18);
        gameDayQuantities.put(2, 24);
        gameDayQuantities.put(1, 7);
        gameDayQuantities.put(0, 9);

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
