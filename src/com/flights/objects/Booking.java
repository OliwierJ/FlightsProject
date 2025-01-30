package com.flights.objects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import com.flights.util.DBConnectivity;
import com.flights.util.JErrorDialog;

public class Booking extends DBConnectivity {
    private String bookingID;
    private String email;
    private int priorityBoarding;
    private int luggage;
    private boolean luggage20kg;
    private Passenger[] passengers;
    private Flight departureFlight;
    private Flight returnFlight;
    private boolean newBooking;
    private String tier;

    // make new empty booking and add values using setters
    public Booking(String tier) {
        this.generateBookingID();
        this.newBooking = true;
        this.priorityBoarding = 0; // default initialisation of new booking
        this.luggage = 0;
        this.passengers = new Passenger[0];
        setTier(tier);
    }

    // make new booking giving values in constructor // TODO test maybe delete
    public Booking(String email, int priorityBoarding, Flight departureFlight, Flight returnFlight, boolean luggage20kg, String tier) {
        this.generateBookingID();
        this.email = email;
        this.priorityBoarding = priorityBoarding;
        this.departureFlight = departureFlight;
        this.returnFlight = returnFlight;
        this.luggage = 0;
        this.luggage20kg = luggage20kg;
        if (luggage20kg) {
            luggage++;
        }
        this.passengers = new Passenger[0];
        this.newBooking = true;
        setTier(tier);
    }

    private void generateBookingID() {
        boolean exists = true;
        do {
            int bookingNo = (int) (Math.random() * 999999);
            try {
                exists = connectAndExecuteQuery("SELECT * FROM booking WHERE booking_no="+bookingNo).next();
                if (!exists) {
                    this.bookingID = String.valueOf(bookingNo);
                }
            } catch (SQLException e) {
                JErrorDialog.showError("An error occurred when generating the bookingID", e);
            } finally {
                closeConnection();
            }
        } while (exists);
    }

    // gets an existing booking from database
    public Booking(String bookingID, String email) {
        this.newBooking = false;
        try {
            if(verifyBookingDetails(bookingID, email)) {
                String[] result = getRow(connectAndExecuteQuery("SELECT priority_boarding, luggage_amount, tier FROM booking WHERE booking_no=" + bookingID));

                this.bookingID = bookingID;
                this.email = email;
                this.priorityBoarding = Integer.parseInt(result[0]);
                this.luggage = Integer.parseInt(result[1]);
                this.tier = result[2];

                String[][] passengerIDs = getMultipleRows(connectAndExecuteQuery("SELECT passenger.passenger_ID FROM passenger WHERE booking_no=" + bookingID));
                int passengerCount = passengerIDs.length;
                this.passengers = new Passenger[passengerCount];
                this.luggage20kg = passengerCount < luggage;
                for (int i = 0; i < passengerCount; i++) {
                    passengers[i] = new Passenger(passengerIDs[i][0]); // this cannot be in a for each loop it has to be in a regular for loop
                }
                String[][] flightIDs = getMultipleRows(connectAndExecuteQuery("SELECT flight_id, is_return FROM flight_booking WHERE booking_no="+bookingID));
                if (flightIDs.length > 2 || flightIDs[0].length == 0) {
                    throw new IllegalStateException("Invalid flight amount");
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
                JErrorDialog.showWarning("Booking does not exist!");
            }
        } catch (SQLException e) {
            JErrorDialog.showError("An error occurred while retrieving booking from database", e);
        }
    }

    public static boolean verifyBookingDetails(String bookingID, String email) {
        try {
            return connectAndExecuteQuery("SELECT * FROM booking WHERE booking_no='" + bookingID + "' AND email='" + email + "'").next(); // true if entry exists, false otherwise
        } catch (SQLException e) {
            JErrorDialog.showError("An error occurred while verifying booking details", e);
            return false;
        } finally {
            closeConnection();
        }
    }

    public int getLuggage() {
        return luggage;
    }

    public boolean get20kgluggage() {
        return luggage20kg;
    }

    public String getTier() {
        return tier;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPriorityBoarding(int priorityBoarding) {
        this.priorityBoarding = priorityBoarding;
    }

    public void set20kgluggage(boolean l) {
        this.luggage20kg = l;
        if (luggage20kg && luggage == passengers.length) {
            luggage++;
        } else if (!luggage20kg && luggage > passengers.length) {
            luggage--;
        }
    }

    public void setDepartureFlight(Flight departureFlight) {
        if (newBooking) {
            this.departureFlight = departureFlight;
        } else {
            throw new UnsupportedOperationException("Cannot change departure flight on an existing booking!");
        }
    }

    public void setReturnFlight(Flight returnFlight) {
        if (newBooking) {
            this.returnFlight = returnFlight;
        } else {
            throw new UnsupportedOperationException("Cannot change return flight on an existing booking!");
        }
    }

    public void setTier(String tier) { // TODO: test
        if (tier.equals("Basic") || tier.equals("Standard") || tier.equals("Deluxe")) { //TODO test
            this.tier = tier;
            this.luggage20kg = tier.equals("Standard") || tier.equals("Deluxe");
        } else {
            JErrorDialog.showWarning("Invalid tier set!");
        }
    }

    public void addPassengers(Passenger... newPassengers) {
        Passenger[] tempPassengers = new Passenger[passengers.length + newPassengers.length];

        System.arraycopy(passengers, 0, tempPassengers, 0, passengers.length);
        int i = passengers.length;
        for (Passenger p : newPassengers) {
            tempPassengers[i] = p;
            i++;
        }
        passengers = tempPassengers;
        this.luggage = passengers.length;
        if (luggage20kg) {
            luggage++;
        }
    }

    @Override
    public void updateDatabase() {
        try {
            // create new booking
            if (newBooking) {
                if (email == null || departureFlight == null || passengers.length == 0) {
                    throw new IllegalStateException("Booking is not complete!");
                } 
                connectAndExecuteUpdate("INSERT INTO booking (booking_no, email, priority_boarding, luggage_amount, tier) VALUES ('" + bookingID + "', '" + email + "', " + priorityBoarding + ", " + luggage + ", '" + tier + "')");
                connectAndExecuteUpdate("INSERT INTO flight_booking (flight_id, booking_no, is_return) VALUES (" + departureFlight.getFlightID() + ", '" + bookingID + "', 0)");
                if (returnFlight != null) {
                    connectAndExecuteUpdate("INSERT INTO flight_booking (flight_id, booking_no, is_return) VALUES (" + returnFlight.getFlightID() + ", '" + bookingID + "', 1)");
                }
                this.newBooking = false;
            } else {
                // update existing booking
                ResultSet rs = connectAndExecuteQuery("SELECT * FROM booking WHERE booking_no='"+bookingID+"'");
                if (!rs.next()) {
                    throw new IllegalArgumentException("No such booking exists!");
                }
                connectAndExecuteUpdate("UPDATE booking SET email='"+email+"', priority_boarding="+priorityBoarding+", luggage_amount="+luggage+", tier='"+tier+"' WHERE booking_no="+bookingID);
            }
            // update each passenger
            for (Passenger passenger : passengers) {
                if (passenger.getReturnSeat() == null && returnFlight != null) {
                    throw new IllegalStateException(passenger.getName()+" "+passenger.getSurname()+" does not have a return seat assigned!");
                }
                passenger.updateDatabase(); // this will work as objects are mutable in a for each loop, however assigning new objects required a standard for loop
            }
        } catch (SQLException e) {
            JErrorDialog.showError("An error occurred while updating database!", e);
        } finally {
            closeConnection();
        }
    }

    public void deleteEntry() {
        if (newBooking) {
            throw new IllegalAccessError("Can't delete a booking that hasn't been inserted into the database yet!");
        } else {
            try {
                connectAndExecuteUpdate("DELETE seat FROM seat INNER JOIN passenger ON seat.passenger_id=passenger.passenger_ID WHERE passenger.booking_no='"+bookingID+"'");
                connectAndExecuteUpdate("DELETE FROM passenger WHERE booking_no='"+bookingID+"'");
                connectAndExecuteUpdate("DELETE FROM flight_booking WHERE booking_no='"+bookingID+"'");
                connectAndExecuteUpdate("DELETE FROM booking WHERE booking_no='"+bookingID+"'");
            } catch (SQLException e) {
                JErrorDialog.showError("An error occurred while deleting from the database!", e);
            } finally {
                closeConnection();
            }
        }
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingID='" + bookingID + '\'' +
                ", email='" + email + '\'' +
                ", priorityBoarding=" + priorityBoarding +
                ", luggage=" + luggage +
                ", luggage20kg=" + luggage20kg +
                ", tier=" + tier +
                ", passengers=" + Arrays.toString(passengers) +
                ", departureFlight=" + departureFlight +
                ", returnFlight=" + returnFlight +
                ", newBooking=" + newBooking +
                '}';
    }
}
