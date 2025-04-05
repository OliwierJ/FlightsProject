package com.flights.util;

import com.flights.Main;

import java.time.LocalTime;

import javax.swing.JOptionPane;

/**
 * Helper class to display JOptionPane messages and automatically print exceptions to <code>System.err</code> with timestamps
 */
public class JErrorDialog {
    /**
     * Display a JOptionPane message and print exception to <code>System.err</code>
     * @param message String error message
     * @param e Exception
     */
    public static void showError(String message, Exception e) {
        JOptionPane.showMessageDialog(Main.frame, message+"\nDetails: "+e.getMessage(), e.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
        LocalTime t = LocalTime.now();
        System.err.print(t.toString().substring(0, t.toString().length()-4) + ": ");
        e.printStackTrace(System.err);
    }

    /**
     * Display a JOptionPane message and print to <code>System.err</code>
     * @param message String error message
     * @param title String dialog title
     */
    public static void showError(String message, String title) {
        JOptionPane.showMessageDialog(Main.frame, message, title, JOptionPane.ERROR_MESSAGE);
        LocalTime t = LocalTime.now();
        System.err.println(t.toString().substring(0, t.toString().length()-4) + ": "+message);
    }

    /**
     * Display a JOptionPane warning message
     * @param message String warning message
     */
    public static void showWarning(String message) {
        JOptionPane.showMessageDialog(Main.frame, message, message, JOptionPane.WARNING_MESSAGE);
    }
}