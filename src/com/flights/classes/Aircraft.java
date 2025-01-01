package com.flights.classes;

public abstract class Aircraft {
    private final int id;
    private final int economySeats;
    private final int businessSeats;
    private final int firstClassSeats;
    private int seatAmount;

    public Aircraft(int id, int economySeats, int businessSeats, int firstClassSeats) {
        this.id = id;
        this.economySeats = economySeats;
        this.businessSeats = businessSeats;
        this.firstClassSeats = firstClassSeats;
        this.seatAmount = countSeats();
        Seat[] seats = new Seat[seatAmount];

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

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Aircraft{" +
                "id=" + id +
                ", economySeats=" + economySeats +
                ", businessSeats=" + businessSeats +
                ", firstClassSeats=" + firstClassSeats +
                '}';
    }
}
