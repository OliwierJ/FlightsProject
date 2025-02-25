package com.flights.gui;

import com.flights.objects.Booking;
import com.flights.objects.Flight;
import com.flights.objects.Passenger;

import javax.swing.*;
import java.util.Arrays;

public class PassengerSeatSelectionMenu extends JPanel {
    private final JLabel testLabel = new JLabel();
    private final Booking b;
    public PassengerSeatSelectionMenu(Booking b) {
        this.b = b;
        // TODO: basically identical screen to the previous one but with buttons to choose passenger seats
        // for testing purposes ONLY
        JButton test = new JButton("Test");
        System.out.println(Arrays.toString(b.getPassengers()));
        // some buttons will set departure while others set return seat
        test.addActionListener(e -> {
            MainWindow.createAndShowGUI(b.getDepartureFlight().getAircraft().renderSeats(b.getPassengers()[0], false));
        });
        add(test);
        refreshText(); // render text
        add(testLabel);
    }

    public void refreshText() {
        testLabel.setText("Passenger departure seat="+b.getPassengers()[0].getDepartureSeat());
        // TODO: refresh/render any labels that contain seatNos by using setText again
    }

    public static void main(String[] args) {
        Booking b = new Booking("Basic");
        b.setDepartureFlight(new Flight(100));
        b.addPassengers(new Passenger("Mr", "Brandon", "Jaroszczak", b.getBookingID()));
        MainWindow.createAndShowGUI(new PassengerSeatSelectionMenu(b));
    }
}
