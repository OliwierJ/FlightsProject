package com.flights;

import java.util.Arrays;

public class SQLTest {
    public static void main(String[] args) throws Exception{
//        System.out.println(Arrays.deepToString(DBUtil.getTable("flight")));
//        System.out.println(Arrays.deepToString(DBUtil.getFlightInfo("Dublin", "Barcelona")));
//        System.out.println(Arrays.toString(DBUtil.getAircraftInfo("100")));

        //all possible booking details (excludes IDs)
//        if(DBUtil.verifyBookingDetails("123456", "johndoe@gmail.com")) {
//            System.out.println(Arrays.toString(DBUtil.getBookingInfo("123456")));
//            System.out.println(Arrays.deepToString(DBUtil.getPassengerInfoFromBookingID("123456")));
//            System.out.println(Arrays.deepToString(DBUtil.getFlightInfoFromBookingID("123456")));
//        } else {
//            System.out.println("Incorrect booking details!");
//        }


        // This should add all details for a booking with 1 passenger return flight to database
//        DBUtil.addBooking("696969", "google@google.ie", 1, 3, 100, 101);
//        DBUtil.addPassengerWithSeats("696969", "Brandon", "Jaroszczak", "A1", "A2");

        // This should retrieve all relevant booking details for a single booking
        if (DBUtil.verifyBookingDetails("696969", "google@google.ie")) {
            System.out.println(Arrays.toString(DBUtil.getBookingInfo("696969")));
            System.out.println(Arrays.deepToString(DBUtil.getFlightInfoFromBookingID("696969")));
            System.out.println(Arrays.deepToString(DBUtil.getPassengerInfoFromBookingID("696969")));
        }

        System.out.println(Arrays.deepToString(DBUtil.getSeatFromPassengerID("1")));;
    }
}
