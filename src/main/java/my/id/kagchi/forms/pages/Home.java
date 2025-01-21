package my.id.kagchi.forms.pages;

import my.id.kagchi.Main;
import my.id.kagchi.forms.BaseForm;
import my.id.kagchi.forms.pages.admin.ListForklift;
import my.id.kagchi.forms.pages.admin.NewForklift;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class Home extends BaseForm {
    private final Color SIDEBAR_BG = new Color(54, 57, 63);
    private final Color BUTTON_BG = new Color(70, 130, 180);
    private final Color BUTTON_HOVER = new Color(60, 110, 160);

    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    public Home() {
        super("Dashboard");

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel sidebarContainer = new JPanel();
        sidebarContainer.setLayout(new BoxLayout(sidebarContainer, BoxLayout.Y_AXIS));
        sidebarContainer.setBackground(SIDEBAR_BG);

        sidebarContainer.add(createUserSidebar());
        if (Main.getSession().getRole().equalsIgnoreCase("admin")) {
            sidebarContainer.add(createAdminSidebar());
        }

        JPanel paddedContainer = new JPanel(new BorderLayout());
        paddedContainer.setBackground(SIDEBAR_BG);
        paddedContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 6));
        paddedContainer.add(sidebarContainer, BorderLayout.CENTER);

        JScrollPane sidebarScrollPane = new JScrollPane(paddedContainer);
        sidebarScrollPane.setBorder(null);
        sidebarScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sidebarScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        customizeScrollBar(sidebarScrollPane);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createMainContent(), "MainContent");
        mainPanel.add(new NewForklift(), "AddForklift");
        mainPanel.add(new ListForklift(), "ListForklift");

        JPanel westContainer = new JPanel(new BorderLayout());
        westContainer.setPreferredSize(new Dimension(220, getHeight()));
        westContainer.add(sidebarScrollPane);

        add(westContainer, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createUserSidebar() {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(SIDEBAR_BG);
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        sidebarPanel.setMaximumSize(new Dimension(200, Integer.MAX_VALUE));

        JLabel headerLabel = new JLabel("User Navigation");
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        sidebarPanel.add(headerLabel);

        String[] userActions = {
                "Home"
        };

        for (String action : userActions) {
            JButton button = createNavButton(action);
            sidebarPanel.add(button);
            sidebarPanel.add(Box.createVerticalStrut(5));
            button.addActionListener(e -> handleUserAction(action));
        }

        sidebarPanel.add(Box.createVerticalStrut(20));
        JButton logoutButton = createNavButton("Logout");
        logoutButton.addActionListener(e -> {
            dispose();
            JOptionPane.showMessageDialog(this, "You have been logged out.");
        });
        sidebarPanel.add(logoutButton);

        return sidebarPanel;
    }

    private JPanel createAdminSidebar() {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(SIDEBAR_BG);
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        sidebarPanel.setMaximumSize(new Dimension(200, Integer.MAX_VALUE));

        sidebarPanel.add(new JSeparator() {
            {
                setMaximumSize(new Dimension(180, 1));
                setForeground(new Color(80, 83, 88));
            }
        });
        sidebarPanel.add(Box.createVerticalStrut(20));

        JLabel headerLabel = new JLabel("Admin Tools");
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        sidebarPanel.add(headerLabel);

        String[] adminActions = {
                "Add Forklift", "List Forklift"
        };

        for (String action : adminActions) {
            JButton button = createNavButton(action);
            sidebarPanel.add(button);
            sidebarPanel.add(Box.createVerticalStrut(5));
            button.addActionListener(e -> handleAdminAction(action));
        }

        return sidebarPanel;
    }

    private void handleAdminAction(String action) {
        switch (action) {
            case "Add Forklift" -> cardLayout.show(mainPanel, "AddForklift");
            case "List Forklift" -> cardLayout.show(mainPanel, "ListForklift");
        }
    }

    private void handleUserAction(String action) {
        switch (action) {
            case "Home" -> cardLayout.show(mainPanel, "MainContent");
        }
    }

    private void customizeScrollBar(JScrollPane scrollPane) {
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(80, 83, 88);
                this.trackColor = SIDEBAR_BG;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                return button;
            }
        });

        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, Integer.MAX_VALUE));
    }

    private JPanel createMainContent() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Selamat datang di rental Forklift Wowok", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        contentPanel.add(welcomeLabel, BorderLayout.NORTH);
        contentPanel.setBackground(Color.WHITE);

        return contentPanel;
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                g2.setColor(getForeground());
                FontMetrics fm = g2.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), textX, textY);

                g2.dispose();
            }
        };

        button.setFocusPainted(false);
        button.setBackground(BUTTON_BG);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.setPreferredSize(new Dimension(180, 40));
        button.setBorderPainted(false);
        button.setOpaque(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_HOVER);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_BG);
            }
        });

        return button;
    }
}
