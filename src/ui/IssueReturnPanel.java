package ui;

import model.Loan;
import model.Staff;
import service.LoanService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * UI Panel for Issue and Return workflow.
 * The most important panel — core library workflow.
 */
public class IssueReturnPanel extends JPanel {

    private final LoanService loanService;
    private final Staff       loggedInStaff;

    // Issue fields
    private JTextField bookIdField, memberIdField;
    private JTextArea  issueResultArea;

    // Return fields
    private JTextField copyIdField;
    private JTextArea  returnResultArea;

    public IssueReturnPanel(Staff staff) {
        this.loggedInStaff = staff;
        this.loanService   = new LoanService();

        setLayout(new GridLayout(1, 2, 20, 0));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(245, 245, 250));

        add(buildIssuePanel());
        add(buildReturnPanel());
    }

    // ── ISSUE PANEL ───────────────────────────────────────────

    private JPanel buildIssuePanel() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(34, 139, 34), 2),
            "  Issue Book  ", TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14), new Color(34, 139, 34)));
        p.setBackground(Color.WHITE);

        // Form
        JPanel form = new JPanel(new GridLayout(4, 2, 8, 12));
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        form.add(makeLabel("Book ID:"));
        bookIdField = makeField();
        form.add(bookIdField);

        form.add(makeLabel("Member ID:"));
        memberIdField = makeField();
        form.add(memberIdField);

        form.add(makeLabel("Loan Period:"));
        form.add(makeLabel("14 days (due date auto-set)"));

        JButton issueBtn = makeButton("Issue Book", new Color(34, 139, 34));
        issueBtn.setPreferredSize(new Dimension(0, 40));
        issueBtn.addActionListener(e -> issueBook());
        form.add(new JLabel());
        form.add(issueBtn);

        p.add(form, BorderLayout.NORTH);

        // Result area
        issueResultArea = new JTextArea(6, 20);
        issueResultArea.setEditable(false);
        issueResultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        issueResultArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        issueResultArea.setBackground(new Color(240, 255, 240));
        p.add(new JScrollPane(issueResultArea), BorderLayout.CENTER);

        return p;
    }

    // ── RETURN PANEL ──────────────────────────────────────────

    private JPanel buildReturnPanel() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            "  Return Book  ", TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14), new Color(70, 130, 180)));
        p.setBackground(Color.WHITE);

        // Form
        JPanel form = new JPanel(new GridLayout(4, 2, 8, 12));
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        form.add(makeLabel("Copy ID:"));
        copyIdField = makeField();
        form.add(copyIdField);

        form.add(makeLabel("Fine Rate:"));
        form.add(makeLabel("Rs. 2 per day overdue"));

        form.add(new JLabel());
        form.add(new JLabel());

        JButton returnBtn = makeButton("Return Book", new Color(70, 130, 180));
        returnBtn.setPreferredSize(new Dimension(0, 40));
        returnBtn.addActionListener(e -> returnBook());
        form.add(new JLabel());
        form.add(returnBtn);

        p.add(form, BorderLayout.NORTH);

        // Result area
        returnResultArea = new JTextArea(6, 20);
        returnResultArea.setEditable(false);
        returnResultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        returnResultArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        returnResultArea.setBackground(new Color(240, 248, 255));
        p.add(new JScrollPane(returnResultArea), BorderLayout.CENTER);

        return p;
    }

    // ── ACTIONS ───────────────────────────────────────────────

    private void issueBook() {
        try {
            int bookId   = Integer.parseInt(bookIdField.getText().trim());
            int memberId = Integer.parseInt(memberIdField.getText().trim());
            int staffId  = loggedInStaff != null ? loggedInStaff.getStaffId() : 1;

            Loan loan = loanService.issueBook(bookId, memberId, staffId);

            issueResultArea.setForeground(new Color(0, 100, 0));
            issueResultArea.setText(
                "✅ Book Issued Successfully!\n" +
                "─────────────────────────\n" +
                "Copy ID    : " + loan.getCopyId()   + "\n" +
                "Member ID  : " + memberId            + "\n" +
                "Issue Date : " + loan.getIssueDate() + "\n" +
                "Due Date   : " + loan.getDueDate()   + "\n" +
                "─────────────────────────\n" +
                "Please return before due date."
            );
            bookIdField.setText(""); memberIdField.setText("");

        } catch (NumberFormatException ex) {
            showResultError(issueResultArea, "❌ Enter valid numeric Book ID and Member ID.");
        } catch (Exception ex) {
            showResultError(issueResultArea, "❌ " + ex.getMessage());
        }
    }

    private void returnBook() {
        try {
            int copyId = Integer.parseInt(copyIdField.getText().trim());
            double fine = loanService.returnBook(copyId);

            returnResultArea.setForeground(new Color(0, 70, 130));
            if (fine > 0) {
                returnResultArea.setText(
                    "⚠️ Book Returned — OVERDUE\n" +
                    "─────────────────────────\n" +
                    "Copy ID    : " + copyId        + "\n" +
                    "Return Date: " + java.time.LocalDate.now() + "\n" +
                    "─────────────────────────\n" +
                    "Fine Amount: Rs. " + String.format("%.2f", fine) + "\n" +
                    "Please collect fine from member."
                );
            } else {
                returnResultArea.setText(
                    "✅ Book Returned Successfully!\n" +
                    "─────────────────────────\n" +
                    "Copy ID    : " + copyId        + "\n" +
                    "Return Date: " + java.time.LocalDate.now() + "\n" +
                    "─────────────────────────\n" +
                    "No fine. Returned on time."
                );
            }
            copyIdField.setText("");

        } catch (NumberFormatException ex) {
            showResultError(returnResultArea, "❌ Enter a valid numeric Copy ID.");
        } catch (Exception ex) {
            showResultError(returnResultArea, "❌ " + ex.getMessage());
        }
    }

    // ── HELPERS ───────────────────────────────────────────────

    private void showResultError(JTextArea area, String msg) {
        area.setForeground(Color.RED);
        area.setText(msg);
    }

    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return l;
    }

    private JTextField makeField() {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return f;
    }

    private JButton makeButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg); b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}
