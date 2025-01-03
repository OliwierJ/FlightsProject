package com.flights.tests;

import com.flights.objects.*;

import java.util.Arrays;

public class NEWTEST2025 {
    public static void main(String[] args) {
        // only run 1 method for testing purposes
//        viewBooking();
        newBooking();
    }

    // simulates view booking gui and update booking gui
    private static void viewBooking() {
        // assume credentials were entered in gui
        // the booking should generate, as well as all passenger details in the booking, all seats occupied by passenger and all flight details
        Booking b = new Booking("123456", "johndoe@gmail.com");
        // everything should be accessible through booking
        // updates to booking can now be done and then updateDatabase() on booking must be called for changes to sync to database
        Flight depFlight = b.getDepartureFlight();
        Flight returnFlight = b.getReturnFlight();

        System.out.println(depFlight);
        System.out.println(returnFlight);

        Passenger[] passengers = b.getPassengers();
        for (Passenger p : passengers) {
            System.out.println(p);
            System.out.println(p.getDepartureSeat());
            System.out.println(p.getReturnSeat());
        }

        // probably won't be needed for view booking, might be needed for update booking or elsewhere
        Aircraft depFlightPlane = depFlight.getAircraft();
        Aircraft returnFlightPlane = returnFlight.getAircraft();
        System.out.println(depFlightPlane);
        System.out.println(returnFlightPlane);
        // prints out everything for debug purposes
//        System.out.println(b);
    }

    // simulates book flight gui
    private static void newBooking() {
        // assume this flight was selected for departure flight, another object needed for return
        Flight f = new Flight(100);
        Flight f2 = new Flight(101); // return flight if selected

        // make a new empty booking, bookingID is generated automatically
        // either of these 2 work
//        Booking b = new Booking();
        Booking b = new Booking("google@google.com", 1, 2, f, null); // null if no return flight needed

        Passenger passenger1 = new Passenger("Brandon", "Jaroszczak", b.getBookingID());
        Passenger passenger2 = new Passenger("Random", "Dude", b.getBookingID());
        // seats can only be added using the method below (for now, can be changed if needed), other seat details are added via database calls (class, aircraftID), passenger ID only added when updateDatabase() on booking is called
        //TODO: later during GUI implementation, figure out a different way to do this, VERY error prone, currently if seat doesn't exist nothing happens
        passenger1.setDepartureSeat(new Seat("A1", f.getFlightID(), false));
        passenger2.setDepartureSeat(new Seat("A2", f.getFlightID(), false)); // if u don't add a seat A2 with flightID 100 to database manually u will get error, will be fixed by me later
        // add details to booking, unnecessary if added in constructor
        b.setEmail("google@google.com");
        b.setLuggage(2);
        b.setPriorityBoarding(1);
        b.setDepartureFlight(f);

        // always necessary to add passengers, passenger's can't be added in constructor as they need a booking ID to link to
        b.addPassengers(passenger1, passenger2); // varargs can add as many as you want, null not allowed

        // debug
        System.out.println(b);

        // call to booking update database will also automatically detect and update passengers and seats if required
//        b.updateDatabase();
    }
}
