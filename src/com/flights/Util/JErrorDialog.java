package com.flights.util;

import javax.swing.JOptionPane;

public class JErrorDialog {
    public static void showError(String message, Exception e) {
        JOptionPane.showMessageDialog(null, message+"\nDetails: "+e.getMessage(), e.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
    }
}