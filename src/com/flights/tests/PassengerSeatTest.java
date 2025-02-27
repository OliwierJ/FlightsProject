package com.flights.tests;

import com.flights.objects.Booking;
import com.flights.objects.Flight;
import com.flights.objects.Passenger;

public class PassengerSeatTest {
    public static void main(String[] args) {
        Booking b = new Booking("Basic");
        Flight f = new Flight(100);
        Passenger p = new Passenger("Mr", "Random", "Dude", b.getBookingID());
        System.out.println(p);
        System.out.println(f.getSeat(150));
        System.out.println(f.getSeat(151));
        System.out.println(f.getSeat(152));

        p.setDepartureSeat(f.getSeat(151));
        System.out.println(f.getSeat(150));
        System.out.println(f.getSeat(151));
        System.out.println(f.getSeat(152));

        p.setDepartureSeat(f.getSeat(152));
        System.out.println(f.getSeat(150));
        System.out.println(f.getSeat(151));
        System.out.println(f.getSeat(152));

        p.setDepartureSeat(f.getSeat(150));
        System.out.println(f.getSeat(150));
        System.out.println(f.getSeat(151));
        System.out.println(f.getSeat(152));

        p.setDepartureSeat(f.getSeat(188));
        System.out.println(p);

    }
}
