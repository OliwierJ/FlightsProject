package com.flights.gui;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.*;

import com.flights.objects.Booking;
import com.flights.util.FlightsConstants;

public class LoginMenu extends JPanel implements FlightsConstants{
    private JLabel bookingLabel = new JLabel("Booking ID: ");
    private JLabel emailLabel = new JLabel("Email: ");
//    private JTextField bookingID = new JTextField("123456",15);
//    private JTextField email = new JTextField("johndoe@gmail.com",15);
    private JTextField bookingID = new JTextField("522558",15);
    private JTextField email = new JTextField("govie@setu.ie",15);
    private JButton enter = new JButton("Log in");

    public LoginMenu() {
        
        JPanel toolBarPanel = new JPanel();
        toolBarPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        toolBarPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        toolBarPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        toolBarPanel.setBackground(TRUEBLUE);
        toolBarPanel.setMinimumSize(new Dimension(MainWindow.FRAME_WIDTH, 40));
        toolBarPanel.setPreferredSize(new Dimension(MainWindow.FRAME_WIDTH, 40));
        toolBarPanel.setBorder(BorderFactory.createMatteBorder(0,3,3,0,MAIZE));

        enter.addActionListener(e -> {
            if (Booking.verifyBookingDetails(bookingID.getText(), email.getText())) {
                Booking b = new Booking(bookingID.getText(), email.getText());
            }
        });
        add(bookingLabel);
        add(bookingID);
        add(emailLabel);
        add(email);
        add(enter);

        setPreferredSize(new Dimension(MainWindow.FRAME_WIDTH, MainWindow.FRAME_HEIGHT));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> MainWindow.createAndShowGUI(new LoginMenu()));
    }
}
