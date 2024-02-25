package io.github.tom0794.schedule;

import io.github.tom0794.objects.Game;
import io.github.tom0794.objects.Team;

import java.util.*;

public class ScheduleUtils {
    private static final int interDivisionGames = 6;
    private static final int interConferenceGames = 4;
    private static final int intraConferenceGames = 2;

    private static HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> divisionMatchupMappings = new HashMap<Integer, HashMap<Integer, ArrayList<Integer>>>();

    public static List<Game> createSeasonGames(List<Team> teams, int year) {
        createDivisionMatchupMappings();
        List<Game> games = new ArrayList<>();
        HashMap<Integer, ArrayList<Integer>> divisionMapping = divisionMatchupMappings.get(getDivisionMatchupMapping(year));

        for (int i = 0; i < teams.size() - 1; i++) {
            if (teams.get(i).getDivisionId() == teams.get(i + 1).getDivisionId()) {
                for (int j = 0; j < interDivisionGames / 2; j++) {
                    games.add(new Game(teams.get(i), teams.get(i + 1)));
                    games.add(new Game(teams.get(i + 1), teams.get(i)));
                }
                continue;
            }
            // if same conference


            ArrayList<Integer> divisionsToPlay = divisionMapping.get(teams.get(i).getDivisionId());
            if (divisionsToPlay.contains(teams.get(i + 1).getDivisionId())) {
                for (int j = 0; j < intraConferenceGames / 2; j++) {
                    games.add(new Game(teams.get(i), teams.get(i + 1)));
                    games.add(new Game(teams.get(i + 1), teams.get(i)));
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
}
