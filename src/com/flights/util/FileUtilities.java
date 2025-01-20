package com.flights.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class FileUtilities {
    public static BufferedImage loadImage(String filePath) {
        File file = new File(filePath);
        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            System.err.println("Error while loading image from " + filePath);
            throw new RuntimeException(e.getMessage() + "\n" + e.getCause());
        }
    }

}
