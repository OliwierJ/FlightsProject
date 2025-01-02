package com.flights.tests.Logins;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


// start class
public class SignUp extends JPanel implements ActionListener {

    // instance variables
    private final JButton signUpButton;
    private final JPasswordField userPasswordField;
    private final JLabel messageLabel;
    private final JTextField userIDField;
    private final JTextField emailField;
    private final JLabel emailMessageLabel;
    private final JPasswordField passwordConfirmField;
    private boolean validEmail = false;
    private boolean validPasswords = false;


    public SignUp() {

        this.setLayout(null);
        setPreferredSize(new Dimension(420,420));


        JLabel userIDLabel = new JLabel("Username:");
        userIDLabel.setForeground(Color.WHITE);
        userIDLabel.setBounds(50, 50, 75, 25);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setBounds(50, 100, 75, 25);

        JLabel userPasswordLabel = new JLabel("Password:");
        userPasswordLabel.setForeground(Color.WHITE);
        userPasswordLabel.setBounds(50, 150, 75, 25);

        JLabel passwordConfirmLabel = new JLabel("<html>Confirm Password:<html>");
        passwordConfirmLabel.setForeground(Color.WHITE);
        passwordConfirmLabel.setBounds(50, 200, 75, 25);

        userIDField = new JTextField();
        userIDField.setBounds(125, 50, 200, 25);

        emailField = new JTextField();
        emailField.setBounds(125, 100, 200, 25);
        emailField.addFocusListener(new EmailRegex());

        userPasswordField = new JPasswordField();
        userPasswordField.setBounds(125, 150, 200, 25);

        passwordConfirmField = new JPasswordField();
        passwordConfirmField.setBounds(125, 200, 200, 25);
        passwordConfirmField.addFocusListener(new myFocusListener());

        signUpButton = new JButton("Sign up");
        signUpButton.setBounds(50, 250, 100, 25);
        signUpButton.addActionListener(this);

        messageLabel = new JLabel();
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setBounds(50, 275, 300, 25);

        emailMessageLabel = new JLabel();
        emailMessageLabel.setForeground(Color.WHITE);
        emailMessageLabel.setBounds(155, 125, 300, 25);
        
        add(userIDLabel);
        add(emailLabel);
        add(userPasswordLabel);
        add(userIDField);
        add(userPasswordField);
        add(passwordConfirmLabel);
        add(passwordConfirmField);
        add(signUpButton);
        add(messageLabel);
        add(emailField);
        add(emailMessageLabel);

        setBackground(Color.DARK_GRAY);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

//         If the user clicked the "Login" button
        if (e.getSource() == signUpButton && validEmail && validPasswords) {

            String userID = userIDField.getText();
            String email = emailField.getText();
            String password = Arrays.toString(userPasswordField.getPassword());

            System.out.println(userID);
            System.out.println(email);
            System.out.println(password);
        }
    }

    // check if email matches an email regex
    class EmailRegex implements FocusListener {
        @Override
        public void focusGained(FocusEvent e) {
//            Dont do anything
        }

        @Override
        public void focusLost(FocusEvent e) {
//            regex pattern for email address
            Pattern pattern = Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
            Matcher matcher = pattern.matcher(emailField.getText());
            if (matcher.matches()) {
                validEmail = true;
                emailField.setBorder(null);
                emailMessageLabel.setText("");
            } else {
                validEmail = false;
                emailMessageLabel.setText("Invalid Email Address");
                emailField.setBorder(BorderFactory.createLineBorder(Color.RED,2));

            }
            
        }

    }
    class myFocusListener implements FocusListener {
        @Override
        public void focusGained(FocusEvent e) {}

        @Override
        public void focusLost(FocusEvent e) {
            char[] pass = userPasswordField.getPassword();
            char[] confirmPass = passwordConfirmField.getPassword();

            if (Arrays.equals(pass, confirmPass)) {
                messageLabel.setText("");
                messageLabel.setForeground(Color.WHITE);
                validPasswords = true;
                passwordConfirmField.setBorder(null);
            } else {
                messageLabel.setText("Passwords do not match");
                messageLabel.setForeground(Color.RED);
                validPasswords = false;
                passwordConfirmField.setBorder(BorderFactory.createLineBorder(Color.RED,2));

            }
        }
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SignUp signUp = new SignUp();
        frame.setContentPane(signUp);
        frame.pack();
        frame.setVisible(true);
    }
}