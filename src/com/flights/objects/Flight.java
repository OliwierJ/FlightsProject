package com.flights.objects;

import com.flights.DBConnectivity;

public class Flight extends DBConnectivity {
    private int flightID;
    private String departureAirport;
    private String arrivalAirport;
    private String departureTime;
    private String arrivalTime;
    private Aircraft aircraft;

    public Flight(int flightID) {
        try {
            String[] result = getRow(connectAndExecuteQuery("SELECT departure_airport, arrival_airport, departure_time, arrival_time, aircraft_id FROM flight WHERE flight_id="+flightID));
            this.flightID = flightID;
            this.departureAirport = result[0];
            this.arrivalAirport = result[1];
            this.departureTime = result[2];
            this.arrivalTime = result[3];

            // aircraft of flight goes here
            String[] aircraftResult = getRow(connectAndExecuteQuery("SELECT model, no_economy_seats, no_business_seats, no_firstclass_seats FROM plane WHERE aircraft_id="+result[4]));
            if (aircraftResult[0].equals("Boeing 737-800")) {
                this.aircraft = new Boeing737(Integer.parseInt(aircraftResult[1]), Integer.parseInt(aircraftResult[2]), Integer.parseInt(aircraftResult[3]));
            } else {
                System.out.println("Unknown aircraft");
            }
        } catch (Exception e) {
            System.out.println("An error occurred!");
        }
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public int getFlightID() {
        return flightID;
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

    // TODO; OTHER STUFF GOES HERE COPY FROM FLIGHT CLASS
    //todo: like converting to timestamp

    @Override
    public void updateDatabase() {
        // empty, nothing needs to ever be updated in flight or aircraft, for now
    }
}
