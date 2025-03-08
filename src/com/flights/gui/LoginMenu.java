package com.flights.gui;

import com.flights.gui.components.JSubmitButton;
import com.flights.gui.components.JTopBar;
import com.flights.util.FlightsConstants;
import com.flights.util.JErrorDialog;

import javax.swing.*;
import java.awt.*;

public class LoginMenu extends JPanel implements FlightsConstants {
    public LoginMenu() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(MainWindow.FRAME_WIDTH, MainWindow.FRAME_HEIGHT));
        
        JPanel contentPanel = new JPanel();
        JLabel username = new JLabel("Username: ");
        JLabel password = new JLabel("Password: ");
        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JButton enter = new JSubmitButton("Log in");
        enter.addActionListener(e -> {
            if (usernameField.getText().equals("admin") && String.valueOf(passwordField.getPassword()).equals("admin")) {
                MainWindow.createAndShowGUI(new AdminMenu());
            } else {
                JErrorDialog.showWarning("Invalid username and/or password!");
            }
        });
        
        username.setFont(ARIAL20);
        password.setFont(ARIAL20);
        usernameField.setFont(ARIAL20PLAIN);
        passwordField.setFont(ARIAL20PLAIN);

        contentPanel.add(Box.createVerticalStrut(75));
        contentPanel.add(username);
        contentPanel.add(usernameField);
        contentPanel.add(password);
        contentPanel.add(passwordField);
        contentPanel.add(enter);

        add(new JTopBar(), BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
}