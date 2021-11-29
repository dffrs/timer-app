package com.dffrs.util.db.connector;

import com.dffrs.comp.time.Time;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DBConnector {

    private static final Properties prop;

    static {
        prop = new Properties();
        try {
            prop.load(DBConnector.class.getResourceAsStream(("/db/properties/db.properties")));
        } catch (IOException e) {
            System.err.println("ERROR: Could not get properties from db.properties file." +
                "Check the file.\n");
            e.printStackTrace();
        }
    }


    private static Connection establishConnection() throws SQLException {
        Connection conn;

        String url = prop.getProperty("url");
        String user = prop.getProperty("user");
        String pass = prop.getProperty("password");

        conn = DriverManager.getConnection(url, user, pass);

        return conn;
    }

    public static void saveTimeToDB(String name, String desc, Time time) {
        Connection conn = null;

        try {

            conn = establishConnection();

            String user = System.getProperty("user.name");
            java.sql.Time timeSQL = java.sql.Time.valueOf(time.toString());

            String query = "{CALL SaveTime(?, ?, ?, ?)}";
            CallableStatement stmt = conn.prepareCall(query);

            stmt.setString(1, name);
            stmt.setString(2, desc);
            stmt.setString(3, user);
            stmt.setTime(4, timeSQL);

            stmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
