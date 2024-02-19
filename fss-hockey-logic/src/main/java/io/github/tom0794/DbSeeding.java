package io.github.tom0794;

import io.github.tom0794.database.DbOperations;
import io.github.tom0794.objects.Conference;
import io.github.tom0794.objects.Division;
import io.github.tom0794.objects.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.*;

public class DbSeeding {
    private static final Logger logger = LoggerFactory.getLogger("fss-hockey-database");

    public static void createTables() throws SQLException {
        DbOperations.createTablePosition();
        DbOperations.createTableCountry();
        DbOperations.createTableConference();
        DbOperations.createTableDivision();
        DbOperations.createTableTeam();
        DbOperations.createTablePlayer();
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

        ArrayList<Team> teamList = new ArrayList<Team>();
        teamList.add(new Team(divisionList.get(0).getDivisionId(), "Montreal", "Canadiens", "MTL", "000000", "000000"));
        teamList.add(new Team(divisionList.get(0).getDivisionId(), "Ottawa", "Senators", "OTT", "000000", "000000"));
        teamList.add(new Team(divisionList.get(0).getDivisionId(), "Boston", "Bruins", "BOS", "000000", "000000"));
        teamList.add(new Team(divisionList.get(0).getDivisionId(), "New York", "Rangers", "NYR", "000000", "000000"));
        teamList.add(new Team(divisionList.get(1).getDivisionId(), "Columbus", "Blue Jackets", "CBJ", "000000", "000000"));
        teamList.add(new Team(divisionList.get(1).getDivisionId(), "Carolina", "Hurricanes", "CAR", "000000", "000000"));
        teamList.add(new Team(divisionList.get(1).getDivisionId(), "Florida", "Panthers", "FLA", "000000", "000000"));
        teamList.add(new Team(divisionList.get(1).getDivisionId(), "Tampa Bay", "Lightning", "TBL", "000000", "000000"));
        teamList.add(new Team(divisionList.get(2).getDivisionId(), "Pittsburgh", "Penguins", "PIT", "000000", "000000"));
        teamList.add(new Team(divisionList.get(2).getDivisionId(), "Philadelphia", "Flyers", "PHI", "000000", "000000"));
        teamList.add(new Team(divisionList.get(2).getDivisionId(), "New Jersey", "Devils", "NJD", "000000", "000000"));
        teamList.add(new Team(divisionList.get(2).getDivisionId(), "Washington", "Capitals", "WSH", "000000", "000000"));
        teamList.add(new Team(divisionList.get(3).getDivisionId(), "Detroit", "Red Wings", "DET", "000000", "000000"));
        teamList.add(new Team(divisionList.get(3).getDivisionId(), "Toronto", "Maple Leafs", "TOR", "000000", "000000"));
        teamList.add(new Team(divisionList.get(3).getDivisionId(), "Buffalo", "Sabres", "BUF", "000000", "000000"));
        teamList.add(new Team(divisionList.get(3).getDivisionId(), "New York", "Islanders", "NYI", "000000", "000000"));

        teamList.add(new Team(divisionList.get(4).getDivisionId(), "Seattle", "Kraken", "SEA", "000000", "000000"));
        teamList.add(new Team(divisionList.get(4).getDivisionId(), "Vancouver", "Canucks", "VAN", "000000", "000000"));
        teamList.add(new Team(divisionList.get(4).getDivisionId(), "Calgary", "Flames", "CGY", "000000", "000000"));
        teamList.add(new Team(divisionList.get(4).getDivisionId(), "Edmonton", "Oilers", "EDM", "000000", "000000"));
        teamList.add(new Team(divisionList.get(5).getDivisionId(), "Colorado", "Avalanche", "COL", "000000", "000000"));
        teamList.add(new Team(divisionList.get(5).getDivisionId(), "Dallas", "Stars", "DAL", "000000", "000000"));
        teamList.add(new Team(divisionList.get(5).getDivisionId(), "Nashville", "Predators", "NSH", "000000", "000000"));
        teamList.add(new Team(divisionList.get(5).getDivisionId(), "Salt Lake City", "Zephyrs", "SLC", "000000", "000000"));
        teamList.add(new Team(divisionList.get(6).getDivisionId(), "San Jose", "Sharks", "SJS", "000000", "000000"));
        teamList.add(new Team(divisionList.get(6).getDivisionId(), "Los Angeles", "Kings", "LAK", "000000", "000000"));
        teamList.add(new Team(divisionList.get(6).getDivisionId(), "Anaheim", "Ducks", "ANA", "000000", "000000"));
        teamList.add(new Team(divisionList.get(6).getDivisionId(), "Las Vegas", "Golden Knights", "VGK", "000000", "000000"));
        teamList.add(new Team(divisionList.get(7).getDivisionId(), "Chicago", "Blackhawks", "CHI", "000000", "000000"));
        teamList.add(new Team(divisionList.get(7).getDivisionId(), "Winnipeg", "Jets", "WPG", "000000", "000000"));
        teamList.add(new Team(divisionList.get(7).getDivisionId(), "St. Louis", "Blues", "STL", "000000", "000000"));
        teamList.add(new Team(divisionList.get(7).getDivisionId(), "Minnesota", "Wild", "MIN", "000000", "000000"));
        for (Team team : teamList) {
            team.createTeam();
        }
    }
}
