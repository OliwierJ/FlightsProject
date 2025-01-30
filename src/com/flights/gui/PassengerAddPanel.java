package com.flights.gui;

import com.flights.util.FlightsConstants;
import com.flights.util.JErrorDialog;

import javax.swing.*;
import java.awt.*;

public class PassengerAddPanel extends JPanel {

    int[] passType;
    int passCount = 0;
    JButton doneButton;

    public PassengerAddPanel(int[] passType) {
        super();
        if (passType.length != 4) {
            throw new IllegalArgumentException("passType must be equal to 4");
        }
        this.passType = passType;
        for (int j : passType) {
            passCount += j;
        }
        setPreferredSize(new Dimension(300, 400));
        setLayout(new GridLayout(5, 4, 0, 0));
        Font f = new Font("Arial", Font.PLAIN, 20);
        JPanel adultPanel = new JPanel();
        adultPanel.setLayout(new BoxLayout(adultPanel, BoxLayout.X_AXIS));
        JPanel teenPanel = new JPanel();
        teenPanel.setLayout(new BoxLayout(teenPanel, BoxLayout.X_AXIS));
        JPanel childPanel = new JPanel();
        childPanel.setLayout(new BoxLayout(childPanel, BoxLayout.X_AXIS));
        JPanel infantPanel = new JPanel();
        infantPanel.setLayout(new BoxLayout(infantPanel, BoxLayout.X_AXIS));
        JPanel southPanel = new JPanel();
        southPanel.setBackground(FlightsConstants.APPLEGREEN);
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.X_AXIS));
        doneButton = new JButton("Done");
        JLabel adultCount = new JLabel(passType[0] + "");
        adultCount.setFont(f);
        JLabel teenCount = new JLabel(passType[1] + "");
        teenCount.setFont(f);
        JLabel childCount = new JLabel(passType[2] + "");
        childCount.setFont(f);
        JLabel infantCount = new JLabel(passType[3] + "");
        infantCount.setFont(f);
        JLabel adultLabel = new JLabel("Adults 16+");
        adultLabel.setFont(f);
        JLabel teenLabel = new JLabel("Teens 12 - 15");
        teenLabel.setFont(f);
        JLabel childLabel = new JLabel("Children 3 - 11");
        childLabel.setFont(f);
        JLabel infantLabel = new JLabel("Infants 0 - 2");
        infantLabel.setFont(f);

        RoundButton increase1 = new RoundButton(true);
        increase1.setForeground(FlightsConstants.SEAGREEN);
        increase1.setMaximumSize(new Dimension(35, 35));
        increase1.setPreferredSize(new Dimension(35, 35));
        increase1.setFocusable(false);
        increase1.addActionListener(e -> {
            if (passCount < 6) {
                passCount++;
                passType[0]++;
                adultCount.setText(Integer.parseInt(adultCount.getText()) + 1 + "");
            } else {
                JErrorDialog.showError("Max Passenger count reached", "Error");
            }
        });

        RoundButton increase2 = new RoundButton(true);
        increase2.setForeground(FlightsConstants.SEAGREEN);
        increase2.setMaximumSize(new Dimension(35, 35));
        increase2.setPreferredSize(new Dimension(35, 35));
        increase2.setFocusable(false);
        increase2.addActionListener(e -> {
            if (passCount < 6) {
                passCount++;
                passType[1]++;
                teenCount.setText(Integer.parseInt(teenCount.getText()) + 1 + "");
            } else {
                JErrorDialog.showError("Max Passenger count reached", "Error");
            }
        });

        RoundButton increase3 = new RoundButton(true);
        increase3.setForeground(FlightsConstants.SEAGREEN);
        increase3.setMaximumSize(new Dimension(35, 35));
        increase3.setPreferredSize(new Dimension(35, 35));
        increase3.setFocusable(false);
        increase3.addActionListener(e -> {
            if (passCount < 6) {
                passCount++;
                passType[2]++;
                childCount.setText(Integer.parseInt(childCount.getText()) + 1 + "");
            } else {
                JErrorDialog.showError("Max Passenger count reached", "Error");
            }
        });

        RoundButton increase4 = new RoundButton(true);
        increase4.setForeground(FlightsConstants.SEAGREEN);
        increase4.setMaximumSize(new Dimension(35, 35));
        increase4.setPreferredSize(new Dimension(35, 35));
        increase4.setFocusable(false);
        increase4.addActionListener(e -> {
            if (Integer.parseInt(infantCount.getText()) == Integer.parseInt(adultCount.getText())) {
                JErrorDialog.showError("Number of infants cannot exceed the number of adults", "Error");

            } else if (passCount < 6) {
                passCount++;
                passType[3]++;
                infantCount.setText(Integer.parseInt(infantCount.getText()) + 1 + "");
            } else {
                JErrorDialog.showError("Max Passenger count reached", "Error");
            }
        });

        RoundButton decrease1 = new RoundButton(false);
        decrease1.setMaximumSize(new Dimension(35, 35));
        decrease1.setPreferredSize(new Dimension(35, 35));
        decrease1.setFocusable(false);
        decrease1.addActionListener(e -> {
            if (Integer.parseInt(adultCount.getText()) > 1) {
                passCount--;
                passType[0]--;
                adultCount.setText(Integer.parseInt(adultCount.getText()) - 1 + "");
            }
        });

        RoundButton decrease2 = new RoundButton(false);
        decrease2.setMaximumSize(new Dimension(35, 35));
        decrease2.setPreferredSize(new Dimension(35, 35));
        decrease2.setFocusable(false);
        decrease2.addActionListener(e -> {
            if (Integer.parseInt(teenCount.getText()) > 0) {
                passCount--;
                passType[1]--;
                teenCount.setText(Integer.parseInt(teenCount.getText()) - 1 + "");
            }
        });
        RoundButton decrease3 = new RoundButton(false);
        decrease3.setMaximumSize(new Dimension(35, 35));
        decrease3.setPreferredSize(new Dimension(35, 35));
        decrease3.setFocusable(false);
        decrease3.addActionListener(e -> {
            if (Integer.parseInt(childCount.getText()) > 0) {
                passCount--;
                passType[2]--;
                childCount.setText(Integer.parseInt(childCount.getText()) - 1 + "");
            }
        });
        RoundButton decrease4 = new RoundButton(false);
        decrease4.setMaximumSize(new Dimension(35, 35));
        decrease4.setPreferredSize(new Dimension(35, 35));
        decrease4.setFocusable(false);
        decrease4.addActionListener(e -> {
            if (Integer.parseInt(infantCount.getText()) > 0) {
                passCount--;
                passType[3]--;
                infantCount.setText(Integer.parseInt(infantCount.getText()) - 1 + "");
            }
        });

        adultPanel.add(Box.createHorizontalStrut(15));
        adultPanel.add(adultLabel);
        adultPanel.add(Box.createHorizontalGlue());
        adultPanel.add(decrease1);
        adultPanel.add(Box.createHorizontalStrut(10));
        adultPanel.add(adultCount);
        adultPanel.add(Box.createHorizontalStrut(10));
        adultPanel.add(increase1);
        adultPanel.add(Box.createHorizontalStrut(5));

        teenPanel.add(Box.createHorizontalStrut(15));
        teenPanel.add(teenLabel);
        teenPanel.add(Box.createHorizontalGlue());
        teenPanel.add(decrease2);
        teenPanel.add(Box.createHorizontalStrut(10));
        teenPanel.add(teenCount);
        teenPanel.add(Box.createHorizontalStrut(10));
        teenPanel.add(increase2);
        teenPanel.add(Box.createHorizontalStrut(5));

        childPanel.add(Box.createHorizontalStrut(15));
        childPanel.add(childLabel);
        childPanel.add(Box.createHorizontalGlue());
        childPanel.add(decrease3);
        childPanel.add(Box.createHorizontalStrut(10));
        childPanel.add(childCount);
        childPanel.add(Box.createHorizontalStrut(10));
        childPanel.add(increase3);
        childPanel.add(Box.createHorizontalStrut(5));

        infantPanel.add(Box.createHorizontalStrut(15));
        infantPanel.add(infantLabel);
        infantPanel.add(Box.createHorizontalGlue());
        infantPanel.add(decrease4);
        infantPanel.add(Box.createHorizontalStrut(10));
        infantPanel.add(infantCount);
        infantPanel.add(Box.createHorizontalStrut(10));
        infantPanel.add(increase4);
        infantPanel.add(Box.createHorizontalStrut(5));

        southPanel.add(Box.createHorizontalStrut(15));
        doneButton.setFocusable(false);
        doneButton.setFont(new Font("Arial", Font.BOLD, 15));
        doneButton.setForeground(FlightsConstants.TRUEBLUE);
        doneButton.setPreferredSize(new Dimension(100, 35));
        doneButton.setMaximumSize(new Dimension(100, 35));
        southPanel.add(doneButton);

        add(adultPanel);
        add(teenPanel);
        add(childPanel);
        add(infantPanel);
        add(southPanel);
    }

    public JButton getDoneButton() {
        return doneButton;
    }

    public int[] getPassType() {
        return passType;
    }

    public int getPassCount() {
        return passCount;
    }
}
