package com.flights.gui;

import com.flights.objects.Flight;
import com.flights.util.FileUtilities;
import com.flights.util.FlightsConstants;
import com.flights.util.JErrorDialog;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

public class MainWindow extends JPanel  implements ItemListener, FlightsConstants{
    public static final JFrame frame = new JFrame();
    public static final int FRAME_WIDTH = 1350;
    public static final int FRAME_HEIGHT = 800;
    JPanel northPanel;
    JPanel logoPanel;
    JPanel toolbarAndSelection;
    JRadioButton returnFlight;
    JDatePickerImpl datePickerArrival;
    JDatePickerImpl datePicker;
    JLabel arrivalLabel;
    int[] passArray = {1, 0, 0, 0};

    public MainWindow() {
        super(new BorderLayout());
        setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.X_AXIS));
        northPanel.setPreferredSize(new Dimension(1300, 400));
        northPanel.setBackground(FlightsConstants.SEAGREEN);

        logoPanel = new JPanel(new BorderLayout());
        logoPanel.setBackground(FlightsConstants.DARKSPRINGGREEN);
        logoPanel.setMaximumSize(new Dimension(300, 400));
        logoPanel.setMinimumSize(new Dimension(300, 400));
        logoPanel.setPreferredSize(new Dimension(300, 400));

        BufferedImage logo = FileUtilities.loadImage("/com/flights/gui/images/logo.png");
        assert logo != null;
        logoPanel.add(new JLabel(new ImageIcon(logo)));

        toolbarAndSelection = new JPanel(new BorderLayout());
        toolbarAndSelection.setBackground(FlightsConstants.SEAGREEN);

        JPanel toolBarPanel = new JTopBar();

        JPanel mainSelectionPanel = new JPanel();
        mainSelectionPanel.setLayout(new BoxLayout(mainSelectionPanel, BoxLayout.X_AXIS));
        mainSelectionPanel.setBackground(FlightsConstants.SEAGREEN);
        mainSelectionPanel.setBorder(BorderFactory.createMatteBorder(0,3,0,0,FlightsConstants.MAIZE));

        JPanel departurePanel = new JPanel();
        departurePanel.setLayout(new BoxLayout(departurePanel, BoxLayout.Y_AXIS));
        departurePanel.setOpaque(false);
        departurePanel.setPreferredSize(new Dimension(420,360));
        departurePanel.setMaximumSize(new Dimension(500,360));
        departurePanel.setMinimumSize(new Dimension(420,360));

        JPlaceHolderTextField departureField = new JPlaceHolderTextField(FlightsConstants.DEPARTURE_PLACEHOLDER, 20);

        departureField.setPreferredSize(new Dimension(300,55));
        departureField.setFont(new Font("Arial", Font.PLAIN, 25));
        departureField.setMaximumSize(new Dimension(300,55));


        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model, new Properties());
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        datePicker.setPreferredSize(new Dimension(300,60));
        datePicker.setMaximumSize(new Dimension(300,60));
        datePicker.getJFormattedTextField().setPreferredSize(new Dimension(300,60));
        datePicker.getJFormattedTextField().setFont(new Font("Arial", Font.PLAIN, 25));
        datePicker.getJFormattedTextField().requestFocusInWindow();
        datePicker.addAncestorListener(new RequestFocusListener(false));
        datePicker.getJFormattedTextField().setHorizontalAlignment(JTextField.CENTER);

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        radioPanel.setMinimumSize(new Dimension(300,80));
        radioPanel.setMaximumSize(new Dimension(500,80));
        radioPanel.setOpaque(false);

        // create a button group for the radio buttons
        ButtonGroup flightType = new ButtonGroup();

        // TODO add custom icons to radio buttons to make them larger
        returnFlight = new JRadioButton("Return Flight");
        returnFlight.setSelected(true);     // selected by default
        returnFlight.setBackground(FlightsConstants.SEAGREEN);
        returnFlight.setForeground(Color.WHITE);
        returnFlight.setFont(ARIAL20);
        returnFlight.setFocusable(false);

        JRadioButton oneWayFlight = new JRadioButton("One Way Flight");
        oneWayFlight.setBackground(FlightsConstants.SEAGREEN);
        oneWayFlight.setForeground(Color.WHITE);
        oneWayFlight.setFocusable(false);
        oneWayFlight.setFont(ARIAL20);

        // add the item listener to check if the radio selection is changed
        returnFlight.addItemListener(this);
        oneWayFlight.addItemListener(this);

        JLabel departureLabel = new JLabel("Departure date");
        departureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        departureLabel.setForeground(Color.WHITE);
        departureLabel.setFont(ARIAL20);
        departureLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        radioPanel.add(returnFlight);
        radioPanel.add(oneWayFlight);
        flightType.add(returnFlight);
        flightType.add(oneWayFlight);

        departureField.setFocusable(false);
        departureField.setFocusable(true);

        departurePanel.add(Box.createVerticalGlue());
        departurePanel.add(radioPanel);
        departurePanel.add(departureField);
        departurePanel.add(Box.createVerticalStrut(35));
        departurePanel.add(departureLabel);
        departurePanel.add(datePicker);
        departurePanel.add(Box.createVerticalStrut(45));


        // Arrival Panel
        JPanel arrivalPanel = new JPanel();
        arrivalPanel.setOpaque(false);
        arrivalPanel.setMinimumSize(new Dimension(300,360));
        arrivalPanel.setPreferredSize(new Dimension(400,360));
        arrivalPanel.setMaximumSize(new Dimension(500,360));
        arrivalPanel.setLayout(new BoxLayout(arrivalPanel,BoxLayout.Y_AXIS));

        JPlaceHolderTextField arrivalField = new JPlaceHolderTextField(FlightsConstants.ARRIVAL_PLACEHOLDER, 20);
        arrivalField.setPreferredSize(new Dimension(300,55));
        arrivalField.setFont(new Font("Arial", Font.PLAIN, 25));
        arrivalField.setMaximumSize(new Dimension(300,55));
        arrivalField.setMinimumSize(new Dimension(300,55));

        arrivalLabel = new JLabel("Arrival date");
        arrivalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        arrivalLabel.setForeground(Color.WHITE);
        arrivalLabel.setFont(ARIAL20);
        arrivalLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        UtilDateModel model2 = new UtilDateModel();
        JDatePanelImpl datePanelArrival = new JDatePanelImpl(model2, new Properties());
        datePickerArrival = new JDatePickerImpl(datePanelArrival, new DateLabelFormatter());
        datePickerArrival.setPreferredSize(new Dimension(400,60));
        datePickerArrival.setMaximumSize(new Dimension(300,60));
        datePickerArrival.setMinimumSize(new Dimension(400,60));

        datePickerArrival.getJFormattedTextField().setPreferredSize(new Dimension(300,60));
        datePickerArrival.getJFormattedTextField().setFont(new Font("Arial", Font.PLAIN, 25));
        datePickerArrival.getJFormattedTextField().requestFocusInWindow();
        datePickerArrival.addAncestorListener(new RequestFocusListener(false));
        datePickerArrival.getJFormattedTextField().setHorizontalAlignment(JTextField.CENTER);

        arrivalPanel.add(Box.createVerticalStrut(122));
        arrivalPanel.add(arrivalField);
        arrivalPanel.add(Box.createVerticalStrut(35));
        arrivalPanel.add(arrivalLabel);
        arrivalPanel.add(datePickerArrival);


        // Search Panel
        JLayeredPane searchPanel = new JLayeredPane();
        searchPanel.setLayout(new BoxLayout(searchPanel,BoxLayout.Y_AXIS));
        searchPanel.setOpaque(false);
        searchPanel.setMinimumSize(new Dimension(300,360));
        searchPanel.setPreferredSize(new Dimension(300,360));
        searchPanel.setMaximumSize(new Dimension(500,360));

        JLabel passengerLabel = new JLabel("Passengers");
        passengerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passengerLabel.setForeground(Color.WHITE);
        passengerLabel.setFont(ARIAL20);

        JButton passengerBtn = new JButton("1 Passenger");
        passengerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        passengerBtn.setFocusable(false);
        passengerBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        passengerBtn.setMinimumSize(new Dimension(200, 60));
        passengerBtn.setPreferredSize(new Dimension(200, 60));
        passengerBtn.setMaximumSize(new Dimension(200, 60));
        passengerBtn.setBackground(Color.WHITE);

        PopupFactory popupFactory = new PopupFactory();
        PassengerAddPanel selectPassengerPanel = new PassengerAddPanel(passArray);
//        selectPassengerPanel.setBorder(BorderFactory.createLineBorder(FlightsConstants.RICHBLACK));
        AtomicReference<Popup> p = new AtomicReference<>(popupFactory.getPopup(frame, selectPassengerPanel, 0, 0));
        selectPassengerPanel.getDoneButton().addActionListener(e -> {
            p.get().hide();
            passArray = selectPassengerPanel.getPassType();
            if (selectPassengerPanel.getPassCount() == 1) {
                passengerBtn.setText(selectPassengerPanel.getPassCount() + " Passenger");
            } else {
                passengerBtn.setText(selectPassengerPanel.getPassCount() + " Passengers");
            }
            p.set(popupFactory.getPopup(frame, selectPassengerPanel, passengerBtn.getLocationOnScreen().x, passengerBtn.getLocationOnScreen().y));
        });
        passengerBtn.addActionListener(e -> {
            p.get().hide();
            p.set(popupFactory.getPopup(frame, selectPassengerPanel, passengerBtn.getLocationOnScreen().x, passengerBtn.getLocationOnScreen().y));
            p.get().show();
        });


        JButton searchButton = new JButton("Search");
        searchButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        searchButton.setBackground(FlightsConstants.MAIZE);
        searchButton.setForeground(FlightsConstants.RICHBLACK);
        searchButton.setFont(new Font("Arial", Font.BOLD, 30));
        searchButton.setPreferredSize(new Dimension(250,60));
        searchButton.setMinimumSize(new Dimension(250,60));
        searchButton.setMaximumSize(new Dimension(250,60));
        searchButton.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        searchButton.setFocusable(false);


        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                searchButton.setBackground(searchButton.getBackground().darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                searchButton.setBackground(FlightsConstants.MAIZE);
            }
        });
        searchButton.addActionListener(e -> {
            String arrivalTime;
            String arrival;
            String departureTime = "";
            String departure;

            // check if any of the fields are empty
            if (departureField.getText().equals(FlightsConstants.DEPARTURE_PLACEHOLDER)) {
                JErrorDialog.showWarning("Please enter the departure airport");
                return;
            }
            if (arrivalField.getText().equals(FlightsConstants.ARRIVAL_PLACEHOLDER)) {
                JErrorDialog.showWarning("Please enter the arrival airport");
                return;
            }
            if (datePicker.getJFormattedTextField().getText().isEmpty()) {
                JErrorDialog.showWarning("Please enter the departure date");
                return;
            }
            // check first if the return flight is selected before checking if its empty
            if (returnFlight.isSelected()) {
                if (datePickerArrival.getJFormattedTextField().getText().isEmpty()) {
                    JErrorDialog.showWarning("Please enter the arrival date");
                    return;
                }
                departureTime = datePickerArrival.getJFormattedTextField().getText();
                datePickerArrival.getJFormattedTextField().setText("");
            } 

            // store the field data and clear them after submitting
            arrival = arrivalField.getText();
            departure = departureField.getText();
            departureField.setText(FlightsConstants.DEPARTURE_PLACEHOLDER);
            arrivalField.setText(FlightsConstants.ARRIVAL_PLACEHOLDER);
            arrivalTime = datePicker.getJFormattedTextField().getText();
            datePicker.getJFormattedTextField().setText("");

            // TODO delete print statements for debugging :D
            System.out.println(arrival);
            System.out.println(arrivalTime);
            System.out.println(departure);
            System.out.println(departureTime);

            Flight f = new Flight(departure,arrival,arrivalTime);
            createAndShowGUI(new FlightSelection(f, returnFlight.isSelected(), departureTime, passArray));
        });

        // button to panel
        searchPanel.add(Box.createVerticalGlue());
        searchPanel.add(passengerLabel);
        searchPanel.add(passengerBtn);
        searchPanel.add(Box.createVerticalStrut(75));
        searchPanel.add(searchButton);
        searchPanel.add(Box.createVerticalStrut(45));

        // add to main selection panel
        mainSelectionPanel.add(departurePanel);
        mainSelectionPanel.add(arrivalPanel);
        mainSelectionPanel.add(searchPanel);

        toolbarAndSelection.add(toolBarPanel, BorderLayout.PAGE_START);
        toolbarAndSelection.add(mainSelectionPanel,BorderLayout.SOUTH);

        northPanel.add(logoPanel);
        northPanel.add(toolbarAndSelection);
        add(northPanel, BorderLayout.NORTH);
        add(new JPanel(), BorderLayout.CENTER);
    }
    //  checks which radio button is selected
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == returnFlight) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                datePickerArrival.setVisible(true);
                arrivalLabel.setVisible(true);
            }
        }
        else {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                // hide the return flight date if one way flight is selected
                datePickerArrival.setVisible(false);
                datePicker.requestFocus();
                arrivalLabel.setVisible(false);
            }
        }
    }
    private static JPanel previousPanel = new MainWindow();
    public static void createAndShowGUI(JPanel panel) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // catch if any panel throws exception e.g. FlightSelection
        try {
            if (!panel.getClass().isInstance(frame.getContentPane())) {
                if (panel instanceof ViewBookingMenu){
                    ((ViewBookingMenu) panel).refreshText();
                }
                previousPanel = (JPanel) frame.getContentPane();
                frame.setContentPane(panel);
                frame.setTitle(panel.getClass().getSimpleName());
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        frame.pack();
        frame.setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        frame.setVisible(true);
    }

    public static void returnToPreviousMenu() {
        createAndShowGUI(previousPanel);
    }

    private static class RequestFocusListener implements AncestorListener {
        private final boolean removeListener;

        public RequestFocusListener(boolean removeListener) {
            this.removeListener = removeListener;
        }

        @Override
            public void ancestorAdded(AncestorEvent e) {
                JComponent component = e.getComponent();
                component.requestFocusInWindow();

                if (removeListener)
                    component.removeAncestorListener(this);
            }

            @Override
            public void ancestorMoved(AncestorEvent e) {
            }

            @Override
            public void ancestorRemoved(AncestorEvent e) {
            }
        }

    //    Date label formatter will format the Object to a String
    private static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

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
}
