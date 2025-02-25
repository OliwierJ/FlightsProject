package com.flights.gui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

import com.flights.gui.LoginMenu;
import com.flights.gui.MainWindow;
import com.flights.gui.MyBookingMenu;
import com.flights.util.FlightsConstants;

public class JTopBar extends JPanel implements FlightsConstants, MouseListener {
    private final JLabel loginLabel = new JLabel("Login");
    private final JLabel myBookingLabel = new JLabel("My Bookings");
    private final JLabel FAQLabel = new JLabel("FAQ");
    private final JLabel home = new JLabel("Home");

    public JTopBar() {
        setLayout(new FlowLayout(FlowLayout.RIGHT));
        setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        setAlignmentX(Component.RIGHT_ALIGNMENT);
        setBackground(TRUEBLUE);
        setPreferredSize(new Dimension(MainWindow.FRAME_WIDTH, 40));
        setMaximumSize(new Dimension(10000, 40));
        setBorder(BorderFactory.createMatteBorder(0,3,3,0,MAIZE));

        add(loginLabel);
        add(Box.createHorizontalStrut(20));
        add(myBookingLabel);
        add(Box.createHorizontalStrut(20));
        add(FAQLabel);
        add(Box.createHorizontalStrut(20));
        add(home);

        for (Component c : getComponents()) {
            if (c instanceof JLabel) {
                JLabel l = (JLabel) c;
                l.setAlignmentX(Component.CENTER_ALIGNMENT);
                l.setAlignmentY(Component.CENTER_ALIGNMENT);
                l.setForeground(Color.WHITE);
                l.setFont(ARIAL20);
                l.addMouseListener(this);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getSource().equals(myBookingLabel)) {
            MainWindow.createAndShowGUI(new MyBookingMenu());
        } else if (e.getSource().equals(FAQLabel)) {
            //TODO switch windows
        } else if (e.getSource().equals(loginLabel)) {
            MainWindow.createAndShowGUI(new LoginMenu());
        } else if (e.getSource().equals(home)) {
            MainWindow.createAndShowGUI(new MainWindow());
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        e.getComponent().setForeground(FlightsConstants.SELECTEDGRAY);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        e.getComponent().setForeground(Color.WHITE);
    }
}
