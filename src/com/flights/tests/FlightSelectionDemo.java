//package com.flights.tests;
//
//import com.flights.objects.Flight;
//
//import javax.swing.*;
//import javax.swing.border.LineBorder;
//import javax.swing.border.TitledBorder;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//
//public class FlightSelectionDemo extends JPanel {
//    static JFrame frame = new JFrame();
//
//
//    public FlightSelectionDemo(String arrS, String dep, boolean showReturns) throws Exception {
//        setPreferredSize(new Dimension(900, 800));
//        setLayout(null);
//
//        JButton btnBack = new JButton("Back");
//        btnBack.setBounds(50,30,100,40);
//        btnBack.addActionListener(new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
////                testUI.createAndShowGUI(new testUI());
//            }
//        });
//        JPanel flightsWindow = new JPanel();
//        JPanel flightsWindowReturn = new JPanel();
//        flightsWindow.setLayout(new BoxLayout(flightsWindow, BoxLayout.Y_AXIS));
//        flightsWindowReturn.setLayout(new BoxLayout(flightsWindowReturn, BoxLayout.Y_AXIS));
//
//        String[][] queryResult = Flight.getFlightInfo(dep, arrS);
//        String[][] queryResultReturns = Flight.getFlightInfo(arrS, dep);
//
//        // Creates array list of flights
//        // for each String array turn it into a flight object and add it to the list
//        ArrayList<Flight> flights = new ArrayList<>();
//        for (String[] arr : queryResult) {
//            flights.add(new Flight(Integer.parseInt(arr[0]), arr[1], arr[2], Timestamp.valueOf(arr[3]), Timestamp.valueOf(arr[4])));
//        }
//
//
//        JPanel[] flightBoxes = new JPanel[flights.size()];
//        for (int i = 0; i < flights.size(); i++) {
//            flightBoxes[i] = new JPanel();
//            flightBoxes[i].add(new JLabel(flights.get(i).toString()));
//            flightBoxes[i].setLayout(new FlowLayout(FlowLayout.LEFT));
//            flightBoxes[i].setBorder(new LineBorder(Color.BLACK, 1));
//            flightBoxes[i].add(Box.createRigidArea(new Dimension(0, 100)));
//
//            flightBoxes[i].addMouseListener(new MouseAdapter() {
//
//                @Override
//                public void mouseReleased(MouseEvent e) {
//                    // do stuff
//                }
//            });
//            flightsWindow.add(flightBoxes[i]);
//
//        }
//
//
//        JScrollPane scrollPane = new JScrollPane(flightsWindow);
//        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//        scrollPane.setBounds(50, 100, 800, 250);
//        scrollPane.setBorder(new TitledBorder("Flight Selection  | \t" + dep + " - " + arrS));
//
//        add(scrollPane);
//
//        if (showReturns) {
//            // for each String array turn it into a flight object and add it to the list
//            ArrayList<Flight> returnFlights = new ArrayList<>();
//            for (String[] arr : queryResultReturns) {
//                returnFlights.add(new Flight(Integer.parseInt(arr[0]), arr[1], arr[2], Timestamp.valueOf(arr[3]), Timestamp.valueOf(arr[4])));
//            }
//
//            JPanel[] returnFlightBoxes = new JPanel[returnFlights.size()];
//            for (int i = 0; i < returnFlights.size(); i++) {
//                returnFlightBoxes[i] = new JPanel();
//                returnFlightBoxes[i].add(new JLabel(returnFlights.get(i).toString()));
//                returnFlightBoxes[i].setBorder(new LineBorder(Color.BLACK, 1));
//                returnFlightBoxes[i].add(Box.createRigidArea(new Dimension(0, 100)));
//                returnFlightBoxes[i].addMouseListener(new MouseAdapter() {
//
//                    @Override
//                    public void mouseReleased(MouseEvent e) {
//                        for (JPanel jPanel : returnFlightBoxes) {
//                            jPanel.setBackground(null);
//                        }
//                        e.getComponent().setBackground(Color.BLACK);
//                    }
//                });
//                flightsWindowReturn.add(returnFlightBoxes[i]);
//
//            }
//            JScrollPane scrollPane2 = new JScrollPane(flightsWindowReturn);
//            scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//            scrollPane2.setBounds(50, 400, 800, 250);
//            scrollPane2.setBorder(new TitledBorder("Return Flight Selection  | \t " + arrS + " - " + dep));
//            add(scrollPane2);
//
//        }
//        add(btnBack);
//    }
//
//    public static void createAndShowGUI() {
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        try {
//            frame.setContentPane(new FlightSelectionDemo( "Dublin","Barcelona", true));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        frame.pack();
//        frame.setVisible(true);
//    }
//
//
//    public static void main(String[] args) {
//        javax.swing.SwingUtilities.invokeLater(FlightSelectionDemo::createAndShowGUI);
//    }
//}
