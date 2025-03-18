package com.flights.objects;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.flights.util.DBConnectivity;
import com.flights.util.JErrorDialog;

public class Flight extends DBConnectivity {
    private int flightID;
    private String departureAirport;
    private String arrivalAirport;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Aircraft aircraft;
    private double basePrice;

    // retrieves a flight from database
    public Flight(int flightID) {
        try {
            String[] result = getRow(connectAndExecuteQuery("SELECT departure_airport, arrival_airport, departure_time, arrival_time, aircraft, base_price FROM flight WHERE flight_id="+flightID));
            this.flightID = flightID;
            this.departureAirport = result[0];
            this.arrivalAirport = result[1];
            this.departureTime = Timestamp.valueOf(result[2]).toLocalDateTime();
            this.arrivalTime = Timestamp.valueOf(result[3]).toLocalDateTime();

            setAircraft(result[4]);
            this.basePrice = Double.parseDouble(result[5]);
        } catch (SQLException e) {
            JErrorDialog.showError("An error occurred while retrieving flight details", e);
        } finally {
            closeConnection();
        }
    }

    // used by flight selection menu
    public Flight(String departureAirport, String arrivalAirport, String date) {
        try {
            String[] result = getRow(connectAndExecuteQuery("Select flight_id, departure_time, arrival_time, aircraft, base_price FROM flight WHERE departure_airport=\""+departureAirport+"\" AND arrival_airport=\""+arrivalAirport + "\" AND CAST(departure_time AS DATE)=\""+date+"\""));
            this.departureAirport = departureAirport;
            this.arrivalAirport = arrivalAirport;
            this.flightID = Integer.parseInt(result[0]);
            this.departureTime = Timestamp.valueOf(result[1]).toLocalDateTime();
            this.arrivalTime = Timestamp.valueOf(result[2]).toLocalDateTime();
            setAircraft(result[3]);
            this.basePrice = Double.parseDouble(result[4]);
        } catch (SQLException e) {
            throw new IllegalArgumentException("No flight found");
        } finally {
            closeConnection();
        }
    }

    private void setAircraft(String name) {
        // aircraft of flight goes here
        if (name.equals("Boeing 737-800")) {
            this.aircraft = new Boeing737(flightID);
        } else if (name.equals("Airbus A320")) {
            this.aircraft = new AirbusA320(flightID);
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
        return departureTime.toLocalTime().toString();
    }

    public String getArrivalTime() {
        return arrivalTime.toLocalTime().toString();
    }

    public LocalTime getDepartureLocalTime() {
        return departureTime.toLocalTime();
    }

    public LocalTime getArrivalLocalTime() {
        return arrivalTime.toLocalTime();
    }

    public LocalDateTime getDepartureLocalDateTime() {
        return departureTime;
    }

    public LocalDateTime getArrivalLocalDateTime() {
        return arrivalTime;
    }

    public LocalDate getDepartureLocalDate() {
        return departureTime.toLocalDate();
    }

    public LocalDate getArrivalLocalDate() {
        return arrivalTime.toLocalDate();
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

    public int getFlightDuration() {
        return (int) Duration.between(departureTime, arrivalTime).toMinutes();

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

    public static String[][] getFlightInfo(String dep, String arr) throws SQLException {
        return getMultipleRows(connectAndExecuteQuery("SELECT * FROM flight WHERE departure_airport='"+dep+"' AND arrival_airport='"+arr+"'"));
    }

    @Override
    protected void updateDatabase() {
        throw new UnsupportedOperationException("Can't update flights database!");
        // empty, nothing needs to ever be updated in flight or aircraft, for now
    }
}
