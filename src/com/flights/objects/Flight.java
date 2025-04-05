package com.flights.objects;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.flights.util.DBConnectivity;
import com.flights.util.JErrorDialog;

/**
 * Flight object containing all flight details including Aircraft and Seats
 */
public class Flight extends DBConnectivity {
    private int flightID;
    private String departureAirport;
    private String arrivalAirport;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Aircraft aircraft;
    private double basePrice;

    /**
     * Construct a new Flight object using flightID to retrieve from database
     * @param flightID ID of flight in database
     */
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

    /**
     * Construct a new Flight object
     * @param departureAirport departure airport of flight
     * @param arrivalAirport arrival airport of flight
     * @param date date of flight
     */
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

    /**
     * Get Flight departure airport
     * @return String
     */
    public String getDepartureAirport() {
        return departureAirport;
    }

    /**
     * Get Flight arrival airport
     * @return String
     */
    public String getArrivalAirport() {
        return arrivalAirport;
    }

    /**
     * Get Flight departure time
     * @return String
     */
    public String getDepartureTime() {
        return departureTime.toLocalTime().toString();
    }

    /**
     * Get Flight arrival time
     * @return String
     */
    public String getArrivalTime() {
        return arrivalTime.toLocalTime().toString();
    }

    /**
     * Get Flight departure time
     * @return LocalTime
     */
    public LocalTime getDepartureLocalTime() {
        return departureTime.toLocalTime();
    }

    /**
     * Get Flight arrival time
     * @return LocalTime
     */
    public LocalTime getArrivalLocalTime() {
        return arrivalTime.toLocalTime();
    }

    /**
     * Get Flight departure date and time
     * @return LocalDateTime
     */
    public LocalDateTime getDepartureLocalDateTime() {
        return departureTime;
    }

    /**
     * Get Flight arrival date and time
     * @return LocalDateTime
     */
    public LocalDateTime getArrivalLocalDateTime() {
        return arrivalTime;
    }

    /**
     * Get Flight departure date
     * @return LocalDate
     */
    public LocalDate getDepartureLocalDate() {
        return departureTime.toLocalDate();
    }

    /**
     * Get Flight arrival date
     * @return LocalDate
     */
    public LocalDate getArrivalLocalDate() {
        return arrivalTime.toLocalDate();
    }

    /**
     * Get Flight Aircraft
     * @return Aircraft
     */
    public Aircraft getAircraft() {
        return aircraft;
    }

    /**
     * Get Flight id
     * @return int
     */
    public int getFlightID() {
        return flightID;
    }

    /**
     * Get Flight base price, multipliers to be added during the booking process for different tiers of booking
     * @return double
     */
    public double getBasePrice() {
        return basePrice;
    }

    /**
     * Get Flight duration in minutes
     * @return int
     */
    public int getFlightDuration() {
        return (int) Duration.between(departureTime, arrivalTime).toMinutes();
    }

    /**
     * Get all Flight Seats
     * @return Seat[]
     */
    public Seat[] getAllSeats() {
        return aircraft.getAllSeats();
    }

    /**
     * Ger a specific Flight Seat
     * @param i index of Seat
     * @return Seat
     */
    public Seat getSeat(int i) {
        return aircraft.getSeat(i);
    }

    /**
     * Get all Flight details for debug purposes
     * @return String
     */
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

    /**
     * DO NOT USE
     * @throws UnsupportedOperationException flights DB never gets updated in this project
     */
    @Override
    protected void updateDatabase() {
        throw new UnsupportedOperationException("Can't update flights database!");
    }
}
