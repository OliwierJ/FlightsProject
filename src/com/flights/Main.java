package com.flights;

import com.flights.gui.MainWindow;
import com.flights.util.JErrorDialog;

import javax.swing.*;
import static com.flights.gui.MainWindow.createAndShowGUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) {
    // try {
    //     String jarDir = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
    //     System.setErr(new PrintStream(new File(jarDir, "logfile.txt")));
    // } catch (FileNotFoundException | URISyntaxException e) {
    //     JErrorDialog.showError("Error log file cannot be created or written to, all errors will be logged to console", e);
    //     System.setErr(System.out);
    // }
    SwingUtilities.invokeLater(() -> createAndShowGUI(new MainWindow()));
    }
}
