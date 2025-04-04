package com.flights.gui;

import java.awt.*;
import java.awt.event.KeyEvent;

import javax.swing.*;

import com.flights.gui.components.JSubmitButton;
import com.flights.gui.components.JTopBar;
import com.flights.objects.Booking;
import com.flights.util.FlightsConstants;
import com.flights.util.JErrorDialog;

import static com.flights.gui.MainWindow.frame;

public class MyBookingMenu extends JPanel implements FlightsConstants{
    public MyBookingMenu() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()));


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

        bookingID.registerKeyboardAction(e -> enter.doClick(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), WHEN_FOCUSED);
        email.registerKeyboardAction(e -> enter.doClick(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), WHEN_FOCUSED);
        
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