package com.flights;

import com.flights.gui.MainWindow;
import com.flights.util.Generator;
import com.flights.util.JErrorDialog;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.net.URISyntaxException;

public final class Main {
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
            System.setErr(System.out); // TODO delete
            SwingUtilities.invokeLater(() -> MainWindow.createAndShowGUI(new MainWindow()));
        }
    }
}
