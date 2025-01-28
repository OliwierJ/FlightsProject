package com.flights.tests;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.time.LocalTime;

import com.flights.util.JErrorDialog;

public class TimeTest {
    public static void main(String[] args) {
        try {
            System.setErr(new PrintStream("src/com/flights/logfile.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        LocalTime ltime = LocalTime.now();
        System.out.println(ltime.toString().substring(0, ltime.toString().length()-4));
        try {
            throw new IllegalArgumentException();
        } catch (Exception e) {
            JErrorDialog.showError("An unknown test error occurred!", e);
        }
        try {
            throw new IllegalArgumentException();
        } catch (Exception e) {
            JErrorDialog.showError("An unknown test error occurred!", e);
        }
    }
}
