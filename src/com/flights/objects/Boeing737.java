package com.flights.objects;

import java.sql.SQLException;
import java.util.Arrays;

import javax.swing.*;

import com.flights.util.DBConnectivity;

public class Boeing737 extends Aircraft {

    public Boeing737(int aircraftID ,int economySeats, int businessSeats, int firstClassSeats, int flightID) {
        super(aircraftID, economySeats, businessSeats, firstClassSeats, "Boeing 737", flightID);
        generateSeats();
    }

    @Override
    protected void generateSeats() {
        try {
             String[][] bookedSeats = DBConnectivity.getMultipleRows(DBConnectivity.connectAndExecuteQuery("SELECT seat_no, class, aircraft_id, flight_id, passenger_id FROM seat WHERE flight_id='"+getFlightID()+"' ORDER BY seat_no"));
//            String[][] bookedSeats = {{"1C", "Economy", getAircraftID()+"", null, "10"}, {"2A", "Economy", getAircraftID()+"", null, "20"}, {"30D", "Economy", getAircraftID()+"", null, "30"}, {"30E", "Economy", getAircraftID()+"", null, "40"}};
            int count = 0;
            int bookedCount = 0;
            int rowNo = 1;

            while (count < getSeatCount()) {
                for (int i = 0; i < 6; i++) { // alphabet A-F
                    if (rowNo == 1 && i >= 3) {
                        break;
                    }
                    String currentSeat = rowNo+""+alphabet[i];
                    if (bookedCount < bookedSeats.length && currentSeat.equals(bookedSeats[bookedCount][0])) {
                        System.err.println(Arrays.toString(bookedSeats[bookedCount]));
                        setSeat(bookedSeats[bookedCount], count);
                        
                        System.err.print(currentSeat+",,");
                        bookedCount++;
                    } else {
                        setSeat(new String[]{currentSeat, "Economy", getAircraftID()+"", null, null}, count);
                        System.out.print(currentSeat+",");
                    }
                    count++;
                    if (count >= getSeatCount()) {
                        break;
                    }
                }
                rowNo++;
            }

            System.out.println(Arrays.deepToString(getSeats()));
        // } catch (SQLException e) {
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public JPanel renderSeats() {
        return null;
    }
}
