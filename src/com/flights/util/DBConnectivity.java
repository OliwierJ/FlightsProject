package com.flights.util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public abstract class DBConnectivity {
    private static Connection con;
    private static Statement stmt;
    private static PreparedStatement pstmt;
    private static final String URL = "jdbc:mysql://localhost:3306/flights_project";
    private static final String USERNAME = "project";
    private static final String PASSWORD = "project";

    // connects and executes a QUERY (e.g. SELECT) that doesn't modify the database, returns a result set
    public static ResultSet connectAndExecuteQuery(String query) throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // import jar file if error
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            return stmt.executeQuery(query);
        } catch (ClassNotFoundException e) {
            JErrorDialog.showError("A fatal error occurred! Program will now exit", e);
            System.exit(1);
        }
        return null;
    }

    // converts the next row of a result set into a 1d array and closes connection
    public static String[] getRow(ResultSet rs) throws SQLException {
        int colNo = rs.getMetaData().getColumnCount();
        String[] result = new String[colNo];
        rs.next();
        for (int i = 1; i <= colNo; i++) {
            result[i-1] = rs.getString(i);
        }
        closeConnection();
        return result;
    }

    // converts the next row of a result set into a 1d array, method only used by getMultipleRows()
    private static String[] getRowNoClose(ResultSet rs) throws SQLException {
        int colNo = rs.getMetaData().getColumnCount();
        String[] result = new String[colNo];
        rs.next();
        for (int i = 1; i <= colNo; i++) {
            result[i-1] = rs.getString(i);
        }
        return result;
    }

    // converts all rows of a result set into a 2d array format
    public static String[][] getMultipleRows(ResultSet rs) throws SQLException {
        int colNo = rs.getMetaData().getColumnCount();
        rs.last();
        int rowNo = rs.getRow();
        rs.beforeFirst();
        String[][] result = new String[rowNo][colNo];

        for (int i = 0; i < result.length; i++) {
            result[i] = getRowNoClose(rs);
        }
        closeConnection();
        return result;
    }

    // closes a connection after executing
    public static void closeConnection() {
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
    public static void connectAndExecuteUpdate(String query) throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // import jar file if error
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            pstmt = con.prepareStatement(query);
            pstmt.executeUpdate();
        } catch (ClassNotFoundException e) {
            JErrorDialog.showError("A fatal error occurred! Program will now exit", e);
            System.exit(1);
        }
    }

    // gets entire table from a given table name
    public static String[][] getTable(String name) throws SQLException {
        return getMultipleRows(connectAndExecuteQuery("SELECT * FROM "+name));
    }

    public static JTable getTableFromQuery(String query) throws SQLException {
        ResultSet rs = connectAndExecuteQuery(query);
        ResultSetMetaData rsmd = rs.getMetaData();
        String[][] data = getMultipleRows(rs);
        String[] colNames = new String[rsmd.getColumnCount()];
        for (int i = 0; i < rsmd.getColumnCount(); i++) {
            colNames[i] = rsmd.getColumnLabel(i+1);
        }
        return new JTable(data, colNames);
    }

    public static DefaultTableModel getTableModelFromQuery(String query) throws SQLException {
        ResultSet rs = connectAndExecuteQuery(query);
        ResultSetMetaData rsmd = rs.getMetaData();
        String[][] data = getMultipleRows(rs);
        String[] colNames = new String[rsmd.getColumnCount()];
        for (int i = 0; i < rsmd.getColumnCount(); i++) {
            colNames[i] = rsmd.getColumnLabel(i+1);
        }
        return new DefaultTableModel(data, colNames);
    }

    protected abstract void updateDatabase(); // override this method to implement updating the database based on the contents of the class
}
