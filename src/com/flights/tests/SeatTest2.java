package com.flights.tests;

class Seatt {
    private String seatNumber;

    public Seatt(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
}

class Passengerr {
    private Seatt seat;

    public void setSeat(Seatt seat) {
        this.seat = seat;
    }

    public Seatt getSeat() {
        return seat;
    }
}

public class SeatTest2 {
    public static void main(String[] args) {
        Seatt s = new Seatt("A1");
        Passengerr p = new Passengerr();
        
        System.out.println("Original Seat number: " + s.getSeatNumber());  // Output: B2
        // Add Seat s to Passenger p
        p.setSeat(s);

        // Modify the seat number via Passenger
        p.getSeat().setSeatNumber("B2");

        // Output the seat number of both s and p's seat
        System.out.println("Passenger's seat number: " + p.getSeat().getSeatNumber());  // Output: B2
        System.out.println("Original Seat number: " + s.getSeatNumber());  // Output: B2
    }
}