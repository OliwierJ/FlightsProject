package com.flights.objects;

import java.util.Arrays;

import javax.swing.*;

public abstract class Aircraft {
    private final int economySeats;
    private final int businessSeats;
    private final int firstClassSeats;
    private final int seatAmount;
    private final String name;
    private final Seat[] seats;

    public Aircraft(int economySeats, int businessSeats, int firstClassSeats, String name) {
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

    public Seat[] getAllSeats() {
        return seats;
    }

    public Seat getSeat(int i) {
        return seats[i];
    }

    protected void setSeat(String[] s, int i) {
        this.seats[i] = new Seat(s[0], s[1], s[2], s[3]);
    }

    protected abstract void generateSeats(int flightID);

    public abstract JPanel renderSeats(); // TODO: method for rendering complex seating layouts (see Boeing 777 layout online)

    @Override
    public String toString() {
        return "Aircraft{" +
                "economySeats=" + economySeats +
                ", businessSeats=" + businessSeats +
                ", firstClassSeats=" + firstClassSeats +
                ", seatAmount=" + seatAmount +
                ", name='" + name + '\'' +
                ", seats=\n'" + Arrays.toString(seats) + '\'' +
                '}';
    }
}
