package my.id.kagchi;

import my.id.kagchi.core.Session;
import my.id.kagchi.forms.LoginForm;
import javax.swing.*;

public class Main {
    public static Session session;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
        });
    }

    public static void setSession(Session session) {
        Main.session = session;
    }

    public static Session getSession() {
        return session;
    }
}
