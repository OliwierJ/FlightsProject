package com.flights;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.swing.JFormattedTextField.AbstractFormatter;

public class testUI extends JFrame{
    
    JPanel flightSelectionPanel;
    testUI () {
        setLayout(new BorderLayout());
        flightSelectionPanel = new JPanel();
        flightSelectionPanel.setLayout(new GridLayout(1,3,10,10 ));
        flightSelectionPanel.setBackground(new Color(0x02610e));

        flightSelectionPanel.setPreferredSize(new Dimension(871, 187));


        JPanel columnPanel1 = new JPanel();
        JPanel columnPanel2 = new JPanel(new BorderLayout());
        JPanel columnPanel3= new JPanel();
        columnPanel1.setOpaque(false);
        columnPanel2.setOpaque(false);
        columnPanel3.setOpaque(false);
        flightSelectionPanel.add(columnPanel1);
        flightSelectionPanel.add(columnPanel2);
        flightSelectionPanel.add(columnPanel3);

//        creates the date panel, and picker objects
        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model, new Properties());
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());


        columnPanel2.add(datePicker,BorderLayout.CENTER);



        add(flightSelectionPanel, BorderLayout.PAGE_START);
        setSize(871,504);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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
    }
}
