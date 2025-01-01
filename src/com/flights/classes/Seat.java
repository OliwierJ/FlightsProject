package com.flights.classes;

public class Seat {

    private final String seatNo;
    private final String seatClass;
    private final boolean isOccupied;


    public Seat(String seatNo, String seatClass, boolean isOccupied) {
        this.seatNo = seatNo;
        this.seatClass = seatClass;
        this.isOccupied = isOccupied;
    }

    public String getSeatNo(){
        return seatNo;
    }

    public boolean isOccupied(){
        return isOccupied;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "seatNo='" + seatNo + '\'' +
                ", seatClass='" + seatClass + '\'' +
                ", isOccupied=" + isOccupied +
                '}';
    }
}
