package my.id.kagchi.forms.pages.admin;

import my.id.kagchi.QueryBuilder;
import my.id.kagchi.core.Database;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Vector;

public class ListForklift extends JPanel {
    private final JTable table;
    private final DefaultTableModel tableModel;

    public ListForklift() {
        super(new BorderLayout(10, 10));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("Forklift List");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Name", "Price"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshButton = new JButton("Refresh");
        JButton deleteButton = new JButton("Delete");

        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshButton.addActionListener(e -> fetchForkliftData());
        deleteButton.addActionListener(e -> deleteForklift());

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
                    .select("*")
                    .from("forklifts")
                    .build();

            tableModel.setRowCount(0);
            var result = Database.executeStatement(query);

            for (var val : result) {
                Vector<Object> row = new Vector<>();
                row.add(val.get("id").toString());
                row.add(val.get("name").toString());
                row.add(val.get("price").toString());
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteForklift() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this forklift?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String id = tableModel.getValueAt(selectedRow, 0).toString();
                try {
                    String query = new QueryBuilder()
                            .delete()
                            .from("forklifts")
                            .where("id = " + id)
                            .build();
                    Database.executeDeleteStatement(query);
                    fetchForkliftData();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
