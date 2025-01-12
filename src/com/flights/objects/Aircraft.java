package com.flights.objects;

import javax.swing.*;

public abstract class Aircraft {
    private final int economySeats;
    private final int businessSeats;
    private final int firstClassSeats;
    private int seatAmount;
    private final String name;

    public Aircraft(int economySeats, int businessSeats, int firstClassSeats, String name) {
        this.economySeats = economySeats;
        this.businessSeats = businessSeats;
        this.firstClassSeats = firstClassSeats;
        this.seatAmount = countSeats();
        this.name = name;
    }

    private int countSeats() {
        seatAmount += economySeats;
        seatAmount += businessSeats;
        seatAmount += firstClassSeats;
        return seatAmount;
    }

    public int getSeatAmount() {
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
