package com.flights.tests;

import com.flights.objects.Booking;

public class NEWTEST2025 {
    public static void main(String[] args) {
        // assume you're in view booking gui and entered credentials already
        // the booking should generate, as well as all passenger details in the booking and flight details
        Booking b = new Booking("123456", "johndoe@gmail.com");
        // everything should be accessible through booking

        // prints out everything for debug purposes
        System.out.println(b);
    }
}
