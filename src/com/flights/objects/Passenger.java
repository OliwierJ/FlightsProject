package com.flights.objects;

import com.flights.DBConnectivity;

public class Passenger extends DBConnectivity {
    private String name;
    private String surname;
    private String passengerID;
    private String bookingID;
    private Seat departureSeat;
    private Seat returnSeat;

    // gets an existing passenger from database
    public Passenger(String passengerID) {
        try {
            String[] result = getRow(connectAndExecuteQuery("SELECT first_name, last_name, booking_no FROM passenger WHERE passenger_ID="+passengerID));
            this.name = result[0];
            this.surname = result[1];
            this.bookingID = result[2];
            this.passengerID = passengerID;

            // gets passenger seats
            String[][] seats = getMultipleRows(connectAndExecuteQuery("SELECT seat_no, class, aircraft_id, seat.flight_id, is_return FROM seat INNER JOIN flights_project.flight_booking fb on seat.flight_id = fb.flight_id WHERE passenger_id="+passengerID+" AND booking_no="+bookingID));
            if (seats.length == 0 || seats.length > 2) {
                throw new Exception();
            } else {
                for (String[] seat : seats) {
                    if (seat[4].equals("0")) {
                        departureSeat = new Seat(seat[0], seat[1], seat[2], seat[3], passengerID, false);
                    } else {
                        returnSeat = new Seat(seat[0], seat[1], seat[2], seat[3], passengerID, true);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred in passenger");
            e.printStackTrace();
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

    @Override
    public void updateDatabase() {
        //TODO: update database appropriately after passenger details have been changed
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", passengerID='" + passengerID + '\'' +
                ", bookingID='" + bookingID + '\'' +
                ", departureSeat='" + departureSeat + '\'' +
                ", returnSeat='" + returnSeat + '\'' +
                '}';
    }
}
