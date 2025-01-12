package com.flights.objects;

import javax.swing.*;

public class Boeing777 extends Aircraft {

    public Boeing777(int economySeats, int businessSeats, int firstClassSeats) {
        super(economySeats,businessSeats,firstClassSeats, "Boeing 777");
    }


    @Override
    public JPanel renderSeats() {
        return null;
    }
}
