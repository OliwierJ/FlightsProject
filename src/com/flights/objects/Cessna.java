package com.flights.objects;

import javax.swing.*;

public class Cessna extends Aircraft {

    public Cessna(int economySeats, int businessSeats, int firstClassSeats) {
        super(economySeats, businessSeats, firstClassSeats, "Cessna");
    }

    @Override
    public JPanel renderSeats() {
        return null;
    }
}
