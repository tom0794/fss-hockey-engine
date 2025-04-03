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
import java.util.concurrent.ThreadLocalRandom;

public class ScheduleUtils {
    private static final Logger logger = LoggerFactory.getLogger("fss-hockey-logic");
    private static final int interDivisionGames = 6;
    private static final int interConferenceGames = 4;
    private static final int MAX_OFF_DAY_GAMES = 7;
    private static final int MAX_ON_DAY_GAMES = 15;
    private static LocalDate currentDate;

    private static final HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> divisionMatchupMappings = new HashMap<Integer, HashMap<Integer, ArrayList<Integer>>>();

    public static List<Game> createSeasonGames(List<Team> teams, int year) {
        List<Game> games = new ArrayList<>();
        HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> divisionMachupMappings = createDivisionMatchupMappings();
        HashMap<Integer, ArrayList<Integer>> divisionMapping = divisionMachupMappings.get(getDivisionMatchupMapping(year, divisionMachupMappings));

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

                // Add intra-conference games
                // One home game and one away game against each of the teams in the two divisions in the opposite conference
                // the current division is mapped against
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

    /**
     * Teams play one game against each opponent in the opposite conference. They will be at home against two divisions
     * and on the road against the other two.
     * Example: mapping 1, division id 1 is home against division 6 and 7
     * @return
     */
    public static HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> createDivisionMatchupMappings() {
        HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> divisionMatchupMappings = new HashMap<Integer, HashMap<Integer, ArrayList<Integer>>>();
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
        return divisionMatchupMappings;
    }

    public static int getDivisionMatchupMapping(int year, HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> divisionMatchupMappings) {
        int frequency = divisionMatchupMappings.size();
        return (year - (frequency * (year / frequency)));
    }

    public static Season createSeason(int year, String yearString, LocalDate startDate) {
        Season season = new Season(yearString);
        currentDate = startDate;

        List<Game> initialGamePool = createSeasonGames(getTeamList(), year - 2000);
        // improvement: instead of shuffling the games, arrange them in some consistent, incrementable way
        // if an arrangement is invalid try the next one
        Collections.shuffle(initialGamePool);
        HashMap<String, ArrayList<Boolean>> teamGameHistory = new HashMap<>();
        for (Team t : getTeamList()) {
            teamGameHistory.put(t.getAbbreviation(), new ArrayList<>());
        }

        season = createSeasonInternal(season, initialGamePool, teamGameHistory, 0);

        return season;
    }

    /**
     * Create a Season populated with a full schedule of Games.
     * @param season
     * @param gamePool
     * @param teamGameHistory
     * @param offDayPointer
     * @return Season with all games scheduled once the pool of games is empty
     */
    private static Season createSeasonInternal(Season season, List<Game> gamePool, HashMap<String, ArrayList<Boolean>> teamGameHistory, int offDayPointer) {
        // params: probably need to pass by value/clone
        if (gamePool.isEmpty()) {
            return season;
        }
        List<Game> filteredGamePool = getFilteredGamePool(gamePool, teamGameHistory);

        List<String> ineligibleTeams = new ArrayList<String>();

        Day newGameDay = new Day();
        int filteredGamePoolSentinel = 0;

        String[] offDayPattern = {"OFF", "OFF", "ON", "OFF", "ON", "OFF", "ON"};
        int offDayMax = ThreadLocalRandom.current().nextInt(1, MAX_OFF_DAY_GAMES + 1);
        int onDayMax = ThreadLocalRandom.current().nextInt(MAX_OFF_DAY_GAMES + 3, MAX_ON_DAY_GAMES + 1);
        int maxGames = offDayPattern[offDayPointer].equals("ON") ? onDayMax : offDayMax;
        while (newGameDay.getGames().size() < maxGames && filteredGamePoolSentinel < filteredGamePool.size()) {
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
        for (Team team : getTeamList()) {
            teamGameHistory.get(team.getAbbreviation()).add(ineligibleTeams.contains(team.getAbbreviation()));
        }
        newGameDay.setDate(currentDate);
        currentDate = currentDate.plusDays(1);
        season.addDay(newGameDay);
        offDayPointer = (offDayPointer + 1) % offDayPattern.length;
        return createSeasonInternal(season, gamePool, teamGameHistory, offDayPointer);
    }

    /**
     * Consumes a game pool and team history map and returns a filtered game pool. The filtered game pool will not contain
     * any teams that are ineligible to play.
     * @param inputGamePool List of Games
     * @param teamGameHistory History of games played for each team in the previous five days
     * @return Filtered list of Games
     */
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

    /**
     * Consumes an array of integers representing indices in a collection and a collection size.
     * Increments the indices in the array to the next combination.
     * @param inputIndices
     * @param collectionSize
     * @Example: Input [0, 1, 2, 3, 4] and 10 output [0, 1, 2, 3, 5]
     * @return Incremented array of indices; null if array can't be incremented
     */
    public static int[] incrementIndices(int[] inputIndices, int collectionSize) {
        int maxIndexOfIndexArray = inputIndices.length - 1;
        int maxIndexOfCollection = collectionSize - 1;
        int currentIndex = maxIndexOfIndexArray;

        while (true) {
            if (inputIndices[currentIndex] + 1 <= maxIndexOfCollection) {
                inputIndices[currentIndex] += 1;
                for (int i = currentIndex; i < maxIndexOfIndexArray; i++) {
                    inputIndices[i + 1] = inputIndices[i] + 1;
                }
                break;
            }

            if (currentIndex == 0) {
                return null;
            }
            currentIndex--;
            maxIndexOfCollection--;
        }
        return inputIndices;
    }

    /**
     *
     * @param teamPreviousDays
     * @return true if a constraint is violated for the given array of previous days
     */
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

    // Need to get this from the database
    // Should seed DB with this sample data
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
