package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {
    private static final String url = "jdbc:sqlite:insta.db";

    public static Connection connect() {
        System.out.println("MIPS");
        try {
            Connection conn = DriverManager.getConnection(url);
            System.out.println("SQLite connection established!");
            return conn;
        } catch (SQLException e) {
            System.out.println("Error connecting: " + e.getMessage());
            return null;
        }
    }

}
