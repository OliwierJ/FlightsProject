package com.flights.objects;

import com.flights.DBConnectivity;

public class Seat extends DBConnectivity {

    //dont make them final for now
    private String seatNo;
    private String seatClass;
    private String aircraftID;
    private String flightID;
    private String passengerID;
    private boolean isReturn;

    // makes a new passenger seat object from database data
    public Seat(String seatNo, String seatClass, String aircraftID, String flightID, String passengerID, boolean isReturn) {
        this.seatNo = seatNo;
        this.seatClass = seatClass;
        this.aircraftID = aircraftID;
        this.flightID = flightID;
        this.passengerID = passengerID;
        this.isReturn = isReturn;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "seatNo='" + seatNo + '\'' +
                ", seatClass='" + seatClass + '\'' +
                ", aircraftID='" + aircraftID + '\'' +
                ", flightID='" + flightID + '\'' +
                ", passengerID='" + passengerID + '\'' +
                ", isReturn=" + isReturn +
                '}';
    }

    @Override
    public void updateDatabase() {
        //may or may not be needed idk
    }
}
