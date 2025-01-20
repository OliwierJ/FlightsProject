package com.flights;

import com.flights.gui.MainWindow;

import javax.swing.*;
import static com.flights.gui.MainWindow.createAndShowGUI;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI(new MainWindow()));
    }
}
