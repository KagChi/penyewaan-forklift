package my.id.kagchi.forms.pages.admin;

import my.id.kagchi.QueryBuilder;
import my.id.kagchi.core.Database;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Vector;

public class ListRequest extends JPanel {
    private final JTable table;
    private final DefaultTableModel tableModel;

    public ListRequest() {
        super(new BorderLayout(10, 10));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("Forklift Request List");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Name", "Rented On", "Rented Until", "Rented By", "Length (Days)", "Price", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshButton = new JButton("Refresh");
        JButton deleteButton = new JButton("Delete");
        JButton acceptButton = new JButton("Accept");

        buttonPanel.add(deleteButton);
        buttonPanel.add(acceptButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshButton.addActionListener(e -> fetchForkliftData());
        deleteButton.addActionListener(e -> deleteRequestForklift());
        acceptButton.addActionListener(e -> acceptRequestForkLift());

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                fetchForkliftData();
            }
        });
    }

    private void fetchForkliftData() {
        try {
            String query = new QueryBuilder()
                    .select("forklifts.id", "forklifts.name", "forklifts.rented_on", "forklifts.rented_until", "forklifts.price", "forklifts.rented_status", "users.username")
                    .from("forklifts")
                    .join("users", "forklifts.rented_by = users.id")
                    .where("forklifts.rented_by IS NOT NULL")
                    .build();

            tableModel.setRowCount(0);
            var result = Database.executeStatement(query);

            for (var val : result) {
                Vector<Object> row = new Vector<>();

                String rentedOn = val.get("rented_on").toString();
                String rentedUntil = val.get("rented_until").toString();
                String price = val.get("price").toString();

                LocalDate rentedOnDate = LocalDate.parse(rentedOn);
                LocalDate rentedUntilDate = LocalDate.parse(rentedUntil);
                long durationDays = ChronoUnit.DAYS.between(rentedOnDate, rentedUntilDate);

                double pricePerDay = Double.parseDouble(price);
                double totalPrice = durationDays * pricePerDay;
                NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                String formattedPrice = rupiahFormat.format(totalPrice);

                row.add(val.get("id").toString());
                row.add(val.get("name").toString());
                row.add(rentedOn);
                row.add(rentedUntil);
                row.add(val.get("username").toString());
                row.add(durationDays);
                row.add(formattedPrice);

                String rentedStatus = val.get("rented_status").toString();
                if (rentedStatus != null && !rentedStatus.isEmpty()) {
                    rentedStatus = rentedStatus.substring(0, 1).toUpperCase() + rentedStatus.substring(1);
                }

                row.add(rentedStatus);

                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRequestForklift() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this request?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String id = tableModel.getValueAt(selectedRow, 0).toString();
                try {
                    String query = new QueryBuilder()
                            .update(
                                    "rented_by = NULL",
                                    "rented_until = NULL",
                                    "rented_on = NULL",
                                    "rented_status = 'none'"
                            )
                            .to("forklifts")
                            .where("id = " + id)
                            .build();
                    Database.executeUpdateStatement(query);
                    fetchForkliftData();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete the request.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void acceptRequestForkLift() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to accept this request?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String id = tableModel.getValueAt(selectedRow, 0).toString();
                try {
                    String query = new QueryBuilder()
                            .update("rented_status = 'rented'")
                            .to("forklifts")
                            .where("id = " + id)
                            .build();
                    Database.executeUpdateStatement(query);
                    fetchForkliftData();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to accept the request.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
