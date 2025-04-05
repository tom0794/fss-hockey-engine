package io.github.tom0794.database;

import java.sql.*;
import java.util.*;

import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbOperations {
    private static final String DB_NAME = "fsshockey";
    private static final String PLAYER_TABLE_NAME = "player";
    private static final String PLAYER_TABLE_PK = "playerId";
    private static final String SKATER_TABLE_NAME = "skater";
    private static final String GOALTENDER_TABLE_NAME = "goaltender";
    private static final String TEAM_TABLE_NAME = "team";
    private static final String TEAM_TABLE_PK = "teamId";
    private static final String DIVISION_TABLE_NAME = "division";
    private static final String DIVISION_TABLE_PK = "divisionId";
    private static final String CONFERENCE_TABLE_NAME = "conference";
    private static final String CONFERENCE_TABLE_PK = "conferenceId";
    private static final String POSITION_TABLE_NAME = "position";
    private static final String POSITION_TABLE_PK = "positionId";
    private static final String COUNTRY_TABLE_NAME = "country";
    private static final String COUNTRY_TABLE_PK = "countryId";
    private static final String SEASON_TABLE_NAME = "season";
    private static final String SEASON_TABLE_PK = "seasonId";
    private static final String DAY_TABLE_NAME = "day";
    private static final String DAY_TABLE_PK = "dayId";
    private static final String GAME_TABLE_NAME = "game";
    private static final String GAME_TABLE_PK = "gameId";
    private static final Logger logger = LoggerFactory.getLogger("fss-hockey-database");

    public static Boolean executeSqlUpdate(String sql, String database) {
        try (Connection connect = DbConnection.connect(database)) {
            Statement statement = connect.createStatement();
            statement.executeUpdate(sql);
            logger.info("Successfully executed statement: [{}]", sql);
        } catch (SQLException e) {
            logger.error("Error executing statement [{}]: {}", sql, e.getMessage());
            return false;
        }
        return true;
    }

    public static void createDatabase() {
        logger.info("Creating database {}", DB_NAME);
        String sql = "CREATE DATABASE " + DB_NAME;
        if (executeSqlUpdate(sql, "")) {
            logger.info("Database {} created", DB_NAME);
        }
    }

    public static void dropDatabase() {
        logger.info("Dropping database {}", DB_NAME);
        String sql = "DROP DATABASE " + DB_NAME;
        if (executeSqlUpdate(sql, "")) {
            logger.info("Database {} dropped", DB_NAME);
        }
    }

    public static void createTablePlayer() {
        String sql = "CREATE TABLE IF NOT EXISTS \"${PLAYER_TABLE_NAME}\" (" +
                "\"${PLAYER_TABLE_PK}\" SERIAL PRIMARY KEY," +
                "\"${TEAM_TABLE_PK}\" INTEGER NOT NULL," +
                "\"positionPrimaryId\" INTEGER NOT NULL," +
                "\"${COUNTRY_TABLE_PK}\" INTEGER NOT NULL," +
                "\"firstName\" VARCHAR (50) NOT NULL," +
                "\"lastName\" VARCHAR (50) NOT NULL," +
                "\"height\" INTEGER NOT NULL," +
                "\"weight\" INTEGER NOT NULL," +
                "\"number\" INTEGER NOT NULL," +
                "\"dateOfBirth\" DATE NOT NULL," +
                "FOREIGN KEY (\"${TEAM_TABLE_PK}\") REFERENCES \"${TEAM_TABLE_NAME}\" (\"${TEAM_TABLE_PK}\")," +
                "FOREIGN KEY (\"positionPrimaryId\") REFERENCES \"${POSITION_TABLE_NAME}\" (\"${POSITION_TABLE_PK}\")," +
                "FOREIGN KEY (\"${COUNTRY_TABLE_PK}\") REFERENCES \"${COUNTRY_TABLE_NAME}\" (\"${COUNTRY_TABLE_PK}\")" +
                ")";
        if (executeSqlUpdate(interpolateConstants(sql), DB_NAME)) {
            logger.info("Table {} created", PLAYER_TABLE_NAME);
        }
        createTableSkater();
        createTableGoaltender();
    }

    public static void createTableSkater() {
        String sql = "CREATE TABLE IF NOT EXISTS \"${SKATER_TABLE_NAME}\" (" +
                "\"positionSecondaryId\" INTEGER," +
                "\"positionTertiaryId\" INTEGER," +
                "\"skating\" INTEGER NOT NULL," +
                "\"shooting\" INTEGER NOT NULL," +
                "\"passing\" INTEGER NOT NULL," +
                "\"physicality\" INTEGER NOT NULL," +
                "\"faceoffs\" INTEGER NOT NULL," +
                "\"defense\" INTEGER NOT NULL," +
                "\"puckHandling\" INTEGER NOT NULL," +
                "\"isForward\" BOOLEAN NOT NULL," +
                "FOREIGN KEY (\"positionSecondaryId\") REFERENCES \"${POSITION_TABLE_NAME}\" (\"${POSITION_TABLE_PK}\")," +
                "FOREIGN KEY (\"positionTertiaryId\") REFERENCES \"${POSITION_TABLE_NAME}\" (\"${POSITION_TABLE_PK}\")" +
                ") INHERITS (\"${PLAYER_TABLE_NAME}\")";
        if (executeSqlUpdate(interpolateConstants(sql), DB_NAME)) {
            logger.info("Table {} created", SKATER_TABLE_NAME);
        }
    }

    public static void createTableGoaltender() {
        String sql = "CREATE TABLE IF NOT EXISTS \"${GOALTENDER_TABLE_NAME}\" (" +
                "\"reflexes\" INTEGER NOT NULL," +
                "\"rebounds\" INTEGER NOT NULL," +
                "\"agility\" INTEGER NOT NULL," +
                "\"puckHandling\" INTEGER NOT NULL" +
                ") INHERITS (\"${PLAYER_TABLE_NAME}\")";
        if (executeSqlUpdate(interpolateConstants(sql), DB_NAME)) {
            logger.info("Table {} created", GOALTENDER_TABLE_NAME);
        }
    }

    public static void createTableConference() {
        String sql = "CREATE TABLE IF NOT EXISTS \"${CONFERENCE_TABLE_NAME}\" (" +
                "\"${CONFERENCE_TABLE_PK}\" SERIAL PRIMARY KEY," +
                "\"name\" VARCHAR (50) NOT NULL," +
                "\"abbreviation\" VARCHAR (50) NOT NULL" +
                ")";
        if (executeSqlUpdate(interpolateConstants(sql), DB_NAME)) {
            logger.info("Table {} created", TEAM_TABLE_NAME);
        }
    }

    public static void createTableDivision() {
        String sql = "CREATE TABLE IF NOT EXISTS \"${DIVISION_TABLE_NAME}\" (" +
                "\"${DIVISION_TABLE_PK}\" SERIAL PRIMARY KEY," +
                "\"${CONFERENCE_TABLE_PK}\" INTEGER NOT NULL," +
                "\"name\" VARCHAR (50) NOT NULL," +
                "\"abbreviation\" VARCHAR (50) NOT NULL," +
                "FOREIGN KEY (\"${CONFERENCE_TABLE_PK}\") REFERENCES \"${CONFERENCE_TABLE_NAME}\" (\"${CONFERENCE_TABLE_PK}\")" +
                ")";
        if (executeSqlUpdate(interpolateConstants(sql), DB_NAME)) {
            logger.info("Table {} created", TEAM_TABLE_NAME);
        }
    }

    public static void createTableTeam() {
        String sql = "CREATE TABLE IF NOT EXISTS \"${TEAM_TABLE_NAME}\" (" +
                "\"${TEAM_TABLE_PK}\" SERIAL PRIMARY KEY," +
                "\"${DIVISION_TABLE_PK}\" INTEGER NOT NULL," +
                "\"name\" VARCHAR (50) NOT NULL," +
                "\"abbreviation\" VARCHAR (50) NOT NULL," +
                "\"city\" VARCHAR (50) NOT NULL," +
                "\"primaryColour\" VARCHAR (6) NOT NULL," +
                "\"secondaryColour\" VARCHAR (6) NOT NULL," +
                "\"tertiaryColour\" VARCHAR (6)," +
                "FOREIGN KEY (\"${DIVISION_TABLE_PK}\") REFERENCES \"${DIVISION_TABLE_NAME}\" (\"${DIVISION_TABLE_PK}\")" +
                ")";
        if (executeSqlUpdate(interpolateConstants(sql), DB_NAME)) {
            logger.info("Table {} created", TEAM_TABLE_NAME);
        }
    }

    public static void createTableCountry() {
        String sql = "CREATE TABLE IF NOT EXISTS \"${COUNTRY_TABLE_NAME}\" (" +
                "\"${COUNTRY_TABLE_PK}\" SERIAL PRIMARY KEY," +
                "\"name\" VARCHAR (50) NOT NULL" +
                ")";
        if (executeSqlUpdate(interpolateConstants(sql), DB_NAME)) {
            logger.info("Table {} created", COUNTRY_TABLE_NAME);
        }
    }

    public static void createTablePosition() throws SQLException {
        String dropSql = "DROP TABLE IF EXISTS ${POSITION_TABLE_NAME} CASCADE";
        if (executeSqlUpdate(interpolateConstants(dropSql), DB_NAME)) {
            logger.info("Table {} dropped", POSITION_TABLE_NAME);
        } else {
            throw new SQLException("Error creating table ", POSITION_TABLE_NAME);
        }

        String sql = "CREATE TABLE IF NOT EXISTS \"${POSITION_TABLE_NAME}\" (" +
                "\"${POSITION_TABLE_PK}\" SERIAL PRIMARY KEY," +
                "\"name\" VARCHAR (50) NOT NULL," +
                "\"forward\" BOOLEAN NOT NULL" +
                ")";
        if (executeSqlUpdate(interpolateConstants(sql), DB_NAME)) {
            logger.info("Table {} created", POSITION_TABLE_NAME);
        } else {
            throw new SQLException("Error creating table ", POSITION_TABLE_NAME);
        }

        String insertSql = "INSERT INTO ${POSITION_TABLE_NAME} (name, forward) VALUES" +
                "('Center', true)," +
                "('Left Wing', true)," +
                "('Right Wing', true)," +
                "('Left Defense', false)," +
                "('Right Defense', false)," +
                "('Goaltender', false)";
        if (executeSqlUpdate(interpolateConstants(insertSql), DB_NAME)) {
            logger.info("Table {} populated", POSITION_TABLE_NAME);
        } else {
            throw new SQLException("Error populating table ", POSITION_TABLE_NAME);
        }
    }

    public static void createTableSeason() {
        String sql = "CREATE TABLE IF NOT EXISTS \"${SEASON_TABLE_NAME}\" (" +
                "\"${SEASON_TABLE_PK}\" SERIAL PRIMARY KEY," +
                "\"year\" VARCHAR (50) NOT NULL" +
                ")";
        if (executeSqlUpdate(interpolateConstants(sql), DB_NAME)) {
            logger.info("Table {} created", SEASON_TABLE_NAME);
        }
    }

    public static void createTableDay() {
        String sql = "CREATE TABLE IF NOT EXISTS \"${DAY_TABLE_NAME}\" (" +
                "\"${DAY_TABLE_PK}\" SERIAL PRIMARY KEY," +
                "\"${SEASON_TABLE_PK}\" INTEGER NOT NULL," +
                "\"date\" DATE NOT NULL," +
                "FOREIGN KEY (\"${SEASON_TABLE_PK}\") REFERENCES \"${SEASON_TABLE_NAME}\" (\"${SEASON_TABLE_PK}\")" +
                ")";
        if (executeSqlUpdate(interpolateConstants(sql), DB_NAME)) {
            logger.info("Table {} created", DAY_TABLE_NAME);
        }
    }

    public static void createTableGame() {
        String sql = "CREATE TABLE IF NOT EXISTS \"${GAME_TABLE_NAME}\" (" +
                "\"${GAME_TABLE_PK}\" SERIAL PRIMARY KEY," +
                "\"homeTeamId\" INTEGER NOT NULL," +
                "\"roadTeamId\" INTEGER NOT NULL," +
                "\"${DAY_TABLE_PK}\" INTEGER NOT NULL," +
                "FOREIGN KEY (\"homeTeamId\") REFERENCES \"${TEAM_TABLE_NAME}\" (\"${TEAM_TABLE_PK}\")," +
                "FOREIGN KEY (\"roadTeamId\") REFERENCES \"${TEAM_TABLE_NAME}\" (\"${TEAM_TABLE_PK}\")," +
                "FOREIGN KEY (\"${DAY_TABLE_PK}\") REFERENCES \"${DAY_TABLE_NAME}\" (\"${DAY_TABLE_PK}\")" +
                ")";
        if (executeSqlUpdate(interpolateConstants(sql), DB_NAME)) {
            logger.info("Table {} created", GAME_TABLE_NAME);
        }
    }

    public static String interpolateConstants(String input) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("DB_NAME", DB_NAME);
        parameters.put("PLAYER_TABLE_NAME", PLAYER_TABLE_NAME);
        parameters.put("PLAYER_TABLE_PK", PLAYER_TABLE_PK);
        parameters.put("SKATER_TABLE_NAME", SKATER_TABLE_NAME);
        parameters.put("GOALTENDER_TABLE_NAME", GOALTENDER_TABLE_NAME);
        parameters.put("TEAM_TABLE_NAME", TEAM_TABLE_NAME);
        parameters.put("TEAM_TABLE_PK", TEAM_TABLE_PK);
        parameters.put("DIVISION_TABLE_NAME", DIVISION_TABLE_NAME);
        parameters.put("DIVISION_TABLE_PK", DIVISION_TABLE_PK);
        parameters.put("CONFERENCE_TABLE_NAME", CONFERENCE_TABLE_NAME);
        parameters.put("CONFERENCE_TABLE_PK", CONFERENCE_TABLE_PK);
        parameters.put("POSITION_TABLE_NAME", POSITION_TABLE_NAME);
        parameters.put("POSITION_TABLE_PK", POSITION_TABLE_PK);
        parameters.put("COUNTRY_TABLE_NAME", COUNTRY_TABLE_NAME);
        parameters.put("COUNTRY_TABLE_PK", COUNTRY_TABLE_PK);
        parameters.put("SEASON_TABLE_NAME", SEASON_TABLE_NAME);
        parameters.put("SEASON_TABLE_PK", SEASON_TABLE_PK);
        parameters.put("DAY_TABLE_NAME", DAY_TABLE_NAME);
        parameters.put("DAY_TABLE_PK", DAY_TABLE_PK);
        parameters.put("GAME_TABLE_NAME", GAME_TABLE_NAME);
        parameters.put("GAME_TABLE_PK", GAME_TABLE_PK);
        StringSubstitutor substitutor = new StringSubstitutor(parameters);
        return substitutor.replace(input);
    }

    public static Integer insert(String table, HashMap<String, Object> inputValues) {
        StringBuilder insertString = new StringBuilder("INSERT INTO " + table + " (");
        StringBuilder columns =  new StringBuilder();
        StringBuilder values =  new StringBuilder();
        for (int i = 0; i < inputValues.keySet().size(); i++) {
            String separator = inputValues.keySet().size() - 1 == i ? ") " : ", ";
            columns.append("\"").append(inputValues.keySet().toArray()[i]).append("\"").append(separator);
            values.append("'").append(inputValues.values().toArray()[i]).append("'").append(separator);
        }
        insertString.append(columns).append("VALUES (").append(values);

        try (Connection connect = DbConnection.connect(DB_NAME);
             PreparedStatement insert = connect.prepareStatement(String.valueOf(insertString), Statement.RETURN_GENERATED_KEYS)) {
            logger.info("Insert statement: {}", insertString);
            insert.executeUpdate();
            ResultSet resultSet = insert.getGeneratedKeys();
            if (resultSet.next()) {
                logger.info("Insert into {} successful, id is {}", table, resultSet.getInt(1));
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    public static Integer update(String table, String primaryKey, HashMap<String, Object> updateValues) {
        StringBuilder updateString = new StringBuilder("UPDATE " + table + " SET ");
        for (int i = 0; i < updateValues.size(); i++) {
            String separator = updateValues.keySet().size() - 1 == i ? " " : ", ";
            updateString.append("\"")
                    .append(updateValues.keySet().toArray()[i])
                    .append("\" = '")
                    .append(updateValues.values().toArray()[i]).append("'")
                    .append(separator);
        }
        updateString.append("WHERE \"").append(primaryKey).append("\" = ").append(updateValues.get(primaryKey));
        logger.info(String.valueOf(updateString));
        if (executeSqlUpdate(String.valueOf(updateString), DB_NAME)) {
            return 0;
        }
        return null;
    }

    public static HashMap<String, Object> retrieve(String table, String primaryKey, Integer id) {
        String selectString = "SELECT * FROM " + table + " WHERE \"" + primaryKey + "\" = " + id;
        logger.info(selectString);
        try (Connection connect = DbConnection.connect(DB_NAME);
             PreparedStatement select = connect.prepareStatement(selectString)) {
            ResultSet resultSet = select.executeQuery();
            ResultSetMetaData rsMetaData = resultSet.getMetaData();
            if (resultSet.next()) {
                HashMap<String, Object> result = new HashMap<String, Object>();
                for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
                    String key = rsMetaData.getColumnName(i);
                    result.put(key, resultSet.getObject(key));
                }
                logger.info("result: {}", result);
                return result;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public static List<HashMap<String, Object>> retrieveAll(String table) {
        String selectString = "SELECT * FROM " + table;
        logger.info(selectString);
        List<HashMap<String, Object>> results = new ArrayList<>();
        try (Connection connect = DbConnection.connect(DB_NAME);
             PreparedStatement select = connect.prepareStatement(selectString)) {
            ResultSet resultSet = select.executeQuery();
            ResultSetMetaData rsMetaData = resultSet.getMetaData();
            while (resultSet.next()) {
                HashMap<String, Object> result = new HashMap<String, Object>();
                for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
                    String key = rsMetaData.getColumnName(i);
                    result.put(key, resultSet.getObject(key));
                }
                logger.info("result: {}", result);
                results.add(result);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return results;
    }

    public static Integer delete(String table, String primaryKey, Integer id) {
        String deleteString = "DELETE FROM " + table + " WHERE \"" + primaryKey + "\" = " + id;
        logger.info(deleteString);
        if (executeSqlUpdate(deleteString, DB_NAME)) {
            return 0;
        }
        return null;
    }
}
