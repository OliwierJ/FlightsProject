package com.flights.gui;

import com.flights.gui.components.BackgroundImagePanel;
import com.flights.gui.components.JTopBar;
import com.flights.objects.Flight;
import com.flights.objects.Seat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

public class SelectSeats extends JPanel {

    char[] seatLetters = {'A', 'B', 'C', 'D', 'E', 'F'};
    String selectedSeat;
    Seat[] seatsFromDB;

    public SelectSeats() {
        Flight f = new Flight(100);
        System.out.println(Arrays.toString(f.getAllSeats()));
        seatsFromDB = f.getAllSeats();

        setPreferredSize(new Dimension(MainWindow.FRAME_WIDTH, MainWindow.FRAME_HEIGHT));
        setLayout(new BorderLayout());

        add(new JTopBar(), BorderLayout.NORTH);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        BackgroundImagePanel image = new BackgroundImagePanel("src/com/flights/gui/images/boeing 737 800 seat map.png");
        image.setAlignmentX(Component.CENTER_ALIGNMENT);
        image.setLayout(new BoxLayout(image, BoxLayout.Y_AXIS));

        int imgWidth = 600;
        int imgHeight = 3200;
        image.setPreferredSize(new Dimension(imgWidth, imgHeight));
        image.setMaximumSize(new Dimension(imgWidth, imgHeight));
        image.setMinimumSize(new Dimension(imgWidth, imgHeight));

        JPanel seatsPanel = new JPanel();
        seatsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        seatsPanel.setLayout(new BoxLayout(seatsPanel, BoxLayout.X_AXIS));
        setEqualSizes(seatsPanel, 400, 2400);
//        seatsPanel.setBorder(BorderFactory.createLineBorder(Color.RED));
        seatsPanel.setOpaque(false);

        int seatsWidth = 150;
        int seatsHeight = 2400;

        /*
            Left seats

         */
        JPanel leftSeatsPanel = new JPanel();
        leftSeatsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftSeatsPanel.setLayout(new BoxLayout(leftSeatsPanel, BoxLayout.Y_AXIS));
        setEqualSizes(leftSeatsPanel, seatsWidth, seatsHeight);
//        leftSeatsPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        leftSeatsPanel.setOpaque(false);

        leftSeatsPanel.add(Box.createVerticalStrut(15));
        SeatPanel[] seats = new SeatPanel[400];
        int seatsIndex = 0;

        for (int i = 0; i < 14; i++) {
            JPanel seatRow = new JPanel();
            int individualSeatHeight = 71;
            seatRow.setAlignmentX(Component.CENTER_ALIGNMENT);
            seatRow.setLayout(new BoxLayout(seatRow, BoxLayout.X_AXIS));
            setEqualSizes(seatRow, seatsWidth, individualSeatHeight);
            seatRow.setOpaque(false);
            if (i < 2) {
                seatRow.add(Box.createHorizontalStrut(5));
            }
            for (int j = 0; j < 3; j++) {
                seats[seatsIndex] = new SeatPanel(seats, seatsIndex, createSeatName(j, seatsIndex));
                seatRow.add(seats[seatsIndex]);
                seatsIndex++;
            }
            leftSeatsPanel.add(seatRow);

        }
        leftSeatsPanel.add(Box.createVerticalStrut(20));


        JPanel emergencyRow = new JPanel();
        emergencyRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        emergencyRow.setLayout(new BoxLayout(emergencyRow, BoxLayout.X_AXIS));
        int individualSeatHeight = 72;
        setEqualSizes(emergencyRow, seatsWidth, individualSeatHeight);
        emergencyRow.setOpaque(false);
        for (int j = 0; j < 3; j++) {
            seats[seatsIndex] = new SeatPanel(seats, seatsIndex, createSeatName(j, seatsIndex));
            emergencyRow.add(seats[seatsIndex]);
            seatsIndex++;
        }

        leftSeatsPanel.add(emergencyRow);
        leftSeatsPanel.add(Box.createVerticalStrut(20));

        for (int i = 0; i < 17; i++) {
            JPanel seatRow = new JPanel();
            seatRow.setAlignmentX(Component.CENTER_ALIGNMENT);
            seatRow.setLayout(new BoxLayout(seatRow, BoxLayout.X_AXIS));
            setEqualSizes(seatRow, seatsWidth, individualSeatHeight);
            seatRow.setOpaque(false);
            for (int j = 0; j < 3; j++) {
                seats[seatsIndex] = new SeatPanel(seats, seatsIndex, createSeatName(j, seatsIndex));
                seatRow.add(seats[seatsIndex]);
                seatsIndex++;
            }
            leftSeatsPanel.add(seatRow);

        }
/*
        Right seats
 */
        JPanel rightSeatsPanel = new JPanel();
        rightSeatsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightSeatsPanel.setLayout(new BoxLayout(rightSeatsPanel, BoxLayout.Y_AXIS));
        setEqualSizes(rightSeatsPanel, seatsWidth, seatsHeight);
//        rightSeatsPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        rightSeatsPanel.setOpaque(false);
        int rightSeatsIndex = 3;
        individualSeatHeight = 71;
        rightSeatsPanel.add(Box.createVerticalStrut(14 + individualSeatHeight));

        for (int i = 0; i < 13; i++) {
            JPanel seatRow = new JPanel();
            seatRow.setAlignmentX(Component.CENTER_ALIGNMENT);
            seatRow.setLayout(new BoxLayout(seatRow, BoxLayout.X_AXIS));
            setEqualSizes(seatRow, seatsWidth, individualSeatHeight);
            seatRow.setOpaque(false);

            for (int j = 0; j < 3; j++) {
                seats[seatsIndex] = new SeatPanel(seats, seatsIndex, createSeatName(j + 3, rightSeatsIndex));
                seatRow.add(seats[seatsIndex]);
                seatsIndex++;
                rightSeatsIndex++;
            }
            rightSeatsPanel.add(seatRow);

        }
        rightSeatsPanel.add(Box.createVerticalStrut(20));

        JPanel rightEmergencyRow = new JPanel();
        rightEmergencyRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightEmergencyRow.setLayout(new BoxLayout(rightEmergencyRow, BoxLayout.X_AXIS));
        individualSeatHeight = 72;
        setEqualSizes(rightEmergencyRow, seatsWidth, individualSeatHeight);
        rightEmergencyRow.setOpaque(false);
        for (int j = 0; j < 3; j++) {
            seats[seatsIndex] = new SeatPanel(seats, seatsIndex, createSeatName(j + 3, rightSeatsIndex));
            rightEmergencyRow.add(seats[seatsIndex]);
            rightSeatsIndex++;
            seatsIndex++;
        }

        rightSeatsPanel.add(rightEmergencyRow);
        rightSeatsPanel.add(Box.createVerticalStrut(20));

        for (int i = 0; i < 17; i++) {
            JPanel seatRow = new JPanel();
            seatRow.setAlignmentX(Component.CENTER_ALIGNMENT);
            seatRow.setLayout(new BoxLayout(seatRow, BoxLayout.X_AXIS));
            setEqualSizes(seatRow, seatsWidth, individualSeatHeight);
            seatRow.setOpaque(false);
            for (int j = 0; j < 3; j++) {
                seats[seatsIndex] = new SeatPanel(seats, seatsIndex, createSeatName(j + 3, rightSeatsIndex));
                seatRow.add(seats[seatsIndex]);
                rightSeatsIndex++;
                seatsIndex++;
            }
            rightSeatsPanel.add(seatRow);

        }

        // end right seats
        seatsPanel.add(Box.createHorizontalGlue());
        seatsPanel.add(leftSeatsPanel);
        seatsPanel.add(Box.createHorizontalStrut(27));
        seatsPanel.add(rightSeatsPanel);
        seatsPanel.add(Box.createHorizontalGlue());

        image.add(Box.createVerticalGlue());
        image.add(Box.createVerticalStrut(400));
        image.add(seatsPanel);
        image.add(Box.createVerticalGlue());


        JLabel text = new JLabel("Select your seats");
        text.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        text.setFont(MainWindow.ARIAL20);
        text.setAlignmentX(Component.CENTER_ALIGNMENT);


        mainPanel.add(text);
        mainPanel.add(image);
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(30);


        add(scrollPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        MainWindow.createAndShowGUI(new SelectSeats());
    }

    private String createSeatName(int col, int seatIndex) {
        int num = seatIndex / 3 + 1;
        if (num >= 13) {
            num++;
        }
        //append 0 if less then 10
        if (num < 10) {
            return "0" + num + seatLetters[col];
        }
        // else
        return String.valueOf(num) + seatLetters[col];
    }

    private void setEqualSizes(JPanel panel, int w, int h) {
        panel.setPreferredSize(new Dimension(w, h));
        panel.setMaximumSize(new Dimension(w, h));
        panel.setMinimumSize(new Dimension(w, h));
    }

    private static class TransparentPanel extends JPanel {
        {
            setOpaque(false);
            setBackground(new Color(255, 0, 0, 0));
            setLayout(new BorderLayout());
        }

        public void paintComponent(Graphics g) {
            g.setColor(getBackground());
            Rectangle r = g.getClipBounds();
            g.fillRect(r.x, r.y, r.width, r.height);
            super.paintComponent(g);
        }
    }

    class SeatPanel extends JPanel {
        JPanel transparentPanel = new TransparentPanel();
        boolean isSelected = false;
        boolean isBooked;
//        boolean pickedPrevious;

        String name;

        SeatPanel(SeatPanel[] seats, int i, String name) {
            int individualSeatHeight = 72;
            this.name = name;
            setPreferredSize(new Dimension(49, individualSeatHeight));
            setMaximumSize(new Dimension(49, individualSeatHeight));
            setMinimumSize(new Dimension(49, individualSeatHeight));
            setLayout(new BorderLayout());
            setOpaque(false);
            transparentPanel.setOpaque(false);
            add(transparentPanel);

            for (Seat aircraftSeat : seatsFromDB) {
                if (aircraftSeat.getSeatNo().equals(name) && aircraftSeat.getPassengerID() != null) {
                    isBooked = true;
                    transparentPanel.setBackground(new Color(73, 73, 79, 192));
                    JLabel x = new JLabel("X");
                    x.setForeground(Color.LIGHT_GRAY);
                    x.setFont(new Font("Arial", Font.BOLD, 25));
                    x.setHorizontalAlignment(SwingConstants.CENTER);
                    x.setAlignmentX(Component.CENTER_ALIGNMENT);
                    x.setAlignmentY(Component.CENTER_ALIGNMENT);
                    transparentPanel.add(x);
                    break;
                }
            }

            addMouseListener(new MouseListener() {

                public void mouseClicked(MouseEvent e) {

                }

                public void mousePressed(MouseEvent e) {

                }
                
                public void mouseReleased(MouseEvent e) {
                    System.out.println("mousePressed" + name + " isbooked " + isBooked);
                    if (isBooked) {
                        return;
                    }
                    for (SeatPanel seat : seats) {
                        if (seat != null && !seat.isBooked) {

                            seat.transparentPanel.setOpaque(false);
                            seat.transparentPanel.setBackground(new Color(0, 0, 255, 0));
                            seat.transparentPanel.repaint();
                            seat.transparentPanel.revalidate();
                            seat.isSelected = false;
                            seat.setBorder(null);

                        }
                    }
                    transparentPanel.setBackground(new Color(0, 0, 255, 150));
                    transparentPanel.repaint();
                    transparentPanel.revalidate();
                    isSelected = true;
                    selectedSeat = seats[i].name;
                    System.out.println(selectedSeat);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (isBooked) {
                        return;
                    }
                    if (!isSelected) {
                        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        transparentPanel.setBackground(new Color(0, 0, 255, 100));
                        transparentPanel.repaint();
                        transparentPanel.revalidate();
                    }
                }

                public void mouseExited(MouseEvent e) {
                    if (isBooked) {
                        return;
                    }
                    if (!isSelected) {
                        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                        transparentPanel.setBackground(new Color(0, 0, 255, 0));
                        transparentPanel.repaint();
                        transparentPanel.revalidate();

                    }
                }
            });
        }
    }
}
