package com.flights.util;

import com.flights.objects.Booking;
import com.flights.objects.Flight;
import com.flights.objects.Passenger;
import com.flights.objects.Seat;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public final class Generator extends DBConnectivity {
    private final Random r = new Random();
    private static final String[] TIERS = new String[] {"Basic", "Standard", "Premium+"};
    private static final String[] DESTINATIONS = new String[] {"Dublin", "Barcelona", "Warsaw", "London", "Paris", "Berlin", "Rome"};
    private static final String[] EMAILS = new String[] {"gmail.com", "yahoo.com", "hotmail.com", "msn.com", "outlook.com", "icloud.com", "setu.ie"};
    private static final String[] TITLES = new String[] {null, "Mr", "Mrs", "Ms"};
    private static final String[] NAMES = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Generator.class.getResourceAsStream("/com/flights/util/names.txt")))).lines().toArray(String[]::new);

    private void generateFlights(String departureAirport, String arrivalAirport, LocalDate firstDay, LocalDate lastDay, String aircraft, int basePrice, int daysBetweenFlights, int flightDuration) {
        if (!new HashSet<>(Arrays.asList(DESTINATIONS)).containsAll(Arrays.asList(departureAirport, arrivalAirport))) {
            System.out.println("Airports not allowed!");
            return;
        }
        try {
            PreparedStatement pstat = getPreparedStatement("INSERT INTO flight (departure_airport, arrival_airport, departure_time, arrival_time, aircraft, base_price) VALUES('"+departureAirport+"', '"+arrivalAirport+"', ?, ?, '"+aircraft+"', "+basePrice+")");
            LocalDate currentDay = firstDay;
            while (currentDay.isBefore(lastDay)) {
                int time = r.nextInt(7, 20); // get random hour of flight between 7am and 8pm
                LocalTime l = LocalTime.of(time, 0);
                pstat.setTimestamp(1, Timestamp.valueOf(LocalDateTime.of(currentDay, l)));
                pstat.setTimestamp(2, Timestamp.valueOf(LocalDateTime.of(currentDay, l.plusMinutes(flightDuration))));
                pstat.addBatch();
                currentDay = currentDay.plusDays(daysBetweenFlights);
            }
            pstat.executeBatch();
            System.out.println("Generated flights between " + departureAirport + " and " + arrivalAirport + ".");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }

    }

    private void generateBookings(int numberOfBookings) throws SQLException {
        String[][] flights = getMultipleRows(connectAndExecuteQuery("SELECT flight_id FROM flight"));
    
        for (int i = 0; i < numberOfBookings; i++) {
            Booking b = new Booking(TIERS[r.nextInt(TIERS.length)]);
            b.setPriorityBoarding(r.nextInt(2));
            b.set20kgluggage(r.nextBoolean());

            boolean isReturn = r.nextBoolean();
            Flight depFlight;
            Flight returnFlight = null;
            if (!isReturn) {
                depFlight = new Flight(Integer.parseInt(flights[r.nextInt(flights.length)][0]));
            } else {
                boolean flightFound = false;
                do {
                    depFlight = new Flight(Integer.parseInt(flights[r.nextInt(flights.length)][0]));
                    String[][] returnFlights = getMultipleRows(connectAndExecuteQuery("SELECT flight_id FROM flight WHERE departure_airport='"+depFlight.getArrivalAirport()+"' AND arrival_airport='"+depFlight.getDepartureAirport()+"' AND departure_time>'"+depFlight.getArrivalLocalDate()+"'"));
                    if (returnFlights.length>0) {
                        returnFlight = new Flight(Integer.parseInt(returnFlights[r.nextInt(returnFlights.length)][0]));
                        b.setReturnFlight(returnFlight);
                        flightFound = true;
                    } 
                } while (!flightFound);
            }
            b.setDepartureFlight(depFlight);

            for (int j = 0; j < r.nextInt(1,7); j++) {
                String name = NAMES[r.nextInt(NAMES.length)];
                int spacePos = name.indexOf(' ');
                if (j == 0) {
                    b.setEmail((name.substring(0, spacePos)+name.substring(spacePos+1)+"@"+EMAILS[r.nextInt(EMAILS.length)]).toLowerCase());
                }
                Passenger p = new Passenger(TITLES[r.nextInt(TITLES.length)], name.substring(0, spacePos), name.substring(spacePos+1), b.getBookingID());
                boolean seatSelected = false;
                do {
                    Seat s = depFlight.getSeat(r.nextInt(depFlight.getAllSeats().length));
                    if (!s.isOccupied()) {
                        p.setDepartureSeat(s);
                        seatSelected = true;
                    }
                } while (!seatSelected);
                if (isReturn) {
                    seatSelected = false;
                    do {
                        Seat s = returnFlight.getSeat(r.nextInt(returnFlight.getAllSeats().length));
                        if (!s.isOccupied()) {
                            p.setReturnSeat(s);
                            seatSelected = true;
                        }
                    } while (!seatSelected);
                }
                b.addPassengers(p);
            }
            b.updateDatabase();
            System.out.println("Booking #"+i+" added, booking NO: "+b.getBookingID());
        }
    }

    public static void generate() {
        Generator g = new Generator();

        LocalDate start = LocalDate.of(2025,4,1);
        LocalDate end = start.plusDays(15);
        LocalDate startReturn = start.plusDays(1);
        LocalDate endReturn = end.plusDays(1);

        g.generateFlights("Dublin", "Barcelona", start, end, "Boeing 737-800", 70, 4, 145);
        g.generateFlights("Barcelona", "Dublin", startReturn, endReturn, "Boeing 737-800", 70, 4, 165);

        g.generateFlights("Dublin", "Warsaw", start, end, "Boeing 737-800", 65, 7, 165);
        g.generateFlights("Warsaw", "Dublin", startReturn, endReturn, "Boeing 737-800", 65, 7, 185);

        g.generateFlights("Dublin", "London", start, end, "Boeing 737-800", 30, 2, 90);
        g.generateFlights("London", "Dublin", startReturn, endReturn, "Boeing 737-800", 30, 2, 80);

        g.generateFlights("Dublin", "Paris", start, end, "Boeing 737-800", 30, 2, 90);
        g.generateFlights("Paris", "Dublin", startReturn, endReturn, "Boeing 737-800", 30, 2, 100);

        g.generateFlights("London", "Paris", start, end, "Boeing 737-800", 30, 2, 80);
        g.generateFlights("Paris", "London", startReturn, endReturn, "Boeing 737-800", 30, 2, 70);

        g.generateFlights("London", "Rome", start, end, "Boeing 737-800", 50, 4, 145);
        g.generateFlights("Rome", "London", startReturn, endReturn, "Boeing 737-800", 50, 4, 160);

        g.generateFlights("Paris", "Barcelona", start, end, "Boeing 737-800", 55, 5, 105);
        g.generateFlights("Barcelona", "Paris", startReturn, endReturn, "Boeing 737-800", 55, 5, 120);

        g.generateFlights("Barcelona", "Rome", start, end, "Boeing 737-800", 60, 5, 110);
        g.generateFlights("Rome", "Barcelona", startReturn, endReturn, "Boeing 737-800", 60, 5, 110);

        g.generateFlights("Paris", "Berlin", start, end, "Airbus A320", 25, 4, 110);
        g.generateFlights("Berlin", "Paris", startReturn, endReturn, "Airbus A320", 25, 4, 115);

        g.generateFlights("Rome", "Warsaw", start, end, "Airbus A320", 50, 7, 140);
        g.generateFlights("Warsaw", "Rome", startReturn, endReturn, "Airbus A320", 50, 7, 135);

        g.generateFlights("Rome", "Berlin", start, end, "Airbus A320", 40, 4, 130);
        g.generateFlights("Berlin", "Rome", startReturn, endReturn, "Airbus A320", 40, 4, 130);

        g.generateFlights("Barcelona", "Berlin", start, end, "Airbus A320", 70, 5, 165);
        g.generateFlights("Berlin", "Barcelona", startReturn, endReturn, "Airbus A320", 70, 5, 155);

        try {
            g.generateBookings(1000);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        } finally {
            closeConnection();
        }
    }

    @Override
    protected void updateDatabase() {
        throw new UnsupportedOperationException("Operation not supported.");
    }
}
