package com.flights.objects;

import java.util.Arrays;

import javax.swing.*;

/**
 * Abstract aircraft object, extend this object to create a new aircraft type
 */
@SuppressWarnings("unused")
public abstract class Aircraft {
    private final int economySeats;
    private final int businessSeats;
    private final int firstClassSeats;
    private final int seatAmount;
    private final String name;
    private final Seat[] seats;

    /**
     * Construct a new aircraft object
     * @param economySeats number of economy seats
     * @param businessSeats number of business seats
     * @param firstClassSeats number of first class seats
     * @param name aircraft name
     */
    public Aircraft(int economySeats, int businessSeats, int firstClassSeats, String name) {
        this.economySeats = economySeats;
        this.businessSeats = businessSeats;
        this.firstClassSeats = firstClassSeats;
        this.seatAmount = economySeats+businessSeats+firstClassSeats;
        this.name = name;
        this.seats = new Seat[seatAmount];
    }

    /**
     * Get aircraft total seat count
     * @return total seat count
     */
    public int getSeatCount() {
        return seatAmount;
    }

    /**
     * Get aircraft name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Get aircraft economy seat count
     * @return economy seat count
     */
    public int getEconomySeats() {
        return economySeats;
    }

    /**
     * Get aircraft business seat count
     * @return business seat count
     */
    public int getBusinessSeats() {
        return businessSeats;
    }

    /**
     * Get aircraft first class seat count
     * @return first class seat count
     */
    public int getFirstClassSeats() {
        return firstClassSeats;
    }

    /**
     * Get all aircraft seats
     * @return Seat[] all aircraft seat
     */
    public Seat[] getAllSeats() {
        return seats;
    }

    /**
     * Get a specific aircraft seat
     * @param i index of seat
     * @return Seat object
     */
    public Seat getSeat(int i) {
        return seats[i];
    }

    /**
     * Sets/updates an aircraft seat
     * @param s String[] seat data
     * @param i index of Seat
     */
    protected void setSeat(String[] s, int i) {
        this.seats[i] = new Seat(s[0], s[1], s[2], s[3]);
    }

    /**
     * Print all seats to console for debug purposes
     */
    public void printSeats() {
        System.out.println(Arrays.toString(seats));
    }

    /**
     * Retrieve all seats from database and generate the Seat[]
     * @param flightID flightID of the flight
     */
    protected abstract void generateSeats(int flightID);

    /**
     * Render all seats of this aircraft in the Swing GUI to be later used for Flight Seat selection
     * @param p the Passenger object to later assign the Seat to
     * @param isReturn <code>true</code> if return flight, <code>false otherwise</code>
     * @param price price to render in JTopBar
     * @return JPanel
     */
    public abstract JPanel renderSeats(Passenger p, boolean isReturn, double price);

    /**
     * Get all aircraft details excluding Seat[] for debug purposes (use printSeats() to get Seat[] details)
     * @return String containing info
     */
    @Override
    public String toString() {
        return "Aircraft{" +
                "economySeats=" + economySeats +
                ", businessSeats=" + businessSeats +
                ", firstClassSeats=" + firstClassSeats +
                ", seatAmount=" + seatAmount +
                ", name='" + name + '\'' +
                '}';
    }
}
