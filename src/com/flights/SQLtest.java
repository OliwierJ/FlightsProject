package com.flights;

import java.sql.*;

public class SQLtest {
    public static void main(String[] args) throws Exception{
        final String URL = "jdbc:mysql://localhost:3306/flights_project";
        final String USERNAME = "project";
        final String PASSWORD = "project";
        Class.forName("com.mysql.cj.jdbc.Driver"); // import jar file if error
        Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        String url = "jdbc:mysql://localhost:3306/flights_project";
        String username = "project";
        String password = "project";
        String query = "SELECT * FROM booking";

        Class.forName("com.mysql.cj.jdbc.Driver");


        Connection con = DriverManager.getConnection(url, username, password);
        System.out.println("Connection established successfully");
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        rs.next();
        String name = rs.getString("email");
        System.out.println(name);
        st.close();
        con.close();
        System.out.println("Connection closed");
    }
}
