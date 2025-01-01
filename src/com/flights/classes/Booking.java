package com.flights.classes;

public class Booking {
    private final String bookingID;
    private final String email;
    private final boolean priorityBoarding;
    private final int luggageAmount;
    private final Flight flight;
    private final Flight returnFlight;

    // array of all the passengers of the booking
    private final Passenger[] passengers = new Passenger[6];
    private int numOfPassengers = 0;



    public Booking(String bookingID, String email, boolean priorityBoarding, int luggageAmount, Flight flight, Flight returnFlight) {
        this.bookingID = bookingID;
        this.email = email;
        this.priorityBoarding = priorityBoarding;
        this.luggageAmount = luggageAmount;
        this.flight = flight;
        this.returnFlight = returnFlight;

    }

    public void addPassenger(Passenger passenger) {
        passengers[numOfPassengers] = passenger;
        numOfPassengers++;
    }
    public void printPassengers() {
        for (Passenger passenger : passengers) {
            if (passenger != null) {System.out.println(passenger);}
        }
    }
    @Override
    public String toString() {
        String str = "Booking{" +
                "bookingID='" + bookingID + '\'' +
                ", email='" + email + '\'' +
                ", priorityBoarding=" + priorityBoarding +
                ", luggageAmount=" + luggageAmount +
                ", flight=" + flight.getFlightNo() +
                '}';
        if (returnFlight != null) {
            str += ", returnFlight=" + returnFlight.getFlightNo();
        }
        return str;
    }
}
