package com.flights.gui;

import com.flights.objects.Flight;
import com.flights.util.FlightsConstants;
import com.flights.objects.Passenger;
import com.flights.util.JErrorDialog;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SetPassengers extends JPanel {

    JPanel progressPanel;
    private final PassengerAdditionPanel[] passengersPanels = new PassengerAdditionPanel[6];
    private ArrayList<Passenger> passengers = new ArrayList<>();
    private PassengerAdditionPanel primaryAdult;
    private int count = 0;

    public SetPassengers(Flight flight, Flight returnFlight, int[] passengerType, double price) {
        setPreferredSize(new Dimension(MainWindow.FRAME_WIDTH, MainWindow.FRAME_HEIGHT));
        setLayout(new BorderLayout());

        add(new JTopBar(), BorderLayout.NORTH);
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        int passengerTypeCount = 4;

        // panel for the first adult is always required
        for (int i = 0; i < passengerTypeCount; i++) {
            for (int j = 0; j < passengerType[i]; j++) {

                if (i == 0 && j == 0) {
                    primaryAdult = new PassengerAdditionPanel("Adult", true);
                    passengersPanels[0] = primaryAdult;
                } else {
                    switch (i) {
                        case 0:
                            passengersPanels[count] = new PassengerAdditionPanel("Adult", false);
                            break;
                        case 1:
                            passengersPanels[count] = new PassengerAdditionPanel("Teen", false);
                            break;
                        case 2:
                            passengersPanels[count] = new PassengerAdditionPanel("Child", false);
                            break;
                        case 3:
                            passengersPanels[count] = new PassengerAdditionPanel("Infant", false);

                        default:
                            break;
                    }
                }

            }
        }

        contentPanel.add(Box.createVerticalStrut(75));
        for (int i = 0; i < count; i++) {
            contentPanel.add(passengersPanels[i]);
            contentPanel.add(Box.createVerticalStrut(50));
//            passengers.add(new Passenger(passengersPanels[i].get,null,null,null));
        }

        JButton submitButton = new JButton("Confirm passengers");
        submitButton.setFont(MainWindow.ARIAL20);
        submitButton.setPreferredSize(new Dimension(200,75));
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        submitButton.addActionListener(e -> {
            passengers.clear();
            if (!verifyPassengerFields(passengersPanels)) {
                JErrorDialog.showWarning("Passenger fields are incomplete!");
                return;
            }

            for (int i = 0; i < count; i++) {
                PassengerAdditionPanel panel = passengersPanels[i];
                if (i == 0) {
                    passengers.add(new Passenger(panel.getTitle(),panel.getFname(),panel.getSname(),panel.getEmail()));
                } else if (panel.getTitlePanel() == null) {
                    passengers.add(new Passenger(null,panel.getFname(),panel.getSname(),null));
                } else {
                    passengers.add(new Passenger(panel.getTitle(),panel.getFname(),panel.getSname(),null));
                }
//                System.out.println(passengers.get(i));
            }
            System.out.println(passengers.toString());
            System.out.println("Correct");
            int choice = JOptionPane.showConfirmDialog(this, "Confirm passengers?");

            if (choice == JOptionPane.OK_OPTION) {
                MainWindow.createAndShowGUI(new SelectSeats());
            }
        });

        contentPanel.add(submitButton);
        add(new JScrollPane(contentPanel), BorderLayout.CENTER);
    }

    private boolean verifyPassengerFields(PassengerAdditionPanel[] array) {
        for (int i = 0; i < count; i++) {
            System.out.println(i);
            if (i == 0) {
                if (array[i].getTitle().isEmpty() || array[i].getFname().isEmpty() ||
                        array[i].getSname().isEmpty() || array[i].getEmail().isEmpty()) {
                    return false;
                }
            }
            if (array[i].getTitlePanel() == null) {
                System.out.println(array[i].getFname());
                if (array[i].getFname().isEmpty() || array[i].getSname().isEmpty()) {

                    return false;
                }
            }
            if ((array[i].getTitlePanel() != null) && array[i].getTitle().isEmpty() || array[i].getFname().isEmpty() ||
                    array[i].getSname().isEmpty()) {
                return false;
            }
//            System.out.println(3);
        }
        return true;
    }

    class PassengerAdditionPanel extends JPanel {

        TextBoxPanel passengerTitle;
        TextBoxPanel firstName;
        TextBoxPanel lastName;
        TextBoxPanel email;

        PassengerAdditionPanel(String name, boolean isPrimary) {
            setBackground(Color.BLUE);

            setPreferredSize(new Dimension(800, 180));
            setMinimumSize(new Dimension(800, 180));
            setMaximumSize(new Dimension(800, 180));
            setLayout(new BorderLayout());

            JPanel titleFlowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            titleFlowPanel.setBackground(FlightsConstants.APPLEGREEN);
            int titleHeight = 60;
            titleFlowPanel.setPreferredSize(new Dimension(800, titleHeight));
            titleFlowPanel.setMinimumSize(new Dimension(800, titleHeight));
            titleFlowPanel.setMaximumSize(new Dimension(800, titleHeight));

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
            passengerInfoPanel.setBackground(FlightsConstants.ASPARAGUS);
            passengerInfoPanel.setPreferredSize(new Dimension(800, 180 - titleHeight));


            firstName = new TextBoxPanel(200, "First name");
            lastName = new TextBoxPanel(200, "Last name");

            passengerInfoPanel.add(Box.createHorizontalGlue());

            if (name.equals("Adult") || name.equals("Teen")) {
                passengerTitle = new TextBoxPanel(125, "Title");
                passengerInfoPanel.add(passengerTitle);
            }
            passengerInfoPanel.add(firstName);
            passengerInfoPanel.add(lastName);

            if (isPrimary) {
                email = new TextBoxPanel(200, "Email");
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

    static class TextBoxPanel extends JPanel {

        private final Component field;
        String text;

        public TextBoxPanel(int width, String name) {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            // setBackground(Color.BLUE);
            setOpaque(false);
            setPreferredSize(new Dimension(width, 120));

            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panel.setOpaque(false);
            panel.setPreferredSize(new Dimension(width, 40));
            panel.setMaximumSize(new Dimension(width, 40));
            JLabel label = new JLabel(name);
            label.setFont(MainWindow.ARIAL20);

            panel.add(label);
            
            if (name.equals("Title")) {
                field = new JComboBox<>(new String[] {"", "Mr", "Ms", "Mrs" });
            } else {
                field = new JPlaceHolderTextField(name);
            }

            field.setPreferredSize(new Dimension(width, 35));
            field.setMaximumSize(new Dimension(width - 10, 35));
            field.setMinimumSize(new Dimension(width, 35));
            field.setFont(new Font("Arial", Font.PLAIN, 18));
            add(Box.createVerticalGlue());
            add(panel);
            add(field);
            add(Box.createVerticalGlue());
            add(Box.createVerticalStrut(20));

        }

        public String getText() {
            if (field instanceof JPlaceHolderTextField) {
//                System.out.println(((JPlaceHolderTextField) field).getText());
                return ((JPlaceHolderTextField) field).getText();
            } else {
                return ((JComboBox<String>) field).getSelectedItem().toString();
            }
        }
    }

    public static void main(String[] args) {
        MainWindow.createAndShowGUI(new SetPassengers(null, null, new int[] { 1, 1, 1, 0 }, 0));
    }

}
