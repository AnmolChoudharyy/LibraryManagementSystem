package ui;

import model.Fine;
import model.Loan;
import service.FineService;
import service.LoanService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

/**
 * UI Panel for Reports and Fines.
 * Shows active loans, overdue loans (highlighted red), and fine management.
 */
public class ReportsPanel extends JPanel {

    private final LoanService loanService = new LoanService();
    private final FineService fineService = new FineService();

    private JTable            activeTable,  overdueTable,  finesTable;
    private DefaultTableModel activeModel,  overdueModel,  finesModel;

    public ReportsPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 245, 250));

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 12));

        tabs.addTab("📋 Active Loans",  buildActiveLoansTab());
        tabs.addTab("⚠️ Overdue Loans", buildOverdueTab());
        tabs.addTab("💰 Fines",         buildFinesTab());

        // Refresh when switching tabs
        tabs.addChangeListener(e -> {
            switch (tabs.getSelectedIndex()) {
                case 0 -> loadActiveLoans();
                case 1 -> loadOverdueLoans();
                case 2 -> loadFines();
            }
        });

        add(tabs, BorderLayout.CENTER);
        loadActiveLoans();
    }

    // ── ACTIVE LOANS TAB ──────────────────────────────────────

    private JPanel buildActiveLoansTab() {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        p.setBackground(Color.WHITE);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        top.setOpaque(false);
        JButton refresh = makeButton("Refresh", new Color(70, 130, 180));
        refresh.addActionListener(e -> loadActiveLoans());
        top.add(refresh);
        p.add(top, BorderLayout.NORTH);

        String[] cols = {"Loan ID", "Copy ID", "Member ID", "Staff ID", "Issue Date", "Due Date", "Status"};
        activeModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        activeTable = new JTable(activeModel);
        styleTable(activeTable);
        p.add(new JScrollPane(activeTable), BorderLayout.CENTER);
        return p;
    }

    // ── OVERDUE TAB ───────────────────────────────────────────

    private JPanel buildOverdueTab() {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        p.setBackground(Color.WHITE);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        top.setOpaque(false);
        JButton refresh = makeButton("Refresh", new Color(178, 34, 34));
        refresh.addActionListener(e -> loadOverdueLoans());
        top.add(refresh);
        p.add(top, BorderLayout.NORTH);

        String[] cols = {"Loan ID", "Copy ID", "Member ID", "Issue Date", "Due Date", "Days Overdue"};
        overdueModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        overdueTable = new JTable(overdueModel);
        styleTable(overdueTable);

        // Highlight overdue rows in red
        overdueTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                if (!sel) {
                    c.setBackground(new Color(255, 220, 220));
                    c.setForeground(new Color(150, 0, 0));
                }
                return c;
            }
        });

        p.add(new JScrollPane(overdueTable), BorderLayout.CENTER);
        return p;
    }

    // ── FINES TAB ─────────────────────────────────────────────

    private JPanel buildFinesTab() {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        p.setBackground(Color.WHITE);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        top.setOpaque(false);
        JButton refresh  = makeButton("Refresh",      new Color(70, 130, 180));
        JButton markPaid = makeButton("Mark as Paid", new Color(34, 139, 34));
        refresh.addActionListener(e  -> loadFines());
        markPaid.addActionListener(e -> markSelectedFineAsPaid());
        top.add(refresh); top.add(markPaid);
        p.add(top, BorderLayout.NORTH);

        String[] cols = {"Fine ID", "Loan ID", "Amount (Rs.)", "Paid", "Paid Date"};
        finesModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        finesTable = new JTable(finesModel);
        styleTable(finesTable);
        p.add(new JScrollPane(finesTable), BorderLayout.CENTER);
        return p;
    }

    // ── DATA LOADERS ──────────────────────────────────────────

    private void loadActiveLoans() {
        activeModel.setRowCount(0);
        for (Loan l : loanService.getAllActiveLoans()) {
            activeModel.addRow(new Object[]{
                l.getLoanId(), l.getCopyId(), l.getMemberId(),
                l.getStaffId(), l.getIssueDate(), l.getDueDate(), l.getStatus()
            });
        }
    }

    private void loadOverdueLoans() {
        overdueModel.setRowCount(0);
        List<Loan> loans = loanService.getOverdueLoans();
        for (Loan l : loans) {
            long days = java.time.temporal.ChronoUnit.DAYS.between(
                java.time.LocalDate.parse(l.getDueDate()),
                java.time.LocalDate.now()
            );
            overdueModel.addRow(new Object[]{
                l.getLoanId(), l.getCopyId(), l.getMemberId(),
                l.getIssueDate(), l.getDueDate(), days + " days"
            });
        }
    }

    private void loadFines() {
        finesModel.setRowCount(0);
        for (Fine f : fineService.getAllFines()) {
            finesModel.addRow(new Object[]{
                f.getFineId(), f.getLoanId(),
                String.format("%.2f", f.getAmount()),
                f.isPaid() ? "Yes" : "No",
                f.getPaidDate() != null ? f.getPaidDate() : "-"
            });
        }
    }

    private void markSelectedFineAsPaid() {
        int row = finesTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a fine row first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int loanId = (int) finesModel.getValueAt(row, 1);
        try {
            fineService.markFineAsPaid(loanId);
            JOptionPane.showMessageDialog(this, "Fine marked as paid!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadFines();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── HELPERS ───────────────────────────────────────────────

    private void styleTable(JTable t) {
        t.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        t.setRowHeight(22);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private JButton makeButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg); b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}
