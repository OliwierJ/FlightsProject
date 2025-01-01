package com.flights;

import java.sql.*;

public final class DBUtil {
    private static Connection con;
    private static Statement stmt;
    private static PreparedStatement pstmt;
    private static final String URL = "jdbc:mysql://localhost:3306/flights_project";
    private static final String USERNAME = "project";
    private static final String PASSWORD = "project";

    // connects and executes a QUERY (e.g. SELECT) that doesn't modify the database, returns a result set
    private static ResultSet connectAndExecuteQuery(String query) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver"); // import jar file if error
        con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        return stmt.executeQuery(query);
    }

    // converts the next row of a result set into a 1d array
    private static String[] getRow(ResultSet rs) throws Exception{
        int colNo = rs.getMetaData().getColumnCount();
        String[] result = new String[colNo];
        rs.next();
        for (int i = 1; i <= colNo; i++) {
            result[i-1] = rs.getString(i);
        }
        return result;
    }

    // converts all rows of a result set into a 2d array format
    private static String[][] getMultipleRows(ResultSet rs) throws Exception{
        int colNo = rs.getMetaData().getColumnCount();
        rs.last();
        int rowNo = rs.getRow();
        rs.beforeFirst();
        String[][] result = new String[rowNo][colNo];

        for (int i = 0; i < result.length; i++) {
            result[i] = getRow(rs);
        }
        closeConnection();
        return result;
    }

    // closes a connection after executing QUERY, uses statement
    private static void closeConnection() throws Exception{
        stmt.close();
        con.close();
    }

    // closes a connection after executing UPDATE, used prepared statement
    private static void closeUpdateConnection() throws Exception {
        pstmt.close();
        con.close();
    }

    // connects and executes an UPDATE (e.g. INSERT, UPDATE) that modifies the database
    private static void connectAndExecuteUpdate(String query) throws Exception{
        Class.forName("com.mysql.cj.jdbc.Driver"); // import jar file if error
        con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        pstmt = con.prepareStatement(query);
        pstmt.executeUpdate();
    }

    // gets entire table from a given table name
    public static String[][] getTable(String name) throws Exception {
        return getMultipleRows(connectAndExecuteQuery("SELECT * FROM "+name));
    }

    // gets basic booking information from a given booking ID
    public static String[] getBookingInfo(String bookingID) throws Exception{
        String[] result = getRow(connectAndExecuteQuery("SELECT * FROM booking WHERE booking_no="+bookingID));
        closeConnection();
        return result;
    }

    // gets all flights matching the given departure and arrival airport
    public static String[][] getFlightInfo(String dep, String arr) throws Exception {
        return getMultipleRows(connectAndExecuteQuery("SELECT * FROM flight WHERE departure_airport='"+dep+"' AND arrival_airport='"+arr+"'"));
    }
    // gets all the passenger info matching the passenger ID
    public static String[] getPassengerInfo(String passengerID) throws Exception{
        String[] result = getRow(connectAndExecuteQuery("SELECT * FROM passenger WHERE passenger_id="+passengerID));
        closeConnection();
        return result;
    }

    // gets just the passengers from the booking ID
    public static String[][] getPassengersFromBookingID(String bookingID) throws Exception {
        return getMultipleRows(connectAndExecuteQuery("SELECT passenger.*  FROM passenger WHERE booking_no="+bookingID));
    }
    // verifies given booking details, true if details valid, false if invalid
    public static boolean verifyBookingDetails(String bookingID, String email) throws Exception {
        boolean valid = connectAndExecuteQuery("SELECT * FROM booking WHERE booking_no='"+bookingID+"' AND email='"+email+"'").next(); // true if entry exists, false otherwise
        closeConnection();
        return valid;
    }

    /* Is this needed? Especially as the Passenger Class already holds this information */
    // gets all relevant passenger information from a given booking ID, contains name, surname, seat no, seat class
    public static String[][] getPassengerInfoFromBookingID(String bookingID) throws Exception {
        return getMultipleRows(connectAndExecuteQuery("SELECT passenger.first_name, passenger.last_name, seat_passenger.seat_no, seat_passenger.is_return, seat.class FROM ((seat_passenger INNER JOIN passenger ON seat_passenger.passenger_id = passenger.passenger_ID) INNER JOIN seat ON seat_passenger.seat_no = seat.seat_no) WHERE booking_no="+bookingID));
    }
    // gets relevant seat information for a passenger from a given passenger ID, so the seat number and seat class.
    public static String[][] getSeatFromPassengerID(String passengerID) throws Exception {
        return getMultipleRows(connectAndExecuteQuery("SELECT seat.seat_no, seat.class FROM seat_passenger INNER JOIN seat ON seat_passenger.seat_no = seat.seat_no WHERE passenger_id="+passengerID));
    }

    // gets all booked flights from a given booking ID, departure and return flight (if applicable)
    public static String[][] getFlightInfoFromBookingID(String bookingID) throws Exception {
        return getMultipleRows(connectAndExecuteQuery("SELECT flight.* FROM flight_booking INNER JOIN flight ON flight_booking.flight_id = flight.flight_id WHERE booking_no="+bookingID));
    }

    // gets information about an aircraft from a given aircraftID
    public static String[] getAircraftInfo(String aircraftID) throws Exception {
        String[] result = getRow(connectAndExecuteQuery("SELECT * FROM plane WHERE aircraft_id="+aircraftID));
        closeConnection();
        return result;
    }

    // creates a new booking with the given details and updates all relevant tables
    public static void addBooking(String bookingID, String email, int priority, int luggage, int depFlightID, int arrFlightID) throws Exception { //TODO: add flight to add booking
        connectAndExecuteUpdate("INSERT INTO booking (booking_no, email, priority_boarding, luggage_amount) VALUES ('"+bookingID+"', '"+email+"', "+priority+", "+luggage+")");
        connectAndExecuteUpdate("INSERT INTO flight_booking (flight_id, booking_no, is_return) VALUES ("+depFlightID+", '"+bookingID+"', 0)");
        if (arrFlightID != -1) {
            connectAndExecuteUpdate("INSERT INTO flight_booking (flight_id, booking_no, is_return) VALUES ("+arrFlightID+", '"+bookingID+"', 1)");
        }
        closeUpdateConnection();
    }

    // overloaded above method, used in the event no return flight is booked
    public static void addBooking(String bookingID, String email, int priority, int luggage, int depFlightID) throws Exception { //TODO: add flight to add booking
        addBooking(bookingID, email, priority, luggage, depFlightID, -1);
    }

    // adds a single passenger to a booking and updates all relevant tables
    public static void addPassengerWithSeats(String bookingID, String name, String surname, String depSeat, String returnSeat) throws Exception {
        connectAndExecuteUpdate("INSERT INTO passenger (first_name, last_name, booking_no) VALUES ('"+name+"', '"+surname+"', '"+bookingID+"')");

        String passengerID = getRow(connectAndExecuteQuery("SELECT passenger_ID FROM passenger WHERE first_name='"+name+"' AND last_name='"+surname+"' AND booking_no='"+bookingID+"'"))[0];
        String[][] flightIDs = getMultipleRows(connectAndExecuteQuery("SELECT flight_id FROM flight_booking WHERE booking_no='"+bookingID+"'"));
        connectAndExecuteUpdate("INSERT INTO seat_passenger (seat_no, passenger_id, is_return) VALUES ('"+depSeat+"', '"+passengerID+"', 0)");
        connectAndExecuteUpdate("UPDATE seat SET is_occupied=1 WHERE seat_no='"+depSeat+"' AND flight_id="+flightIDs[0][0]);
        if (returnSeat != null) {
            connectAndExecuteUpdate("INSERT INTO seat_passenger (seat_no, passenger_id, is_return) VALUES ('"+returnSeat+"', '"+passengerID+"', 1)");
            connectAndExecuteUpdate("UPDATE seat SET is_occupied=1 WHERE seat_no='"+returnSeat+"' AND flight_id="+flightIDs[1][0]);
        }
        closeConnection();
    }

    // overloaded method from above, used in the event no return flight is booked
    public static void addPassengerWithSeats(String bookingID, String name, String surname, String depSeat) throws Exception {
        addPassengerWithSeats(bookingID, name, surname, depSeat, null);
    }

    //TODO: update booking (passenger details, change seat, priority, luggage)
    //TODO: cancel booking (delete booking, passengers, free up seats, etc)
    //TODO: add planes and generate seats database (one time use)

    //TODO: if time allows, add accounts, accounts -> booking, one -> many, primary key username, foreign key in booking username, update ER
    //TODO: if needed, proper javadocs comments (ask jason) (private methods don't show up in javadocs)
}