package com.flights.gui;

import com.flights.tests.ResultSetTableModel;
import com.flights.util.DBConnectivity;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Objects;

import static com.flights.gui.MainWindow.*;

public class AdminMenu extends JPanel {
    JPanel resultPanel = new JPanel(new BorderLayout());
    JTable resultTable;

    public AdminMenu() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        JPanel topBar = new JTopBar();
        add(topBar, BorderLayout.NORTH);
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JPanel tablePanel = new JPanel();
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"Booking", "Flight", "Passenger", "Seat", "FlightBooking"});
        JButton viewTable = new JButton("View table");
        viewTable.addActionListener(e -> {
            String selectedTable = (String) comboBox.getSelectedItem();
            selectedTable = Objects.requireNonNull(selectedTable).toLowerCase();
            if (selectedTable.equals("flightbooking")) {
                selectedTable = "flight_booking";
            }
            try {
                runQuery("SELECT * FROM "+selectedTable);
                System.out.println(selectedTable);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        tablePanel.add(comboBox);
        tablePanel.add(viewTable);
        contentPanel.add(tablePanel);

        try {
            runQuery("SELECT * FROM booking");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        resultPanel.add(resultTable.getTableHeader(), BorderLayout.NORTH);
        resultPanel.add(resultTable, BorderLayout.CENTER);
        resultPanel.setBorder(new EmptyBorder(0,50,0,50));
        contentPanel.add(resultPanel);
        add(contentPanel, BorderLayout.CENTER);

//        try {
//            tableModel = new ResultSetTableModel(DATABASE_URL, USERNAME, PASSWORD, DEFAULT_QUERY);
//            queryArea = new JTextArea(DEFAULT_QUERY, 3, 100);
//            queryArea.setWrapStyleWord(true);
//            queryArea.setLineWrap(true);
//            JScrollPane scrollPane = new JScrollPane(queryArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//            JButton submitButton = new JButton("Submit Query");
//            Box boxNorth = Box.createHorizontalBox();
//            boxNorth.add(scrollPane);
//            boxNorth.add(submitButton);
//            JTable resultTable = new JTable(tableModel);
//            JLabel filterLabel = new JLabel("Filter : ");
//            final JTextField filterText = new JTextField(15);
//            JButton filterButton = new JButton("Apply Filter");
//            Box boxSouth = Box.createHorizontalBox();
//            boxSouth.add(filterLabel);
//            boxSouth.add(filterText);
//            boxSouth.add(filterButton);
//            add(boxNorth, BorderLayout.NORTH);
//            add(new JScrollPane(resultTable), BorderLayout.CENTER);
//            add(boxSouth, BorderLayout.SOUTH);
//            submitButton.addActionListener(e -> {
//                try {
//                    tableModel.setQuery(queryArea.getText());
//                } catch (SQLException sqlException) {
//                    JOptionPane.showMessageDialog(this, sqlException.getMessage(),"Database error", JOptionPane.ERROR_MESSAGE);
//                    try {
//                        tableModel.setQuery(DEFAULT_QUERY);
//                        queryArea.setText(DEFAULT_QUERY);
//                    } catch (SQLException sqlException2) {
//                        JOptionPane.showMessageDialog(this, sqlException2.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
//                        tableModel.disconnectFromDatabase();
//                        System.exit(1);
//                    }
//                }
//            }
//                    //end Submit Button Action Listener class
//            );
//            final TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
//            resultTable.setRowSorter(sorter);
//            setSize(500, 250);
//            setVisible(true);
//            filterButton.addActionListener(e -> {
//                String text = filterText.getText();
//                if (text.isEmpty())
//                    sorter.setRowFilter(null);
//                else {
//                    try {
//                        sorter.setRowFilter(RowFilter.regexFilter(text));
//                    } catch (PatternSyntaxException pse) {
//                        JOptionPane.showMessageDialog(null, "Bad regex pattern", "Bad regex pattern", JOptionPane.ERROR_MESSAGE);
//                    }
//                }
//            }//end Filter Button Action Listener class
//            );
//        } catch (SQLException sqlException) {
//            JOptionPane.showMessageDialog(null, sqlException.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
//            tableModel.disconnectFromDatabase();
//            System.exit(1);
//        }
    }
    private void runQuery(String query) throws SQLException {
        DefaultTableModel model = DBConnectivity.getTableModelFromQuery(query);
        resultTable = new JTable(model);
//        resultTable = DBConnectivity.getTableFromQuery(query);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(resultTable.getModel());
        resultTable.setRowSorter(sorter);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> MainWindow.createAndShowGUI(new AdminMenu()));
    }
}