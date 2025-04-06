package com.flights.gui;

import com.flights.Main;
import com.flights.gui.components.JPlaceHolderTextField;
import com.flights.gui.components.JTopBar;
import com.flights.gui.components.RoundButton;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

import static com.flights.Main.frame;

/**
 * Main Menu of the flights project
 */
public class MainWindow extends JPanel  implements ItemListener, FlightsConstants{
    private final JRadioButton returnFlight;
    private final JDatePickerImpl datePickerArrival;
    private final JDatePickerImpl datePicker;
    private final JLabel arrivalLabel;
    private final JPlaceHolderTextField departureField;
    private final JPlaceHolderTextField arrivalField;
    private int[] passArray = {1, 0, 0, 0};
    private final String DEPARTURE_PLACEHOLDER = "Enter departure airport";
    private final String ARRIVAL_PLACEHOLDER = "Enter arrival airport";

    /**
     * Construct a new MainWindow JPanel
     */
    public MainWindow() {
        super(new BorderLayout());
        setPreferredSize(Main.getFrameSize());
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.X_AXIS));
        northPanel.setPreferredSize(new Dimension(1300, 400));
        northPanel.setBackground(SEAGREEN);

        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setBackground(DARKSPRINGGREEN);
        logoPanel.setMaximumSize(new Dimension(300, 400));
        logoPanel.setMinimumSize(new Dimension(300, 400));
        logoPanel.setPreferredSize(new Dimension(300, 400));

        logoPanel.add(new JLabel(new ImageIcon(Objects.requireNonNull(FileUtilities.loadImage("/com/flights/gui/images/logo.png")))));

        JPanel toolbarAndSelection = new JPanel(new BorderLayout());
        toolbarAndSelection.setBackground(SEAGREEN);

        JPanel toolBarPanel = new JTopBar();

        JPanel mainSelectionPanel = new JPanel();
        mainSelectionPanel.setLayout(new BoxLayout(mainSelectionPanel, BoxLayout.X_AXIS));
        mainSelectionPanel.setBackground(SEAGREEN);
        mainSelectionPanel.setBorder(BorderFactory.createMatteBorder(0,3,0,0,MAIZE));

        JPanel departurePanel = new JPanel();
        departurePanel.setLayout(new BoxLayout(departurePanel, BoxLayout.Y_AXIS));
        departurePanel.setOpaque(false);
        departurePanel.setPreferredSize(new Dimension(420,360));
        departurePanel.setMaximumSize(new Dimension(500,360));
        departurePanel.setMinimumSize(new Dimension(420,360));

        departureField = new JPlaceHolderTextField(DEPARTURE_PLACEHOLDER, 20);

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

        returnFlight = new JRadioButton("Return Flight");
        returnFlight.setSelected(true);     // selected by default
        returnFlight.setBackground(SEAGREEN);
        returnFlight.setForeground(Color.WHITE);
        returnFlight.setFont(ARIAL20);
        returnFlight.setFocusable(false);

        JRadioButton oneWayFlight = new JRadioButton("One Way Flight");
        oneWayFlight.setBackground(SEAGREEN);
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

        arrivalField = new JPlaceHolderTextField(ARRIVAL_PLACEHOLDER, 20);
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


        JButton searchButton = new SubmitButton("Search");

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
    /**
     * This checks which radio button is selected
     * @param e the event to be processed
     */
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

    private record RequestFocusListener(boolean removeListener) implements AncestorListener {
        @Override
        public void ancestorAdded(AncestorEvent e) {
            JComponent component = e.getComponent();
            component.requestFocusInWindow();
            if (removeListener) component.removeAncestorListener(this);
        }

        @Override
        public void ancestorMoved(AncestorEvent e) {}
        @Override
        public void ancestorRemoved(AncestorEvent e) {}
    }

    private class SubmitButton extends JButton implements ActionListener, MouseListener{
        public SubmitButton(String text) {
            super(text);
            setAlignmentX(Component.CENTER_ALIGNMENT);
            setBackground(MAIZE);
            setForeground(RICHBLACK);
            setFont(new Font("Arial", Font.BOLD, 30));
            setPreferredSize(new Dimension(250,60));
            setMinimumSize(new Dimension(250,60));
            setMaximumSize(new Dimension(250,60));
            setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
            setFocusable(false);
            addMouseListener(this);
            addActionListener(this);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            setBackground(getBackground().darker());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setBackground(MAIZE);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (departureField.getText().equals(DEPARTURE_PLACEHOLDER)) {
                JOptionPane.showMessageDialog(frame, "Please enter the departure airport.","Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (arrivalField.getText().equals(ARRIVAL_PLACEHOLDER)) {
                JOptionPane.showMessageDialog(frame, "Please enter the arrival airport.","Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (datePicker.getJFormattedTextField().getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter the departure date.","Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // check first if the return flight is selected before checking if its empty
            String departureTime = "";
            if (returnFlight.isSelected()) {
                if (datePickerArrival.getJFormattedTextField().getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter the arrival date.","Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                departureTime = datePickerArrival.getJFormattedTextField().getText();
                datePickerArrival.getJFormattedTextField().setText("");
            }

            // store the field data and clear them after submitting
            String departure = departureField.getText();
            String arrival = arrivalField.getText();
            departureField.setText(DEPARTURE_PLACEHOLDER);
            arrivalField.setText(ARRIVAL_PLACEHOLDER);
            String arrivalTime = datePicker.getJFormattedTextField().getText();
            datePicker.getJFormattedTextField().setText("");

            try {
                Flight f = new Flight(departure,arrival,arrivalTime);
                Main.createAndShowGUI(new FlightSelection(f, returnFlight.isSelected(), departureTime, passArray));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame,"An error has occurred " + ex.getMessage() + " " + ex.getCause());
            }
        }

        public void mouseClicked(MouseEvent e) {}
        public void mousePressed(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
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

    private static class PassengerAddPanel extends JPanel implements FlightsConstants{
        int[] passType;
        int passCount = 0;
        JButton doneButton;

        PassengerAddPanel(int[] passType) {
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
            JPanel adultPanel = new JPanel();
            adultPanel.setLayout(new BoxLayout(adultPanel, BoxLayout.X_AXIS));
            JPanel teenPanel = new JPanel();
            teenPanel.setLayout(new BoxLayout(teenPanel, BoxLayout.X_AXIS));
            JPanel childPanel = new JPanel();
            childPanel.setLayout(new BoxLayout(childPanel, BoxLayout.X_AXIS));
            JPanel infantPanel = new JPanel();
            infantPanel.setLayout(new BoxLayout(infantPanel, BoxLayout.X_AXIS));
            JPanel southPanel = new JPanel();
            southPanel.setBackground(APPLEGREEN);
            southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.X_AXIS));
            doneButton = new JButton("Done");
            JLabel adultCount = new JLabel(passType[0] + "");
            adultCount.setFont(ARIAL20PLAIN);
            JLabel teenCount = new JLabel(passType[1] + "");
            teenCount.setFont(ARIAL20PLAIN);
            JLabel childCount = new JLabel(passType[2] + "");
            childCount.setFont(ARIAL20PLAIN);
            JLabel infantCount = new JLabel(passType[3] + "");
            infantCount.setFont(ARIAL20PLAIN);
            JLabel adultLabel = new JLabel("Adults 16+");
            adultLabel.setFont(ARIAL20PLAIN);
            JLabel teenLabel = new JLabel("Teens 12 - 15");
            teenLabel.setFont(ARIAL20PLAIN);
            JLabel childLabel = new JLabel("Children 3 - 11");
            childLabel.setFont(ARIAL20PLAIN);
            JLabel infantLabel = new JLabel("Infants 0 - 2");
            infantLabel.setFont(ARIAL20PLAIN);

            RoundButton increase1 = new RoundButton(true);
            increase1.setForeground(SEAGREEN);
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
            increase2.setForeground(SEAGREEN);
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
            increase3.setForeground(SEAGREEN);
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
            increase4.setForeground(SEAGREEN);
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
            doneButton.setForeground(TRUEBLUE);
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
}
