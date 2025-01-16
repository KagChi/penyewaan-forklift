package my.id.kagchi.forms;

import javax.swing.*;

public class BaseForm extends JFrame {
    public BaseForm(String title) {
        setTitle(String.format("Penyewaan Forklift - %s", title));
    }

    public void showErrorMessage(Exception error) {
        JOptionPane.showMessageDialog(this, error.getMessage());
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void showConfirmationMessage(String message) {
        JOptionPane.showConfirmDialog(this, message);
    }
}
