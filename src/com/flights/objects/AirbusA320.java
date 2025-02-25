package com.flights.objects;

import java.awt.*;
import java.sql.SQLException;

import javax.swing.*;

import com.flights.gui.MainWindow;
import com.flights.util.DBConnectivity;
import com.flights.util.JErrorDialog;

public class AirbusA320 extends Aircraft {

    public AirbusA320(int flightID) {
        super(96,42,12,"Airbus A320");
        generateSeats(flightID);
    }


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

    @Override
    public JPanel renderSeats(Passenger p, boolean isDepartureSeat) {
        return new AirbusA320Seats(getAllSeats(), p, isDepartureSeat);
    }

    static class AirbusA320Seats extends JPanel {
        private AirbusA320Seats(Seat[] seats, Passenger p, boolean isDepartureSeat) {
            setLayout(new FlowLayout());
            setMinimumSize(new Dimension(MainWindow.FRAME_WIDTH, MainWindow.FRAME_HEIGHT));
            add(new JLabel("Airbus A320"));

            JComboBox<String> selector = new JComboBox<>();
            for (Seat s: seats) {
                selector.addItem(s.getSeatNo());
            }
            add(selector);
            JButton confirm = new JButton("Confirm choice");
            confirm.addActionListener(e -> {
                int choice = selector.getSelectedIndex();
                if (seats[choice].isOccupied()) {
                    JErrorDialog.showWarning("Seat is already occupied");
                } else {
                    if (isDepartureSeat) {
                        p.setDepartureSeat(seats[choice]);
                    } else {
                        p.setReturnSeat(seats[choice]);
                    }
                    MainWindow.returnToPreviousMenu();
                }
            });
            add(confirm);
        }
    }
}
