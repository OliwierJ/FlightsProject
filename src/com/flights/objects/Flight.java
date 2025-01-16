package com.flights.objects;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Flight extends DBConnectivity {
    private int flightID;
    private String departureAirport;
    private String arrivalAirport;
    private Timestamp departureTime;
    private Timestamp arrivalTime;
    private Aircraft aircraft;

    // retrieves a flight from database
    public Flight(int flightID) {
        try {
            String[] result = getRow(connectAndExecuteQuery("SELECT departure_airport, arrival_airport, departure_time, arrival_time, aircraft_id FROM flight WHERE flight_id="+flightID));
            this.flightID = flightID;
            this.departureAirport = result[0];
            this.arrivalAirport = result[1];
            this.departureTime = Timestamp.valueOf(result[2]);
            this.arrivalTime = Timestamp.valueOf(result[3]);

            // aircraft of flight goes here
            String[] aircraftResult = getRow(connectAndExecuteQuery("SELECT model, no_economy_seats, no_business_seats, no_firstclass_seats FROM plane WHERE aircraft_id="+result[4]));
            if (aircraftResult[0].equals("Boeing 737-800")) {
                this.aircraft = new Boeing737(100, Integer.parseInt(aircraftResult[1]), Integer.parseInt(aircraftResult[2]), Integer.parseInt(aircraftResult[3]), 100);
            } else {
                System.out.println("Unknown aircraft");
            }
        } catch (SQLException e) {
            System.out.println("An error occurred!"+e.getMessage());
        }
    }

    // testing purposes
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
            String[] aircraftResult = getRow(connectAndExecuteQuery("Select flight_id, " +
                    "departure_time, arrival_time, aircraft_id FROM flight WHERE departure_airport=\""+departureAirport+"\" AND arrival_airport=\""+arrivalAirport + "\" AND CAST(departure_time AS DATE)=\""+date+"\""));


            this.departureAirport = departureAirport;
            this.arrivalAirport = arrivalAirport;
            this.flightID = Integer.parseInt(aircraftResult[0]);
            this.departureTime = Timestamp.valueOf(aircraftResult[1]);
            this.arrivalTime = Timestamp.valueOf(aircraftResult[2]);
        } catch (SQLException e) {
            throw new Exception("No flight found");
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

    public int getPrice() {
        return 0;
    }

    public double getFlightDuration() {
        // Turn the Timestamp from sql to localDateTime
        LocalDateTime d1 = departureTime.toLocalDateTime();
        LocalDateTime d2 = arrivalTime.toLocalDateTime();

        // ChronoUnit (???) is a standard for Time units. Between will return the difference between the two times
        return (double) ChronoUnit.MINUTES.between(d1,d2);

    }

    public Seat[] getSeats() {
        return aircraft.getSeats();
    }

    @Override
    public String toString() {
        return "Flight{" +
                "flightID=" + flightID +
                ", departureAirport='" + departureAirport + '\'' +
                ", arrivalAirport='" + arrivalAirport + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", arrivalTime='" + arrivalTime + '\'' +
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
