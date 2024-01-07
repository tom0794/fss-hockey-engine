package io.github.tom0794;

import io.github.tom0794.database.DbConnection;
import io.github.tom0794.database.DbOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
public class Main {
    private static final Logger logger = LoggerFactory.getLogger("fss-hockey-database");

    //TODO: delete this file
    public static void main(String[] args) {
        DbOperations.createTable();

        try (Connection connect = DbConnection.connect("")) {
            logger.info(connect.getCatalog());
            logger.info(connect.getSchema());
        }
        catch (SQLException e) {
            logger.error(e.getMessage());
        }


    }
}