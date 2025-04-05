package com.flights.gui;

import com.flights.Main;
import com.flights.gui.components.JSubmitButton;
import com.flights.gui.components.JTopBar;
import com.flights.util.DBConnectivity;
import com.flights.util.FlightsConstants;
import com.flights.util.JErrorDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Admin Menu Panel to display database inside of the application
 */
public class AdminMenu extends JPanel implements FlightsConstants {
    private final JTable resultTable = new JTable();

    /**
     * Constructs a new AdminMenu JPanel
     */
    public AdminMenu() {
        setLayout(new BorderLayout());
        setPreferredSize(Main.getFrameSize());

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JPanel top = new JPanel();
        JLabel label = new JLabel("Admin Menu");
        JComboBox<String> options = new JComboBox<>(new String[]{"Booking", "Flight", "FlightBooking", "Passenger", "Seat"});
        JButton confirm = new JSubmitButton("View table");
        confirm.addActionListener(e -> {
            String selection = Objects.requireNonNull(options.getSelectedItem()).toString().toLowerCase();
            if (selection.equals("flightbooking")) {
                selection = "flight_booking";
            }
            renderTable("SELECT * FROM "+selection);
        });
        label.setFont(ARIAL20);
        options.setFont(ARIAL20PLAIN);

        top.add(label);
        top.add(options);
        top.add(confirm);

        renderTable("SELECT * FROM booking");

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setMinimumSize(new Dimension(1000, 700));
        tablePanel.setPreferredSize(new Dimension(1000, 700));
        tablePanel.setMaximumSize(new Dimension(1000, 700));
        
        JScrollPane sp = new JScrollPane(resultTable);
        tablePanel.add(resultTable.getTableHeader(), BorderLayout.NORTH);
        tablePanel.add(sp, BorderLayout.CENTER);

        content.add(top);
        content.add(tablePanel);
        content.add(Box.createVerticalGlue());
        add(new JTopBar(), BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
    }

    private void renderTable(String query) {
        try {
            ResultSet rs = DBConnectivity.connectAndExecuteQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            String[] columnNames = new String[rsmd.getColumnCount()];
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                columnNames[i-1] = rsmd.getColumnName(i);
            }
            String[][] data = DBConnectivity.getMultipleRows(rs);
            DefaultTableModel model = new DefaultTableModel(data, columnNames);
            resultTable.setModel(model);
            TableRowSorter<TableModel> sorter = new TableRowSorter<>(resultTable.getModel());
            resultTable.setRowSorter(sorter);
        } catch (SQLException e) {
            JErrorDialog.showError("An error occurred while retrieving database information", e);
        }
    }
}