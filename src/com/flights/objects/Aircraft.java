package com.flights.objects;

public abstract class Aircraft {
    private final int economySeats;
    private final int businessSeats;
    private final int firstClassSeats;
    private int seatAmount;

    public Aircraft(int economySeats, int businessSeats, int firstClassSeats) {
        this.economySeats = economySeats;
        this.businessSeats = businessSeats;
        this.firstClassSeats = firstClassSeats;
        this.seatAmount = countSeats();
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

    @Override
    public String toString() {
        return "Aircraft{" +
                ", economySeats=" + economySeats +
                ", businessSeats=" + businessSeats +
                ", firstClassSeats=" + firstClassSeats +
                '}';
    }
}
