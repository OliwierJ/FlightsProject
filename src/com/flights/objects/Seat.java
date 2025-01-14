package com.flights.objects;

public class Seat {

    private final String seatNo; 
    private final String seatClass;
    private final String aircraftID;
    private final String flightID;
    private String passengerID;

    // create a new seat object, either from Passenger class when viewing booking, or from Flight/Aircraft class when generating flight seats
    public Seat(String seatNo, String seatClass, String aircraftID, String flightID, String passengerID) {
        this.seatNo = seatNo;
        this.seatClass = seatClass;
        this.aircraftID = aircraftID;
        this.flightID = flightID;
        this.passengerID = passengerID;
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

    // This should only be used by the Passenger class!!!
    public void setPassengerID(String passengerID) {
        this.passengerID = passengerID;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "seatNo='" + seatNo + '\'' +
                ", seatClass='" + seatClass + '\'' +
                ", aircraftID='" + aircraftID + '\'' +
                ", flightID='" + flightID + '\'' +
                ", passengerID='" + passengerID + '\'' +
                '}'+"\n";
    }
}
