package com.flights.objects;

import javax.swing.*;

public class Boeing737 extends Aircraft {
    public Boeing737(int economySeats, int businessSeats, int firstClassSeats) {
        super(economySeats, businessSeats, firstClassSeats, "Boeing 737");
    }

    @Override
    public JPanel renderSeats() {
        return null;
    }
}
