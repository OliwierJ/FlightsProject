package com.flights.gui;

import com.flights.Main;
import com.flights.gui.components.JSubmitButton;
import com.flights.gui.components.JTopBar;
import com.flights.util.FlightsConstants;
import com.flights.util.JErrorDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Login Menu screen used to log in and access the Admin Menu
 */
public class LoginMenu extends JPanel implements FlightsConstants {
    /**
     * Construct a new LoginMenu JPanel
     */
    public LoginMenu() {
        setLayout(new BorderLayout());
        setPreferredSize(Main.getFrameSize());
        
        JPanel contentPanel = new JPanel();
        JLabel username = new JLabel("Username: ");
        JLabel password = new JLabel("Password: ");
        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JButton enter = new JSubmitButton("Log in");
        enter.addActionListener(e -> {
            // very secure username and password, NEVER to be used in prod :)
            if (usernameField.getText().equals("admin") && String.valueOf(passwordField.getPassword()).equals("admin")) {
                Main.createAndShowGUI(new AdminMenu());
            } else {
                JErrorDialog.showWarning("Invalid username and/or password!");
            }
        });

        usernameField.registerKeyboardAction(e -> enter.doClick(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), WHEN_FOCUSED);
        passwordField.registerKeyboardAction(e -> enter.doClick(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), WHEN_FOCUSED);
        
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