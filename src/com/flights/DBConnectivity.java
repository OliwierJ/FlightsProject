package com.flights;

import java.sql.*;

public abstract class DBConnectivity {
    private static Connection con;
    private static Statement stmt;
    private static PreparedStatement pstmt;
    private static final String URL = "jdbc:mysql://localhost:3306/flights_project";
    private static final String USERNAME = "project";
    private static final String PASSWORD = "project";

    // connects and executes a QUERY (e.g. SELECT) that doesn't modify the database, returns a result set
    protected static ResultSet connectAndExecuteQuery(String query) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver"); // import jar file if error
        con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        return stmt.executeQuery(query);
    }

    // converts the next row of a result set into a 1d array and closes connection
    protected static String[] getRow(ResultSet rs) throws SQLException {
        int colNo = rs.getMetaData().getColumnCount();
        String[] result = new String[colNo];
        rs.next();
        for (int i = 1; i <= colNo; i++) {
            result[i-1] = rs.getString(i);
        }
        closeConnection();
        return result;
    }

    // converts the next row of a result set into a 1d array, method only used by getMultipleRows(), flag is for different signature
    private static String[] getRow(ResultSet rs, int flag) throws SQLException {
        int colNo = rs.getMetaData().getColumnCount(); // IGNROE DUPLICATED CODE WARNING FOR NOW
        String[] result = new String[colNo];
        rs.next();
        for (int i = 1; i <= colNo; i++) {
            result[i-1] = rs.getString(i);
        }
        return result;
    }

    // converts all rows of a result set into a 2d array format
    protected static String[][] getMultipleRows(ResultSet rs) throws SQLException {
        int colNo = rs.getMetaData().getColumnCount();
        rs.last();
        int rowNo = rs.getRow();
        rs.beforeFirst();
        String[][] result = new String[rowNo][colNo];

        for (int i = 0; i < result.length; i++) {
            result[i] = getRow(rs, 1);
        }
        closeConnection();
        return result;
    }

    // closes a connection after executing
    protected static void closeConnection() {
        // tries to close either statement or prepared statement, depending on the type of query used
        try {
            stmt.close();
        } catch (Exception e) {
            try {
                pstmt.close();
            } catch (Exception ignored) {
            }
        } finally {
            try {
                con.close();
            } catch (Exception ignored) {
            }
        }
    }

    // connects and executes an UPDATE (e.g. INSERT, UPDATE) that modifies the database
    protected static void connectAndExecuteUpdate(String query) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver"); // import jar file if error
        con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        pstmt = con.prepareStatement(query);
        pstmt.executeUpdate();
    }

    // gets entire table from a given table name
    protected static String[][] getTable(String name) throws SQLException, ClassNotFoundException {
        return getMultipleRows(connectAndExecuteQuery("SELECT * FROM "+name));
    }

    protected abstract void updateDatabase(); // override this method to implement updating the database based on the contents of the class
}
