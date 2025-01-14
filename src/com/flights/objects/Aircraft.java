package com.flights.objects;

import javax.swing.*;

public abstract class Aircraft {
    private final String flightID;
    private final int aircraftID;
    private final int economySeats;
    private final int businessSeats;
    private final int firstClassSeats;
    private final int seatAmount;
    private final String name;
    private final Seat[] seats;
    protected static char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K'};

    public Aircraft(int aircraftID, int economySeats, int businessSeats, int firstClassSeats, String name, int flightID) {
        this.flightID = String.valueOf(flightID);
        this.aircraftID = aircraftID;
        this.economySeats = economySeats;
        this.businessSeats = businessSeats;
        this.firstClassSeats = firstClassSeats;
        this.seatAmount = economySeats+businessSeats+firstClassSeats;
        this.name = name;
        this.seats = new Seat[seatAmount];
    }

    public int getSeatCount() {
        return seatAmount;
    }

    public String getName() {
        return name;
    }

    public int getEconomySeats() {
        return economySeats;
    }

    public int getBusinessSeats() {
        return businessSeats;
    }

    public int getFirstClassSeats() {
        return firstClassSeats;
    }

    public Seat[] getSeats() {
        return seats;
    }

    public int getAircraftID() {
        return aircraftID;
    }

    public String getFlightID() {
        return flightID;
    }

    public Seat getSeat(int i) {
        return seats[i];
    }

    public void setSeat(String[] s, int i) {
        this.seats[i] = new Seat(s[0], s[1], s[2], s[3], s[4]);
    }

    protected abstract void generateSeats(); // TODO: method for generating complex seat layout nos

    public abstract JPanel renderSeats(); // TODO: method for rendering complex seating layouts (see Boeing 777 layout online)

    @Override
    public String toString() {
        return "Aircraft{" +
                "economySeats=" + economySeats +
                ", businessSeats=" + businessSeats +
                ", firstClassSeats=" + firstClassSeats +
                ", seatAmount=" + seatAmount +
                ", name='" + name + '\'' +
                '}';
    }
}
