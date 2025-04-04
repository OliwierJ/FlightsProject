package com.flights.gui;

import com.flights.gui.components.JSubmitButton;
import com.flights.gui.components.JTopBar;
import com.flights.objects.Booking;
import com.flights.objects.Flight;
import com.flights.objects.Payment;
import com.flights.util.FlightsConstants;
import com.flights.util.JErrorDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.flights.gui.MainWindow.frame;

public class PaymentMenu extends JPanel implements FlightsConstants {

    private final Booking booking;
    private JTextField cvvField;
    private JTextField nameField;
    private JTextField cardNumberField;
    private final double price;

    public PaymentMenu(Booking b, double price) {
        this.booking = b;
        this.price = price;

        setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Booking Info Panel
        JPanel bookingInfoPanel = createBookingInfoPanel();
        bookingInfoPanel.setMaximumSize(new Dimension(600,500));
        TitledBorder titleBorder = BorderFactory.createTitledBorder("Booking Summary");
        titleBorder.setTitleFont(new Font("SansSerif", Font.BOLD, 18));
        bookingInfoPanel.setBorder(titleBorder);

        // Credit Card Panel
        JPanel paymentPanel = createCreditCardFormPanel();
        paymentPanel.setMaximumSize(new Dimension(600,500));
        TitledBorder paymentTitle = BorderFactory.createTitledBorder("Payment Information");
        paymentTitle.setTitleFont(new Font("SansSerif", Font.BOLD, 18));
        paymentPanel.setBorder(paymentTitle);

        // Add to main layout
        mainPanel.add(bookingInfoPanel, BorderLayout.NORTH);
        mainPanel.add(paymentPanel, BorderLayout.CENTER);

        add(new JTopBar(price), BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

    }

    private JPanel createBookingInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ASPARAGUS); // Light background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Simulated booking data
        String email = booking.getEmail();
        int priorityBoarding = booking.getPriorityBoarding();
        int luggageCount = 2;
        boolean has20kgLuggage = booking.get20kgluggage();

        Flight f = booking.getDepartureFlight();
        Flight f2 = booking.getReturnFlight();
        LocalDateTime time = f.getDepartureLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, hh:mm a");

        String outboundFlight = f.getFlightID() + " | " + f.getDepartureAirport() + " → " + f.getArrivalAirport() + "<br>" + time.format(formatter);


        Font boldFont = new Font("SansSerif", Font.BOLD, 18);
        Font labelFont = new Font("SansSerif", Font.PLAIN, 20);

        // Price
        gbc.gridx = 0;
        panel.add(makeLabel("💰 Total Price:", boldFont), gbc);
        gbc.gridx = 1;
        panel.add(makeLabel(String.valueOf(price), labelFont), gbc);

        // Email
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(makeLabel("📧 Email: ", boldFont), gbc);
        gbc.gridx = 1;
        panel.add(makeLabel(email, labelFont), gbc);

        // Priority Boarding
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(makeLabel("🚀 Priority Boarding: ", boldFont), gbc);
        gbc.gridx = 1;
        panel.add(makeLabel(priorityBoarding == 1 ? "Yes" : "No", labelFont), gbc);

        // Luggage Count
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(makeLabel("Luggage Count: ", boldFont), gbc);
        gbc.gridx = 1;
        panel.add(makeLabel(String.valueOf(luggageCount), labelFont), gbc);

        // 20kg Luggage Included
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(makeLabel("📦 20kg Luggage Included: ", boldFont), gbc);
        gbc.gridx = 1;
        panel.add(makeLabel(has20kgLuggage ? "Yes" : "No", labelFont), gbc);

        // Outbound Flight
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        JLabel outbound = new JLabel("<html>🛫 <b>Outbound Flight:</b><br>" + outboundFlight + "</html>");
        outbound.setFont(labelFont);
        panel.add(outbound, gbc);

        if (f2 != null) {
            LocalDateTime time2 = f2.getDepartureLocalDateTime();
            String returnFlight = f2.getFlightID() + " | " + f2.getDepartureAirport() + " → " + f2.getArrivalAirport() + "<br>" + time2.format(formatter);
            // Return Flight
            gbc.gridy++;
            JLabel inbound = new JLabel("<html>🛬 <b>Return Flight:</b><br>" + returnFlight + "</html>");
            inbound.setFont(labelFont);
            panel.add(inbound, gbc);
        }

        return panel;
    }


    // Helper for styled labels
        private JLabel makeLabel(String text, Font font) {
            JLabel label = new JLabel(text);
            label.setFont(font);
            return label;
        }



    private JPanel createCreditCardFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        Font boldFont = new Font("SansSerif", Font.BOLD, 18);

        cardNumberField = new JTextField(19);
        nameField = new JTextField(20);

        JComboBox<String> monthCombo = new JComboBox<>(new String[]{
                "01 - Jan", "02 - Feb", "03 - Mar", "04 - Apr", "05 - May", "06 - Jun",
                "07 - Jul", "08 - Aug", "09 - Sep", "10 - Oct", "11 - Nov", "12 - Dec"
        });
        JComboBox<String> yearCombo = new JComboBox<>();
        for (int i = 2024; i <= 2035; i++) {
            yearCombo.addItem(String.valueOf(i));
        }

        cvvField = new JTextField(3);

        JButton submitButton = new JSubmitButton("Submit");
        submitButton.addActionListener(e -> {
            if (!validPayment()) {
                return;
            }
            if (JOptionPane.showConfirmDialog(frame, "Are you sure you want to confirm this booking?") !=  JOptionPane.YES_OPTION ) {
                return;
            }
            showConfirmationScreen();

        });
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(makeLabel("Cardholder Name:",boldFont), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(makeLabel("Card Number:",boldFont), gbc);
        gbc.gridx = 1;
        panel.add(cardNumberField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(makeLabel("Expiration Date:",boldFont), gbc);
        gbc.gridx = 1;
        JPanel expiryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        expiryPanel.add(monthCombo);
        expiryPanel.add(yearCombo);
        panel.add(expiryPanel, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(makeLabel("CVV:",boldFont), gbc);
        gbc.gridx = 1;
        panel.add(cvvField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(submitButton, gbc);

        return panel;
    }

    private boolean validPayment() {
        if (cardNumberField.getText().isEmpty() || !Payment.checkNo(Long.parseLong(cardNumberField.getText()))) {
            JErrorDialog.showWarning("Invalid Card Number");
            return false;
        }
        if (cvvField.getText().isEmpty() || !Payment.checkCVV(Integer.parseInt(cvvField.getText()))) {
            JErrorDialog.showWarning("Invalid Card Number");
            return false;
        }
        if (!Payment.checkName(nameField.getText())) {
            JErrorDialog.showWarning("Invalid Name. Only letters, spaces, hyphens, and apostrophes");
            return false;
        }
        return true;
    }

    private void showConfirmationScreen() {
        JDialog dialog = new JDialog(frame, "Booking Confirmed", true); // true = modal
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(frame);
        dialog.setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        contentPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("🎉 Booking Confirmed!");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel message = new JLabel("<html><div style='text-align: center;'>Thank you for your purchase.<br>Your booking Number is :<b> "+booking.getBookingID()+"</b>.<br> Your flights have been booked successfully.</div></html>");
        message.setFont(new Font("SansSerif", Font.PLAIN, 15));
        message.setAlignmentX(Component.CENTER_ALIGNMENT);
        message.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));

        JButton closeButton = new JSubmitButton("Back to home");
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        closeButton.addActionListener(e -> {
            dialog.dispose();
            MainWindow.createAndShowGUI(new MainWindow());
        });

        contentPanel.add(title);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(message);
        contentPanel.add(closeButton);

        booking.updateDatabase();
        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.setVisible(true);


    }

    public static void main(String[] args) {
        MainWindow.createAndShowGUI(new PaymentMenu(new Booking("Standard"), 100));

    }

}
