package com.flights.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.flights.util.DBConnectivity;
import com.flights.util.JErrorDialog;

/**
 * Passenger object containing its details and Seat details
 */
public class Passenger extends DBConnectivity {
    private String title;
    private String name;
    private String surname;
    private String passengerID;
    private String bookingID;
    private Seat departureSeat;
    private Seat returnSeat;
    private Seat oldDepartureSeat;
    private Seat oldReturnSeat;

    /**
     * Construct a new Passenger object to be used in a new Booking
     * @param title passenger title
     * @param name passenger name
     * @param surname passenger surname
     * @param bookingID booking ID that the passenger is assigned to
     */
    public Passenger(String title, String name, String surname, String bookingID) {
        this.title = title;
        this.name = name;
        this.surname = surname;
        this.bookingID = bookingID;
        this.departureSeat = null;
        this.returnSeat = null;
        this.oldDepartureSeat = null;
        this.oldReturnSeat = null;
        this.passengerID = "-1";
        // passenger ID is created when saving to database for the first time
    }

    /**
     * Gets an existing Passenger details from database and constructs a new Passenger object
     * @param passengerID ID of passenger
     */
    public Passenger(String passengerID) {
        try {
            String[] result = getRow(connectAndExecuteQuery("SELECT title, first_name, last_name, booking_no FROM passenger WHERE passenger_ID="+passengerID));
            this.title = result[0];
            this.name = result[1];
            this.surname = result[2];
            this.bookingID = result[3];
            this.passengerID = passengerID;

            // gets passenger seats
            String[][] seats = getMultipleRows(connectAndExecuteQuery("SELECT seat_no, class, seat.flight_id, is_return FROM seat INNER JOIN flights_project.flight_booking fb on seat.flight_id = fb.flight_id WHERE passenger_id="+passengerID+" AND booking_no="+bookingID));
            if (seats.length == 0 || seats.length > 2) {
                throw new IllegalStateException(seats.length+" seats found! Must be 1 or 2!");
            } else {
                for (String[] seat : seats) {
                    if (seat[3].equals("0")) {
                        departureSeat = new Seat(seat[0], seat[1], seat[2], passengerID);
                        oldDepartureSeat = departureSeat;
                    } else {
                        returnSeat = new Seat(seat[0], seat[1], seat[2], passengerID);
                        oldReturnSeat = returnSeat;
                    }
                }
            }
        } catch (SQLException e) {
            JErrorDialog.showError("An error occurred when retrieving passenger details from database", e);
        } finally {
            closeConnection();
        }
    }

    /**
     * Get Passenger title
     * @return String
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get Passenger name
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Get Passenger surname
     * @return String
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Get Passenger departure Seat
     * @return Seat
     */
    public Seat getDepartureSeat() {
        return departureSeat;
    }

    /**
     * Get Passenger return Seat
     * @return Seat
     */
    public Seat getReturnSeat() {
        return returnSeat;
    }

    /**
     * Set Passenger title
     * @param title String, <code>null</code> for "Prefer not to say"
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Set Passenger name
     * @param name String
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set Passenger surname
     * @param surname String
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Set Passenger departure Seat
     * @param s Seat
     */
    public void setDepartureSeat(Seat s) {
        if (departureSeat != null && !departureSeat.equals(oldDepartureSeat)) {
            departureSeat.setPassengerID(null);
        }
        this.departureSeat = s;
        departureSeat.setPassengerID(passengerID);
    }

    /**
     * Set Passenger return Seat
     * @param s Seat
     */
    public void setReturnSeat(Seat s) {
        if (returnSeat != null && !returnSeat.equals(oldReturnSeat)) {
            returnSeat.setPassengerID(null);
        }
        this.returnSeat = s;
        returnSeat.setPassengerID(passengerID);
    }

    /**
     * Update database with the relevant Passenger details
     * <br>This should only be called from <code>Booking.updateDatabase()</code>
     */
    @Override
    protected void updateDatabase() {
        try {
            // update existing passenger in database
            if (!passengerID.equals("-1")) {
                ResultSet rs = connectAndExecuteQuery("SELECT * FROM passenger WHERE passenger_ID="+passengerID);
                if (!rs.next()) {
                    throw new IllegalArgumentException("No such passenger exists!");
                }
                addQueryToUpdate("UPDATE passenger SET title='"+title+"', first_name='"+name+"', last_name='"+surname+"' WHERE passenger_ID="+passengerID);

                // if seat was changed
                if (departureSeat != oldDepartureSeat) {
                    oldDepartureSeat.setPassengerID(null); // reset object reference to be null
                    departureSeat.setPassengerID(passengerID);
                    oldDepartureSeat = departureSeat; // overwrite this reference with new seat
                    addQueryToUpdate("UPDATE seat SET seat_no='"+departureSeat.getSeatNo()+"', class='"+departureSeat.getSeatClass()+"' WHERE passenger_ID="+passengerID+" AND flight_id='"+departureSeat.getFlightID()+"'");
                }
                // if return seat changed
                if (returnSeat != oldReturnSeat && returnSeat != null) {
                    oldReturnSeat.setPassengerID(null); // reset object reference to be null
                    returnSeat.setPassengerID(passengerID);
                    oldReturnSeat = returnSeat; // overwrite this reference with new seat
                    addQueryToUpdate("UPDATE seat SET seat_no='"+returnSeat.getSeatNo()+"', class='"+returnSeat.getSeatClass()+"' WHERE passenger_ID="+passengerID+" AND flight_id='"+returnSeat.getFlightID()+"'");
                }
            } else {
                // add new passenger to database
                if (name == null || surname == null || departureSeat == null || bookingID == null) {
                    throw new IllegalStateException("Passenger details are not complete!");
                }
                addQueryToUpdate("INSERT INTO passenger (title, first_name, last_name, booking_no) VALUES ('"+title+"', '"+name+"', '"+surname+"', '"+bookingID+"')");
            }
        } catch (SQLException e) {
            JErrorDialog.showError("An error occurred while updating passenger database", e);
        }
    }

    /**
     * Update seat database with the relevant Passenger Seat details
     * <br>This should only be called from <code>Booking.updateDatabase()</code>
     */
    protected void updateSeatsDatabase() {
        if (!passengerID.equals("-1")) {
            throw new UnsupportedOperationException("Cannot update passenger seats database on an already existing passenger!");
        }
        try {
            this.passengerID = getRow(connectAndExecuteQuery("SELECT passenger_ID FROM passenger WHERE title='"+title+"' AND first_name='"+name+"' AND last_name='"+surname+"' AND booking_no='"+bookingID+"'"))[0];

            departureSeat.setPassengerID(passengerID);
            oldDepartureSeat = departureSeat;
            addQueryToUpdate("INSERT INTO seat (seat_no, class, flight_id, passenger_id) VALUES ('"+departureSeat.getSeatNo()+"', '"+departureSeat.getSeatClass()+"', '"+departureSeat.getFlightID()+"', '"+passengerID+"')");
            // if a return seat was selected
            if (returnSeat != null) {
                returnSeat.setPassengerID(passengerID);
                oldReturnSeat = returnSeat;
                addQueryToUpdate("INSERT INTO seat (seat_no, class, flight_id, passenger_id) VALUES ('"+returnSeat.getSeatNo()+"', '"+returnSeat.getSeatClass()+"', '"+returnSeat.getFlightID()+"', '"+passengerID+"')");
            }
        } catch (SQLException e) {
            JErrorDialog.showError("An error occurred while updating passenger database", e);
        }
    }

    /**
     * Get the Passenger details for debug purposes
     * @return String of all details
     */
    @Override
    public String toString() {
        return "Passenger{" +
                "title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", passengerID='" + passengerID + '\'' +
                ", bookingID='" + bookingID + '\'' +
                ", departureSeat=" + departureSeat +
                ", returnSeat=" + returnSeat +
                ", oldDepartureSeat=" + oldDepartureSeat +
                ", oldReturnSeat=" + oldReturnSeat +
                '}';
    }
}