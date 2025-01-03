package com.flights.objects;

import com.flights.DBConnectivity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Passenger extends DBConnectivity {
    private String name;
    private String surname;
    private String passengerID;
    private String bookingID;
    private Seat departureSeat;
    private Seat returnSeat;
    private boolean existingPassenger;

    // make a new empty passenger for a new booking
    public Passenger(String name, String surname, String bookingID) {
        this.name = name;
        this.surname = surname;
        this.bookingID = bookingID;
        this.departureSeat = null;
        this.returnSeat = null;
        this.existingPassenger = false;
        // passenger ID is created when saving to database for the first time
    }

    // gets an existing passenger from database
    public Passenger(String passengerID) {
        this.existingPassenger = true;
        try {
            String[] result = getRow(connectAndExecuteQuery("SELECT first_name, last_name, booking_no FROM passenger WHERE passenger_ID="+passengerID));
            this.name = result[0];
            this.surname = result[1];
            this.bookingID = result[2];
            this.passengerID = passengerID;

            // gets passenger seats
            String[][] seats = getMultipleRows(connectAndExecuteQuery("SELECT seat_no, class, aircraft_id, seat.flight_id, is_return FROM seat INNER JOIN flights_project.flight_booking fb on seat.flight_id = fb.flight_id WHERE passenger_id="+passengerID+" AND booking_no="+bookingID));
            if (seats.length == 0 || seats.length > 2) {
                throw new IllegalStateException();
            } else {
                for (String[] seat : seats) {
                    if (seat[4].equals("0")) {
                        departureSeat = new Seat(seat[0], seat[1], seat[2], seat[3], passengerID, false, true);
                    } else {
                        returnSeat = new Seat(seat[0], seat[1], seat[2], seat[3], passengerID, true, true);
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("An error occurred in passenger"+e.getMessage());
        }
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getBookingID() {
        return bookingID;
    }

    public String getPassengerID() {
        return passengerID;
    }

    public Seat getDepartureSeat() {
        return departureSeat;
    }

    public Seat getReturnSeat() {
        return returnSeat;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setDepartureSeat(Seat departureSeat) {
        this.departureSeat = departureSeat;
        this.departureSeat.setPassengerID(this.passengerID);
    }

    public void setReturnSeat(Seat returnSeat) {
        this.returnSeat = returnSeat;
        this.returnSeat.setPassengerID(this.passengerID);
    }

    @Override
    protected void updateDatabase() {
        //TODO: update database appropriately after passenger details have been changed
        try {
            if (existingPassenger) {
                // update existing passenger in database
                ResultSet rs = connectAndExecuteQuery("SELECT * FROM passenger WHERE passenger_ID="+passengerID);
                if (!rs.next()) {
                    throw new IllegalArgumentException("No such passenger exists!");
                }
                connectAndExecuteUpdate("UPDATE passenger SET first_name='"+name+"', last_name='"+surname+"' WHERE passenger_ID="+passengerID);
            } else {
                // add new passenger to database
                if (departureSeat == null) {
                    throw new IllegalStateException("No departure seat exists!");
                }
                connectAndExecuteUpdate("INSERT INTO passenger (first_name, last_name, booking_no) VALUES ('"+name+"', '"+surname+"', '"+bookingID+"')");

                this.passengerID = getRow(connectAndExecuteQuery("SELECT passenger_ID FROM passenger WHERE first_name='"+name+"' AND last_name='"+surname+"' AND booking_no='"+bookingID+"'"))[0];
                this.existingPassenger = true;
            }
            departureSeat.updateDatabase();
            if (returnSeat != null) {
                returnSeat.updateDatabase();
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("An error occurred while updating passenger database"+e.getMessage());
        } finally {
            closeConnection();
        }
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", passengerID='" + passengerID + '\'' +
                ", bookingID='" + bookingID + '\'' +
                ", departureSeat=" + departureSeat +
                ", returnSeat=" + returnSeat +
                ", existingPassenger=" + existingPassenger +
                '}';
    }
}
