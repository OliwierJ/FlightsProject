package com.flights;

import com.flights.gui.MainWindow;
import com.flights.gui.PassengerSeatSelectionMenu;
import com.flights.gui.ViewBookingMenu;
import com.flights.util.Generator;
import com.flights.util.JErrorDialog;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.net.URISyntaxException;

/**
 * Main class containing <code>public static void main</code> and the main JFrame
 */
public final class Main {
    public static final JFrame frame = new JFrame();
    private static JPanel previousPanel;

    /**
     * Entry point into the program
     * @param args use <code>--generate</code> to launch generate flight and bookings into database
     */
    public static void main(String[] args) {
        try {
            String jarDir = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
            System.setErr(new PrintStream(new File(jarDir, "logfile.txt")));
        } catch (FileNotFoundException | URISyntaxException e) {
            System.setErr(System.out);
            JErrorDialog.showError("Error log file cannot be created or written to, all errors will be logged to console", e);
        }
        if (args.length == 1 && args[0].equals("--generate")) {
            Generator.generate();
        } else {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setMinimumSize(new Dimension(1350, 850));
            SwingUtilities.invokeLater(() -> Main.createAndShowGUI(new MainWindow()));
        }
    }

    /**
     * Set the passed in panel as the JFrame's content pane and render to screen
     * @param panel JPanel
     */
    public static void createAndShowGUI(JPanel panel) {
        // catch if any panel throws exception e.g. FlightSelection
        try {
            if (!panel.getClass().isInstance(frame.getContentPane())) {
                if (panel instanceof ViewBookingMenu){
                    ((ViewBookingMenu) panel).refreshText();
                } else if (panel instanceof PassengerSeatSelectionMenu) {
                    ((PassengerSeatSelectionMenu) panel).refreshText();
                }
                previousPanel = (JPanel) frame.getContentPane();
                frame.setContentPane(panel);
                frame.setTitle(panel.getClass().getSimpleName());
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Render the stored previous JPanel allowing the program and user to store 1 previous JPanel to render without regenerating the JPanel
     */
    public static void returnToPreviousMenu() {
        createAndShowGUI(previousPanel);
    }

    /**
     * Get JFrame size excluding all borders, titles and insets
     * @return new Dimension(width, height)
     */
    public static Dimension getFrameSize() {
        Insets insets = frame.getInsets();
        int width = frame.getWidth() - insets.left - insets.right;
        int height = frame.getHeight() - insets.top - insets.bottom;
        return new Dimension(width, height);
    }
}
