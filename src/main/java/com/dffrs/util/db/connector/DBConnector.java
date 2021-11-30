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


    private static Connection establishConnection() throws SQLException, IllegalArgumentException {
        Connection conn;

        String url = prop.getProperty("url");
        String user = prop.getProperty("user");
        String pass = prop.getProperty("password");

        if (url.isEmpty() || user.isEmpty() || pass.isEmpty()) {
            // If this exception is thrown, it means their values are empty.
            throw new IllegalArgumentException();
        }

        conn = DriverManager.getConnection(url, user, pass);

        System.out.println("Establishing connection to DB as "+ user +".\n ");
        return conn;
    }

    private static void closeConnectionToDB(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
                System.out.println("Connection to DB closed.\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

        } catch (SQLException | IllegalArgumentException e) {
            if (e instanceof IllegalArgumentException)
                System.err.println("ERROR: Values with empty parameters. Check db.properties file.\n");
            e.printStackTrace();
        } finally {
            closeConnectionToDB(conn);
        }
    }
}
