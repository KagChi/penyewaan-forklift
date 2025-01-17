package my.id.kagchi.forms;

import my.id.kagchi.Main;
import my.id.kagchi.QueryBuilder;
import my.id.kagchi.Util;
import my.id.kagchi.core.Database;
import my.id.kagchi.core.Session;
import my.id.kagchi.forms.pages.Home;

import javax.swing.*;
import java.awt.*;

public class LoginForm extends BaseForm {
    public LoginForm() {
        super("Login");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 240, 240));

        JLabel titleLabel = new JLabel("Login to Your Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(50, 50, 150));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField usernameField = new JTextField(20);
        usernameField.setMaximumSize(new Dimension(200, 30));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(new Dimension(200, 30));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton loginButton = new JButton("Login");
        JButton resetButton = new JButton("Reset");
        JButton registerButton = new JButton("Register");

        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));

        resetButton.setFocusPainted(false);
        resetButton.setFont(new Font("Arial", Font.BOLD, 14));

        registerButton.setFocusPainted(false);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));

        buttonPanel.add(loginButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(registerButton);

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(usernameLabel);
        mainPanel.add(usernameField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(passwordLabel);
        mainPanel.add(passwordField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(buttonPanel);

        add(mainPanel);

        resetButton.addActionListener(e -> {
            usernameField.setText("");
            passwordField.setText("");
        });

        registerButton.addActionListener(e -> {
            setVisible(false);
            SwingUtilities.invokeLater(() -> {
                RegisterForm registerForm = new RegisterForm();
                registerForm.setVisible(true);
            });
        });

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = Util.hashWithMD5(passwordField.getText());

            try {
                String query = new QueryBuilder()
                        .select("id", "username", "role")
                        .from("users")
                        .where(String.format("username = '%s'", username), String.format("password = '%s'", password))
                        .limit(1)
                        .build();

                var result = Database.executeStatement(query);
                if (result.isEmpty()) {
                    this.showMessage("Akun tidak ditemukan!");
                    return;
                }

                var firstResult = result.get(0);
                this.showMessage(String.format("Selamat datang %s", firstResult.get("username")));

                Session session = new Session()
                        .setId(Integer.parseInt(firstResult.get("id").toString()))
                        .setUsername(firstResult.get("username").toString())
                        .setRole(firstResult.get("role").toString());

                Main.setSession(session);
                setVisible(false);

                SwingUtilities.invokeLater(() -> {
                    Home home = new Home();
                    home.setVisible(true);
                });
            } catch (Exception ex) {
                this.showErrorMessage(ex);
            }
        });
    }
}
