package com.flights.gui.components;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import com.flights.util.FlightsConstants;

public class JSubmitButton extends JButton implements FlightsConstants{
    public JSubmitButton(String text) {
        setText(text);
        setBorder(new EmptyBorder(5,20,5,20));
        setBackground(MAIZE);
        setFocusable(false);
        setFont(new Font("Arial", Font.PLAIN, 18));
    }
}
