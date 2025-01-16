package my.id.kagchi;

import my.id.kagchi.forms.LoginForm;

import javax.swing.*;

public class Main {
    private final static String title = "Penyewaan Forklift";
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginForm loginForm = new LoginForm(title);
            loginForm.setVisible(true);
        });
    }
}
