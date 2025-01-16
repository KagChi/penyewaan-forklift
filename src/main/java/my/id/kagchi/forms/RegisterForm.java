package my.id.kagchi.forms;

import my.id.kagchi.QueryBuilder;
import my.id.kagchi.Util;
import my.id.kagchi.core.Database;
import my.id.kagchi.forms.pages.Home;

import javax.swing.*;
import java.awt.*;

public class RegisterForm extends BaseForm {
    public RegisterForm() {
        super("Register");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 320);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 240, 240));

        JLabel titleLabel = new JLabel("Register Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(50, 50, 150));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField usernameField = new JTextField(20);
        usernameField.setMaximumSize(new Dimension(200, 30));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField emailField = new JTextField(20);
        emailField.setMaximumSize(new Dimension(200, 30));
        emailField.setAlignmentX(Component.CENTER_ALIGNMENT);

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

        buttonPanel.add(registerButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(loginButton);

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(usernameLabel);
        mainPanel.add(usernameField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(emailLabel);
        mainPanel.add(emailField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(passwordLabel);
        mainPanel.add(passwordField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(buttonPanel);

        add(mainPanel);

        resetButton.addActionListener(e -> {
            usernameField.setText("");
            emailField.setText("");
            passwordField.setText("");
        });

        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = Util.hashWithMD5(passwordField.getText());

            try {
                String query = new QueryBuilder()
                        .insert("username", "email", "password")
                        .values(String.format("'%s'", username), String.format("'%s'", email), String.format("'%s'", password))
                        .to("users")
                        .build();

                var result = Database.executeUpdateStatement(query);
                if (result) {
                    usernameField.setText("");
                    emailField.setText("");
                    passwordField.setText("");

                    this.showMessage("Sukses mendaftarkan akun!");
                    setVisible(false);

                    SwingUtilities.invokeLater(() -> {
                        LoginForm loginForm = new LoginForm();
                        loginForm.setVisible(true);
                    });

                } else {
                    this.showMessage("Kesalahan saat menyimpan!");
                }
            } catch (Exception ex) {
                this.showErrorMessage(ex);
            }
        });

        loginButton.addActionListener(e -> {
            setVisible(false);
            SwingUtilities.invokeLater(() -> {
                LoginForm loginForm = new LoginForm();
                loginForm.setVisible(true);
            });
        });
    }
}
