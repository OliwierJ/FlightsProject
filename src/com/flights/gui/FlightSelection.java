package com.flights.gui;


import com.flights.objects.Flight;
import com.flights.util.FlightsConstants;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;


public class FlightSelection extends JPanel implements FlightsConstants {
    static JFrame frame = new JFrame();
    final int[] selectedDateIndex = {0};
    JLabel departureLabel = new JLabel("Departure Flight");
    int dateWidth = 125;
    int dateCount = 25;
    JLabel returnLabel = new JLabel("Return Flight");
    int[] passengerTypes;
    SelectedFlight selectedFlightR;

    public FlightSelection(Flight defaultFlight, boolean showReturns, String returnFlightDate, int[] passArray) {
        setPreferredSize(new Dimension(1300, 800));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.passengerTypes = passArray;
        departureLabel.setFont(new Font("Arial", Font.BOLD, 20));
        departureLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        departureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel datesBox = new JPanel();
        datesBox.setMaximumSize(new Dimension(900, 90));
        datesBox.setLayout(new BoxLayout(datesBox, BoxLayout.X_AXIS));

        // Dublin Barcelona 2024-12-7
        System.out.println(defaultFlight.getDepartureDate());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedStartDate = getParsedDate(defaultFlight.getDepartureDate(), sdf);

        JScrollPane datesScroller = new DatesScroller(datesBox);
        SelectedFlight selectedFlight = new SelectedFlight(defaultFlight);

        JPanel[] date = new JPanel[dateCount];
        Flight[] differentFlights = new Flight[dateCount];
        int index = 0;
        for (int j = -12; j <= 12; j++) {

            String newDate = getChangedDate(convertedStartDate, j);
            Flight f = null;
            f = new Flight(defaultFlight.getDepartureAirport(), defaultFlight.getArrivalAirport(), newDate);
            differentFlights[index] = f;
            date[index] = new DateSelections(f, dateWidth, newDate);
            int finalI = index;
            date[index].addMouseListener(new MouseAdapter() {

                @Override
                public void mouseReleased(MouseEvent e) {
                    for (JPanel p : date) {
                        p.setBackground(Color.white);
                    }
                    e.getComponent().setBackground(Color.lightGray);
                    selectedDateIndex[0] = finalI;
                    if (differentFlights[selectedDateIndex[0]] != null) {
                        SwingUtilities.invokeLater(() -> selectedFlight.changeFlight(differentFlights[selectedDateIndex[0]]));
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (selectedDateIndex[0] != finalI) {
                        e.getComponent().setBackground(SELECTEDGRAY);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (selectedDateIndex[0] != finalI) {
                        e.getComponent().setBackground(Color.WHITE);
                    }
                }
            });

            datesBox.add(date[index]);
            index++;
        }
        datesBox.setBorder(BorderFactory.createLineBorder(Color.lightGray, 2));

        add(departureLabel);
        add(datesScroller);
        add(Box.createVerticalStrut(50));
        add(selectedFlight);

        if (showReturns) {
            returnLabel.setFont(new Font("Arial", Font.BOLD, 20));
            returnLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            returnLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JPanel returnDatesBox = new JPanel();
            returnDatesBox.setMaximumSize(new Dimension(900, 90));
            returnDatesBox.setLayout(new BoxLayout(returnDatesBox, BoxLayout.X_AXIS));

            Flight defaultFlightReturn;
            defaultFlightReturn = new Flight(defaultFlight.getArrivalAirport(), defaultFlight.getDepartureAirport(), returnFlightDate );

            Date convertedEndDate = getParsedDate(returnFlightDate,sdf);

            JScrollPane datesScrollerR = new DatesScroller(returnDatesBox);
            selectedFlightR = new SelectedFlight(defaultFlightReturn);

            JPanel[] returnDate = new JPanel[dateCount];
            Flight[] differentFlightsR = new Flight[dateCount];
            int index2 = 0;
            for (int j = -12; j <= 12; j++) {

                String newDate = getChangedDate(convertedEndDate, j);
                Flight f = null;
                try {
                    f = new Flight(defaultFlight.getArrivalAirport(), defaultFlight.getDepartureAirport(), newDate);
                    differentFlightsR[index2] = f;
                } catch (Exception ignored) {
                }
                returnDate[index2] = new DateSelections(f, dateWidth, newDate);
                int finalI = index2;
                returnDate[index2].addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        for (JPanel p : returnDate) {
                            p.setBackground(Color.white);
                        }
                        e.getComponent().setBackground(Color.lightGray);
                        selectedDateIndex[0] = finalI;
                        if (differentFlightsR[selectedDateIndex[0]] != null) {
                            SwingUtilities.invokeLater(() -> selectedFlightR.changeFlight(differentFlightsR[selectedDateIndex[0]]));

                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (selectedDateIndex[0] != finalI) {
                            e.getComponent().setBackground(new Color(0xCEC7C7));
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        if (selectedDateIndex[0] != finalI) {
                            e.getComponent().setBackground(Color.WHITE);
                        }
                    }
                });

                returnDatesBox.add(returnDate[index2]);
                index2++;
            }
            returnDatesBox.setBorder(BorderFactory.createLineBorder(Color.lightGray, 2));

            add(Box.createVerticalStrut(50));
            add(returnLabel);
            add(datesScrollerR);
            add(Box.createVerticalStrut(50));
            add(selectedFlightR);
        } else {
            add(Box.createVerticalStrut(500));
        }

        JButton selectFlights = new JButton("Select Flights");
        selectFlights.setBorder(new EmptyBorder(5,20,5,20));
        selectFlights.setBackground(MAIZE);
        selectFlights.setFocusable(false);
        selectFlights.setFont(new Font("Arial", Font.PLAIN, 18));
        selectFlights.setAlignmentX(CENTER_ALIGNMENT);
        selectFlights.addActionListener(e -> {
            if (!showReturns) {
                MainWindow.createAndShowGUI(new FlightFareSelectionMenu(selectedFlight.getFlight(), null, passengerTypes));
            } else {
                MainWindow.createAndShowGUI(new FlightFareSelectionMenu(selectedFlight.getFlight(), selectedFlightR.getFlight(), passengerTypes));
            }
        });
        add(selectFlights);
    }

    private static void centerScrollBarThumb(JScrollBar scrollBar) {
        // Calculate the center value
        int extent = scrollBar.getModel().getExtent();
        int range = scrollBar.getMaximum() - scrollBar.getMinimum();
        int centerValue = (range - extent) / 2;

        // Set the scrollbar's value to the center value
        scrollBar.setValue(centerValue);
    }

    private Date getParsedDate(String unformattedDate, SimpleDateFormat sdf) {
        try {
            return sdf.parse(unformattedDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    private String getChangedDate(Date date, int j) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, j);
        return sdf.format(cal.getTime());
    }

    private static class PriceAndSelectBtn extends JPanel {
        JLabel priceLabel = new JLabel("Starting from ");
        JLabel flightPriceLabel = new JLabel("€65");
        JButton selectFlightBtn = new JButton("Select");

        public PriceAndSelectBtn(Flight f) {
            super();
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setMaximumSize(new Dimension(250, 175));
            setBackground(SEAGREEN);
            setBorder(BorderFactory.createEmptyBorder(12, 5, 5, 5));

            priceLabel.setFont(new Font("Arial", Font.BOLD, 15));
            priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            priceLabel.setForeground(Color.WHITE);

            flightPriceLabel.setFont(new Font("Arial", Font.BOLD, 35));
            flightPriceLabel.setText("€" + f.getBasePrice());
            flightPriceLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            flightPriceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            flightPriceLabel.setForeground(Color.white);

            selectFlightBtn.setFont(new Font("Arial", Font.BOLD, 18));
            selectFlightBtn.setFocusable(false);
            selectFlightBtn.setBackground(MAIZE);
            selectFlightBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            selectFlightBtn.setMaximumSize(new Dimension(125, 50));

            add(priceLabel);
            add(flightPriceLabel);
            add(Box.createVerticalStrut(10));
            add(selectFlightBtn);
        }
    }

    static class TimeAndPlacePanel extends JPanel {
        JLabel timeLabel;
        JLabel place;

        public TimeAndPlacePanel(Flight f, boolean isDep) {
            super();
            this.setBackground(Color.WHITE);
            this.setMaximumSize(new Dimension(200, 175));
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            // absolutely disgusting ternary statement, but it saves on so many lines so idc
            timeLabel = new JLabel(isDep ? f.getDepartureTime() : f.getArrivalTime());
            timeLabel.setFont(new Font("Arial", Font.BOLD, 40));
            timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            place = new JLabel(isDep ? f.getDepartureAirport() : f.getArrivalAirport());
            place.setFont(new Font("Arial", Font.BOLD, 30));
            place.setAlignmentX(Component.CENTER_ALIGNMENT);

            add(Box.createVerticalStrut(45));
            add(timeLabel, BorderLayout.CENTER);
            add(place, BorderLayout.SOUTH);
        }
    }

    static class FlightDurationPanel extends JPanel {
        JLabel line;
        JLabel durationLabel = new JLabel();

        public FlightDurationPanel(Flight f1) {
            super();
            setBackground(Color.WHITE);
            setMaximumSize(new Dimension(250, 175));
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            line = new JLabel("________");
            line.setFont(new Font("Arial", Font.BOLD, 50));
            line.setAlignmentX(Component.CENTER_ALIGNMENT);

            int duration = (int) f1.getFlightDuration();

            durationLabel.setText(Duration.ofMinutes(duration).toString().substring(2));

            durationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            durationLabel.setFont(new Font("Arial", Font.BOLD, 20));

            add(Box.createVerticalStrut(15));
            add(line);
            add(Box.createVerticalStrut(15));
            add(durationLabel);

        }
    }

    private static class SelectedFlight extends JPanel {
        JPanel priceAndSelectPanel;
        JPanel depTimeAndPlace;
        JPanel flightDuration;
        JPanel arrTimeAndPlace;
        Flight flight;

        public SelectedFlight(Flight f) {
            super();
            flight = f;
            setBackground(Color.WHITE);
            this.setBorder(BorderFactory.createLineBorder(Color.lightGray, 2));
            this.setMaximumSize(new Dimension(900, 175));
            this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

            addSelectedFlightsComponents(f);
        }

        public Flight getFlight() {
            return flight;
        }

        public void changeFlight(Flight f) {
            removeAll();
            addSelectedFlightsComponents(f);
            updateUI();
        }

        private void addSelectedFlightsComponents(Flight f) {
            priceAndSelectPanel = new PriceAndSelectBtn(f);
            depTimeAndPlace = new TimeAndPlacePanel(f, true);
            flightDuration = new FlightDurationPanel(f);
            arrTimeAndPlace = new TimeAndPlacePanel(f, false);
            add(priceAndSelectPanel);
            add(depTimeAndPlace);
            add(flightDuration);
            add(arrTimeAndPlace);
        }
    }

    static class DateSelections extends JPanel {
        JLabel dateLabel;
        JLabel costLabel;
        int height = 85;

        public DateSelections(Flight f, int dateWidth, String newDate) {
            super();
            setMaximumSize(new Dimension(dateWidth, height));
            setMinimumSize(new Dimension(dateWidth, height));
            setPreferredSize(new Dimension(dateWidth, height));
            setBorder(BorderFactory.createLineBorder(Color.black, 2));
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(Color.white);
            dateLabel = new JLabel(newDate);
            dateLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            costLabel = new JLabel(f == null ? "" : "€"+f.getBasePrice());
            costLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            costLabel.setFont(new Font("Arial", Font.PLAIN, 20));

            add(dateLabel);
            add(costLabel);
        }
    }

    class DatesScroller extends JScrollPane {
        public DatesScroller(Container c) {
            super(c);
            JScrollBar hBar = this.getHorizontalScrollBar();
            this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
            this.setMaximumSize(new Dimension(900, 90));
            hBar.setUnitIncrement(dateWidth);

            SwingUtilities.invokeLater(() -> centerScrollBarThumb(hBar));

            hBar.setBlockIncrement(dateWidth);
        }
    }
}