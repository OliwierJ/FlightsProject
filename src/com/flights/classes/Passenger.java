package com.flights.classes;

public class Passenger {
    private final int passengerID;
    private final String firstName;
    private final String lastName;
    private final String seatNum;
    private final String seatClass;
    private String returnSeatNum;
    private String returnSeatClass;

    public Passenger(int passengerID, String firstName, String lastName, String seatNum, String seatClass) {
        this.passengerID = passengerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.seatNum = seatNum;
        this.seatClass = seatClass;
//        this.returnSeatNum = returnSeatNum;
//        this.returnSeatClass = returnSeatClass;

    }

    @Override
    public String toString() {
        return "Passenger{" +
                "passengerID=" + passengerID +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", seatNum='" + seatNum + '\'' +
                ", seatClass='" + seatClass + '\'' +
                '}';
    }
}
