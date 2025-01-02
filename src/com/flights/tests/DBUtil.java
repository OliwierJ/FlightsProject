package com.flights.tests;

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

    // closes a connection after executing
    private static void closeConnection() throws Exception{
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
        return getMultipleRows(connectAndExecuteQuery("SELECT passenger.passenger_ID, passenger.first_name, passenger.last_name, seat_passenger.seat_no, seat_passenger.is_return, seat.class FROM ((seat_passenger INNER JOIN passenger ON seat_passenger.passenger_id = passenger.passenger_ID) INNER JOIN seat ON seat_passenger.seat_no = seat.seat_no) WHERE booking_no="+bookingID));
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
        closeConnection();
    }

    // overloaded above method, used in the event no return flight is booked
    public static void addBooking(String bookingID, String email, int priority, int luggage, int depFlightID) throws Exception { //TODO: add flight to add booking
        addBooking(bookingID, email, priority, luggage, depFlightID, -1);
    }

    // adds a single passenger to a booking and updates all relevant tables
    // TODO: DOESN'T CHECK IS SEAT OCCUPIED OR NOT, THIS MUST BE IMPLEMENTED
    // Will throw SQLException if non-existing bookingID or seatNo provided, as these fields are used as foreign keys in these tables, input must be checked beforehand in driver class

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

    // updates booking details
    public static void updateBooking(String bookingID, String email, int priority, int noOfLuggage) throws Exception{
        ResultSet rs = connectAndExecuteQuery("SELECT * FROM booking WHERE booking_no='"+bookingID+"'");
        if (!rs.next()) {
            closeConnection();
            throw new IllegalArgumentException("No such booking exists!");
        }
        connectAndExecuteUpdate("UPDATE booking SET email='"+email+"', priority_boarding="+priority+", luggage_amount="+noOfLuggage+" WHERE booking_no="+bookingID);
        closeConnection();
    }

    // updates passenger details
    public static void updatePassenger(int passengerID, String name, String surname) throws Exception{
        ResultSet rs = connectAndExecuteQuery("SELECT * FROM passenger WHERE passenger_ID="+passengerID);
        if (!rs.next()) {
            closeConnection();
            throw new IllegalArgumentException("No such passenger exists!");
        }
        connectAndExecuteUpdate("UPDATE passenger SET first_name='"+name+"', last_name='"+surname+"' WHERE passenger_ID="+passengerID);
        closeConnection();
    }

    // private method updates a seat
    private static void updateSeat(int passengerID, String seat, int isReturn) throws Exception{ // 0 for departure flight, 1 for return flight
        ResultSet rs = connectAndExecuteQuery("SELECT * FROM passenger WHERE passenger_ID="+passengerID);
        if (!rs.next()) {
            closeConnection();
            throw new IllegalArgumentException("No such passenger exists!");
        }
        // gets seat from seat table that passenger is currently in, needs seat_no from seat_passenger table and flight_id of the booking
        // fully automatic way of getting flight ID, no need to pass around flightIDs or existing seats in driver program, just passengerID needed
        String[] flightIDS = getRow(connectAndExecuteQuery("SELECT DISTINCT flight_booking.flight_id FROM flight_booking INNER JOIN booking b on flight_booking.booking_no = b.booking_no INNER JOIN passenger p on b.booking_no = p.booking_no INNER JOIN seat_passenger sp on p.passenger_ID = sp.passenger_id WHERE sp.passenger_id="+passengerID+"AND flight_booking.is_return="+isReturn));
        String flightID = flightIDS[0];
        String[] existingSeat = getRow(connectAndExecuteQuery("SELECT seat_passenger.seat_no FROM seat_passenger WHERE passenger_id="+passengerID+" AND is_return=0"));
        String existingSeatNo = existingSeat[0];

        // sets the current seat to be not occupied anymore
        connectAndExecuteUpdate("UPDATE seat SET is_occupied=0 WHERE seat_no='"+existingSeatNo+"' AND flight_id="+flightID);
        // updates the new seat to be occupied
        connectAndExecuteUpdate("UPDATE seat SET is_occupied=1 WHERE seat_no='"+seat+"' AND flight_id="+flightID);
        connectAndExecuteUpdate("UPDATE seat_passenger SET seat_no='"+seat+"' WHERE passenger_id="+passengerID+" AND is_return="+isReturn);
        closeConnection();
    }

    // wrapper method for updateSeat
    public static void updatePassengerDepartureSeat(int passengerID, String depSeat) throws Exception{
        updateSeat(passengerID, depSeat, 0);
    }

    // wrapper method for updateSeat
    public static void updatePassengerReturnSeat(int passengerID, String returnSeat) throws Exception{
        updateSeat(passengerID, returnSeat, 1);
    }

    // updates passenger details and seat(s)
    public static void updatePassengerAndSeats(int passengerID, String name, String surname, String depSeat, String returnSeat) throws Exception{
        updatePassenger(passengerID, name, surname);
        updateSeat(passengerID, depSeat, 0);
        if (returnSeat != null) {
            updateSeat(passengerID, returnSeat, 1);
        }
    }

    // overloaded method if no return seat needed
    public static void updatePassengerAndSeats(int passengerID, String name, String surname, String depSeat) throws Exception{
        updatePassengerAndSeats(passengerID, name, surname, depSeat,null);
    }

    //TODO: update booking (passenger details, change seat, priority, luggage) TODO TESTING
    //TODO: cancel booking (delete booking, passengers, free up seats, etc)
    //TODO: add planes and generate seats database (one time use)

    //TODO: if time allows, add accounts, accounts -> booking, one -> many, primary key username, foreign key in booking username, update ER
    //TODO: if needed, proper javadocs comments (ask jason) (private methods don't show up in javadocs)
}