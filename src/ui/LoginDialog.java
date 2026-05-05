package ui;

import service.StaffService;
import model.Staff;

import javax.swing.*;
import java.awt.*;

/**
 * Login dialog shown on app startup.
 * Blocks access until valid credentials entered.
 */
public class LoginDialog extends JDialog {

    private final StaffService staffService = new StaffService();
    private Staff loggedInStaff = null;

    private JTextField     usernameField;
    private JPasswordField passwordField;
    private JLabel         errorLabel;

    public LoginDialog(JFrame parent) {
        super(parent, "Library Management System — Login", true);
        buildUI();
        setSize(380, 260);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    private void buildUI() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        main.setBackground(new Color(245, 245, 250));

        // Title
        JLabel title = new JLabel("Library Management System", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(new Color(30, 60, 120));
        main.add(title, BorderLayout.NORTH);

        // Form
        JPanel form = new JPanel(new GridLayout(4, 2, 8, 10));
        form.setOpaque(false);

        form.add(makeLabel("Username:"));
        usernameField = new JTextField();
        styleField(usernameField);
        form.add(usernameField);

        form.add(makeLabel("Password:"));
        passwordField = new JPasswordField();
        styleField(passwordField);
        form.add(passwordField);

        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        form.add(new JLabel());
        form.add(errorLabel);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(30, 60, 120));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginBtn.addActionListener(e -> attemptLogin());
        form.add(new JLabel());
        form.add(loginBtn);

        main.add(form, BorderLayout.CENTER);

        // Default button on Enter key
        getRootPane().setDefaultButton(loginBtn);

        JLabel hint = new JLabel("Default: admin / admin123", SwingConstants.CENTER);
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        hint.setForeground(Color.GRAY);
        main.add(hint, BorderLayout.SOUTH);

        add(main);
    }

    private void attemptLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        try {
            loggedInStaff = staffService.login(username, password);
            dispose(); // close dialog on success
        } catch (Exception ex) {
            errorLabel.setText(ex.getMessage());
            passwordField.setText("");
        }
    }

    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return l;
    }

    private void styleField(JTextField f) {
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 200)),
            BorderFactory.createEmptyBorder(4, 6, 4, 6)));
    }

    public Staff getLoggedInStaff() { return loggedInStaff; }
}
