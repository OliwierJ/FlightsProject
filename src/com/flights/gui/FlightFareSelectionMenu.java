package com.flights.gui;

import com.flights.Main;
import com.flights.gui.components.JTopBar;
import com.flights.objects.Booking;
import com.flights.objects.Flight;
import com.flights.util.FlightsConstants;

import javax.swing.*;
import java.awt.*;
public class FlightFareSelectionMenu extends JPanel implements FlightsConstants{
    private final Flight flight;
    private final Flight returnFlight;
    private final int[] passengerTypes;
    private final double price;

    public FlightFareSelectionMenu(Flight flight, Flight returnFlight, int[] passArray, double price) {
        super();
        this.price = price;
        this.flight = flight;
        this.returnFlight = returnFlight;
        this.passengerTypes = passArray;
        setLayout(new BorderLayout());
        setPreferredSize(Main.getFrameSize());

        JPanel infoPanel = new InfoPanel();
        JPanel basicPanel = new PerkPanel(SEAGREEN, 1, "Basic", Color.WHITE, 1);
        JPanel standardPanel = new PerkPanel(DARKSPRINGGREEN, 4, "Standard", Color.WHITE, 1.25);
        JPanel deluxePanel = new PerkPanel(MAIZE, 8, "Premium+", Color.WHITE, 1.5);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));

        contentPanel.add(Box.createGlue());
        contentPanel.add(infoPanel);
        contentPanel.add(basicPanel);
        contentPanel.add(standardPanel);
        contentPanel.add(deluxePanel);
        contentPanel.add(Box.createGlue());

        add(new JTopBar(price),BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    private double calculateFarePrice(double price, double priceMultiplier, int[] passengerTypes) {
        double totalPrice = price * priceMultiplier;
        for (int i = 0; i < passengerTypes.length; i++) {
            for (int j = 0; j < passengerTypes[i]; j ++) {
                if (i == 0 || i == 1) {totalPrice += totalPrice;}
                if (i == 2) {totalPrice += (totalPrice * 0.5);}
                else break;
            }
        }
        return totalPrice;
    }
    public static void addTickLabels(Container c, int i) {
        for (int j = 0; j < i; j++) {
            c.add(new TickLabel("✔"));
        }
    }

    private static class InfoPanel extends JPanel {
        InfoPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(ASPARAGUS);
            int height = (int) Main.getFrameSize().getHeight();
            setPreferredSize(new Dimension(400, height));
            setMaximumSize(new Dimension(400, height));

            JLabel chooseFareLabel = new JLabel("Choose your Fare");
            chooseFareLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            chooseFareLabel.setFont(new Font("Arial", Font.BOLD, 30));
            chooseFareLabel.setBorder(BorderFactory.createEmptyBorder(50, 50, 8, 50));

            JLabel chooseFareSubtext = new JLabel("<html>* Selected fare is applied to all passengers for all flights<html>");
            chooseFareSubtext.setAlignmentX(Component.CENTER_ALIGNMENT);
            chooseFareSubtext.setFont(new Font("Calibri", Font.PLAIN, 18));
            chooseFareSubtext.setBorder(BorderFactory.createEmptyBorder(0, 40, 40, 40));

            JPanel perksListPanel = new PerkListPanel();

            add(chooseFareLabel);
            add(chooseFareSubtext);
            add(Box.createVerticalStrut(35));
            add(perksListPanel);
        }
    }

    private static class PerkListPanel extends JPanel {
        public PerkListPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setMinimumSize(new Dimension(400, 525));
            setMaximumSize(new Dimension(400, 525));
            setPreferredSize(new Dimension(400, 525));
            setAlignmentX(Component.CENTER_ALIGNMENT);
            setBackground(LIGHTGRAY);

            JLabel perk1 = new JLabel("1 small bag");
            perk1.setAlignmentX(Component.RIGHT_ALIGNMENT);
            perk1.setFont(new Font("Arial", Font.BOLD, 18));
            perk1.setBackground(SELECTEDGRAY);
            perk1.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
            JLabel perk2 = new JLabel("Reserved seats");
            perk2.setAlignmentX(Component.RIGHT_ALIGNMENT);
            perk2.setFont(new Font("Arial", Font.BOLD, 18));
            perk2.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
            JLabel perk3 = new JLabel("Priority boarding");
            perk3.setAlignmentX(Component.RIGHT_ALIGNMENT);
            perk3.setFont(new Font("Arial", Font.BOLD, 18));
            perk3.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
            JLabel perk4 = new JLabel("10kg overhead/check-in luggage");
            perk4.setAlignmentX(Component.RIGHT_ALIGNMENT);
            perk4.setFont(new Font("Arial", Font.BOLD, 18));
            perk4.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
            JLabel perk5 = new JLabel("20 kg luggage");
            perk5.setAlignmentX(Component.RIGHT_ALIGNMENT);
            perk5.setFont(new Font("Arial", Font.BOLD, 18));
            perk5.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
            JLabel perk6 = new JLabel("Free check-in");
            perk6.setAlignmentX(Component.RIGHT_ALIGNMENT);
            perk6.setFont(new Font("Arial", Font.BOLD, 18));
            perk6.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
            JLabel perk7 = new JLabel("Cancellation refund");
            perk7.setAlignmentX(Component.RIGHT_ALIGNMENT);
            perk7.setFont(new Font("Arial", Font.BOLD, 18));
            perk7.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
            JLabel perk8 = new JLabel("Fast track through security");
            perk8.setAlignmentX(Component.RIGHT_ALIGNMENT);
            perk8.setFont(new Font("Arial", Font.BOLD, 18));
            perk8.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));

            add(perk1);
            add(perk2);
            add(perk3);
            add(perk4);
            add(perk5);
            add(perk6);
            add(perk7);
            add(perk8);
        }
    }

    private static class TickLabel extends JLabel {
        TickLabel(String text) {
            super(text);
            setAlignmentX(Component.CENTER_ALIGNMENT);
            setAlignmentY(Component.CENTER_ALIGNMENT);
            setFont(new Font(null, Font.BOLD, 32));
            setForeground(DARKSPRINGGREEN);
            setBorder(BorderFactory.createEmptyBorder(10, 30, 11, 30));
        }
    }

    private class PerkPanel extends JPanel {
        PerkPanel(Color c, int perkCount, String fareType, Color textColor, double priceMultiplier) {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            int height = (int) Main.getFrameSize().getHeight();
            setPreferredSize(new Dimension(300, height));
            setMaximumSize(new Dimension(300, height));
            setMinimumSize(new Dimension(300, height));

            JPanel holder = new JPanel();
            holder.setMaximumSize(new Dimension(300, 215));
            holder.setMinimumSize(new Dimension(300, 215));
            holder.setLayout(new BorderLayout());

            JPanel basicTitle = new JPanel();
            basicTitle.setBackground(c);
            basicTitle.setLayout(new BoxLayout(basicTitle, BoxLayout.Y_AXIS));

            JLabel title = new JLabel(fareType);
            title.setFont(new Font("Arial", Font.BOLD, 35));
            title.setForeground(textColor);
            title.setAlignmentX(Component.CENTER_ALIGNMENT);
            title.setHorizontalAlignment(SwingConstants.CENTER);

            double priceLabel = calculateFarePrice(price, priceMultiplier, passengerTypes);

            JButton select = new JButton("Select €" + priceLabel);
            select.setFont(new Font("Arial", Font.BOLD, 18));
            select.setMinimumSize(new Dimension(150, 50));
            select.setPreferredSize(new Dimension(150, 50));
            select.setMaximumSize(new Dimension(150, 50));
            select.setAlignmentX(Component.CENTER_ALIGNMENT);
            select.setFocusable(false);
            select.addActionListener(e -> {
                Booking b = new Booking(fareType);
                if (fareType.equals("Basic")) {
                    b.setPriorityBoarding(0);
                    b.set20kgluggage(false);
                } else if (fareType.equals("Standard")) {
                    b.setPriorityBoarding(1);
                    b.set20kgluggage(false);
                } else {
                    b.setPriorityBoarding(1);
                    b.set20kgluggage(true);
                }
                b.setDepartureFlight(flight);
                b.setReturnFlight(returnFlight);
                Main.createAndShowGUI(new SetPassengers(b, passengerTypes, priceLabel));
            });

            basicTitle.add(Box.createVerticalGlue());
            basicTitle.add(title);
            basicTitle.add(Box.createVerticalStrut(30));
            basicTitle.add(select);
            basicTitle.add(Box.createVerticalStrut(20));

            holder.add(basicTitle, BorderLayout.CENTER);
            holder.add(new JPanel(), BorderLayout.EAST);
            holder.add(new JPanel(), BorderLayout.WEST);
            holder.add(new JPanel(), BorderLayout.SOUTH);
            holder.add(new JPanel(), BorderLayout.NORTH);

            add(holder);
            add(Box.createVerticalStrut(5));
            addTickLabels(this, perkCount);
        }
    }
}