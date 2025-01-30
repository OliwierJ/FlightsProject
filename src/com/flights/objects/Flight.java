package com.flights.objects;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.flights.util.DBConnectivity;
import com.flights.util.JErrorDialog;

public class Flight extends DBConnectivity {
    private int flightID;
    private String departureAirport;
    private String arrivalAirport;
    private Timestamp departureTime;
    private Timestamp arrivalTime;
    private Aircraft aircraft;
    private double basePrice;

    // retrieves a flight from database
    public Flight(int flightID) {
        try {
            String[] result = getRow(connectAndExecuteQuery("SELECT departure_airport, arrival_airport, departure_time, arrival_time, aircraft, base_price FROM flight WHERE flight_id="+flightID));
            this.flightID = flightID;
            this.departureAirport = result[0];
            this.arrivalAirport = result[1];
            this.departureTime = Timestamp.valueOf(result[2]);
            this.arrivalTime = Timestamp.valueOf(result[3]);
            setAircraft(result[4]);
            this.basePrice = Double.parseDouble(result[5]);
        } catch (SQLException e) {
            JErrorDialog.showError("An error occurred while retrieving flight details", e);
        }
    }

    // TODO: delete later testing purposes
    public Flight(int flightID, String departureAirport, String arrivalAirport, Timestamp departureTime, Timestamp arrivalTime) {
        this.flightID = flightID;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    // used by flight selection menu
    public Flight(String departureAirport, String arrivalAirport, String date) throws Exception {
        try {
            String[] result = getRow(connectAndExecuteQuery("Select flight_id, departure_time, arrival_time, aircraft, base_price FROM flight WHERE departure_airport=\""+departureAirport+"\" AND arrival_airport=\""+arrivalAirport + "\" AND CAST(departure_time AS DATE)=\""+date+"\""));
            this.departureAirport = departureAirport;
            this.arrivalAirport = arrivalAirport;
            this.flightID = Integer.parseInt(result[0]);
            this.departureTime = Timestamp.valueOf(result[1]);
            this.arrivalTime = Timestamp.valueOf(result[2]);
            setAircraft(result[3]);
            this.basePrice = Double.parseDouble(result[4]);
        } catch (SQLException e) {
            throw new IllegalArgumentException("No flight found");
        }
    }

    private void setAircraft(String name) {
        // aircraft of flight goes here
        if (name.equals("Boeing 737-800")) {
            this.aircraft = new Boeing737(flightID);
        } else if (name.equals("Boeing 777")) {
            this.aircraft = new Boeing777(flightID);
        } else {
            throw new IllegalArgumentException("Unknown aircraft!");
        }
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public String getDepartureTime() {
        LocalDateTime t = departureTime.toLocalDateTime();
        String str = t.getHour() + ":";
        str = t.getMinute() < 10 ? str + "0" + t.getMinute() : str + t.getMinute();
        return str;
    }

    public String getArrivalTime() {
        LocalDateTime t = arrivalTime.toLocalDateTime();
        String str = t.getHour() + ":";
        str = t.getMinute() < 10 ? str + "0" + t.getMinute() : str + t.getMinute();
        return str;
    }

    public String getDepartureDate() {
        return departureTime.toString();
    }
    public String getArrivalDate() {
        return arrivalTime.toString();
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public int getFlightID() {
        return flightID;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public double getFlightDuration() {
        // Turn the Timestamp from sql to localDateTime
        LocalDateTime d1 = departureTime.toLocalDateTime();
        LocalDateTime d2 = arrivalTime.toLocalDateTime();

        // ChronoUnit (???) is a standard for Time units. Between will return the difference between the two times
        return (double) ChronoUnit.MINUTES.between(d1,d2);

    }

    public Seat[] getAllSeats() {
        return aircraft.getAllSeats();
    }

    public Seat getSeat(int i) {
        return aircraft.getSeat(i);
    }

    @Override
    public String toString() {
        return "Flight{" +
                "flightID=" + flightID +
                ", departureAirport='" + departureAirport + '\'' +
                ", arrivalAirport='" + arrivalAirport + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", arrivalTime='" + arrivalTime + '\'' +
                ", basePrice=" + basePrice +
                ", aircraft=" + aircraft +
                '}';
    }

    // copied from deleted DBUtil class
    public static String[][] getFlightInfo(String dep, String arr) throws Exception {
        return getMultipleRows(connectAndExecuteQuery("SELECT * FROM flight WHERE departure_airport='"+dep+"' AND arrival_airport='"+arr+"'"));
    }

    @Override
    protected void updateDatabase() {
        // empty, nothing needs to ever be updated in flight or aircraft, for now
    }
}
