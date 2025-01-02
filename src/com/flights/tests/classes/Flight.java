package com.flights.tests.classes;

import com.flights.objects.Aircraft;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


public class Flight {
    private final int flightNo;
    private final String departureAirport;
    private final String arrivalAirport;
    private final Timestamp departureTime;
    private final Timestamp arrivalTime;
    private Aircraft aircraft;

    public Flight(int flightNo,String departureAirport,String arrivalAirport,Timestamp departureTime,Timestamp arrivalTime,Aircraft aircraft) {
        this.flightNo = flightNo;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.aircraft = aircraft;
    }

    // second constructor without Aircraft being defined yet
    public Flight(int flightNo,String departureAirport,String arrivalAirport,Timestamp departureTime,Timestamp arrivalTime) {
        this.flightNo = flightNo;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;

    }
    // Set aircraft
    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public int getFlightNo() {
        return flightNo;
    }

    public double flightDuration() {
        // Turn the Timestamp from sql to localDateTime
        LocalDateTime d1 = departureTime.toLocalDateTime();
        LocalDateTime d2 = arrivalTime.toLocalDateTime();

        // ChronoUnit (???) is a standard for Time units. Between will return the difference between the two times
        return (double) ChronoUnit.MINUTES.between(d1,d2);

    }

    @Override
    public String toString() {
        return "Flight{" +
                "flightNo=" + flightNo +
                ", departureAirport='" + departureAirport + '\'' +
                ", arrivalAirport='" + arrivalAirport + '\'' +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                '}';
    }
}
