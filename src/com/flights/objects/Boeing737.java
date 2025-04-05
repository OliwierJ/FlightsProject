package com.flights.objects;

import java.sql.SQLException;

import javax.swing.*;

import com.flights.gui.SelectSeatBoeing737;
import com.flights.util.DBConnectivity;
import com.flights.util.JErrorDialog;

/**
 * Boeing737 object extends Aircraft, contains all Seat objects related to a specific Flight
 */
public class Boeing737 extends Aircraft {

    /**
     * Construct a new Boeing737 object
     * @param flightID the flightID of the Flight
     */
    public Boeing737(int flightID) {
        super(195, 0, 0, "Boeing 737-800");
        generateSeats(flightID);
    }

    /**
     * Retrieve all seats from database and generate the Seat[]
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
                    if (rowNo == 1 && i >= 3) {
                        break;
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
                        setSeat(new String[]{currentSeat, "Economy", String.valueOf(flightID), null}, count);
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
     * Render all seats in the Swing GUI to be later used for Flight Seat selection
     * @param p the Passenger object to later assign the Seat to
     * @param isReturn <code>true</code> if return flight, <code>false otherwise</code>
     * @param price price to render in JTopBar
     * @return JPanel
     */
    @Override
    public JPanel renderSeats(Passenger p, boolean isReturn, double price) {
        return new SelectSeatBoeing737(getAllSeats(), p, isReturn, price);
    }
}
