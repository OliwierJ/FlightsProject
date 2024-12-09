package com.flights.Logins;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.logging.LoggingPermission;


public class LoginPage extends JPanel implements ActionListener {


    private final JButton loginButton;
    private final JButton resetButton;
    private final JButton signUpButton;
    private final JTextField userIDField;
    private final JPasswordField userPasswordField;
    private final JLabel userIDLabel;
    private final JLabel userPasswordLabel;
    private final JLabel messageLabel;

    public LoginPage() {

        this.setLayout(null);
        setPreferredSize(new Dimension(420,420));


        userIDLabel = new JLabel("User id:");
        userIDLabel.setForeground(Color.WHITE);
        userIDLabel.setBounds(50, 100, 75, 25);

        userPasswordLabel = new JLabel("Password:");
        userPasswordLabel.setForeground(Color.WHITE);
        userPasswordLabel.setBounds(50, 150, 75, 25);

        userIDField = new JTextField();
        userIDField.setBounds(125, 100, 200, 25);

        userPasswordField = new JPasswordField();
        userPasswordField.setBounds(125, 150, 200, 25);

        loginButton = new JButton("Login");
        loginButton.setBounds(50, 200, 100, 25);
        loginButton.setFocusable(false);
        loginButton.addActionListener(this);

        resetButton = new JButton("Reset");
        resetButton.setBounds(200, 200, 100, 25);
        resetButton.setFocusable(false);
        resetButton.addActionListener(this);

        signUpButton = new JButton("Sign up");
        signUpButton.setBounds(50, 250, 100, 25);
        signUpButton.setFocusable(false);
        signUpButton.addActionListener(this);

        messageLabel = new JLabel();
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setBounds(50, 250, 300, 25);

        add(userIDLabel);
        add(userPasswordLabel);
        add(userIDField);
        add(userPasswordField);
        add(loginButton);
        add(resetButton);
        add(signUpButton);
        add(messageLabel);

        setBackground(Color.DARK_GRAY);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // If the user clicked the "Reset" button
        if (e.getSource() == resetButton) {
            // Clear the text fields
            userIDField.setText("");
            userPasswordField.setText("");
            // Clear the message label
            messageLabel.setText("");
        }

        // If the user clicked the "Login" button
        if (e.getSource() == signUpButton) {
            createAndShowGUI(new SignUp());
        }
    }
    static JFrame frame = new JFrame();

    public static void createAndShowGUI(JPanel panel) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panel);
        frame.pack();
        frame.setLayout(null);
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginPage loginPage = new LoginPage();
            createAndShowGUI(loginPage);
        });
    }
}