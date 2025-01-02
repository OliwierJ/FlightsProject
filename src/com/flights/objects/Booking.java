package com.flights.objects;

import com.flights.DBConnectivity;

import java.util.Arrays;

public class Booking extends DBConnectivity {
    private String bookingID;
    private String email;
    private int priorityBoarding;
    private int luggage;
    private Passenger[] passengers;
    private Flight departureFlight;
    private Flight returnFlight;

    public Booking() {
        // TODO: generate new booking no, new empty booking goes here
    }

    // gets an existing booking from database
    public Booking(String bookingID, String email) {
        try {
            if(verifyBookingDetails(bookingID, email)) {
                String[] result = getRow(connectAndExecuteQuery("SELECT priority_boarding, luggage_amount FROM booking WHERE booking_no=" + bookingID));

                this.bookingID = bookingID;
                this.email = email;
                this.priorityBoarding = Integer.parseInt(result[0]);
                this.luggage = Integer.parseInt(result[1]);

                String[][] passengerIDs = getMultipleRows(connectAndExecuteQuery("SELECT passenger.passenger_ID FROM passenger WHERE booking_no=" + bookingID));
                int passengerCount = passengerIDs.length;
                this.passengers = new Passenger[passengerCount];
                for (int i = 0; i < passengerCount; i++) {
                    passengers[i] = new Passenger(passengerIDs[i][0]);
                }
                String[][] flightIDs = getMultipleRows(connectAndExecuteQuery("SELECT flight_id, is_return FROM flight_booking WHERE booking_no="+bookingID));
                if (flightIDs.length > 2 || flightIDs[0].length == 0) {
                    throw new Exception();
                } else {
                    for (String[] flightID : flightIDs) {
                        if (flightID[1].equals("0")) {
                            departureFlight = new Flight(Integer.parseInt(flightID[0]));
                        } else {
                            returnFlight = new Flight(Integer.parseInt(flightID[0]));
                        }
                    }
                }

            } else {
                System.out.println("Booking does not exist");
            }
        } catch (Exception e) {
            System.out.println("An error occurred!");
            e.printStackTrace();
        }
    }

    public static boolean verifyBookingDetails(String bookingID, String email) throws Exception {
        boolean valid = connectAndExecuteQuery("SELECT * FROM booking WHERE booking_no='"+bookingID+"' AND email='"+email+"'").next(); // true if entry exists, false otherwise
        closeConnection();
        return valid;
    }

    public int getLuggage() {
        return luggage;
    }

    public int getPriorityBoarding() {
        return priorityBoarding;
    }

    public Passenger[] getPassengers() {
        return passengers;
    }

    public String getBookingID() {
        return bookingID;
    }

    public Flight getDepartureFlight() {
        return departureFlight;
    }

    public Flight getReturnFlight() {
        return returnFlight;
    }

    public String getEmail() {
        return email;
    }

    public int getPassengerCount() {
        return passengers.length;
    }
    @Override
    public void updateDatabase() {
        // TODO: update the database with the latest details
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingID='" + bookingID + '\'' +
                ", email='" + email + '\'' +
                ", priorityBoarding=" + priorityBoarding +
                ", luggage=" + luggage +
                ", passengers=" + Arrays.toString(passengers) +
                ", departureFlight=" + departureFlight +
                ", returnFlight=" + returnFlight +
                '}';
    }
}
