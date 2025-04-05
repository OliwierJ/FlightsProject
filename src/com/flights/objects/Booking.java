package com.flights.objects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Random;

import com.flights.util.DBConnectivity;
import com.flights.util.JErrorDialog;

/**
 * Booking object containing everything needed to create, read, update or remove a booking from database
 */
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

    /**
     * make new empty booking and add values later using setters
     * @param tier tier of booking, can only accept "Basic", "Standard", "Premium+"
     */
    public Booking(String tier) {
        this.generateBookingID();
        this.newBooking = true;
        this.priorityBoarding = 0; // default initialisation of new booking
        this.luggage = 0;
        this.passengers = new Passenger[0];
        setTier(tier);
    }

    private void generateBookingID() {
        boolean exists = true;
        Random r = new Random();
        do {
            int bookingNo = r.nextInt(100000, 1000000); // AKA 100000 - 999999 inclusive
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

    /**
     * Retrieve existing booking from database
     * @param bookingID the booking no
     * @param email the booking email
     */
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
        } finally {
            closeConnection();
        }
    }

    /**
     * Verify does the booking exist in database
     * @param bookingID the booking no
     * @param email the booking email
     * @return true if exists, false otherwise
     */
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

    /**
     * Gets amount of luggage in the booking
     * @return luggage amount
     */
    public int getLuggage() {
        return luggage;
    }

    /**
     * Gets 20kg luggage in the booking
     * @return true if 20kg luggage reserved, false otherwise
     */
    public boolean get20kgluggage() {
        return luggage20kg;
    }

    /**
     * Gets tier of the booking
     * @return booking tier
     */
    public String getTier() {
        return tier;
    }

    /**
     * Gets priority boarding of the booking
     * @return 1 if priority boarding selected, 0 otherwise
     */
    public int getPriorityBoarding() {
        return priorityBoarding;
    }

    /**
     * Gets the passengers of the booking
     * @return array of Passenger objects
     */
    public Passenger[] getPassengers() {
        return passengers;
    }

    /**
     * Gets the booking no
     * @return booking no
     */
    public String getBookingID() {
        return bookingID;
    }

    /**
     * Gets the departure flight of the booking
     * @return Flight object
     */
    public Flight getDepartureFlight() {
        return departureFlight;
    }

    /**
     * Gets the return flight of the booking
     * @return Flight object
     */
    public Flight getReturnFlight() {
        return returnFlight;
    }

    /**
     * Gets the email of the booking
     * @return booking email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets passenger count in the booking
     * @return int passenger count
     */
    public int getPassengerCount() {
        return passengers.length;
    }

    /**
     * Sets booking email
     * @param email String booking email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets priority boarding
     * @param priorityBoarding <code>0</code> for false, <code>1</code> for true
     */
    public void setPriorityBoarding(int priorityBoarding) {
        this.priorityBoarding = priorityBoarding;
    }

    /**
     * Set 20kg luggage
     * @param l <code>true</code> if luggage included, <code>false</code> otherwise
     */
    public void set20kgluggage(boolean l) {
        this.luggage20kg = l;
        if (luggage20kg && luggage == passengers.length) {
            luggage++;
        } else if (!luggage20kg && luggage > passengers.length) {
            luggage--;
        }
    }

    /**
     * Set departure flight
     * @param departureFlight Flight object
     * @throws UnsupportedOperationException if attempting to change flight on existing booking
     */
    public void setDepartureFlight(Flight departureFlight) {
        if (newBooking) {
            this.departureFlight = departureFlight;
        } else {
            throw new UnsupportedOperationException("Cannot change departure flight on an existing booking!");
        }
    }

    /**
     * Set return flight
     * @param returnFlight Flight object
     * @throws UnsupportedOperationException if attempting to change flight on existing booking
     */
    public void setReturnFlight(Flight returnFlight) {
        if (newBooking) {
            this.returnFlight = returnFlight;
        } else {
            throw new UnsupportedOperationException("Cannot change return flight on an existing booking!");
        }
    }

    /**
     * Set tier of booking
     * @param tier can only accept "Basic", "Standard", "Premium+"
     */
    public void setTier(String tier) {
        if (tier.equals("Basic") || tier.equals("Standard") || tier.equals("Premium+")) {
            this.tier = tier;
            this.luggage20kg = tier.equals("Standard") || tier.equals("Premium+");
        } else {
            JErrorDialog.showWarning("Invalid tier set!");
        }
    }

    /**
     * Adds passengers to the booking
     * @param newPassengers Passenger... 1 or more Passenger objects, or Passenger[]
     */
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

    /**
     * Updates the database with the booking details
     * <br>Automatically detects is it a new or existing booking and updates the database accordingly
     * <br>Also updates its child Passenger and Seat objects database entries, use this method ONLY to update the database
     */
    @Override
    public void updateDatabase() {
        try {
            // create new booking
            if (newBooking) {
                if (email == null || departureFlight == null || passengers.length == 0) {
                    throw new IllegalStateException("Booking is not complete!");
                } 
                addQueryToUpdate("INSERT INTO booking (booking_no, email, priority_boarding, luggage_amount, tier) VALUES ('" + bookingID + "', '" + email + "', " + priorityBoarding + ", " + luggage + ", '" + tier + "')");
                addQueryToUpdate("INSERT INTO flight_booking (flight_id, booking_no, is_return) VALUES (" + departureFlight.getFlightID() + ", '" + bookingID + "', 0)");
                if (returnFlight != null) {
                    addQueryToUpdate("INSERT INTO flight_booking (flight_id, booking_no, is_return) VALUES (" + returnFlight.getFlightID() + ", '" + bookingID + "', 1)");
                }
            } else {
                // update existing booking
                ResultSet rs = connectAndExecuteQuery("SELECT * FROM booking WHERE booking_no='"+bookingID+"'");
                if (!rs.next()) {
                    throw new IllegalArgumentException("No such booking exists!");
                }
                addQueryToUpdate("UPDATE booking SET email='"+email+"', priority_boarding="+priorityBoarding+", luggage_amount="+luggage+", tier='"+tier+"' WHERE booking_no="+bookingID);
            }
            // update each passenger
            for (Passenger passenger : passengers) {
                if (passenger.getReturnSeat() == null && returnFlight != null) {
                    throw new IllegalStateException(passenger.getName()+" "+passenger.getSurname()+" does not have a return seat assigned!");
                }
                passenger.updateDatabase(); // this will work as objects are mutable in a for each loop, however assigning new objects required a standard for loop
            }
            executeUpdates(); // automatically closes connection
            // now update the seats for each passenger, this needs to be done because the passengerIDs are generated in MYSQL so a batch must be executed first to then retrieve the passenger IDs to then update the seat database accordingly
            if (newBooking) {
                for (Passenger passenger : passengers) {
                    passenger.updateSeatsDatabase();
                }
                executeUpdates(); // automatically closes connection
                this.newBooking = false;
            }
        } catch (SQLException e) {
            JErrorDialog.showError("An error occurred while updating database!", e);
        } finally {
            closeConnection(); // in case something goes wrong not needed here in normal operations
        }
    }

    /**
     * Deletes the booking and all related objects from the database
     * <br>This action is IRREVERSIBLE
     */
    public void deleteEntry() {
        if (newBooking) {
            throw new UnsupportedOperationException("Can't delete a booking that hasn't been inserted into the database yet!");
        } else {
            try {
                addQueryToUpdate("DELETE seat FROM seat INNER JOIN passenger ON seat.passenger_id=passenger.passenger_ID WHERE passenger.booking_no='"+bookingID+"'");
                addQueryToUpdate("DELETE FROM passenger WHERE booking_no='"+bookingID+"'");
                addQueryToUpdate("DELETE FROM flight_booking WHERE booking_no='"+bookingID+"'");
                addQueryToUpdate("DELETE FROM booking WHERE booking_no='"+bookingID+"'");
                executeUpdates(); // automatically closes connection
            } catch (SQLException e) {
                JErrorDialog.showError("An error occurred while deleting from the database!", e);
            } finally {
                closeConnection(); // in case something goes wrong not needed here in normal operations
            }
        }
    }

    /**
     * Get all booking details for debug purposes
     * @return String containing all booking details and all child objects details
     */
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
