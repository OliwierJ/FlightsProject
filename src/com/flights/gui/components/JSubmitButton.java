package com.flights.gui.components;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import com.flights.util.FlightsConstants;

public class JSubmitButton extends JButton implements FlightsConstants {
    public JSubmitButton(String text) {
        setText(text);
        setBorder(new EmptyBorder(5,20,5,20));
        setBackground(MAIZE);
        setFocusable(false);
        setFont(new Font("Arial", Font.PLAIN, 18));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(getBackground().darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(MAIZE);
            }
        });
    }
}