package com.flights.util;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;


public class FileUtilities {
    public static BufferedImage loadImage(String path) {
        try (InputStream stream = FileUtilities.class.getResourceAsStream(path)) {
            if (stream == null) {
                throw new IOException("Resource not found: " + path);
            }
            return ImageIO.read(stream);
        } catch (IOException e) {
            JErrorDialog.showError("Could not load image: " + path, e);
            return null;
        }
    }

}