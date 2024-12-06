package com.flights;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

public class testUI extends JFrame{
    
    JPanel flightSelectionPanel;
    String DEPARTURE_PLACEHOLDER = "Departure";
    String ARRIVAL_PLACEHOLDER = "Arrival";
    testUI () {
        setLayout(new BorderLayout());
        flightSelectionPanel = new JPanel();
        flightSelectionPanel.setLayout(new GridLayout(1,3,10,10 ));
        flightSelectionPanel.setBackground(new Color(0x02610e));

        flightSelectionPanel.setPreferredSize(new Dimension(871, 187));


        JPanel columnPanel1 = new JPanel();
        JPanel columnPanel2 = new JPanel();
        JPanel columnPanel3= new JPanel();
        columnPanel1.setOpaque(false);
        columnPanel2.setOpaque(false);
        columnPanel3.setOpaque(false);
        flightSelectionPanel.add(columnPanel1);
        flightSelectionPanel.add(columnPanel2);
        flightSelectionPanel.add(columnPanel3);

//        creates the date panel, and picker objects
        UtilDateModel model = new UtilDateModel();
        UtilDateModel model2 = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model, new Properties());
        JDatePanelImpl datePanel2 = new JDatePanelImpl(model2, new Properties());
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        JDatePickerImpl datePicker2 = new JDatePickerImpl(datePanel2, new DateLabelFormatter());

        ButtonGroup flightType = new ButtonGroup();
        JRadioButton returnFlight = new JRadioButton("Return Flight");
        JRadioButton oneWayFlight = new JRadioButton("One Way Flight");
        returnFlight.setSelected(true);
        returnFlight.setBackground(new Color(0x02610e));
        returnFlight.setForeground(Color.WHITE);
        oneWayFlight.setBackground(new Color(0x02610e));
        oneWayFlight.setForeground(Color.WHITE);
        flightType.add(returnFlight);
        flightType.add(oneWayFlight);

        JTextField departureField = new JTextField(20);
        departureField.setText(DEPARTURE_PLACEHOLDER);
        departureField.setForeground(Color.DARK_GRAY);

        JTextField arrivalField = new JTextField(20);
        arrivalField.setText(ARRIVAL_PLACEHOLDER);
        arrivalField.setForeground(Color.DARK_GRAY);

//        Add placeholder text listener
        class PlaceholderFocus implements FocusListener {

//            Check if the focus gained and the placeholder is still in place
            @Override
            public void focusGained(FocusEvent e) {
                if (e.getSource().equals(departureField)) {
                    if (((JTextField) e.getSource()).getText().equals(DEPARTURE_PLACEHOLDER)) {
                        ((JTextField) e.getSource()).setForeground(Color.BLACK);
                        ((JTextField) e.getSource()).setText("");
                    }
                }

                if (e.getSource().equals(arrivalField)) {
                    if (((JTextField) e.getSource()).getText().equals(ARRIVAL_PLACEHOLDER)) {
                        ((JTextField) e.getSource()).setForeground(Color.BLACK);
                        ((JTextField) e.getSource()).setText("");
                    }
                }
            }

//          Check if focus is lost and if nothing is typed, replace with placeholder
            @Override
            public void focusLost(FocusEvent e) {
                if (e.getSource().equals(departureField)) {
                    if (((JTextField) e.getSource()).getText().isEmpty()) {
                        ((JTextField) e.getSource()).setForeground(Color.BLACK);
                        ((JTextField) e.getSource()).setText(DEPARTURE_PLACEHOLDER);
                    }
                }

                if (e.getSource().equals(arrivalField)) {
                    if (((JTextField) e.getSource()).getText().isEmpty()) {
                        ((JTextField) e.getSource()).setForeground(Color.BLACK);
                        ((JTextField) e.getSource()).setText(ARRIVAL_PLACEHOLDER);
                    }
                }
            }
        }


        arrivalField.addFocusListener(new PlaceholderFocus());
        departureField.addFocusListener(new PlaceholderFocus());

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String arrivalTime;
            String arrival;
            String departureTime;
            String departure;

            if (departureField.getText().equals(DEPARTURE_PLACEHOLDER)) {
                JOptionPane.showMessageDialog(this, "Please enter the departure airport.","Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (arrivalField.getText().equals(ARRIVAL_PLACEHOLDER)) {
                JOptionPane.showMessageDialog(this, "Please enter the arrival airport.","Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (datePicker.getJFormattedTextField().getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the departure date.","Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (datePicker2.getJFormattedTextField().getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the arrival date.","Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            arrival = arrivalField.getText();
            departure = departureField.getText();
            departureField.setText(DEPARTURE_PLACEHOLDER);
            arrivalField.setText(ARRIVAL_PLACEHOLDER);
            arrivalTime = datePicker.getJFormattedTextField().getText();
            departureTime = datePicker2.getJFormattedTextField().getText();
            datePicker.getJFormattedTextField().setText("");
            datePicker2.getJFormattedTextField().setText("");

            System.out.println(arrival);
            System.out.println(arrivalTime);
            System.out.println(departure);
            System.out.println(departureTime);

        });


        columnPanel1.add(returnFlight);
        columnPanel1.add(departureField);
        columnPanel1.add(datePicker,BorderLayout.CENTER);

        columnPanel2.add(oneWayFlight);
        columnPanel2.add(arrivalField);
        columnPanel2.add(datePicker2,BorderLayout.CENTER);


        columnPanel3.add(submitButton);
        add(flightSelectionPanel, BorderLayout.PAGE_START);
        setSize(871,504);

    }


    //    Date label formatter will format the Object to a String
    private static class DateLabelFormatter extends AbstractFormatter {

        private final String datePattern = "yyyy-MM-dd";
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }

            return "";
        }

    }

//    Main
    public static void main(String[] args) {
        testUI UI = new testUI();
        UI.setVisible(true);
        UI.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
