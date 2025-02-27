package com.flights.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.*;

import com.flights.gui.components.JTopBar;
import com.flights.objects.Booking;
import com.flights.util.FlightsConstants;

public class ExtrasMenu extends JPanel implements FlightsConstants { // TODO implement whatever is remaining for booking
    public ExtrasMenu(Booking b) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(MainWindow.FRAME_WIDTH, MainWindow.FRAME_HEIGHT));
        add(new JTopBar(), BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.add(new JLabel("Passenger seats assigned successfully!"));
        add(mainPanel, BorderLayout.CENTER);
    }
}
