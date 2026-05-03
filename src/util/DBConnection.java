package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class — provides a single shared SQLite connection.
 * Singleton pattern: only one connection is created.
 */
public class DBConnection {

    private static final String DB_URL = "jdbc:sqlite:library.db";
    private static Connection connection = null;

    // Private constructor — no one can instantiate this class
    private DBConnection() {}

    /**
     * Returns the single shared connection.
     * Creates it if it doesn't exist yet.
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(DB_URL);
                connection.setAutoCommit(true);
                System.out.println("Database connected successfully.");
            } catch (ClassNotFoundException e) {
                throw new SQLException("SQLite JDBC Driver not found. Add sqlite-jdbc jar to lib/", e);
            }
        }
        return connection;
    }

    /**
     * Closes the connection — call this when app shuts down.
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
