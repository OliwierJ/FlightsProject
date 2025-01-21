package com.flights.tests;

import com.flights.objects.Aircraft;
import com.flights.objects.Boeing737;

public class SeatTest {
    public static void main(String[] args) {
        Aircraft aircraft = new Boeing737(100);
        System.out.println(aircraft);
        System.out.println(aircraft.getSeat(0).isOccupied());
        System.out.println(aircraft.getSeat(1).isOccupied());
        System.out.println(aircraft.getSeat(2).isOccupied());
    }
}
