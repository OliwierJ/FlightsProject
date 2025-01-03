package com.flights.objects;

import com.flights.DBConnectivity;

import java.sql.SQLException;

public class Seat extends DBConnectivity {

    //dont make them final for now
    private String seatNo; // existing seat no
    private String newSeatNo;
    private String seatClass;
    private String aircraftID; // figure out how to assign this and is it even needed here
    private String flightID;
    private String passengerID; // TODO: passengerID is null until sent to database, add a different way of tracking this
    private boolean isReturn;
    private boolean existingBooking;

    // makes a new empty seat to be then updated in the database later
    public Seat(String seatNo, int flightID, boolean isReturn) {
        this.seatNo = seatNo;
        this.newSeatNo = seatNo;
        this.flightID = String.valueOf(flightID);
        this.isReturn = isReturn;
        this.existingBooking = false;
        this.passengerID = null;
        try {
            String[] seatData = getRow(connectAndExecuteQuery("SELECT class, aircraft_id FROM seat WHERE seat_no='"+seatNo+"' AND flight_id="+flightID));
            this.seatClass = seatData[0];
            this.aircraftID = seatData[1];
        } catch (SQLException | ClassNotFoundException e) {
            // TODO: remove this, check NEWTEST2025.java TODO
            System.out.println("An error occurred when getting seat data"+e.getMessage());
        }
        // passenger ID is currently added when assigning a seat object to passenger, TODO testing needs to be done
    }

    // makes a new passenger seat object from database data taken from passenger
    public Seat(String seatNo, String seatClass, String aircraftID, String flightID, String passengerID, boolean isReturn, boolean existingBooking) {
        this.seatNo = seatNo;
        this.seatClass = seatClass;
        this.aircraftID = aircraftID;
        this.flightID = flightID;
        this.passengerID = passengerID;
        this.isReturn = isReturn;
        this.existingBooking = existingBooking;
        if (!existingBooking) {
            newSeatNo = seatNo;
        }
    }

    public String getSeatNo() {
        return seatNo;
    }

    public String getSeatClass() {
        return seatClass;
    }

    public String getPassengerID() {
        return passengerID;
    }

    public String getAircraftID() {
        return aircraftID;
    }

    public String getFlightID() {
        return flightID;
    }

    public void setPassengerID(String passengerID) {
        this.passengerID = passengerID;
    }
    // requires updateDatabase() for changes to take effect
    public void changeSeatNo(String seatNo) {
        this.newSeatNo = seatNo;
    }

    // returns true if occupied, false otherwise
    private boolean checkSeatIsOccupied(String seatNo, String flightID) throws SQLException, ClassNotFoundException {
        String[] newSeat = getRow(connectAndExecuteQuery("SELECT passenger_id FROM seat WHERE seat_no='"+seatNo+"' AND flight_id="+flightID));
        return !newSeat[0].equals("0");
    }

    @Override
    protected void updateDatabase() {
        try {
            if (existingBooking) {
                // seat was changed
                if (newSeatNo != null){
                    // update old seat
                    connectAndExecuteUpdate("UPDATE seat SET passenger_id=0 WHERE seat_no='"+seatNo+"' AND flight_id="+flightID);
                } else {
                    return;
                }
            }

            // todo: error checking VERY IMPORTANT
            if ((seatNo != null || newSeatNo != null) && passengerID != null) {
                if (!checkSeatIsOccupied(newSeatNo, flightID)) {
                    // update new seat, change this seat object to new seat
                    connectAndExecuteUpdate("UPDATE seat SET passenger_id=" + passengerID + " WHERE seat_no='" + newSeatNo + "' AND flight_id=" + flightID);
                    this.seatClass = getRow(connectAndExecuteQuery("SELECT class FROM seat WHERE seat_no='" + newSeatNo + "' AND flight_id=" + flightID + " AND passenger_id=" + passengerID))[0];
                    this.seatNo = newSeatNo;
                    this.newSeatNo = null;
                } else {
                    throw new IllegalArgumentException("Seat already taken!");
                }
            } else {
                throw new IllegalArgumentException("Seat details missing!");
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("An error occurred while updating the database"+e.getMessage());
        } finally {
            closeConnection();
        }
    }

    @Override
    public String toString() {
        return "Seat{" +
                "seatNo='" + seatNo + '\'' +
                ", newSeatNo='" + newSeatNo + '\'' +
                ", seatClass='" + seatClass + '\'' +
                ", aircraftID='" + aircraftID + '\'' +
                ", flightID='" + flightID + '\'' +
                ", passengerID='" + passengerID + '\'' +
                ", isReturn=" + isReturn +
                ", existingBooking=" + existingBooking +
                '}';
    }
}
