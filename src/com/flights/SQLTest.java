package com.flights;

import java.sql.*;

public class SQLTest {
    public static void main(String[] args) throws Exception{
        final String URL = "jdbc:mysql://localhost:3306/flights_project";
        final String USERNAME = "project";
        final String PASSWORD = "project";
        Class.forName("com.mysql.cj.jdbc.Driver"); // import jar file if error
        Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        String query = "SELECT * FROM booking";

        Class.forName("com.mysql.cj.jdbc.Driver");
        Statement st = null;
        try {
            System.out.println("Connection established successfully");
            st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData metaData = rs.getMetaData();
            int noOfColumns = metaData.getColumnCount();

            for (int i = 1; i <= noOfColumns; i++) {
                System.out.print(metaData.getColumnName(i) + "\t");
            }
            System.out.println("\n--------------------------------------------------------");
            while (rs.next()) {
                for (int i = 1; i <= noOfColumns; i++) {
                    System.out.print(rs.getObject(i) + "\t\t");
                }
                System.out.println();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            assert st != null;
            st.close();
            con.close();
            System.out.println("\nConnection closed");

        }
    }
}
