package com.flights.gui.components;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * Background Image Panel class for constructing a JPanel with image
 */
public class BackgroundImagePanel extends JPanel {
    private final Image backgroundImage;

    /**
     * Constructor to load the image
     * @param imagePath image file path
     */
    public BackgroundImagePanel(String imagePath) {
        backgroundImage = new ImageIcon(Objects.requireNonNull(getClass().getResource(imagePath))).getImage();
    }

    /**
     * Overridden method
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

}
