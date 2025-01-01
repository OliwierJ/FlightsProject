package com.flights.classes;

import com.flights.DBUtil;
import java.sql.Timestamp;
import java.util.Arrays;

public class Driver {


    // Creates Booking Object from DB
    public static Booking arrayToBooking(String[] arr) {
        String bookingID = arr[0];
        String email = arr[1];
//        returns true if the priority is 1, or else its false
        boolean priority = arr[2].equals("1");

        int luggage = Integer.parseInt(arr[3]);

        // initialise flights
        String[][] flights = new String[2][];
        try {
            flights[0] = DBUtil.getFlightInfoFromBookingID(bookingID)[0];
            // try to see if there is a return flight
            try {
                flights[1] = DBUtil.getFlightInfoFromBookingID(bookingID)[1];
            } catch (Exception e) {System.err.println(e.getMessage());}

        } catch (Exception e) {System.err.println(e.getMessage());}

        // set the dep flight and return flight
        Flight depFlight = arrayToFlight(flights[0]);
        Flight returnFlight = null;
        // check to see if flights has another row
        if (flights[1] != null) {
            returnFlight = arrayToFlight(flights[1]);
        }
        // create booking
        Booking b = new Booking(bookingID,email,priority,luggage,depFlight,returnFlight);

        // make passenger array
        String[][] passengers;
        try {
            // get passengers from booking ID
            passengers = DBUtil.getPassengersFromBookingID(arr[0]);
            for (String[] pass : passengers) {
                // add the passenger to the booking after turning it into an object
                b.addPassenger(arraytoPassenger(pass));
            }
        } catch (Exception e) {throw new RuntimeException(e);}

        return b;
    }

    // Creates Passenger object from DB
    public static Passenger arraytoPassenger(String[] arr) {
        int id = Integer.parseInt(arr[0]);
        String firstName = arr[1];
        String lastName = arr[2];

        // get the seats for the passenger
        String[][] seat;
        try {
            // get the seats from the db
            seat = DBUtil.getSeatFromPassengerID(arr[0]);
        } catch (Exception e) {throw new RuntimeException(e);}

        return new Passenger(id,firstName,lastName, seat[0][0], seat[0][1]);
    }

    // Creates Seat object from DB
    public static Seat arrayToSeat(String[] arr) {
        String seatNo = arr[0];
        String seatType = arr[1];
        boolean isOccupied = Boolean.parseBoolean(arr[2]);
        return new Seat(seatNo, seatType, isOccupied);
    }

    // Creates Aircraft Object using DBUtil
    public static Aircraft arrayToAircraft(String[] arr) {
        int id = Integer.parseInt(arr[0]);
        int ecSeats = Integer.parseInt(arr[2]);
        int busSeats = Integer.parseInt(arr[3]);
        int fcSeats = Integer.parseInt(arr[4]);
        // selects the correct subclass dependent on the model saved in the DB
        if(arr[1].equals("Boeing 737-800")) { return new Boeing737(id, ecSeats, busSeats, fcSeats);}
        if(arr[1].equals("Cessna")) {return new Cessna(id, ecSeats, busSeats, fcSeats);}
        if(arr[1].equals("Boeing777")) {return new Boeing777(id, ecSeats, busSeats, fcSeats);}
        return null;
    }

    // Creates Flight object using DBUtil
    public static Flight arrayToFlight(String[] arr) {
        int id = Integer.parseInt(arr[0]);
        String dep = arr[1];
        String arrival = arr[2];
        // Timestamp is used to save the times. IDK why java.sql.Date isn't supported with SQL's own DateTime variables
        Timestamp depTime = Timestamp.valueOf(arr[3]);
        Timestamp arrTime = Timestamp.valueOf(arr[4]);
        Aircraft plane;
        try {
            // create the aircraft object using the aircraft_id saved in the flight object.
            plane = arrayToAircraft(DBUtil.getAircraftInfo(arr[5]));
        } catch (Exception e) {throw new RuntimeException(e);}

        return new Flight(id, dep, arrival, depTime, arrTime, plane);
    }

    public static void main(String[] args) throws Exception {

        String[][] arr = DBUtil.getFlightInfo("Dublin","Barcelona");

/*
        String arr2[] = DBUtil.getAircraftInfo(String.valueOf(100));
        Aircraft plane = arrayToAircraft(arr2);
        System.out.println(Arrays.deepToString(arr));
*/
        Flight flight = arrayToFlight(arr[0]);
        Seat a1 = new Seat("11","Economy",false);
        System.out.println(a1.getSeatNo());
//    This works on my computer in theory, but I had to add some more data into the Seats and Seat/Passenger Database for it to work
        Booking b1 = arrayToBooking(DBUtil.getBookingInfo("123456"));
        System.out.println(b1);
        b1.printPassengers();

//        System.out.println(a1);
//        System.out.println(flight);
        System.out.println(flight.flightDuration());

    }
}
