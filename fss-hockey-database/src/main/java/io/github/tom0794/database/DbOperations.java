package io.github.tom0794.database;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbOperations {
    private static final String DB_NAME = "fsshockey";
    private static final String PLAYER_TABLE_NAME = "player";
    private static final String PLAYER_TABLE_PK = "player_id";
    private static final String SKATER_TABLE_NAME = "skater";
    private static final String GOALTENDER_TABLE_NAME = "goaltender";
    private static final String TEAM_TABLE_NAME = "team";
    private static final String TEAM_TABLE_PK = "team_id";
    private static final String POSITION_TABLE_NAME = "position";
    private static final String POSITION_TABLE_PK = "position_id";
    private static final String COUNTRY_TABLE_NAME = "country";
    private static final String COUNTRY_TABLE_PK = "country_id";
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

    // TODO: refactor/remove
    public static void createTable() {
        try (Connection connect = DbConnection.connect("")) {
            Statement newTable = connect.createStatement();
            String sql = "CREATE TABLE PLAYER " +
                    "(id INTEGER not NULL, " +
                    " firstName VARCHAR(255), " +
                    " lastName VARCHAR(255), " +
                    " PRIMARY KEY ( id ))";
            newTable.executeUpdate(sql);
        }
        catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public static void createTablePlayer() {
        String sql = "CREATE TABLE IF NOT EXISTS ${PLAYER_TABLE_NAME} (" +
                "${PLAYER_TABLE_PK} SERIAL PRIMARY KEY," +
                "${TEAM_TABLE_PK} INTEGER NOT NULL," +
                "position_primary_id INTEGER NOT NULL," +
                "${COUNTRY_TABLE_PK} INTEGER NOT NULL," +
                "first_name VARCHAR (50) NOT NULL," +
                "last_name VARCHAR (50) NOT NULL," +
                "height INTEGER NOT NULL," +
                "weight INTEGER NOT NULL," +
                "number INTEGER NOT NULL," +
                "dob DATE NOT NULL," +
                "FOREIGN KEY (${TEAM_TABLE_PK}) REFERENCES ${TEAM_TABLE_NAME} (${TEAM_TABLE_PK})," +
                "FOREIGN KEY (position_primary_id) REFERENCES ${POSITION_TABLE_NAME} (${POSITION_TABLE_PK})," +
                "FOREIGN KEY (${COUNTRY_TABLE_PK}) REFERENCES ${COUNTRY_TABLE_NAME} (${COUNTRY_TABLE_PK})" +
                ")";
        if (executeSqlUpdate(interpolateConstants(sql), DB_NAME)) {
            logger.info("Table {} created", PLAYER_TABLE_NAME);
        }
        createTableSkater();
        createTableGoaltender();
    }

    public static void createTableSkater() {
        String sql = "CREATE TABLE IF NOT EXISTS ${SKATER_TABLE_NAME} (" +
                "position_secondary_id INTEGER," +
                "position_tertiary_id INTEGER," +
                "skating INTEGER NOT NULL," +
                "shooting INTEGER NOT NULL," +
                "passing INTEGER NOT NULL," +
                "physicality INTEGER NOT NULL," +
                "faceoffs INTEGER NOT NULL," +
                "defense INTEGER NOT NULL," +
                "puck_handling INTEGER NOT NULL," +
                "is_forward BOOLEAN NOT NULL," +
                "FOREIGN KEY (position_secondary_id) REFERENCES ${POSITION_TABLE_NAME} (${POSITION_TABLE_PK})," +
                "FOREIGN KEY (position_tertiary_id) REFERENCES ${POSITION_TABLE_NAME} (${POSITION_TABLE_PK})" +
                ") INHERITS (${PLAYER_TABLE_NAME})";
        if (executeSqlUpdate(interpolateConstants(sql), DB_NAME)) {
            logger.info("Table {} created", SKATER_TABLE_NAME);
        }
    }

    public static void createTableGoaltender() {
        String sql = "CREATE TABLE IF NOT EXISTS ${GOALTENDER_TABLE_NAME} (" +
                "reflexes INTEGER NOT NULL" +
                ") INHERITS (${PLAYER_TABLE_NAME})";
        if (executeSqlUpdate(interpolateConstants(sql), DB_NAME)) {
            logger.info("Table {} created", GOALTENDER_TABLE_NAME);
        }
    }

    public static void createTableTeam() {
        String sql = "CREATE TABLE IF NOT EXISTS ${TEAM_TABLE_NAME} (" +
                "${TEAM_TABLE_PK} SERIAL PRIMARY KEY," +
                "name VARCHAR (50) NOT NULL" +
                ")";
        if (executeSqlUpdate(interpolateConstants(sql), DB_NAME)) {
            logger.info("Table {} created", TEAM_TABLE_NAME);
        }
    }

    public static void createTableCountry() {
        String sql = "CREATE TABLE IF NOT EXISTS ${COUNTRY_TABLE_NAME} (" +
                "${COUNTRY_TABLE_PK} SERIAL PRIMARY KEY," +
                "name VARCHAR (50) NOT NULL" +
                ")";
        if (executeSqlUpdate(interpolateConstants(sql), DB_NAME)) {
            logger.info("Table {} created", COUNTRY_TABLE_NAME);
        }
    }

    public static void createTablePosition() throws SQLException {
        String dropSql = "DROP TABLE IF EXISTS ${POSITION_TABLE_NAME}";
        if (executeSqlUpdate(interpolateConstants(dropSql), DB_NAME)) {
            logger.info("Table {} dropped", POSITION_TABLE_NAME);
        } else {
            throw new SQLException("Error creating table ", POSITION_TABLE_NAME);
        }

        String sql = "CREATE TABLE IF NOT EXISTS ${POSITION_TABLE_NAME} (" +
                "${POSITION_TABLE_PK} SERIAL PRIMARY KEY," +
                "name VARCHAR (50) NOT NULL," +
                "forward BOOLEAN NOT NULL" +
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

    public static String interpolateConstants(String input) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("DB_NAME", DB_NAME);
        parameters.put("PLAYER_TABLE_NAME", PLAYER_TABLE_NAME);
        parameters.put("PLAYER_TABLE_PK", PLAYER_TABLE_PK);
        parameters.put("SKATER_TABLE_NAME", SKATER_TABLE_NAME);
        parameters.put("GOALTENDER_TABLE_NAME", GOALTENDER_TABLE_NAME);
        parameters.put("TEAM_TABLE_NAME", TEAM_TABLE_NAME);
        parameters.put("TEAM_TABLE_PK", TEAM_TABLE_PK);
        parameters.put("POSITION_TABLE_NAME", POSITION_TABLE_NAME);
        parameters.put("POSITION_TABLE_PK", POSITION_TABLE_PK);
        parameters.put("COUNTRY_TABLE_NAME", COUNTRY_TABLE_NAME);
        parameters.put("COUNTRY_TABLE_PK", COUNTRY_TABLE_PK);
        StringSubstitutor substitutor = new StringSubstitutor(parameters);
        return substitutor.replace(input);
    }
}
