package com.flights.util;

import java.sql.*;

/**
 * Class providing database access and functionality
 */
public abstract class DBConnectivity {
    private static Connection con;
    private static PreparedStatement pstmt;
    private static PreparedStatement pstmt2;
    private static final String URL = "jdbc:mysql://localhost:3306/flights_project";
    private static final String USERNAME = "project";
    private static final String PASSWORD = "project";

    // This will always close the database connection even if the program crashes or uses System.exit()
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (con != null && !con.isClosed()) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }
        }));
    }

    // private method for creating the database connection
    private static void connectToDB() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // import jar file if error
            if (con == null || con.isClosed()) {
                con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
        } catch (ClassNotFoundException e) {
            JErrorDialog.showError("A fatal error occurred! Program will now exit", e);
            System.exit(1);
        }
    }

    /**
     * Closes all database connections and resets PreparedStatement(s)
     */
    public static void closeConnection() {
        try {
            pstmt.close();
        } catch (Exception ignored) {
        } finally {
            try {
                pstmt2.close();
            } catch (Exception ignored) {
            } finally {
                try {
                    con.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    /**
     * Connects and executes a QUERY (e.g. SELECT) that doesn't modify the database
     * @param query the query to execute
     * @return the ResultSet of the query
     * @throws SQLException if anything goes wrong
     */
    public static ResultSet connectAndExecuteQuery(String query) throws SQLException {
        connectToDB();
        pstmt2 = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        return pstmt2.executeQuery();
    }

    /**
     * Converts the next row of a ResultSet into a String[] array
     * @param rs ResultSet
     * @return String[] of the next row
     * @throws SQLException if any exception occurs
     */
    public static String[] getRow(ResultSet rs) throws SQLException {
        int colNo = rs.getMetaData().getColumnCount();
        String[] result = new String[colNo];
        rs.next();
        for (int i = 1; i <= colNo; i++) {
            result[i-1] = rs.getString(i);
        }
        return result;
    }

    /**
     * Converts all rows of a ResultSet into a String[][] array
     * @param rs ResultSet
     * @return String[][] of all data
     * @throws SQLException if any exception occurs
     */
    public static String[][] getMultipleRows(ResultSet rs) throws SQLException {
        int colNo = rs.getMetaData().getColumnCount();
        rs.last();
        int rowNo = rs.getRow();
        rs.beforeFirst();
        String[][] result = new String[rowNo][colNo];
        for (int i = 0; i < result.length; i++) {
            rs.next();
            for (int j = 1; j <= colNo; j++) {
                result[i][j-1] = rs.getString(j);
            }
        }
        return result;
    }

    /**
     * Connects (if needed) and adds an UPDATE (e.g. INSERT, UPDATE) that modifies the database to the batch of commands to be run when executeUpdates() is called
     * @param query the query to be added to the batch
     * @throws SQLException if any exception occurs
     */
    public static void addQueryToUpdate(String query) throws SQLException {
        if (con.isClosed()) {
            connectToDB();
        }
        if (pstmt == null || pstmt.isClosed()) {
            pstmt = con.prepareStatement(query);
            pstmt.addBatch();
        } else {
            pstmt.addBatch(query);
        }
    }

    /**
     * Add a query to a PreparedStatement and return the PreparedStatement
     * @param query query to be added
     * @return PreparedStatement
     * @throws SQLException if any exception occurs
     */
    public static PreparedStatement getPreparedStatement(String query) throws SQLException {
        connectToDB();
        pstmt = con.prepareStatement(query);
        return pstmt;
    }

    /**
     * Executes all updates in batch and closes all connections
     * @throws SQLException if any exception occurs
     */
    public static void executeUpdates() throws SQLException {
        pstmt.executeBatch();
        closeConnection();
    }

    /**
     * Override this method to implement updating the database in other classes
     */
    protected abstract void updateDatabase();
}
