package com.flights.gui;

import com.flights.gui.components.JTopBar;
import com.flights.objects.Booking;
import com.flights.objects.Flight;
import com.flights.objects.Passenger;

import javax.swing.*;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import com.flights.util.FlightsConstants;

public class ViewBookingMenu extends JPanel implements FlightsConstants, ActionListener {
    private final Booking booking;
    private final JPanel passengersPanel = new JPanel(new CardLayout());
    private final JButton back = new JButton("Back");
    private final JButton next = new JButton("Next");
    private int currentPassenger = 0;
    private final JPanel[] passengerPanels;

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
        passengerPanels = new JPanel[booking.getPassengerCount()];
        for (Passenger p : booking.getPassengers()) {
            JPanel cardPanel = new JPanel();
            cardPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            JPanel pp = new JPanel();
            pp.setLayout(new BoxLayout(pp, BoxLayout.Y_AXIS));
            JLabel p1 = new JLabel("Passenger #"+(i+1)+":");
            String p2String;
            if (p.getTitle() == null || p.getTitle().isEmpty()) {
                p2String = p.getName() + " " + p.getSurname();
            } else {
                p2String = p.getTitle() + " " + p.getName() + " " + p.getSurname();
            }
            JLabel p2 = new JLabel(p2String);
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
            JPanel bp = new JPanel();
            bp.setLayout(new BoxLayout(bp, BoxLayout.Y_AXIS));
            JButton b1 = new JButton("Edit");
            b1.addActionListener(e -> {
                JPanel popup = new JPanel();
                popup.setLayout(new GridLayout(6, 1));
                JLabel l1 = new JLabel("Title:");
                JComboBox<String> titles = new JComboBox<>(new String[]{"Mr", "Mrs", "Ms", "Prefer not to say"});
                if (p.getTitle() == null || p.getTitle().isEmpty()) {
                    titles.setSelectedIndex(3);
                } else {
                    titles.setSelectedItem(p.getTitle());
                }
                JLabel l2 = new JLabel("Name:");
                JTextField name = new JTextField(p.getName(), 20);
                JLabel l3 = new JLabel("Surname:");
                JTextField surname = new JTextField(p.getSurname(),20);

                popup.add(l1);
                popup.add(titles);
                popup.add(l2);
                popup.add(name);
                popup.add(l3);
                popup.add(surname);
                titles.setFont(ARIAL20);
                setAllFonts(popup);
                String[] options = {"Cancel", "Confirm"};
                int n = JOptionPane.showOptionDialog(MainWindow.frame, popup, "Amend details", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                if (n == 1) {
                    if (Objects.equals(titles.getSelectedItem(), "Prefer not to say")) {
                        p.setTitle(null);
                    } else {
                        p.setTitle(Objects.requireNonNull(titles.getSelectedItem()).toString());
                    }
                    p.setName(name.getText());
                    p.setSurname(surname.getText());
                    refreshText();
                }
            });
            bp.add(b1);
            JButton b2 = new JButton("Change departure seat");
            b2.addActionListener(e -> MainWindow.createAndShowGUI(booking.getDepartureFlight().getAircraft().renderSeats(p, true)));
            bp.add(b2);
            if (booking.getReturnFlight() != null) {
                JButton b3 = new JButton("Change return seat");
                b3.addActionListener(e -> MainWindow.createAndShowGUI(booking.getDepartureFlight().getAircraft().renderSeats(p, false)));
                bp.add(b3);
            }
            cardPanel.add(pp);
            cardPanel.add(bp);
            passengersPanel.add(cardPanel, String.valueOf(i));
            passengerPanels[i] = pp;
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

    public void refreshText() {
        JPanel cp = passengerPanels[currentPassenger];
        Passenger p = booking.getPassengers()[currentPassenger];
        JLabel p2 = (JLabel) cp.getComponent(1);
        JLabel p3 = (JLabel) cp.getComponent(2);
        JLabel p4 = (JLabel) cp.getComponent(3);
        String p2String;
        if (p.getTitle() == null || p.getTitle().isEmpty()) {
            p2String = p.getName() + " " + p.getSurname();
        } else {
            p2String = p.getTitle() + " " + p.getName() + " " + p.getSurname();
        }
        p2.setText(p2String);
        p3.setText("Departure seat: "+p.getDepartureSeat().getSeatNo());
        p4.setText("Class: " + p.getDepartureSeat().getSeatClass());
        if (booking.getReturnFlight() != null) {
            JLabel p5 = (JLabel) cp.getComponent(4);
            JLabel p6 = (JLabel) cp.getComponent(5);
            p5.setText("Return seat: " + p.getReturnSeat().getSeatNo());
            p6.setText("Class: " + p.getReturnSeat().getSeatClass());
        }
    }

    private void setAllFonts(JPanel p) {
        for (Component c : p.getComponents()) {
            if (c instanceof JLabel || c instanceof JButton && c.getFont().equals(DEFAULT_FONT)) {
                c.setFont(ARIAL20);
            } else if (c instanceof JPanel) {
                setAllFonts((JPanel) c);
            } else if (c instanceof JTextField && c.getFont().equals(DEFAULT_FONT_TEXTFIELD)) {
                c.setFont(ARIAL20);
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
        MainWindow.createAndShowGUI(new ViewBookingMenu(new Booking("522558", "govie@setu.ie"))); // TODO debug delete later
    }
}
