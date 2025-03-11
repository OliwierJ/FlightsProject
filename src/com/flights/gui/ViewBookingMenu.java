package com.flights.gui;

import com.flights.gui.components.JSubmitButton;
import com.flights.gui.components.JTopBar;
import com.flights.objects.Booking;
import com.flights.objects.Flight;
import com.flights.objects.Passenger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import com.flights.util.FlightsConstants;

public class ViewBookingMenu extends JPanel implements FlightsConstants, ActionListener {
    private final Booking booking;
    private final JPanel passengersCardPanel = new JPanel(new CardLayout());
    private final JButton back = new JSubmitButton("Previous");
    private final JButton next = new JSubmitButton("Next");
    private final JButton update = new JSubmitButton("Update booking");
    private final JButton cancel = new JSubmitButton("Delete booking");
    private final JButton weather = new JSubmitButton("Check weather");
    private int currentPassenger = 0;
    private final JPanel[] passengerDetailsPanels;
    private final Font TITLE_FONT = new Font("Arial", Font.BOLD, 26);

    public ViewBookingMenu(Booking booking) {
        this.booking = booking;
        passengerDetailsPanels = new JPanel[booking.getPassengerCount()];
        setPreferredSize(new Dimension(MainWindow.FRAME_WIDTH, MainWindow.FRAME_HEIGHT));
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("My Booking");
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        back.setEnabled(false);
        back.addActionListener(this);
        next.addActionListener(this);
        cancel.addActionListener(this);
        update.addActionListener(this);
        weather.addActionListener(this);

        JPanel buttons = new JPanel();
        setSizes(buttons, MainWindow.FRAME_WIDTH, 50);
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttons.add(weather);
        buttons.add(update);
        buttons.add(cancel);

        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(title);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(new TopPanel());
        contentPanel.add(Box.createVerticalStrut(30));
        contentPanel.add(new FlightsPanel());
        contentPanel.add(Box.createVerticalStrut(30));
        contentPanel.add(new PassengerPanel());
        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(buttons);

        add(new JTopBar(), BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        setAllFonts(this);
    }

    public void refreshText() {
        JPanel cp = passengerDetailsPanels[currentPassenger];
        Passenger p = booking.getPassengers()[currentPassenger];
        JLabel p2 = (JLabel) cp.getComponent(2);
        JLabel p3 = (JLabel) cp.getComponent(3);
        String p2String;
        if (p.getTitle() == null || p.getTitle().isEmpty()) {
            p2String = p.getName() + " " + p.getSurname();
        } else {
            p2String = p.getTitle() + " " + p.getName() + " " + p.getSurname();
        }
        p2.setText(p2String);
        p3.setText("Departure seat: "+p.getDepartureSeat().getSeatNo() + ", " + p.getDepartureSeat().getSeatClass());
        if (booking.getReturnFlight() != null) {
            JLabel p4 = (JLabel) cp.getComponent(4);
            p4.setText("Return seat: " + p.getReturnSeat().getSeatNo() + ", " + p.getReturnSeat().getSeatClass());
        }
    }

    private void setAllFonts(JPanel p) {
        for (Component c : p.getComponents()) {
            if ((c instanceof JLabel || c instanceof JButton) && c.getFont().equals(DEFAULT_FONT)) {
                c.setFont(ARIAL20);
            } else if (c instanceof JPanel) {
                setAllFonts((JPanel) c);
            } else if (c instanceof JTextField && c.getFont().equals(DEFAULT_FONT_TEXTFIELD)) {
                c.setFont(ARIAL20PLAIN);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == back || e.getSource() == next) {
            if (e.getSource() == back) {
                currentPassenger--;
            } else if (e.getSource() == next) {
                currentPassenger++;
            }
            back.setEnabled(currentPassenger > 0);
            next.setEnabled(currentPassenger != booking.getPassengerCount()-1);
            CardLayout cl = (CardLayout)(passengersCardPanel.getLayout());
            cl.show(passengersCardPanel, String.valueOf(currentPassenger));
        } else if (e.getSource() == cancel) {
            int n = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel your booking?\nThis action is irreversible!", "Are you sure?", JOptionPane.YES_NO_OPTION);
            if (n == 0) {
                booking.deleteEntry();
                JOptionPane.showMessageDialog(this, "Booking cancelled successfully!", "Booking cancelled successfully!", JOptionPane.INFORMATION_MESSAGE);
                MainWindow.createAndShowGUI(new MainWindow());
            }
        } else if (e.getSource() == update) {
            int n = JOptionPane.showConfirmDialog(this, "Are you sure you want to save your booking changes?", "Are you sure?", JOptionPane.YES_NO_OPTION);
            if (n == 0) {
                booking.updateDatabase();
                JOptionPane.showMessageDialog(this, "Booking updated successfully!", "Booking updated successfully!", JOptionPane.INFORMATION_MESSAGE);
                MainWindow.createAndShowGUI(new MainWindow());
            }
        } else if (e.getSource() == weather) {
            // TODO implement check weather
        }
    }

    public static void main(String[] args) {
        MainWindow.createAndShowGUI(new ViewBookingMenu(new Booking("522558", "govie@setu.ie"))); // TODO debug delete later
    }

    private void setSizes(JPanel p, int width, int height) {
        p.setMinimumSize(new Dimension(width, height));
        p.setPreferredSize(new Dimension(width, height));
        p.setMaximumSize(new Dimension(width, height));
    }

    private class TopPanel extends JPanel {
        {
            JPanel title = new JPanel();
            JPanel content = new JPanel();
            setSizes(this, 1000, 160);
            setLayout(new BorderLayout());
            setAlignmentX(Component.CENTER_ALIGNMENT);

            title.setLayout(new FlowLayout(FlowLayout.LEFT));
            content.setLayout(new FlowLayout(FlowLayout.LEFT));
            title.setBackground(APPLEGREEN);
            content.setBackground(ASPARAGUS);


            JPanel text = new JPanel();
            text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
            JLabel t1 = new JLabel("Email: " + booking.getEmail());
            JLabel t2 = new JLabel();
            if (booking.getPriorityBoarding() == 1) {
                t2.setText("Priority boarding included");
            } else {
                t2.setText("Priority boarding not included");
            }
            JLabel t3 = new JLabel("Total luggage: " + booking.getLuggage());
            JLabel t4;
            if (booking.get20kgluggage()) {
                t4 = new JLabel("20kg luggage included");
            } else {
                t4 = new JLabel("20kg luggage not included");
            }
            text.add(t1);
            text.add(t2);
            text.add(t3);
            text.add(t4);

            JPanel buttons = new JPanel();
            buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
            JButton changeEmail = new JSubmitButton("Change email");
            JButton buyPriority = new JSubmitButton("Buy priority boarding");
            JButton buyLuggage = new JSubmitButton("Buy 20kg luggage");

            changeEmail.addActionListener(e -> {
                JPanel popup = new JPanel();
                popup.setLayout(new GridLayout(2, 1));
                JLabel l = new JLabel("Email address:");
                JTextField email = new JTextField(booking.getEmail(), 20);
                popup.add(l);
                popup.add(email);
                setAllFonts(popup);
                String[] options = {"Cancel", "Confirm"};
                int n = JOptionPane.showOptionDialog(MainWindow.frame, popup, "Edit email", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                if (n == 1) {
                    booking.setEmail(email.getText());
                    t1.setText("Email: "+booking.getEmail());
                }
            });

            buyPriority.addActionListener(e -> {
                booking.setPriorityBoarding(1);
                buyPriority.setVisible(false);
                t2.setText("Priority boarding included");
            });
            buyLuggage.addActionListener(e -> {
                booking.set20kgluggage(true);
                buyLuggage.setVisible(false);
                t4.setText("20kg luggage included");
            });

            buttons.add(changeEmail);
            if (booking.getPriorityBoarding() == 0) {
                buttons.add(Box.createVerticalStrut(5));
                buttons.add(buyPriority);
            }
            if (!booking.get20kgluggage()) {
                buttons.add(Box.createVerticalStrut(5));
                buttons.add(buyLuggage);
            }
            text.setOpaque(false);
            buttons.setOpaque(false);

            content.add(Box.createHorizontalStrut(10));
            content.add(text);
            content.add(Box.createHorizontalStrut(100));
            content.add(buttons);

            JLabel t = new JLabel("Booking details (ID "+booking.getBookingID()+"):");
            t.setFont(TITLE_FONT);
            title.add(Box.createHorizontalStrut(10));
            title.add(t);
            add(title, BorderLayout.NORTH);
            add(content, BorderLayout.CENTER);
        }
    }

    private class FlightsPanel extends JPanel {
        {
            JPanel title = new JPanel();
            JPanel content = new JPanel();
            setSizes(this, 1000, 175);
            setLayout(new BorderLayout());
            setAlignmentX(Component.CENTER_ALIGNMENT);

            content.setLayout(null);
            title.setLayout(new FlowLayout(FlowLayout.LEFT));
            title.setBackground(APPLEGREEN);
            content.setBackground(ASPARAGUS);

            JPanel dp = new JPanel();
            dp.setLayout(new BoxLayout(dp, BoxLayout.Y_AXIS));
            Flight depFlight = booking.getDepartureFlight();
            JLabel d1 = new JLabel("<html><u>Departure Flight</u></html>");
            JLabel d2 = new JLabel(depFlight.getDepartureAirport() + " to " + depFlight.getArrivalAirport());
            JLabel d3 = new JLabel("Date: " + depFlight.getDepartureDate().substring(0, depFlight.getDepartureDate().length()-11));
            JLabel d4 = new JLabel("Time: " + depFlight.getDepartureTime() + " - " + depFlight.getArrivalTime());
            dp.add(d1);
            dp.add(Box.createVerticalStrut(10));
            dp.add(d2);
            dp.add(d3);
            dp.add(d4);
            dp.setOpaque(false);
            dp.setBounds(0,0, 500, 150);
            dp.setBorder(new EmptyBorder(15,20,0,0));
            content.add(dp);

            if (booking.getReturnFlight() != null) {
                JPanel rp = new JPanel();
                rp.setLayout(new BoxLayout(rp, BoxLayout.Y_AXIS));
                Flight returnFlight = booking.getReturnFlight();
                JLabel r1 = new JLabel("<html><u>Return Flight</u></html>");
                JLabel r2 = new JLabel(returnFlight.getDepartureAirport() + " to " + returnFlight.getArrivalAirport());
                JLabel r3 = new JLabel("Date: " + returnFlight.getDepartureDate().substring(0, returnFlight.getDepartureDate().length()-11));
                JLabel r4 = new JLabel("Time: " + returnFlight.getDepartureTime() + " - " + returnFlight.getArrivalTime());
                rp.add(r1);
                rp.add(Box.createVerticalStrut(10));
                rp.add(r2);
                rp.add(r3);
                rp.add(r4);
                rp.setOpaque(false);
                rp.setBounds(500, 0, 500, 150);
                rp.setBorder(new EmptyBorder(15,0,0,20));
                content.add(rp);
            }
            JLabel t = new JLabel("Flight details: ");
            t.setFont(TITLE_FONT);
            title.add(Box.createHorizontalStrut(10));
            title.add(t);
            add(title, BorderLayout.NORTH);
            add(content, BorderLayout.CENTER);
        }
    }

    private class PassengerPanel extends JPanel {
        {
            JPanel title = new JPanel();
            JPanel content = new JPanel();
            setSizes(this, 1000, 225);
            setLayout(new BorderLayout());
            setAlignmentX(Component.CENTER_ALIGNMENT);

            content.setLayout(new BorderLayout());
            title.setLayout(new FlowLayout(FlowLayout.LEFT));
            title.setBackground(APPLEGREEN);
            content.setBackground(ASPARAGUS);

            int i = 0;
            for (Passenger p : booking.getPassengers()) {
                JPanel cardPanel = new JPanel();
                cardPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                JPanel detailsPanel = new JPanel();
                detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
                JLabel p1 = new JLabel("Passenger #"+(i+1)+":");
                String p2String;
                if (p.getTitle() == null || p.getTitle().isEmpty()) {
                    p2String = p.getName() + " " + p.getSurname();
                } else {
                    p2String = p.getTitle() + " " + p.getName() + " " + p.getSurname();
                }
                JLabel p2 = new JLabel(p2String);
                JLabel p3 = new JLabel("Departure seat: " + p.getDepartureSeat().getSeatNo() + ", " + p.getDepartureSeat().getSeatClass());
                detailsPanel.add(p1);
                detailsPanel.add(Box.createVerticalStrut(5));
                detailsPanel.add(p2);
                detailsPanel.add(p3);
                if (booking.getReturnFlight() != null) {
                    JLabel p4 = new JLabel("Return seat: " + p.getReturnSeat().getSeatNo() + ", " + p.getReturnSeat().getSeatClass());
                    detailsPanel.add(p4);
                }

                JPanel buttonsPanel = new JPanel();
                buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
                JButton edit = new JSubmitButton("Edit name");
                edit.addActionListener(e -> {
                    JPanel popup = new JPanel();
                    popup.setLayout(new GridLayout(6, 1));
                    JLabel l1 = new JLabel("Title:");
                    JComboBox<String> titles = new JComboBox<>(new String[]{"Mr", "Mrs", "Ms", "Prefer not to say"});
                    if (p.getTitle() == null || p.getTitle().isEmpty()) {
                        titles.setSelectedIndex(3);
                    } else {
                        titles.setSelectedItem(p.getTitle());
                    }
                    JLabel l2 = new JLabel("Name:");
                    JTextField name = new JTextField(p.getName(), 20);
                    JLabel l3 = new JLabel("Surname:");
                    JTextField surname = new JTextField(p.getSurname(),20);

                    popup.add(l1);
                    popup.add(titles);
                    popup.add(l2);
                    popup.add(name);
                    popup.add(l3);
                    popup.add(surname);
                    titles.setFont(new Font("Arial", Font.PLAIN, 20));
                    setAllFonts(popup);
                    String[] options = {"Cancel", "Confirm"};
                    int n = JOptionPane.showOptionDialog(MainWindow.frame, popup, "Amend details", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                    if (n == 1) {
                        if (Objects.equals(titles.getSelectedItem(), "Prefer not to say")) {
                            p.setTitle(null);
                        } else {
                            p.setTitle(Objects.requireNonNull(titles.getSelectedItem()).toString());
                        }
                        p.setName(name.getText());
                        p.setSurname(surname.getText());
                        refreshText();
                    }
                });
                JButton changeDeparture = new JSubmitButton("Change departure seat");
                changeDeparture.addActionListener(e -> MainWindow.createAndShowGUI(booking.getDepartureFlight().getAircraft().renderSeats(p, false)));
                buttonsPanel.add(Box.createVerticalStrut(15));
                buttonsPanel.add(edit);
                buttonsPanel.add(Box.createVerticalStrut(5));
                buttonsPanel.add(changeDeparture);
                if (booking.getReturnFlight() != null) {
                    JButton changeReturn = new JSubmitButton("Change return seat");
                    changeReturn.addActionListener(e -> MainWindow.createAndShowGUI(booking.getReturnFlight().getAircraft().renderSeats(p, true)));
                    buttonsPanel.add(Box.createVerticalStrut(5));
                    buttonsPanel.add(changeReturn);
                }

                detailsPanel.setOpaque(false);
                buttonsPanel.setOpaque(false);
                cardPanel.setOpaque(false);
                cardPanel.add(Box.createHorizontalStrut(10));
                cardPanel.add(detailsPanel);
                cardPanel.add(Box.createHorizontalStrut(50));
                cardPanel.add(buttonsPanel);
                passengerDetailsPanels[i] = detailsPanel;
                passengersCardPanel.add(cardPanel, String.valueOf(i));
                i++;
            }
            passengersCardPanel.setOpaque(false);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            buttonPanel.add(Box.createHorizontalStrut(10));
            buttonPanel.add(back);
            buttonPanel.add(next);
            buttonPanel.setOpaque(false);

            JLabel t = new JLabel("Passenger details: ");
            t.setFont(TITLE_FONT);

            title.add(Box.createHorizontalStrut(10));
            title.add(t);
            content.add(passengersCardPanel, BorderLayout.NORTH);
            content.add(buttonPanel, BorderLayout.SOUTH);
            add(title, BorderLayout.NORTH);
            add(content, BorderLayout.CENTER);
        }
    }
}
