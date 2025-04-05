package com.flights.gui;

import com.flights.Main;
import com.flights.gui.components.JSubmitButton;
import com.flights.gui.components.JTopBar;
import com.flights.objects.Booking;
import com.flights.util.FlightsConstants;
import com.flights.objects.Passenger;
import com.flights.util.JErrorDialog;
import com.flights.gui.components.JPlaceHolderTextField;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class SetPassengers extends JPanel implements FlightsConstants {
    private final PassengerAdditionPanel[] passengersPanels = new PassengerAdditionPanel[6];
    private final ArrayList<Passenger> passengers = new ArrayList<>();
    private int count = 0;

    public SetPassengers(Booking b, int[] passengerType, double price) {
        setPreferredSize(Main.getFrameSize());
        setLayout(new BorderLayout());
        add(new JTopBar(price), BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        int passengerTypeCount = 4;

        // panel for the first adult is always required
        for (int i = 0; i < passengerTypeCount; i++) {
            for (int j = 0; j < passengerType[i]; j++) {

                if (i == 0 && j == 0) {
                    PassengerAdditionPanel primaryAdult = new PassengerAdditionPanel("Adult", true);
                    passengersPanels[0] = primaryAdult;
                } else {
                    switch (i) {
                        case 0 -> passengersPanels[count] = new PassengerAdditionPanel("Adult", false);
                        case 1 -> passengersPanels[count] = new PassengerAdditionPanel("Teen", false);
                        case 2 -> passengersPanels[count] = new PassengerAdditionPanel("Child", false);
                        case 3 -> passengersPanels[count] = new PassengerAdditionPanel("Infant", false);
                    }
                }
            }
        }

        for (int i = 0; i < count; i++) {
            contentPanel.add(Box.createVerticalStrut(50));
            contentPanel.add(passengersPanels[i]);
        }

        JButton submitButton = new JSubmitButton("Confirm passengers");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.addActionListener(e -> {
            passengers.clear();
            if (!verifyPassengerFields(passengersPanels)) {
                JErrorDialog.showWarning("Passenger fields are incomplete!");
                return;
            }
            int choice = JOptionPane.showConfirmDialog(this, "Confirm passengers?", "Confirm passengers?", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.OK_OPTION) {
                for (int i = 0; i < count; i++) {
                    PassengerAdditionPanel panel = passengersPanels[i];
                    if (i == 0) {
                        passengers.add(new Passenger(panel.getTitle(),panel.getFname(),panel.getSname(), b.getBookingID()));
                        b.setEmail(panel.getEmail());
                    } else if (panel.getTitlePanel() == null) {
                        passengers.add(new Passenger(null,panel.getFname(),panel.getSname(), b.getBookingID()));
                    } else {
                        passengers.add(new Passenger(panel.getTitle(),panel.getFname(),panel.getSname(), b.getBookingID()));
                    }
                }
                b.addPassengers(passengers.toArray(new Passenger[0]));
                Main.createAndShowGUI(new PassengerSeatSelectionMenu(b,price));
            }
        });

        contentPanel.add(Box.createVerticalStrut(25));
        contentPanel.add(submitButton);
        contentPanel.add(Box.createVerticalStrut(25));
        JScrollPane sp = new JScrollPane(contentPanel);
        sp.getVerticalScrollBar().setUnitIncrement(20);
        add(sp, BorderLayout.CENTER);
    }

    private void setSizes(JComponent p, int width, int height) {
        p.setMinimumSize(new Dimension(width, height));
        p.setPreferredSize(new Dimension(width, height));
        p.setMaximumSize(new Dimension(width, height));
    }

    private boolean verifyPassengerFields(PassengerAdditionPanel[] array) {
        for (int i = 0; i < count; i++) {
            if (i == 0) {
                if (array[i].getTitle().isEmpty() || array[i].getFname().isEmpty() ||
                        array[i].getSname().isEmpty() || array[i].getEmail().isEmpty()) {
                    return false;
                }
            }
            if (array[i].getTitlePanel() == null) {
                if (array[i].getFname().isEmpty() || array[i].getSname().isEmpty()) {
                    return false;
                }
            }
            if ((array[i].getTitlePanel() != null) && array[i].getTitle().isEmpty() || array[i].getFname().isEmpty() ||
                    array[i].getSname().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private class PassengerAdditionPanel extends JPanel {
        private TextBoxPanel passengerTitle;
        private final TextBoxPanel firstName;
        private final TextBoxPanel lastName;
        private TextBoxPanel email;

        PassengerAdditionPanel(String name, boolean isPrimary) {
            setBackground(Color.BLUE);
            setSizes(this, 1000, 180);
            setLayout(new BorderLayout());

            JPanel titleFlowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            titleFlowPanel.setBackground(APPLEGREEN);
            int titleHeight = 60;
            setSizes(titleFlowPanel, 1000, titleHeight);

            JLabel title = new JLabel("Passenger " + ++count);
            title.setFont(new Font("Arial", Font.BOLD, 35));
            title.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
            JLabel titleType = new JLabel(name);
            titleType.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            titleType.setFont(new Font("Arial", Font.PLAIN, 22));

            titleFlowPanel.add(Box.createHorizontalStrut(10));
            titleFlowPanel.add(title);
            titleFlowPanel.add(Box.createHorizontalStrut(15));
            titleFlowPanel.add(titleType);

            JPanel passengerInfoPanel = new JPanel();
            passengerInfoPanel.setLayout(new BoxLayout(passengerInfoPanel, BoxLayout.X_AXIS));
            passengerInfoPanel.setBackground(ASPARAGUS);
            passengerInfoPanel.setPreferredSize(new Dimension(800, 180 - titleHeight));

            firstName = new TextBoxPanel(175, "First name");
            lastName = new TextBoxPanel(175, "Last name");

            passengerInfoPanel.add(Box.createHorizontalGlue());

            if (name.equals("Adult") || name.equals("Teen")) {
                passengerTitle = new TextBoxPanel(125, "Title");
                passengerInfoPanel.add(passengerTitle);
            }
            passengerInfoPanel.add(firstName);
            passengerInfoPanel.add(lastName);

            if (isPrimary) {
                email = new TextBoxPanel(175, "Email");
                passengerInfoPanel.add(email);
            }

            passengerInfoPanel.add(Box.createHorizontalGlue());
            add(titleFlowPanel, BorderLayout.NORTH);
            add(passengerInfoPanel, BorderLayout.CENTER);
        }

        public String getTitle() {
            return passengerTitle.getText();
        }
        public TextBoxPanel getTitlePanel() {
            return passengerTitle;
        }
        public String getFname() {
            return firstName.getText();
        }

        public String getSname() {
            return lastName.getText();
        }

        public String getEmail() {
            return email.getText();
        }
    }

    private class TextBoxPanel extends JPanel {
        private final JComponent field;

        TextBoxPanel(int width, String name) {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setOpaque(false);
            setPreferredSize(new Dimension(width, 120));

            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panel.setOpaque(false);
            setSizes(panel, width, 40);
            JLabel label = new JLabel(name);
            label.setFont(ARIAL20);
            panel.add(label);
            
            if (name.equals("Title")) {
                field = new JComboBox<>(new String[] {"", "Mr", "Ms", "Mrs" });
            } else {
                field = new JPlaceHolderTextField(name);
            }

            setSizes(field, width, 35);
            field.setFont(new Font("Arial", Font.PLAIN, 18));

            add(Box.createVerticalGlue());
            add(panel);
            add(field);
            add(Box.createVerticalGlue());
            add(Box.createVerticalStrut(20));
        }

        public String getText() {
            if (field instanceof JPlaceHolderTextField) {
                return ((JPlaceHolderTextField) field).getText();
            } else {
                return Objects.requireNonNull(((JComboBox<?>) field).getSelectedItem()).toString();
            }
        }
    }
}