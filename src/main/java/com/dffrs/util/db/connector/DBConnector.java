package com.dffrs.util.db.connector;

import com.dffrs.comp.time.Time;

import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * Class (Singleton) responsible to manage all connections to a MySQL Database.
 * It writes and reads from it.
 *
 * @author dffrs.
 */
public final class DBConnector {

    /**
     * Properties instance to access db.properties file and load db's parameters.
     */
    private static final Properties prop;

    /**
     * String to keep the OS username.
     */
    private static final String USER_OS;

    static {
        USER_OS = System.getProperty("user.name");
        prop = new Properties();
        try {
            prop.load(DBConnector.class.getResourceAsStream(("/db/properties/db.properties")));
        } catch (IOException e) {
            System.err.println("ERROR: Could not get properties from db.properties file." +
                "Check the file.\n");
            e.printStackTrace();
        }
    }

    /**
     * Private static method to establish a connection to the database. Every method that writes or reads
     * from it, must call this method first.
     * It does not close the connection - for that the calling method must call, as well,
     * {@link #closeConnectionToDB(Connection)} at the end.
     *
     * @return Connection instance, if the connection was established normally. NULL otherwise.
     * @throws SQLException             Everytime a SQLException occurs.
     * @throws IllegalArgumentException Everytime there is problem reading db.properties parameters.
     */
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

        System.out.println("Establishing connection to DB as " + user + ".\n ");
        return conn;
    }

    /**
     * Private method to close a previously established connection to the database.
     *
     * @param conn Connection instance to close the connection.
     */
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

    /**
     * Public static method to save a project, as well as the time spent on it,
     * to the database. It uses {@link #establishConnection()} method to connect to the db
     * and {@link #closeConnectionToDB(Connection)} to close it.
     *
     * @param name Name of the project to save.
     * @param desc Project's description.
     * @param time Instance of Time representing the time spent working on the project.
     */
    public static void saveTimeToDB(String name, String desc, Time time) {
        Connection conn = null;

        try {

            conn = establishConnection();


            java.sql.Time timeSQL = java.sql.Time.valueOf(time.toString());

            String query = "{CALL SaveTime(?, ?, ?, ?)}";
            CallableStatement stmt = conn.prepareCall(query);

            stmt.setString(1, name);
            stmt.setString(2, desc);
            stmt.setString(3, USER_OS);
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

    /**
     * Public static method to query the database for all projects saved, based on {@link #USER_OS}.
     * It uses {@link #establishConnection()} to connect to the db, and {@link #closeConnectionToDB(Connection)}
     * to close the connection.
     *
     * @return HashMap with all the information about the projects saved, as well as the time that the project
     * was saved, to the db, and the time spent working on it.
     */
    public static Map<String, List<String>> getUserProjects() {
        Map<String, List<String>> mapOfSavedProjects = new HashMap<>();

        //DB Table's names.
        List<String> columnNamesList = List.of("user_name", "project_name", "starting_time", "time_spent");

        Connection conn = null;

        try {
            conn = establishConnection();

            String query = "{CALL GetUserTimeSpentPerProject(?)}";
            CallableStatement stmt = conn.prepareCall(query);

            stmt.setString(1, USER_OS);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                for (String columnName : columnNamesList) {
                    mapOfSavedProjects.computeIfAbsent(columnName, k -> new ArrayList<>());
                    mapOfSavedProjects.get(columnName).add(rs.getString(columnName));
                }
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Getting User's saved Projects.\n");
            e.printStackTrace();
        } finally {
            closeConnectionToDB(conn);
        }

        return mapOfSavedProjects;
    }
}
