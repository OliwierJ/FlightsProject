package com.flights.objects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class Booking extends DBConnectivity {
    private String bookingID;
    private String email;
    private int priorityBoarding;
    private int luggage;
    private Passenger[] passengers;
    private Flight departureFlight;
    private Flight returnFlight;
    private boolean newBooking;

    // make new empty booking and add values using setters
    public Booking() {
        this.generateBookingID();
        this.newBooking = true;
        this.priorityBoarding = 0; // default initialisation of new booking
        this.luggage = 0;
        this.passengers = new Passenger[0];
    }

    // make new booking giving values in constructor
    public Booking(String email, int priorityBoarding, int luggage, Flight departureFlight, Flight returnFlight) {
        this.generateBookingID();
        this.email = email;
        this.priorityBoarding = priorityBoarding;
        this.luggage = luggage;
        this.departureFlight = departureFlight;
        this.returnFlight = returnFlight;
        this.passengers = new Passenger[0];
        this.newBooking = true;
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
            } catch (SQLException | ClassNotFoundException e) {
                System.out.println(e.getMessage());
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
                String[] result = getRow(connectAndExecuteQuery("SELECT priority_boarding, luggage_amount FROM booking WHERE booking_no=" + bookingID));

                this.bookingID = bookingID;
                this.email = email;
                this.priorityBoarding = Integer.parseInt(result[0]);
                this.luggage = Integer.parseInt(result[1]);

                String[][] passengerIDs = getMultipleRows(connectAndExecuteQuery("SELECT passenger.passenger_ID FROM passenger WHERE booking_no=" + bookingID));
                int passengerCount = passengerIDs.length;
                this.passengers = new Passenger[passengerCount];
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
                System.out.println("Booking does not exist");
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("An error occurred!"+e.getMessage());
        }
    }

    public static boolean verifyBookingDetails(String bookingID, String email) throws SQLException, ClassNotFoundException {
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPriorityBoarding(int priorityBoarding) {
        this.priorityBoarding = priorityBoarding;
    }

    public void setLuggage(int luggage) {
        this.luggage = luggage;
    }

    public void setDepartureFlight(Flight departureFlight) {
        this.departureFlight = departureFlight;
    }

    public void setReturnFlight(Flight returnFlight) {
        this.returnFlight = returnFlight;
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
    }

    // may or may not be used, we shall C
    public void removePassenger(Passenger passenger) {
        Passenger[] tempPassengers = new Passenger[passengers.length - 1];
        int currentIndex = 0;
        for (Passenger value : passengers) {
            if (!value.equals(passenger)) {
                tempPassengers[currentIndex] = value;
                currentIndex++;
            }
        }
        passengers = tempPassengers;
    }

    // passenger modification can be done using passenger's setters from the passengers array, no need for separate method

    @Override
    public void updateDatabase() {
        try {
            if (newBooking) {
                if (email == null || departureFlight == null || passengers.length == 0) {
                    throw new IllegalStateException("Booking is not complete!");
                }
                connectAndExecuteUpdate("INSERT INTO booking (booking_no, email, priority_boarding, luggage_amount) VALUES ('" + bookingID + "', '" + email + "', " + priorityBoarding + ", " + luggage + ")");
                connectAndExecuteUpdate("INSERT INTO flight_booking (flight_id, booking_no, is_return) VALUES (" + departureFlight.getFlightID() + ", '" + bookingID + "', 0)");
                if (returnFlight != null) {
                    connectAndExecuteUpdate("INSERT INTO flight_booking (flight_id, booking_no, is_return) VALUES (" + returnFlight.getFlightID() + ", '" + bookingID + "', 1)");
                }
                this.newBooking = false;
            } else {
                ResultSet rs = connectAndExecuteQuery("SELECT * FROM booking WHERE booking_no='"+bookingID+"'");
                if (!rs.next()) {
                    throw new IllegalArgumentException("No such booking exists!");
                }
                connectAndExecuteUpdate("UPDATE booking SET email='"+email+"', priority_boarding="+priorityBoarding+", luggage_amount="+luggage+" WHERE booking_no="+bookingID);
            }
            for (Passenger passenger : passengers) {
                passenger.updateDatabase(); // this will work as objects are mutable in a for each loop, however assigning new objects required a standard for loop
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("An error occurred while updating database!"+e.getMessage());
        } finally {
            closeConnection();
        }
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
                ", newBooking=" + newBooking +
                '}';
    }
}
