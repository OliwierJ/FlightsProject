package com.flights.tests;

import com.flights.gui.FlightSelection;
import com.flights.gui.JPlaceHolderTextField;
import com.flights.objects.Flight;
import com.flights.util.FlightsConstants;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatter;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

// start class
// extends JFrame and implements ItemListener for radio buttons
public class testUI extends JPanel implements ItemListener{
    // variables
    private final JRadioButton returnFlight;
    private final String DEPARTURE_PLACEHOLDER = "Enter departure airport";
    private final String ARRIVAL_PLACEHOLDER = "Enter arrival airport";
    private final JDatePickerImpl datePicker2;

    // constructor
    testUI () {
        // set frame layout to BorderLayout
        setLayout(new BorderLayout());
        // The green panel at the top
        JPanel flightSelectionPanel = new JPanel();
        // grid layout with 1 row and 3 columns
        flightSelectionPanel.setLayout(new GridLayout(1,3,10,10 ));
        flightSelectionPanel.setBackground(FlightsConstants.SEAGREEN); // dark green colour
        flightSelectionPanel.setPreferredSize(new Dimension(871, 187));
        
        // create 3 separate column panels for separating the different elements
        JPanel columnPanel1 = new JPanel();
        JPanel columnPanel2 = new JPanel();
        JPanel columnPanel3= new JPanel();
        columnPanel1.setOpaque(false); // set 'opaque' to equal false to make them see through
        columnPanel2.setOpaque(false);
        columnPanel3.setOpaque(false);
        flightSelectionPanel.add(columnPanel1);
        flightSelectionPanel.add(columnPanel2);
        flightSelectionPanel.add(columnPanel3);

        // UtilDateModel is the format of the date
        UtilDateModel model = new UtilDateModel();
        UtilDateModel model2 = new UtilDateModel();
        // DatePanel is the calendar panel that shows up when the 3 dots are pressed.
        // wtf is a I18N string and why do I need one I dont know, new Properties() fixes it
        JDatePanelImpl datePanel = new JDatePanelImpl(model, new Properties());
        JDatePanelImpl datePanel2 = new JDatePanelImpl(model2, new Properties());
        // the actual textfield that contains the date selected and date selector
        // the textfield is not manually editable but can be changed through the 3 dots button on the right side
        // the textfield itself is a JFormattedTextField, so to reference the Object inside the field, use getJFormattedTextField()
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        datePicker2 = new JDatePickerImpl(datePanel2, new DateLabelFormatter());

        // create a button group for the radio buttons
        ButtonGroup flightType = new ButtonGroup();

        returnFlight = new JRadioButton("Return Flight");
        returnFlight.setSelected(true);     // selected by default
        returnFlight.setBackground(FlightsConstants.SEAGREEN);
        returnFlight.setForeground(Color.WHITE);
        returnFlight.setFocusable(false);

        JRadioButton oneWayFlight = new JRadioButton("One Way Flight");
        oneWayFlight.setBackground(FlightsConstants.SEAGREEN);
        oneWayFlight.setForeground(Color.WHITE);
        oneWayFlight.setFocusable(false);

        flightType.add(returnFlight);
        flightType.add(oneWayFlight);

        // add the item listener to check if the radio selection is changed
        returnFlight.addItemListener(this);
        oneWayFlight.addItemListener(this);

        // create the text fields for inputting the airports with custom placeholder
        JPlaceHolderTextField departureField = new JPlaceHolderTextField(DEPARTURE_PLACEHOLDER, 20);
        JPlaceHolderTextField arrivalField = new JPlaceHolderTextField(ARRIVAL_PLACEHOLDER,20);

        // submit button
        JButton submitButton = new JButton("Submit");
        // submit button listener, intelliShit keeps telling to extract it into a method but Im stubborn
        submitButton.addActionListener(e -> {
            String arrivalTime;
            String arrival;
            String departureTime = "";
            String departure;

            // check if any of the fields are empty
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
            // check first if the return flight is selected before checking if its empty
            if (returnFlight.isSelected()) {
                
                if (datePicker2.getJFormattedTextField().getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter the arrival date.","Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                departureTime = datePicker2.getJFormattedTextField().getText();
                datePicker2.getJFormattedTextField().setText("");
            }

            // store the field data and clear them after submitting
            arrival = arrivalField.getText();
            departure = departureField.getText();
            departureField.setText(DEPARTURE_PLACEHOLDER);
            arrivalField.setText(ARRIVAL_PLACEHOLDER);
            arrivalTime = datePicker.getJFormattedTextField().getText();
            datePicker.getJFormattedTextField().setText("");
            
            // print statements for debugging :D
            System.out.println(arrival);
            System.out.println(arrivalTime);
            System.out.println(departure);
            System.out.println(departureTime);

            try {
                Flight f = new Flight(departure,arrival,arrivalTime);
//                createAndShowGUI(new FlightSelection(f, returnFlight.isSelected(), departureTime));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame,"No available flights found!", "No flights found", JOptionPane.WARNING_MESSAGE);
            }

        });

        // add all the components
        columnPanel1.add(returnFlight);
        columnPanel1.add(departureField);
        columnPanel1.add(datePicker);

        columnPanel2.add(oneWayFlight);
        columnPanel2.add(arrivalField);
        columnPanel2.add(datePicker2);


        columnPanel3.add(submitButton);
        add(flightSelectionPanel, BorderLayout.PAGE_START);
        setPreferredSize(new Dimension(900, 800));

    }

    //  checks which radio button is selected
    public void itemStateChanged(ItemEvent e)
    {
        if (e.getSource() == returnFlight) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                datePicker2.setVisible(true);
            }
        }
        else {

            if (e.getStateChange() == ItemEvent.SELECTED) {
                // hide the return flight date if one way flight is selected
                datePicker2.setVisible(false);
            }
        }
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


    static JFrame frame = new JFrame("TestUI");
    public static void createAndShowGUI(JPanel panel) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            frame.setContentPane(panel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        frame.pack();
        frame.setVisible(true);
    }

    //    Main

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {createAndShowGUI(new testUI());});
    }

} // end class
