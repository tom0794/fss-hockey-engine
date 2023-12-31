package io.github.tom0794.database;

import java.sql.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbOperations {
    private static final String DB_NAME = "fsshockey";
    private static final Logger logger = LoggerFactory.getLogger("fss-hockey-database");

    public static Boolean executeSqlUpdate(String sql) {
        try (Connection connect = DbConnection.connect()) {
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
        if (executeSqlUpdate(sql)) {
            logger.info("Database {} created", DB_NAME);
        }
    }

    public static void dropDatabase() {
        logger.info("Dropping database {}", DB_NAME);
        String sql = "DROP DATABASE " + DB_NAME;
        if (executeSqlUpdate(sql)) {
            logger.info("Database {} dropped", DB_NAME);
        }
    }

    // TODO: refactor
    public static void createTable() {
        try (Connection connect = DbConnection.connect()) {
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
}
