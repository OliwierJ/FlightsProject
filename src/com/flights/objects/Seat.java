package com.flights.objects;

/**
 * Seat class containing Seat information
 */
public class Seat {

    private final String seatNo; 
    private final String seatClass;
    private final String flightID;
    private String passengerID;

    /**
     * Create a new Seat object, either from Passenger class when viewing Booking, or from Flight/Aircraft class when generating Flight seats
     * @param seatNo the number of this Seat
     * @param seatClass the class of this Seat
     * @param flightID the flightID of the Flight this Seat is assigned to
     * @param passengerID the passengerID of the Passenger this Seat is assigned to, null if unoccupied
     */
    public Seat(String seatNo, String seatClass, String flightID, String passengerID) {
        this.seatNo = seatNo;
        this.seatClass = seatClass;
        this.flightID = flightID;
        this.passengerID = passengerID;
    }

    /**
     * Get the seat no of this Seat
     * @return seat no
     */
    public String getSeatNo() {
        return seatNo;
    }

    /**
     * Gets the class of this Seat
     * @return seat class
     */
    public String getSeatClass() {
        return seatClass;
    }

    /**
     * Gets the passengerID of the Passenger assigned to this Seat
     * @return passengerID if occupied, null if unoccupied
     */
    public String getPassengerID() {
        return passengerID;
    }

    /**
     * Gets the flightID of the Flight this Seat is assigned to
     * @return flightID
     */
    public String getFlightID() {
        return flightID;
    }

    /**
     * Checks is this Seat occupied
     * @return <code>true</code> if occupied, <code>false</code> otherwise
     */
    public boolean isOccupied() {
        return passengerID != null;
    }

    /**
     * This should only be used by the Passenger class!!!
     * <br>Assigns a Passenger to this Seat
     * @param passengerID ID of the Passenger to be assigned
     */
    public void setPassengerID(String passengerID) {
        this.passengerID = passengerID;
    }

    /**
     * Get the Seat details for debug purposes
     * @return String of all details
     */
    @Override
    public String toString() {
        return "Seat{" +
                "seatNo='" + seatNo + '\'' +
                ", seatClass='" + seatClass + '\'' +
                ", flightID='" + flightID + '\'' +
                ", passengerID='" + passengerID + '\'' +
                '}'+"\n";
    }
}
