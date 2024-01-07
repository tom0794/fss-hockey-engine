package io.github.tom0794.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbConnection {
    private static final String dockerHost = System.getenv().getOrDefault("DOCKER_HOST", "localhost");
    private static final String url = "jdbc:postgresql://" + dockerHost + ":5432/";
    private static final String user = "postgres";
    private static final String password = "hunter1";
    private static final Logger logger = LoggerFactory.getLogger("fss-hockey-database");

    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     */
    public static Connection connect(String database) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url + database, user, password);
            logger.info("Connected to the PostgreSQL server successfully [{}].", url + database);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return conn;
    }
}
