package com.flights.gui;

import java.awt.*;

import javax.swing.*;

import com.flights.gui.components.JSubmitButton;
import com.flights.gui.components.JTopBar;
import com.flights.objects.Booking;
import com.flights.util.FlightsConstants;
import com.flights.util.JErrorDialog;

public class MyBookingMenu extends JPanel implements FlightsConstants{
    public MyBookingMenu() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(MainWindow.FRAME_WIDTH, MainWindow.FRAME_HEIGHT));

        JPanel contentPanel = new JPanel();
        JTextField bookingID = new JTextField(15);
        JTextField email = new JTextField(15);
        JLabel bookingLabel = new JLabel("Booking ID: ");
        JLabel emailLabel = new JLabel("Email: ");
        JButton enter = new JSubmitButton("Log in");
        enter.addActionListener(e -> {
            if (Booking.verifyBookingDetails(bookingID.getText(), email.getText())) {
                MainWindow.createAndShowGUI(new ViewBookingMenu(new Booking(bookingID.getText(), email.getText())));
            } else {
                JErrorDialog.showWarning("Invalid booking details!");
            }
        });
        
        bookingLabel.setFont(ARIAL20);
        emailLabel.setFont(ARIAL20);
        bookingID.setFont(ARIAL20PLAIN);
        email.setFont(ARIAL20PLAIN);

        contentPanel.add(Box.createVerticalStrut(75));
        contentPanel.add(bookingLabel);
        contentPanel.add(bookingID);
        contentPanel.add(emailLabel);
        contentPanel.add(email);
        contentPanel.add(enter);
        add(new JTopBar(), BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
}