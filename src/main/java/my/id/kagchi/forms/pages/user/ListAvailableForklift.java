package my.id.kagchi.forms.pages.user;

import com.toedter.calendar.JDateChooser;
import my.id.kagchi.Main;
import my.id.kagchi.QueryBuilder;
import my.id.kagchi.core.Database;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

class RentForkliftDialog extends JDialog {
    private final JDateChooser dateChooser;
    private final String forkliftId;

    public RentForkliftDialog(Frame parent, String forkliftId) {
        super(parent, "Rent Forklift", true);
        this.forkliftId = forkliftId;

        setLayout(new GridLayout(2, 2, 10, 10));

        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setMinSelectableDate(new java.util.Date());

        add(new JLabel("Rent Until:"));
        add(dateChooser);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> processRental());
        add(confirmButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton);

        setSize(300, 150);
        setLocationRelativeTo(parent);
    }

    private void processRental() {
        try {
            LocalDate rentedUntil = dateChooser.getDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            LocalDate today = LocalDate.now();

            if (rentedUntil.isBefore(today) || rentedUntil.isEqual(today)) {
                JOptionPane.showMessageDialog(this,
                        "Rental end date cannot be in the past or today!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String query = new QueryBuilder()
                    .update(
                            "rented_by = '" + Main.getSession().getId() + "'",
                            "rented_status = 'pending'",
                            "rented_on = '" + LocalDate.now() + "'",
                            "rented_until = '" + rentedUntil.format(DateTimeFormatter.ISO_DATE) + "'"
                    )
                    .to("forklifts")
                    .where("id = " + forkliftId)
                    .build();

            Database.executeUpdateStatement(query);
            JOptionPane.showMessageDialog(this,
                    "Forklift rented successfully until " + rentedUntil,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}

public class ListAvailableForklift extends JPanel {
    private final JTable table;
    private final DefaultTableModel tableModel;

    public ListAvailableForklift() {
        super(new BorderLayout(10, 10));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("Forklift List");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Name", "Description", "Price"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshButton = new JButton("Refresh");
        JButton rentButton = new JButton("Rent");

        buttonPanel.add(rentButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshButton.addActionListener(e -> fetchForkliftData());
        rentButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                        "Please select a forklift first!",
                        "Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String forkliftId = tableModel.getValueAt(selectedRow, 0).toString();
            new RentForkliftDialog((Frame) SwingUtilities.getWindowAncestor(this), forkliftId).setVisible(true);
        });

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
                    .select("forklifts.id", "forklifts.name", "forklifts.description", "forklifts.price")
                    .from("forklifts")
                    .where("rented_by IS NULL", "rented_status = 'none'")
                    .build();

            tableModel.setRowCount(0);
            var result = Database.executeStatement(query);

            for (var val : result) {
                Vector<Object> row = new Vector<>();

                row.add(val.get("id").toString());
                row.add(val.get("name").toString());
                row.add(val.get("description").toString());
                row.add(val.get("price").toString());

                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
