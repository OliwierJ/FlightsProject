package com.flights;

import java.sql.*;

public class SQLtest {
    public static void main(String[] args) throws Exception{
        final String URL = "jdbc:mysql://localhost:3306/flights_project";
        final String USERNAME = "project";
        final String PASSWORD = "project";
        Class.forName("com.mysql.cj.jdbc.Driver"); // import jar file if error
        Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        System.out.println("Connection established successfully");

        String query = "SELECT * FROM booking";
        Statement st = con.createStatement();
//        st.executeUpdate("DROP TABLE booking"); // should throw access denied error
        st.executeQuery(query);

        ResultSet rs = st.executeQuery(query);
        rs.next();
        String name = rs.getString("email");
        System.out.println(name);
        // close connection
        st.close();
        con.close();
        System.out.println("Connection closed");
    }
}
