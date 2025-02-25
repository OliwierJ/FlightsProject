package com.flights.gui.components;

import javax.swing.*;
import java.awt.*;

public class BackgroundImagePanel extends JPanel {
    private Image backgroundImage;

    // Constructor to load the image
    public BackgroundImagePanel(String imagePath) {
        backgroundImage = new ImageIcon(imagePath).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

}
