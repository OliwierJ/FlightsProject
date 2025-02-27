package com.flights.gui;

import com.flights.objects.Booking;
import com.flights.objects.Flight;
import com.flights.objects.Passenger;

import java.util.Arrays;

import javax.swing.*;

public class PassengerSeatSelectionMenu extends JPanel {
    private final Booking b;
    private final JPanel[] passengerPanels;

    public PassengerSeatSelectionMenu(Booking b) {
        this.b = b;
        this.passengerPanels = new JPanel[b.getPassengerCount()];
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // TODO: refactor layout to look similar to the previous passenger screen
        // for testing purposes ONLY
        int i = 0;
        for (Passenger p: b.getPassengers()) {
            JPanel pp = new JPanel();
            JLabel p1 = new JLabel("Passenger #"+(i+1)+": "+p.getDepartureSeat()+", "+p.getReturnSeat());
            JButton setDeparture = new JButton("Set departure seat");
            setDeparture.addActionListener(e -> {
                MainWindow.createAndShowGUI(b.getDepartureFlight().getAircraft().renderSeats(p, false));
            });
            pp.add(p1);
            pp.add(setDeparture);
            if (b.getReturnFlight() != null) {
                JButton setReturn = new JButton("Set return seat");
                setReturn.addActionListener(e -> {
                    MainWindow.createAndShowGUI(b.getReturnFlight().getAircraft().renderSeats(p, true));
                });
                pp.add(setReturn);
            }
            add(pp);
            passengerPanels[i] = pp;
            i++;
        }
    }

    public void refreshText() {
        for (int i = 0; i < passengerPanels.length; i++) {
            JPanel jp = passengerPanels[i];
            JLabel l = (JLabel) jp.getComponent(0);
            Passenger p = b.getPassengers()[i];
            l.setText("Passenger #"+(i+1)+": "+p.getDepartureSeat()+", "+p.getReturnSeat());
        }
    }

    public static void main(String[] args) {
        Booking b = new Booking("Basic");
        b.setDepartureFlight(new Flight(100));
        b.setReturnFlight(new Flight(101));
        b.addPassengers(new Passenger("Mr", "Brandon", "Jaroszczak", b.getBookingID()), new Passenger(null, "Other", "Guy", b.getBookingID()), new Passenger("Mrs", "Some random", "woman", b.getBookingID()));
        MainWindow.createAndShowGUI(new PassengerSeatSelectionMenu(b));
    }
}
