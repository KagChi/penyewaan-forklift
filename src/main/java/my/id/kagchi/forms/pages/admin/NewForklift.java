package my.id.kagchi.forms.pages.admin;

import my.id.kagchi.QueryBuilder;
import my.id.kagchi.Util;
import my.id.kagchi.core.Database;
import my.id.kagchi.forms.LoginForm;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.text.NumberFormat;

public class NewForklift extends JPanel {
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final int BORDER_RADIUS = 8;

    private JTextField nameField;
    private JTextField descriptionField;
    private JFormattedTextField priceField;

    public NewForklift() {
        super(new BorderLayout(0, 20));
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel headerPanel = createHeaderPanel();

        JPanel formCard = createFormCard();

        add(headerPanel, BorderLayout.NORTH);
        add(formCard, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Add New Forklift");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(TEXT_COLOR);

        JLabel subtitleLabel = new JLabel("Enter the details for the new forklift equipment");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(127, 140, 141));

        JPanel titleWrapper = new JPanel(new GridLayout(2, 1, 0, 5));
        titleWrapper.setBackground(BACKGROUND_COLOR);
        titleWrapper.add(titleLabel);
        titleWrapper.add(subtitleLabel);

        headerPanel.add(titleWrapper, BorderLayout.WEST);
        return headerPanel;
    }


    private JPanel createFormCard() {
        JPanel cardPanel = new JPanel(new BorderLayout(0, 20));
        cardPanel.setBackground(CARD_COLOR);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(BORDER_RADIUS, new Color(230, 236, 241)),
                new EmptyBorder(30, 35, 30, 35)
        ));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CARD_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 0);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;

        nameField = createFormField("Enter forklift name");
        descriptionField = createFormField("Enter forklift description");
        priceField = createPriceField();

        formPanel.add(createFormFieldPanel("Forklift Name", nameField), gbc);
        formPanel.add(createFormFieldPanel("Description", descriptionField), gbc);
        formPanel.add(createPriceFieldPanel(), gbc);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setBackground(CARD_COLOR);

        JButton submitButton = createButton("Submit", PRIMARY_COLOR, TEXT_COLOR);

        submitButton.addActionListener(e -> {
            String name = nameField.getText();
            String description = descriptionField.getText();
            Number priceValue = (Number) priceField.getValue();
            double price = priceValue != null ? priceValue.doubleValue() : 0.0;

            try {
                String query = new QueryBuilder()
                        .insert("name", "description", "price")
                        .values(String.format("'%s'", name), String.format("'%s'", description), String.format("'%s'", price))
                        .to("forklifts")
                        .build();

                var result = Database.executeUpdateStatement(query);
                if (result) {
                    nameField.setText("");
                    descriptionField.setText("");
                    priceField.setText("");

                    JOptionPane.showMessageDialog(this, "Sukses membuat forklift baru!");
                } else {
                    JOptionPane.showMessageDialog(this, "Kesalahan saat menyimpan!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        buttonsPanel.add(Box.createHorizontalStrut(10));
        buttonsPanel.add(submitButton);

        cardPanel.add(formPanel, BorderLayout.CENTER);
        cardPanel.add(buttonsPanel, BorderLayout.SOUTH);

        return cardPanel;
    }

    private JPanel createPriceFieldPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 5));
        panel.setBackground(CARD_COLOR);

        JLabel fieldLabel = new JLabel("Price");
        fieldLabel.setForeground(TEXT_COLOR);

        panel.add(fieldLabel);
        panel.add(priceField);
        return panel;
    }

    private JFormattedTextField createPriceField() {
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(true);
        JFormattedTextField priceField = new JFormattedTextField(format);
        priceField.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(BORDER_RADIUS / 2, new Color(229, 231, 235)),
                new EmptyBorder(10, 15, 10, 15)
        ));
        priceField.setBackground(Color.WHITE);
        priceField.setForeground(TEXT_COLOR);

        return priceField;
    }

    private JPanel createFormFieldPanel(String label, JTextField field) {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 5));
        panel.setBackground(CARD_COLOR);

        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setForeground(TEXT_COLOR);

        panel.add(fieldLabel);
        panel.add(field);
        return panel;
    }

    private JTextField createFormField(String placeholder) {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(BORDER_RADIUS / 2, new Color(229, 231, 235)),
                new EmptyBorder(10, 15, 10, 15)
        ));
        textField.setBackground(Color.WHITE);
        textField.setForeground(TEXT_COLOR);

        textField.setText(placeholder);
        textField.setForeground(new Color(189, 195, 199));
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(TEXT_COLOR);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(new Color(189, 195, 199));
                }
            }
        });

        return textField;
    }

    private JButton createButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setBorder(new RoundedBorder(BORDER_RADIUS / 2, bgColor));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 40));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color color;

        RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius / 2, radius / 2, radius / 2, radius / 2);
        }
    }
}