package com.flights.gui;

import com.flights.objects.Booking;
import com.flights.objects.Flight;
import com.flights.objects.Passenger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.flights.util.FlightsConstants;

public class ViewBookingMenu extends JPanel implements FlightsConstants, ActionListener {
    private final Booking booking;
    JPanel passengersPanel = new JPanel(new CardLayout());
    JButton back = new JButton("Back");
    JButton next = new JButton("Next");
    int currentPassenger = 0;
    private final Font DEFAULT_FONT = new JLabel().getFont();

    public ViewBookingMenu(Booking booking) {
        this.booking = booking;
        setPreferredSize(new Dimension(MainWindow.FRAME_WIDTH, MainWindow.FRAME_HEIGHT));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel topBar = new JTopBar();
        topBar.setAlignmentX(CENTER_ALIGNMENT);
        add(topBar);

        JLabel title = new JLabel("My Booking");
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title);

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setAlignmentX(Component.RIGHT_ALIGNMENT);
        JLabel t1 = new JLabel("Booking ID: " + booking.getBookingID());
        JLabel t2 = new JLabel("Email: " + booking.getEmail());
        JLabel t3 = new JLabel();
        if (booking.getPriorityBoarding() == 1) {
            t3.setText("Priority boarding included");
        } else {
            t3.setText("Priority boarding not included");
        }
        JLabel t4 = new JLabel("Total luggage: " + booking.getLuggage());

        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(Box.createVerticalStrut(30));
        top.add(t1);
        top.add(t2);
        top.add(t3);
        top.add(t4);
        if (booking.get20kgluggage()) {
            JLabel t5 = new JLabel("20kg luggage included");
            top.add(t5);
        }
        top.add(Box.createVerticalStrut(50));
        JLabel t6 = new JLabel("Flight details:");
        top.add(t6);
        top.add(Box.createHorizontalGlue());
        add(top);

        JPanel flightsPanel = new JPanel();
        flightsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        flightsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel dp = new JPanel();
        dp.setLayout(new BoxLayout(dp, BoxLayout.Y_AXIS));
        Flight depFlight = booking.getDepartureFlight();
        JLabel d1 = new JLabel("Departure Flight");
        JLabel d2 = new JLabel("Departure airport: " + depFlight.getDepartureAirport());
        JLabel d3 = new JLabel("Arrival airport: " + depFlight.getArrivalAirport());
        JLabel d4 = new JLabel("Departure date: " + depFlight.getDepartureDate());
        JLabel d5 = new JLabel("Arrival date: " + depFlight.getArrivalDate());
        dp.add(d1);
        dp.add(d2);
        dp.add(d3);
        dp.add(d4);
        dp.add(d5);
        flightsPanel.add(dp);

        if (booking.getReturnFlight() != null) {
            flightsPanel.add(Box.createHorizontalStrut(30));
            JPanel rp = new JPanel();
            rp.setLayout(new BoxLayout(rp, BoxLayout.Y_AXIS));
            Flight returnFlight = booking.getReturnFlight();
            JLabel r1 = new JLabel("Return Flight");
            JLabel r2 = new JLabel("Departure airport: " + returnFlight.getDepartureAirport());
            JLabel r3 = new JLabel("Arrival airport: " + returnFlight.getArrivalAirport());
            JLabel r4 = new JLabel("Departure date: " + returnFlight.getDepartureDate());
            JLabel r5 = new JLabel("Arrival date: " + returnFlight.getArrivalDate());
            rp.add(r1);
            rp.add(r2);
            rp.add(r3);
            rp.add(r4);
            rp.add(r5);
            flightsPanel.add(rp);
        }
        add(flightsPanel, BorderLayout.CENTER);
        add(Box.createVerticalStrut(30));

        int i = 0;
        for (Passenger p : booking.getPassengers()) {
            JPanel pp = new JPanel();
            pp.setLayout(new BoxLayout(pp, BoxLayout.Y_AXIS));
            JLabel p1 = new JLabel("Passenger ID: " + p.getPassengerID());
            JLabel p2 = new JLabel(p.getTitle() + " " + p.getName() + " " + p.getSurname());
            JLabel p3 = new JLabel("Departure seat: " + p.getDepartureSeat().getSeatNo());
            JLabel p4 = new JLabel("Class: " + p.getDepartureSeat().getSeatClass());
            pp.add(p1);
            pp.add(p2);
            pp.add(p3);
            pp.add(p4);
            if (booking.getReturnFlight() != null) {
                JLabel p5 = new JLabel("Return seat: " + p.getReturnSeat().getSeatNo());
                JLabel p6 = new JLabel("Class: " + p.getReturnSeat().getSeatClass());
                pp.add(p5);
                pp.add(p6);
            }
            JButton test = new JButton("Test");
            test.addActionListener(e -> {
                p.setName("Test");
                p2.setText(p.getTitle() + " " + p.getName() + " " + p.getSurname());
                System.out.println(p.getName());
            });
            pp.add(test);
            passengersPanel.add(pp, String.valueOf(i));
            i++;
        }
        add(passengersPanel);

        back.setEnabled(false);
        back.addActionListener(this);
        next.addActionListener(this);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Color.GREEN);
        buttonPanel.add(back);
        buttonPanel.add(next);
        add(buttonPanel);

        setAllFonts(this);
    }

    private void setAllFonts(JPanel p) {
        for (Component c : p.getComponents()) {
            if (c instanceof JLabel || c instanceof JButton) {
                if (c.getFont().equals(DEFAULT_FONT)) {
                    c.setFont(ARIAL20);
                }
            } else if (c instanceof JPanel) {
                setAllFonts((JPanel) c);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == back) {
            currentPassenger--;
        } else if (e.getSource() == next) {
            currentPassenger++;
        }
        back.setEnabled(currentPassenger > 0);
        next.setEnabled(currentPassenger != booking.getPassengerCount()-1);
        CardLayout cl = (CardLayout)(passengersPanel.getLayout());
        cl.show(passengersPanel, String.valueOf(currentPassenger));
    }

    public static void main(String[] args) {
        MainWindow.createAndShowGUI(new ViewBookingMenu(new Booking("522558", "C00296052@setu.ie")));
    }
}
