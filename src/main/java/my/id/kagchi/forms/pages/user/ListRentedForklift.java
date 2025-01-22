package my.id.kagchi.forms.pages.user;

import my.id.kagchi.Main;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ListRentedForklift extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(ListRentedForklift.class.getName());
    private final JTable table;
    private final DefaultTableModel tableModel;

    public ListRentedForklift() {
        super(new BorderLayout(10, 10));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("Forklift List");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Name", "Rented On", "Rented Until", "Length (Days)", "Price", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshButton = new JButton("Refresh");

        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshButton.addActionListener(e -> loadForkliftData());

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                loadForkliftData();
            }
        });
    }

    private void loadForkliftData() {
        try {
            String query = new QueryBuilder()
                    .select("forklifts.id", "forklifts.name", "forklifts.description", "forklifts.price", "forklifts.rented_on", "forklifts.rented_until", "forklifts.rented_status")
                    .from("forklifts")
                    .where(String.format("rented_by = '%s'", Main.getSession().getId()))
                    .build();

            var result = Database.executeStatement(query);

            tableModel.setRowCount(0);

            for (var val : result) {
                List<Object> row = new ArrayList<>();

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
                row.add(durationDays);
                row.add(formattedPrice);

                String rentedStatus = val.get("rented_status").toString();
                if (rentedStatus != null && !rentedStatus.isEmpty()) {
                    rentedStatus = rentedStatus.substring(0, 1).toUpperCase() + rentedStatus.substring(1);
                }

                row.add(rentedStatus);

                tableModel.addRow(row.toArray());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading forklift data", e);
            JOptionPane.showMessageDialog(this,
                    "Failed to load forklift data. Please try again later.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}