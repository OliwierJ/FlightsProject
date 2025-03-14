package com.flights.tests;

import com.flights.objects.*;

import java.util.Arrays;

import javax.swing.*;

public class NEWTEST2025 {
    public static void main(String[] args) {
        // only run 1 method for testing purposes
//       viewBooking();
        newBooking();
        // updateBooking();
        // deleteBooking();
    }

    // simulates view booking gui and update booking gui
    private static void viewBooking() {
        System.out.print("Enter booking ID: ");
        String bookingID = EasyIn.getString();
        System.out.print("Enter email: ");
        String email = EasyIn.getString();
        boolean valid = false;
        try {
            valid = Booking.verifyBookingDetails(bookingID, email);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (valid) {
            Booking b = new Booking(bookingID, email);
            System.out.println("Priority boarding: "+b.getPriorityBoarding());
            System.out.println("Luggage: "+b.getLuggage());
            System.out.println("Passengers: "+Arrays.toString(b.getPassengers()));
            System.out.println("Departure flight: "+b.getDepartureFlight().getDepartureAirport()+", "+b.getDepartureFlight().getArrivalAirport());
            System.out.println("Return flight: "+b.getReturnFlight().getDepartureAirport()+", "+b.getReturnFlight().getArrivalAirport());
            System.out.println("Debug: "+b);
        } else {
            System.out.println("Invalid booking ID and email!");
        }
    }

    // simulates book flight gui
    private static void newBooking() {
        Flight f = new Flight(100);
        Flight f2 = new Flight(101);

        Booking b = new Booking("Basic");
        System.out.println("Generated booking ID: " + b.getBookingID());
        System.out.print("Enter email: ");
        String email = EasyIn.getString();
        System.out.println("20kg luggage hardcoded to true");
        System.out.print("Enter priority boarding: ");
        int priority = EasyIn.getInt();
        b.setDepartureFlight(f);
        b.setReturnFlight(f2);
        b.setEmail(email);
        b.set20kgluggage(true);
        b.setPriorityBoarding(priority);

        System.out.print("Enter number of passengers: ");
        int count = EasyIn.getInt();
        Passenger[] passengers = new Passenger[count];
        for (int i = 0; i < count; i++) {
            System.out.print("Enter passenger title: ");
            String title = EasyIn.getString();
            System.out.print("Enter passenger name: ");
            String name = EasyIn.getString();
            System.out.print("Enter passenger surname: ");
            String surname = EasyIn.getString();
            System.out.print("Enter passenger departure seat index (0-188): ");
            int departure = EasyIn.getInt();

            System.out.println(f.getSeat(departure));
            // if seat is occupied finds the next available seat
            while (f.getSeat(departure).isOccupied()) {
                System.out.println(departure + " seat occupied! Choosing next available seat");
                departure++;
            }

            System.out.print("Enter passenger arrival seat (0-188, -1 to set none, WILL BREAK IF -1): ");
            int arrival = EasyIn.getInt();
            System.out.println(f2.getSeat(arrival));
            while (arrival != -1 && f2.getSeat(arrival).isOccupied()) {
                System.out.println(arrival + " seat occupied! Choosing next available seat");
                arrival++;
            }

            passengers[i] = new Passenger(title, name, surname, b.getBookingID());
            passengers[i].setDepartureSeat(f.getSeat(departure));

            if (arrival != -1) {

                passengers[i].setReturnSeat(f2.getSeat(arrival));
            }
        }
        b.addPassengers(passengers);
        b.updateDatabase();
    }

    private static void updateBooking() {
        Booking b = new Booking("522558", "govie@setu.ie");
        Passenger[] passengers = b.getPassengers();
        passengers[0].setTitle("Ms");
        passengers[0].setName("Norma");
        passengers[0].setSurname("Foley");
        passengers[0].setDepartureSeat(b.getDepartureFlight().getSeat(150));
        passengers[0].setReturnSeat(b.getReturnFlight().getSeat(150));

        // these should throw errors
//        b.setDepartureFlight(null);
//        b.setReturnFlight(null);

        b.setEmail("govie@setu.ie");
        b.setPriorityBoarding(1);
        b.set20kgluggage(true);
        b.setTier("Standard");

        b.updateDatabase();
    }

    public static JFrame f = new JFrame();
    private static final JButton button = new JButton("Click me to delete entry");

    private static void deleteBooking() {
        
        // Booking b = new Booking(1);
        Booking b = new Booking("522558", "govie@setu.ie");

        button.setBounds(50,50,300,50);

        button.addActionListener(e -> b.deleteEntry());
        
        f.add(button);
        f.setLayout(null);
        f.setSize(1300,500);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
