package com.flights.gui;

import com.flights.Main;
import com.flights.gui.components.JSubmitButton;
import com.flights.gui.components.JTopBar;
import com.flights.gui.components.RoundButton;
import com.flights.objects.Booking;
import com.flights.util.FlightsConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ExtrasMenu extends JPanel implements FlightsConstants, ItemListener {
    private final JRadioButton yesButton;
    private final Booking b;
    private double price;
    private final JTopBar topBar = new JTopBar();
    public ExtrasMenu(Booking booking, double price) {
        this.b = booking;

        setLayout(new BorderLayout());
        setPreferredSize(Main.getFrameSize());
        add(topBar, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel priorityPanel = new JPanel();
        priorityPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        priorityPanel.setLayout(new BoxLayout(priorityPanel, BoxLayout.Y_AXIS));
        priorityPanel.setBorder(BorderFactory.createLineBorder(DARKSPRINGGREEN, 4));
        setSizes(priorityPanel, 500, 300);

        JLabel priorityLabel = new JLabel("Priority Boarding");
        priorityLabel.setFont(new Font("Arial", Font.BOLD, 30));
        priorityLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));

        JLabel priorityQ = new JLabel("Do you want to board with Priority?");
        priorityQ.setFont(new Font("Arial", Font.PLAIN, 30));
        priorityQ.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));
        priorityQ.setAlignmentX(Component.LEFT_ALIGNMENT);

        ButtonGroup priorityButtonGroup = new ButtonGroup();
        JPanel radioPanel = new JPanel();
        radioPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.X_AXIS));
        radioPanel.setMaximumSize(new Dimension(500, 50));
        JRadioButton noButton = new JRadioButton("No");
        noButton.setFocusable(false);
        int empty = 50;
        noButton.setBorder(BorderFactory.createEmptyBorder(empty,empty,empty,empty));
        noButton.setFont(ARIAL20);

        if (b.getPriorityBoarding() != 1) {
            noButton.setSelected(true);
        }
        yesButton = new JRadioButton("Yes (€20)");
        yesButton.setFocusable(false);
        yesButton.setSelected(true);
        yesButton.setFont(ARIAL20);
        yesButton.addItemListener(this);
        yesButton.setBorder(BorderFactory.createEmptyBorder(empty,empty,empty,empty));
        priorityButtonGroup.add(noButton);
        priorityButtonGroup.add(yesButton);
        radioPanel.add(noButton);
        radioPanel.add(Box.createHorizontalGlue());
        radioPanel.add(yesButton);

        priorityPanel.add(priorityLabel);
        priorityPanel.add(priorityQ);
        priorityPanel.add(radioPanel);

        JPanel luggagePanel = new JPanel();
        luggagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        luggagePanel.setLayout(new BoxLayout(luggagePanel, BoxLayout.Y_AXIS));
        luggagePanel.setBorder(BorderFactory.createLineBorder(DARKSPRINGGREEN, 4));
        setSizes(luggagePanel, 500, 300);

        JLabel luggageLabel = new JLabel("Add bags");
        luggageLabel.setFont(new Font("Arial", Font.BOLD, 30));
        luggageLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));
        luggageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel luggageAddPanel = new JPanel();
        setSizes(luggageAddPanel, 500, 50);
        luggageAddPanel.setLayout(new BoxLayout(luggageAddPanel, BoxLayout.X_AXIS));

        JPanel pricePanel = new JPanel();
        pricePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        setSizes(pricePanel, 500, 50);
        pricePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel priceLabel = new JLabel("€39.99 per flight");
        priceLabel.setFont(new Font("Arial", Font.ITALIC, 15));
        priceLabel.setForeground(Color.DARK_GRAY);
        priceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        pricePanel.add(priceLabel);

        JLabel label20kg = new JLabel("20kg Luggage");
        label20kg.setFont(ARIAL20);

        JLabel luggageCount = new JLabel(b.get20kgluggage() ? "1" : "0");
        luggageCount.setFont(ARIAL20);

        RoundButton increase1 = new RoundButton(true);
        increase1.setForeground(SEAGREEN);
        setSizes(increase1, 35, 35);
        increase1.setFocusable(false);
        increase1.addActionListener(e -> {
            if (!b.get20kgluggage()) {
                b.set20kgluggage(true);
                updatePrice(39.99);
                luggageCount.setText("1");
            } else {
                JOptionPane.showMessageDialog(Main.frame, "Max 20kg luggage count reached" , "Luggage limit", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        RoundButton decrease1 = new RoundButton(false);
        setSizes(decrease1, 35, 35);
        decrease1.setFocusable(false);
        decrease1.addActionListener(e -> {
            if (b.get20kgluggage()) {
                b.set20kgluggage(false);
                updatePrice(-39.99);
                luggageCount.setText("0");
            }
        });

        luggageAddPanel.add(Box.createHorizontalStrut(15));
        luggageAddPanel.add(label20kg);
        luggageAddPanel.add(Box.createHorizontalGlue());
        luggageAddPanel.add(decrease1);
        luggageAddPanel.add(Box.createHorizontalStrut(10));
        luggageAddPanel.add(luggageCount);
        luggageAddPanel.add(Box.createHorizontalStrut(10));
        luggageAddPanel.add(increase1);
        luggageAddPanel.add(Box.createHorizontalStrut(5));
        luggageAddPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        luggagePanel.add(luggageLabel);
        luggagePanel.add(Box.createVerticalStrut(30));
        luggagePanel.add(luggageAddPanel);
        luggagePanel.add(pricePanel);
        JButton submit = new JSubmitButton("Next");
        submit.setAlignmentX(Component.CENTER_ALIGNMENT);
        submit.addActionListener(e -> Main.createAndShowGUI(new PaymentMenu(booking, this.price)));
        mainPanel.add(Box.createVerticalStrut(50));
        mainPanel.add(priorityPanel);
        mainPanel.add(Box.createVerticalStrut(50));
        mainPanel.add(luggagePanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(submit);
        add(mainPanel, BorderLayout.CENTER);

        // this has to be here
        this.price = price;
        topBar.updatePrice(price);
    }

    private void setSizes(JComponent p, int width, int height) {
        p.setPreferredSize(new Dimension(width, height));
        p.setMaximumSize(new Dimension(width, height));
        p.setMinimumSize(new Dimension(width, height));
    }

    private void updatePrice(double i) {
        price += i;
        if (b.getReturnFlight() != null) {
            price += i;
        }
        topBar.updatePrice(price);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == yesButton) {
            if (yesButton.isSelected()) {
                b.setPriorityBoarding(1);
                updatePrice(20);
            } else {
                b.setPriorityBoarding(0);
                updatePrice(-20);
            }
            topBar.updatePrice(price);
        }
    }
}
