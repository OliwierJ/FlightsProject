package com.flights.gui;

import com.flights.objects.Booking;
import com.flights.objects.Flight;
import com.flights.util.FlightsConstants;

import javax.swing.*;
import java.awt.*;
public class FlightFareSelectionMenu extends JPanel implements FlightsConstants{

    Flight flight;
    Flight returnFlight;
    int[] passengerTypes;

    public FlightFareSelectionMenu(Flight flight, Flight returnFlight, int[] passArray) {
        super();
        this.flight = flight;
        this.returnFlight = returnFlight;
        System.out.println(flight);
        System.out.println(returnFlight); // TODO: fix bug make sure the flights are properly selected in previous menu, choosing any other flight than the default selection doesn't work
        this.passengerTypes = passArray;
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setPreferredSize(new Dimension(MainWindow.FRAME_WIDTH, MainWindow.FRAME_HEIGHT));

        JPanel infoPanel = new InfoPanel();
        // TODO priceMultiplier will be changed
        JPanel basicPanel = new PerkPanel(SEAGREEN, 1, "Basic", Color.WHITE, 0);
        JPanel standardPanel = new PerkPanel(DARKSPRINGGREEN, 4, "Standard", Color.WHITE, 0);
        JPanel deluxePanel = new PerkPanel(MAIZE, 8, "Premium+", Color.WHITE, 0);

        add(Box.createGlue());
        add(infoPanel);
        add(basicPanel);
        add(standardPanel);
        add(deluxePanel);
        add(Box.createGlue());
    }

    private double calculateFarePrice(Flight flight, Flight flight2, int priceMultiplier, int[] passengerTypes) {
        // TODO method to calculate price

        return 0;
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
            setPreferredSize(new Dimension(400, MainWindow.FRAME_HEIGHT));
            setMaximumSize(new Dimension(400, MainWindow.FRAME_HEIGHT));
            setMinimumSize(new Dimension(350, 700));

            JLabel chooseFareLabel = new JLabel("Choose your Fare");
            chooseFareLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            chooseFareLabel.setFont(new Font("Arial", Font.BOLD, 30));
            chooseFareLabel.setBorder(BorderFactory.createEmptyBorder(50, 50, 8, 50));

            JLabel chooseFareSubtext = new JLabel("<html>* Selected fare is applied to all passengers for all flights<html>");
            chooseFareSubtext.setAlignmentX(Component.CENTER_ALIGNMENT);
            chooseFareSubtext.setFont(new Font("Calibri", Font.BOLD, 18));
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

        PerkPanel(Color c, int perkCount, String fareType, Color textColor, int priceMultiplier) {

            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setPreferredSize(new Dimension(300, 800));
            setMaximumSize(new Dimension(300, 800));
            setMinimumSize(new Dimension(300, 700));

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

            // TODO Pricing of fare
            JButton select = new JButton("Select €" + "##.##" + priceMultiplier);
            select.setFont(new Font("Arial", Font.BOLD, 18));
            select.setMinimumSize(new Dimension(150, 50));
            select.setPreferredSize(new Dimension(150, 50));
            select.setMaximumSize(new Dimension(150, 50));
            select.setAlignmentX(Component.CENTER_ALIGNMENT);
            select.setFocusable(false);
            select.addActionListener(e -> {
                // TODO Pricing
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
                MainWindow.createAndShowGUI(new SetPassengers(b, passengerTypes, calculateFarePrice(flight,returnFlight,0,passengerTypes)));
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
