package com.flights.gui;

import java.awt.*;

import javax.swing.*;

import com.flights.objects.Booking;
import com.flights.util.FlightsConstants;
import com.flights.util.JErrorDialog;

public class MyBookingMenu extends JPanel implements FlightsConstants{
    private final JTextField bookingID = new JTextField(15);
    private final JTextField email = new JTextField(15);

    public MyBookingMenu() {
        setLayout(new BorderLayout());
        JPanel toolBarPanel = new JTopBar();
        JPanel contentPanel = new JPanel();
        
        JButton enter = new JButton("Log in");
        enter.addActionListener(e -> {
            if (Booking.verifyBookingDetails(bookingID.getText(), email.getText())) {
                MainWindow.createAndShowGUI(new ViewBookingMenu(new Booking(bookingID.getText(), email.getText())));
            } else {
                JErrorDialog.showWarning("Invalid booking details!");
            }
        });
        JLabel bookingLabel = new JLabel("Booking ID: ");
        JLabel emailLabel = new JLabel("Email: ");
        add(toolBarPanel, BorderLayout.NORTH);
        contentPanel.add(bookingLabel);
        contentPanel.add(bookingID);
        contentPanel.add(emailLabel);
        contentPanel.add(email);
        contentPanel.add(enter);
        add(contentPanel, BorderLayout.CENTER);

        setPreferredSize(new Dimension(MainWindow.FRAME_WIDTH, MainWindow.FRAME_HEIGHT));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> MainWindow.createAndShowGUI(new MyBookingMenu()));
    }
}
