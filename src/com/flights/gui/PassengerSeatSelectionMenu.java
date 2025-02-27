package com.flights.gui;

import com.flights.gui.components.JTopBar;
import com.flights.objects.Booking;
import com.flights.objects.Flight;
import com.flights.objects.Passenger;
import com.flights.util.FlightsConstants;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class PassengerSeatSelectionMenu extends JPanel implements FlightsConstants{
    private final Booking b;
    private final JPanel[] departureSeatPanels;
    private final JPanel[] returnSeatPanels;

    public PassengerSeatSelectionMenu(Booking b) {
        this.b = b;
        this.departureSeatPanels = new JPanel[b.getPassengerCount()];
        this.returnSeatPanels = new JPanel[b.getPassengerCount()];

        setPreferredSize(new Dimension(MainWindow.FRAME_WIDTH, MainWindow.FRAME_HEIGHT));
        setLayout(new BorderLayout());
        add(new JTopBar(), BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(Box.createVerticalStrut(75));

        for (int i = 0; i < b.getPassengerCount(); i++) {
            JPanel pp = new PassengerSeatSelectionPanel(i, b.getPassengers()[i]);
            mainPanel.add(pp);
            mainPanel.add(Box.createVerticalStrut(50));
        }

        JButton confirm = new JButton("Confirm seat selection");
        confirm.setBorder(new EmptyBorder(5,20,5,20));
        confirm.setBackground(MAIZE);
        confirm.setFocusable(false);
        confirm.setFont(new Font("Arial", Font.PLAIN, 18));
        confirm.setAlignmentX(CENTER_ALIGNMENT);
        confirm.addActionListener(e -> {
            boolean valid = true;
            for (Passenger p: b.getPassengers()) {
                if (p.getDepartureSeat() == null || (b.getReturnFlight() != null && p.getReturnSeat() == null)) {
                    JOptionPane.showMessageDialog(this, "Please choose seats for all passengers");
                    valid = false;
                    break;
                }
            }
            if (valid) {
                int n = JOptionPane.showConfirmDialog(this, "Are you sure you want to continue?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (n == 0) {
                    MainWindow.createAndShowGUI(new ExtrasMenu(b));
                }
            }
        });

        mainPanel.add(confirm);
        mainPanel.add(Box.createVerticalStrut(25));

        JScrollPane sp = new JScrollPane(mainPanel);
        sp.getVerticalScrollBar().setUnitIncrement(15);
        add(sp, BorderLayout.CENTER);
    }

    public void refreshText() {
        for (int i = 0; i < b.getPassengerCount(); i++) {
            JPanel jp = departureSeatPanels[i];
            setSeatLabelText((JLabel) jp.getComponent(1), b.getPassengers()[i], false);
            if (b.getReturnFlight() != null) {
                jp = returnSeatPanels[i];
                setSeatLabelText((JLabel) jp.getComponent(1), b.getPassengers()[i], true);
            }
        }
    }

    private class PassengerSeatSelectionPanel extends JPanel {
        PassengerSeatSelectionPanel(int count, Passenger p) {
            setPreferredSize(new Dimension(1000, 180));
            setMinimumSize(new Dimension(1000, 180));
            setMaximumSize(new Dimension(1000, 180));
            setLayout(new BorderLayout());

            JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            titlePanel.setBackground(APPLEGREEN);
            int titleHeight = 60;
            titlePanel.setPreferredSize(new Dimension(1000, titleHeight));
            titlePanel.setMinimumSize(new Dimension(1000, titleHeight));
            titlePanel.setMaximumSize(new Dimension(1000, titleHeight));

            JLabel title = new JLabel("Passenger "+(count+1)+": "+p.getName()+" "+p.getSurname());
            title.setFont(new Font("Arial", Font.BOLD, 35));
            title.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

            titlePanel.add(Box.createHorizontalStrut(10));
            titlePanel.add(title);

            JPanel buttonsPanel = new JPanel();
            buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
            buttonsPanel.setBackground(ASPARAGUS);
            buttonsPanel.setPreferredSize(new Dimension(800, 180 - titleHeight));

            JPanel departureSeatPanel = new SeatButtonPanel(false, p);
            buttonsPanel.add(departureSeatPanel);
            departureSeatPanels[count] = departureSeatPanel;

            buttonsPanel.add(Box.createHorizontalGlue());
            if (b.getReturnFlight() != null) {
                JPanel returnSeatPanel = new SeatButtonPanel(true, p);
                buttonsPanel.add(returnSeatPanel);
                returnSeatPanels[count] = returnSeatPanel;
            }
            add(titlePanel, BorderLayout.NORTH);
            add(buttonsPanel, BorderLayout.CENTER);
        }
    }

    private class SeatButtonPanel extends JPanel {
        SeatButtonPanel(boolean isReturn, Passenger p) {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setOpaque(false);
            setBorder(new EmptyBorder(0,25,0,175));

            JLabel label = new JLabel();
            setSeatLabelText(label, p, isReturn);
            label.setFont(ARIAL20);

            JButton button = new JButton("Select seat");
            button.setBorder(new EmptyBorder(5,20,5,20));
            button.setBackground(MAIZE);
            button.setFocusable(false);
            button.setFont(new Font("Arial", Font.PLAIN, 18));
            button.addActionListener(e -> {
                if (isReturn) {
                    MainWindow.createAndShowGUI(b.getReturnFlight().getAircraft().renderSeats(p, isReturn));
                } else {
                    MainWindow.createAndShowGUI(b.getDepartureFlight().getAircraft().renderSeats(p, isReturn));
                }
            });

            add(Box.createVerticalGlue());
            add(label);
            add(Box.createVerticalStrut(20));
            add(button);
            add(Box.createVerticalGlue());
        }
    }

    public void setSeatLabelText(JLabel l, Passenger p, boolean isReturn) {
        if (isReturn) {
            if (p.getReturnSeat() != null) {
                l.setText("Return seat: "+p.getReturnSeat().getSeatNo()+", "+p.getReturnSeat().getSeatClass());
            } else {
                l.setText("Return seat: not selected yet");
            }
        } else {
            if (p.getDepartureSeat() != null) {
                l.setText("Departure seat: "+p.getDepartureSeat().getSeatNo()+", "+p.getDepartureSeat().getSeatClass());
            } else {
                l.setText("Departure seat: not selected yet");
            }
        }
    }

    public static void main(String[] args) {
        Booking b = new Booking("Basic");
        b.setDepartureFlight(new Flight(100));
        b.setReturnFlight(new Flight(101));
        b.addPassengers(new Passenger("Mr", "Brandon", "Jaroszczak", b.getBookingID()), new Passenger(null, "Other", "Guy", b.getBookingID()), new Passenger("Mrs", "Some random", "woman", b.getBookingID()), new Passenger("Ms", "Oliwier", "Jakubiec", b.getBookingID()));
        MainWindow.createAndShowGUI(new PassengerSeatSelectionMenu(b));
    }
}
