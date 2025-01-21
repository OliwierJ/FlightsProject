package com.flights.objects;

import javax.swing.*;

public class Boeing777 extends Aircraft {

    public Boeing777(int flightID) {
        super(0,0,0,"Boeing 777");
        generateSeats(flightID);
    }


    @Override
    protected void generateSeats(int flightID) {

    }

    @Override
    public JPanel renderSeats() {
        return null;
    }
}
