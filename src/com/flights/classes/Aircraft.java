package com.flights.classes;

public abstract class Aircraft {
    private int id;
    private int economySeats;
    private int businessSeats;
    private int firstClassSeats;
    private Seat seats[];
    private int seatAmount;

    public Aircraft(int id, int economySeats, int businessSeats, int firstClassSeats) {
        this.id = id;
        this.economySeats = economySeats;
        this.businessSeats = businessSeats;
        this.firstClassSeats = firstClassSeats;
        this.seatAmount = countSeats();
        seats = new Seat[seatAmount];

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
