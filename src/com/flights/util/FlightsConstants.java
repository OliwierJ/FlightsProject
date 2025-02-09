package com.flights.util;

import java.awt.*;

import javax.swing.JLabel;
import javax.swing.JTextField;

public interface FlightsConstants {

    Color DARKSPRINGGREEN = new Color(0x28753C);
    Color SEAGREEN = new Color(0x2c8142);
    Color ASPARAGUS = new Color(0x5e9945);
    Color APPLEGREEN = new Color(0x90b148);
    Color MAIZE = new Color(0xf4e04d);
    Color TRUEBLUE = new Color(0x5171a5);
    Color RICHBLACK = new Color(0x00171f);
    Color SELECTEDGRAY = new Color(0xCEC7C7);
    Color LIGHTGRAY = new Color(0xFFDADADA, true);
    Color OFFWHITE = new Color(0xFFFBFB);

    String DEPARTURE_PLACEHOLDER = "Enter departure airport";
    String ARRIVAL_PLACEHOLDER = "Enter arrival airport";

    Font ARIAL20 = new Font("Arial", Font.BOLD, 20);
    Font DEFAULT_FONT = new JLabel().getFont();
    Font DEFAULT_FONT_TEXTFIELD = new JTextField().getFont();
}
