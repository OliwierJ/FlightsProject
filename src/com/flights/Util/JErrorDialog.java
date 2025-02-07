package com.flights.util;

import java.time.LocalTime;

import javax.swing.JOptionPane;

import com.flights.gui.MainWindow;

public class JErrorDialog {

    public static void showError(String message, Exception e) {
        JOptionPane.showMessageDialog(MainWindow.frame, message+"\nDetails: "+e.getMessage(), e.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
        LocalTime t = LocalTime.now();
        System.err.print(t.toString().substring(0, t.toString().length()-4) + ": ");
        e.printStackTrace(System.err);
    }

    public static void showError(String message, String title) {
        JOptionPane.showMessageDialog(MainWindow.frame, message, title, JOptionPane.ERROR_MESSAGE);
        LocalTime t = LocalTime.now();
        System.err.println(t.toString().substring(0, t.toString().length()-4) + ": "+message);
    }

    public static void showWarning(String message) {
        JOptionPane.showMessageDialog(MainWindow.frame , message, message, JOptionPane.WARNING_MESSAGE);
    }
}