package com.flights.objects;

import java.sql.SQLException;

import javax.swing.*;

import com.flights.util.DBConnectivity;
import com.flights.util.JErrorDialog;

public class Boeing737 extends Aircraft {

    public Boeing737(int flightID) {
        super(189, 0, 0, "Boeing 737-800");
        generateSeats(flightID);
    }

    @Override
    protected void generateSeats(int flightID) {
        try {
             String[][] bookedSeats = DBConnectivity.getMultipleRows(DBConnectivity.connectAndExecuteQuery("SELECT seat_no, class, flight_id, passenger_id FROM seat WHERE flight_id='"+flightID+"' ORDER BY seat_no"));
             //TODO: delete later for testing in college PCs
//           String[][] bookedSeats = {
//                {"1C", "Economy", "100", "10"},
//                {"2A", "Economy", "100", "20"},
//                {"30D", "Economy", "100", "30"},
//                {"30E", "Economy", "100", "40"}};
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

    @Override
    public JPanel renderSeats() {
        return null;
    }
}
