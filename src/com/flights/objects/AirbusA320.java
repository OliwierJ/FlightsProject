package com.flights.objects;

import java.awt.*;
import java.sql.SQLException;

import javax.swing.*;

import com.flights.Main;
import com.flights.gui.components.JTopBar;
import com.flights.util.DBConnectivity;
import com.flights.util.JErrorDialog;

/**
 * AirbusA320 object extends Aircraft, contains all Seat objects related to a specific Flight
 */
public class AirbusA320 extends Aircraft {

    /**
     * Construct a new AirbusA320 object
     * @param flightID flightID of the Flight
     */
    public AirbusA320(int flightID) {
        super(96,42,12,"Airbus A320");
        generateSeats(flightID);
    }

    /**
     * Retrieve all seats from database and use to generate Seat[] for this Aircraft
     * @param flightID flightID of the flight
     */
    @Override
    protected void generateSeats(int flightID) {
        try {
            String[][] bookedSeats = DBConnectivity.getMultipleRows(DBConnectivity.connectAndExecuteQuery("SELECT seat_no, class, flight_id, passenger_id FROM seat WHERE flight_id='"+flightID+"' ORDER BY seat_no"));
            int count = 0;
            int bookedCount = 0;
            int rowNo = 1;

            while (count < getSeatCount()) {
                for (int i = 0; i < 6; i++) { // alphabet A-F
                    String currentClass;
                    if (count < getFirstClassSeats()) {
                        if (i != 2 && i != 3) {
                            currentClass = "1st class";
                        } else {
                            continue;
                        }
                    } else if (count < getFirstClassSeats()+getBusinessSeats()) {
                        currentClass = "Business";
                    } else {
                        currentClass = "Economy";
                    }
                    String currentSeat;
                    if (rowNo >= 10) {
                        currentSeat = rowNo + "" + ((char) (65 + i));
                    } else {
                        currentSeat = "0" + rowNo + ((char) (65 + i));
                    }
                    if (bookedCount < bookedSeats.length && currentSeat.equals(bookedSeats[bookedCount][0])) {
                        setSeat(bookedSeats[bookedCount], count);
                        bookedCount++;
                    } else {
                        setSeat(new String[]{currentSeat, currentClass, String.valueOf(flightID), null}, count);
                    }
                    count++;
                    if (count >= getSeatCount()) {
                        break;
                    }
                }
                rowNo++;
            }
        } catch (SQLException e) {
            JErrorDialog.showError("An error occurred while retrieving seat details from database", e);
        }
    }

    /**
     * INCOMPLETE renders the Aircraft Seats to be used in the Swing GUI and for flight seat selection
     * @param p the Passenger object to later assign the Seat to
     * @param isReturn <code>true</code> if return flight, <code>false otherwise</code>
     * @param price price to render in JTopBar
     * @return JPanel
     */
    @Override
    public JPanel renderSeats(Passenger p, boolean isReturn, double price) {
        return new AirbusA320Seats(getAllSeats(), p, isReturn, price);
    }

    private static class AirbusA320Seats extends JPanel {
        private AirbusA320Seats(Seat[] seats, Passenger p, boolean isReturn, double price) {
            setLayout(new BorderLayout());
            add(new JTopBar(price), BorderLayout.NORTH);
            setPreferredSize(Main.getFrameSize());
            JPanel content = new JPanel();
            content.setLayout(new FlowLayout());
            content.add(new JLabel("Airbus A320"));

            JComboBox<String> selector = new JComboBox<>();
            for (Seat s: seats) {
                selector.addItem(s.getSeatNo());
            }
            content.add(selector);
            JButton confirm = new JButton("Confirm choice");
            confirm.addActionListener(e -> {
                int choice = selector.getSelectedIndex();
                if (seats[choice].isOccupied()) {
                    JErrorDialog.showWarning("Seat is already occupied");
                } else {
                    if (isReturn) {
                        p.setReturnSeat(seats[choice]);
                    } else {
                        p.setDepartureSeat(seats[choice]);
                    }
                    Main.returnToPreviousMenu();
                }
            });
            content.add(confirm);
            add(content, BorderLayout.CENTER);
        }
    }
}
