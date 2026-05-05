package ui;

import model.Staff;

import javax.swing.*;
import java.awt.*;

/**
 * Main application window.
 * Entry point for the UI — shows login then loads all panels.
 */
public class MainFrame extends JFrame {

    private Staff loggedInStaff;

    public MainFrame() {
        showLogin();
        if (loggedInStaff == null) {
            System.exit(0); // User closed login dialog
        }
        buildMainUI();
    }

    // ── LOGIN ─────────────────────────────────────────────────

    private void showLogin() {
        LoginDialog login = new LoginDialog(this);
        login.setVisible(true);
        loggedInStaff = login.getLoggedInStaff();
    }

    // ── MAIN UI ───────────────────────────────────────────────

    private void buildMainUI() {
        setTitle("Library Management System  |  Logged in: "
                 + loggedInStaff.getFullName()
                 + " (" + loggedInStaff.getRole() + ")");
        setSize(1000, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 550));

        // Header bar
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(30, 60, 120));
        header.setPreferredSize(new Dimension(0, 50));

        JLabel titleLabel = new JLabel("  📚 Library Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        header.add(titleLabel, BorderLayout.WEST);

        JLabel userLabel = new JLabel("👤 " + loggedInStaff.getFullName() + "  ");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        userLabel.setForeground(new Color(180, 210, 255));
        header.add(userLabel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // Tabbed panels
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));

        tabs.addTab("📚 Books",        new BookPanel());
        tabs.addTab("👥 Members",      new MemberPanel());
        tabs.addTab("🔄 Issue/Return", new IssueReturnPanel(loggedInStaff));
        tabs.addTab("📊 Reports",      new ReportsPanel());

        add(tabs, BorderLayout.CENTER);

        // Status bar
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBackground(new Color(220, 225, 235));
        statusBar.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        JLabel statusLabel = new JLabel("Ready  |  Fine rate: Rs.2/day  |  Default loan: 14 days");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(new Color(80, 80, 100));
        statusBar.add(statusLabel);
        add(statusBar, BorderLayout.SOUTH);

        setVisible(true);
    }

    // ── ENTRY POINT ───────────────────────────────────────────

    public static void main(String[] args) {
        // Use system look and feel for native OS appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(MainFrame::new);
    }
}
