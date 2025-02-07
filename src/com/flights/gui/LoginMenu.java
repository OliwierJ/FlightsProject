package com.flights.gui;

import com.flights.util.JErrorDialog;

import javax.swing.*;
import java.awt.*;

public class LoginMenu extends JPanel {
    public LoginMenu() {
        setLayout(new BorderLayout());
        JPanel toolBarPanel = new JTopBar();
        JPanel contentPanel = new JPanel();

        JLabel username = new JLabel("Username: ");
        JLabel password = new JLabel("Password: ");
        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);

        JButton enter = new JButton("Log in");
        enter.addActionListener(e -> {
            if (usernameField.getText().equals("admin") && String.valueOf(passwordField.getPassword()).equals("admin")) {
                System.out.println("Logged in");
                MainWindow.createAndShowGUI(new AdminMenu());
            } else {
                JErrorDialog.showWarning("Invalid username and/or password!");
            }
        });
        contentPanel.add(username);
        contentPanel.add(usernameField);
        contentPanel.add(password);
        contentPanel.add(passwordField);
        contentPanel.add(enter);

        add(toolBarPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        setPreferredSize(new Dimension(MainWindow.FRAME_WIDTH, MainWindow.FRAME_HEIGHT));
    }
}
