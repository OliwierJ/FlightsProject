package com.flights.tests;

import javax.swing.*;
import java.awt.*;

public class RoundButton extends JButton {

    private boolean plus;

    public RoundButton(boolean plus) {
        super();
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setBackground(Color.WHITE);
        setForeground(Color.DARK_GRAY);
        this.plus = plus;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the button background
        if (getModel().isArmed()) {
            g2.setColor(Color.LIGHT_GRAY);
        } else {
            g2.setColor(getBackground());
        }
        g2.fillOval(0, 0, getWidth(), getHeight());

        // Draw the plus symbol
        g2.setColor(getForeground());
        int size = Math.min(getWidth(), getHeight()) / 3; // Size of the plus
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // Horizontal line
        g2.fillRect(centerX - size / 2, centerY - size / 10, size, size / 5);


        // Vertical line
        if (plus) {
            g2.fillRect(centerX - size / 10, centerY - size / 2, size / 5, size);
        }

        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(getForeground());
        g2.setStroke(new BasicStroke(2)); // Thicker border
        g2.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
    }

    @Override
    public boolean contains(int x, int y) {
        int radius = getWidth() / 2;
        int centerX = radius;
        int centerY = radius;
        return Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2) <= Math.pow(radius, 2);
    }

    @Override
    public Dimension getPreferredSize() {
        // Ensure the button maintains a circular shape
        int size = Math.max(super.getPreferredSize().width, super.getPreferredSize().height);
        return new Dimension(size, size);
    }
}
