package com.flights.gui;
import com.flights.Main;
import com.flights.gui.components.JSubmitButton;
import com.flights.gui.components.JTopBar;
import com.flights.objects.Flight;
import com.flights.util.FlightsConstants;
import com.flights.util.JErrorDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.time.LocalDate;

/**
 * FlightSelection is a GUI component that allows users to select flights for a trip.
 * It displays available departure and return flights along with their prices,
 * allowing users to choose suitable dates and proceed to fare selection.
 */

public class FlightSelection extends JPanel implements FlightsConstants {
    private final int[] selectedDateIndex = {0};
    private final int[] passengerTypes;
    private SelectedFlight selectedFlightReturnPanel;
    private double depPrice = 0;
    private double retPrice = 0;
    private double price = 0;
    private Flight selectedFlight = null;
    private Flight selectedReturnFlight = null;
    private final JTopBar top = new JTopBar();

    /**
     * Constructs a FlightSelection panel with options for departure and return flights.
     *
     * @param defaultFlight   The default departure flight.
     * @param showReturns     Whether to show return flight selection.
     * @param returnFlightDate The default return flight date.
     * @param passArray       Array representing passenger types.
     */
    public FlightSelection(Flight defaultFlight, boolean showReturns, String returnFlightDate, int[] passArray) {
        setPreferredSize(Main.getFrameSize());
        setLayout(new BorderLayout());

        this.passengerTypes = passArray;

        JPanel mainPanel = new JPanel();
        JPanel contentPanel = new JPanel();

        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        JLabel departureLabel = new JLabel("Departure Flight");
        departureLabel.setFont(ARIAL20);
        departureLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        departureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel datesBox = new JPanel();
        datesBox.setMaximumSize(new Dimension(900, 90));
        datesBox.setMinimumSize(new Dimension(900, 90));
        datesBox.setLayout(new BoxLayout(datesBox, BoxLayout.X_AXIS));

        LocalDate startDate = defaultFlight.getDepartureLocalDate();

        JScrollPane datesScroller = new DatesScroller(datesBox);
        datesScroller.setPreferredSize(new Dimension(900, 90));
        datesScroller.setMinimumSize(new Dimension(900, 90));
        SelectedFlight selectedFlightPanel = new SelectedFlight(defaultFlight, false);

        int dateCount = 25;
        JPanel[] date = new JPanel[dateCount];
        Flight[] differentFlights = new Flight[dateCount];
        int index = 0;
        for (int j = -12; j <= 12; j++) {
            LocalDate nextDate = startDate.plusDays(j);
            Flight f = null;
            try {
                f = new Flight(defaultFlight.getDepartureAirport(), defaultFlight.getArrivalAirport(), nextDate.toString());
            } catch (Exception ignored) {}
            differentFlights[index] = f;
            date[index] = new DateSelections(f, nextDate.toString());
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
                        SwingUtilities.invokeLater(() -> selectedFlightPanel.changeFlight(differentFlights[selectedDateIndex[0]], false));
                        selectedFlight = differentFlights[selectedDateIndex[0]];

                        price -= depPrice;
                        depPrice = differentFlights[selectedDateIndex[0]].getBasePrice();
                        price += depPrice;
                        top.updatePrice(price);
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

        contentPanel.add(departureLabel);
        contentPanel.add(datesScroller);
        contentPanel.add(Box.createVerticalStrut(30));
        contentPanel.add(selectedFlightPanel);

        if (showReturns) {
            JLabel returnLabel = new JLabel("Return Flight");
            returnLabel.setFont(ARIAL20);
            returnLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            returnLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JPanel returnDatesBox = new JPanel();
            returnDatesBox.setMaximumSize(new Dimension(900, 90));
            returnDatesBox.setMinimumSize(new Dimension(900, 90));

            returnDatesBox.setLayout(new BoxLayout(returnDatesBox, BoxLayout.X_AXIS));

            Flight defaultFlightReturn;
            defaultFlightReturn = new Flight(defaultFlight.getArrivalAirport(), defaultFlight.getDepartureAirport(), returnFlightDate);

            LocalDate returnStartDate = LocalDate.parse(returnFlightDate);

            JScrollPane datesScrollerReturn = new DatesScroller(returnDatesBox);
            datesScrollerReturn.setPreferredSize(new Dimension(900, 90));
            datesScrollerReturn.setMinimumSize(new Dimension(900, 90));
            selectedFlightReturnPanel = new SelectedFlight(defaultFlightReturn, true);

            JPanel[] returnDate = new JPanel[dateCount];
            Flight[] differentFlightsR = new Flight[dateCount];
            int index2 = 0;
            for (int j = -12; j <= 12; j++) {
                LocalDate nextReturnDate = returnStartDate.plusDays(j);
                Flight f = null;
                try {
                    f = new Flight(defaultFlight.getArrivalAirport(), defaultFlight.getDepartureAirport(), nextReturnDate.toString());
                    differentFlightsR[index2] = f;
                } catch (Exception ignored) {
                }
                returnDate[index2] = new DateSelections(f, nextReturnDate.toString());
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
                            SwingUtilities.invokeLater(() -> selectedFlightReturnPanel.changeFlight(differentFlightsR[selectedDateIndex[0]], true));
                            selectedReturnFlight = differentFlightsR[selectedDateIndex[0]];
                            price -= retPrice;
                            retPrice = differentFlightsR[selectedDateIndex[0]].getBasePrice();
                            price += retPrice;
                            top.updatePrice(price);
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

            contentPanel.add(Box.createVerticalStrut(30));
            contentPanel.add(returnLabel);
            contentPanel.add(datesScrollerReturn);
            contentPanel.add(Box.createVerticalStrut(30));
            contentPanel.add(selectedFlightReturnPanel);
        } else {
            contentPanel.add(Box.createVerticalGlue());
        }

        JButton selectFlightsButton = new JSubmitButton("Select Flights");
        selectFlightsButton.setBorder(new EmptyBorder(5,20,5,20));
        selectFlightsButton.setAlignmentX(CENTER_ALIGNMENT);
        selectFlightsButton.addActionListener(e -> {
            if (!showReturns) {
                if (selectedFlight == null) {
                    JErrorDialog.showWarning("Select flight first");
                    return;
                }
                Main.createAndShowGUI(new FlightFareSelectionMenu(selectedFlight, null, passengerTypes, price));
            } else {
                if (selectedFlight == null || selectedReturnFlight == null) {
                    JErrorDialog.showWarning("Select both flights first");
                    return;
                }
                Main.createAndShowGUI(new FlightFareSelectionMenu(selectedFlight, selectedReturnFlight, passengerTypes, price));
            }
        });
        contentPanel.add(Box.createVerticalStrut(40));
        contentPanel.add(selectFlightsButton);
        contentPanel.add(Box.createVerticalStrut(100));

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(top, BorderLayout.NORTH);
        JScrollPane sp = new JScrollPane(contentPanel);
        sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp.getVerticalScrollBar().setUnitIncrement(20);
        mainPanel.add(sp, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }


    private static void centerScrollBarThumb(JScrollBar scrollBar) {
        // Calculate the center value
        int extent = scrollBar.getModel().getExtent();
        int range = scrollBar.getMaximum() - scrollBar.getMinimum();
        int centerValue = (range - extent) / 2;

        // Set the scrollbar's value to the center value
        scrollBar.setValue(centerValue);
    }

    private class PriceAndSelectBtn extends JPanel {
        PriceAndSelectBtn(Flight f, boolean isReturn) {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setMaximumSize(new Dimension(250, 175));
            setBackground(SEAGREEN);
            setBorder(BorderFactory.createEmptyBorder(12, 5, 5, 5));

            JLabel priceLabel = new JLabel("Starting from ");
            JLabel flightPriceLabel = new JLabel("€65");
            JButton selectFlightBtn = new JButton("Select");

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
            selectFlightBtn.addActionListener(e -> {
                if (isReturn) {
                    selectedReturnFlight = f;
                    price -= retPrice;
                    retPrice = f.getBasePrice();
                    price += retPrice;
                    top.updatePrice(price);
                } else {
                    selectedFlight = f;
                    price -= depPrice;
                    depPrice = f.getBasePrice();
                    price += depPrice;
                    top.updatePrice(price);
                }
            });
            add(priceLabel);
            add(flightPriceLabel);
            add(Box.createVerticalStrut(10));
            add(selectFlightBtn);
        }
    }

    private static class TimeAndPlacePanel extends JPanel {
        TimeAndPlacePanel(Flight f, boolean isDep) {
            setBackground(Color.WHITE);
            setMaximumSize(new Dimension(200, 175));
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            // absolutely disgusting ternary statement, but it saves on so many lines so idc
            JLabel timeLabel = new JLabel(isDep ? f.getDepartureTime() : f.getArrivalTime());
            timeLabel.setFont(new Font("Arial", Font.BOLD, 40));
            timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel place = new JLabel(isDep ? f.getDepartureAirport() : f.getArrivalAirport());
            place.setFont(new Font("Arial", Font.BOLD, 30));
            place.setAlignmentX(Component.CENTER_ALIGNMENT);

            add(Box.createVerticalStrut(45));
            add(timeLabel, BorderLayout.CENTER);
            add(place, BorderLayout.SOUTH);
        }
    }

    private static class FlightDurationPanel extends JPanel {
        FlightDurationPanel(Flight f1) {
            setBackground(Color.WHITE);
            setMaximumSize(new Dimension(250, 175));
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            JLabel line = new JLabel("________");
            line.setFont(new Font("Arial", Font.BOLD, 50));
            line.setAlignmentX(Component.CENTER_ALIGNMENT);

            int duration = f1.getFlightDuration();

            JLabel durationLabel = new JLabel();
            durationLabel.setText(Duration.ofMinutes(duration).toString().substring(2));
            durationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            durationLabel.setFont(ARIAL20);

            add(Box.createVerticalStrut(15));
            add(line);
            add(Box.createVerticalStrut(15));
            add(durationLabel);

        }
    }

    private class SelectedFlight extends JPanel {
        SelectedFlight(Flight f, boolean isReturn) {
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(Color.lightGray, 2));
            setMaximumSize(new Dimension(900, 175));
            setPreferredSize(new Dimension(900, 175));
            setMinimumSize(new Dimension(900, 175));
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

            addSelectedFlightsComponents(f, isReturn);
        }


        public void changeFlight(Flight f, boolean isReturn) {
            removeAll();
            addSelectedFlightsComponents(f, isReturn);
            updateUI();
        }

        private void addSelectedFlightsComponents(Flight f, boolean isReturn) {
            JPanel priceAndSelectPanel = new PriceAndSelectBtn(f, isReturn);
            JPanel depTimeAndPlace = new TimeAndPlacePanel(f, true);
            JPanel flightDuration = new FlightDurationPanel(f);
            JPanel arrTimeAndPlace = new TimeAndPlacePanel(f, false);
            add(priceAndSelectPanel);
            add(depTimeAndPlace);
            add(flightDuration);
            add(arrTimeAndPlace);
        }
    }

    private static class DateSelections extends JPanel {
        DateSelections(Flight f, String newDate) {
            int height = 85;
            setMaximumSize(new Dimension(125, height));
            setMinimumSize(new Dimension(125, height));
            setPreferredSize(new Dimension(125, height));
            setBorder(BorderFactory.createLineBorder(Color.black, 2));
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(Color.white);
            JLabel dateLabel = new JLabel(newDate);
            dateLabel.setFont(ARIAL20PLAIN);
            dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel costLabel = new JLabel(f == null ? "" : "€"+f.getBasePrice());
            costLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            costLabel.setFont(ARIAL20PLAIN);

            add(dateLabel);
            add(costLabel);
        }
    }

    private static class DatesScroller extends JScrollPane {
        DatesScroller(Container c) {
            super(c);
            JScrollBar hBar = this.getHorizontalScrollBar();
            this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
            this.setMaximumSize(new Dimension(900, 90));
            int dateWidth = 125;
            hBar.setUnitIncrement(dateWidth);

            SwingUtilities.invokeLater(() -> centerScrollBarThumb(hBar));

            hBar.setBlockIncrement(dateWidth);
        }
    }
}