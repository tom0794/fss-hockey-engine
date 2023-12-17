package io.github.tom0794;

import java.sql.*;
public class Main {

    private static final String url = "jdbc:postgresql://localhost:5432/";
    private static final String user = "postgres";
    private static final String password = "hunter1";

    public static void main(String[] args) {
        System.out.println("Hello world!");
        Connection connect = connect();
        try {
            System.out.println(connect.getCatalog());
            System.out.println(connect.getSchema());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     */
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }
}