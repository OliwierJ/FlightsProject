package com.flights.gui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

import com.flights.Main;
import com.flights.gui.LoginMenu;
import com.flights.gui.MainWindow;
import com.flights.gui.MyBookingMenu;
import com.flights.util.FlightsConstants;

/**
 * Class for the top menu bar JPanel used across all screens
 */
public class JTopBar extends JPanel implements FlightsConstants, MouseListener {
    private final JLabel loginLabel = new JLabel("Login");
    private final JLabel myBookingLabel = new JLabel("My Bookings");
    private final JLabel home = new JLabel("Home");
    private final JLabel price = new JLabel("€0");

    /**
     * Construct a new JTopBar with default price €0
     */
    public JTopBar() {
        setLayout(new FlowLayout(FlowLayout.RIGHT));
        setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        setAlignmentX(Component.RIGHT_ALIGNMENT);
        setBackground(TRUEBLUE);
        setBorder(BorderFactory.createMatteBorder(0,3,3,0,MAIZE));

        add(loginLabel);
        add(Box.createHorizontalStrut(20));
        add(myBookingLabel);
        add(Box.createHorizontalStrut(20));
        add(home);
        add(Box.createHorizontalStrut(20));
        add(price);

        for (Component c : getComponents()) {
            if (c instanceof JLabel l) {
                l.setAlignmentX(Component.CENTER_ALIGNMENT);
                l.setAlignmentY(Component.CENTER_ALIGNMENT);
                l.setForeground(Color.WHITE);
                l.setFont(ARIAL20);
                l.addMouseListener(this);
            }
        }
    }

    /**
     * Construct new JTopBar with a custom price
     * @param price double
     */
    public JTopBar(double price) {
        this();
        this.price.setText("€" + price);
    }

    /**
     * Updates the price label on the JTopBar
     * @param price double
     */
    public void updatePrice(double price) {
        this.price.setText(String.format("€%.2f", price));
        this.repaint();
    }

    /** @param e the event to be processed */
    @Override
    public void mouseClicked(MouseEvent e) {}
    /** @param e the event to be processed */
    @Override
    public void mousePressed(MouseEvent e) {}

    /**
     * Switch to appropriate screen when label in the top bar is clicked
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getSource().equals(myBookingLabel)) {
            Main.createAndShowGUI(new MyBookingMenu());
        } else if (e.getSource().equals(loginLabel)) {
            Main.createAndShowGUI(new LoginMenu());
        } else if (e.getSource().equals(home)) {
            Main.createAndShowGUI(new MainWindow());
        }
    }

    /**
     * Darken the label text in the top bar when mouse is hovering over the label
     * @param e the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        if (!e.getComponent().equals(price)) {
            e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            e.getComponent().setForeground(SELECTEDGRAY);
        }
    }

    /**
     * Return label to normal when mouse hovers out of the label
     * @param e the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent e) {
        e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        e.getComponent().setForeground(Color.WHITE);
    }
}
