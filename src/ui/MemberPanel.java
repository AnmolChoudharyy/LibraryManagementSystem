package ui;

import model.Member;
import service.MemberService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * UI Panel for Member management.
 * Register, update, deactivate, search members.
 */
public class MemberPanel extends JPanel {

    private final MemberService memberService = new MemberService();

    private JTable            table;
    private DefaultTableModel tableModel;

    private JTextField nameField, emailField, phoneField, addressField, searchField;
    private int selectedMemberId = -1;

    public MemberPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 245, 250));
        add(buildSearchBar(), BorderLayout.NORTH);
        add(buildTable(),     BorderLayout.CENTER);
        add(buildForm(),      BorderLayout.SOUTH);
        loadAllMembers();
    }

    private JPanel buildSearchBar() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        p.setOpaque(false);
        p.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        p.add(searchField);
        JButton srch = makeButton("Search",   new Color(70, 130, 180));
        JButton all  = makeButton("Show All", new Color(100, 100, 100));
        srch.addActionListener(e -> searchMembers());
        all.addActionListener(e  -> loadAllMembers());
        p.add(srch); p.add(all);
        return p;
    }

    private JScrollPane buildTable() {
        String[] cols = {"ID", "Full Name", "Email", "Phone", "Address", "Joined", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(22);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) populateFormFromTable();
        });
        return new JScrollPane(table);
    }

    private JPanel buildForm() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 6));
        wrapper.setOpaque(false);

        JPanel fields = new JPanel(new GridLayout(2, 4, 6, 6));
        fields.setOpaque(false);

        nameField    = addField(fields, "Full Name *");
        emailField   = addField(fields, "Email");
        phoneField   = addField(fields, "Phone *");
        addressField = addField(fields, "Address");
        // fill remaining cells
        fields.add(new JLabel()); fields.add(new JLabel());
        fields.add(new JLabel()); fields.add(new JLabel());

        wrapper.add(fields, BorderLayout.CENTER);
        wrapper.add(buildButtonRow(), BorderLayout.SOUTH);
        return wrapper;
    }

    private JPanel buildButtonRow() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 4));
        p.setOpaque(false);

        JButton regBtn   = makeButton("Register",   new Color(34, 139, 34));
        JButton updBtn   = makeButton("Update",     new Color(70, 130, 180));
        JButton deacBtn  = makeButton("Deactivate", new Color(178, 34, 34));
        JButton clrBtn   = makeButton("Clear",      new Color(120, 120, 120));

        regBtn.addActionListener(e  -> registerMember());
        updBtn.addActionListener(e  -> updateMember());
        deacBtn.addActionListener(e -> deactivateMember());
        clrBtn.addActionListener(e  -> clearForm());

        p.add(regBtn); p.add(updBtn); p.add(deacBtn); p.add(clrBtn);
        return p;
    }

    // ── ACTIONS ───────────────────────────────────────────────

    private void registerMember() {
        try {
            Member m = new Member(nameField.getText().trim(), emailField.getText().trim(),
                                  phoneField.getText().trim(), addressField.getText().trim());
            memberService.registerMember(m);
            showSuccess("Member registered successfully!");
            clearForm(); loadAllMembers();
        } catch (Exception ex) { showError(ex.getMessage()); }
    }

    private void updateMember() {
        if (selectedMemberId == -1) { showError("Select a member from the table first."); return; }
        try {
            Member m = memberService.getMemberById(selectedMemberId);
            m.setFullName(nameField.getText().trim());
            m.setEmail   (emailField.getText().trim());
            m.setPhone   (phoneField.getText().trim());
            m.setAddress (addressField.getText().trim());
            memberService.updateMember(m);
            showSuccess("Member updated successfully!");
            loadAllMembers();
        } catch (Exception ex) { showError(ex.getMessage()); }
    }

    private void deactivateMember() {
        if (selectedMemberId == -1) { showError("Select a member from the table first."); return; }
        int confirm = JOptionPane.showConfirmDialog(this,
            "Deactivate this member?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            memberService.deactivateMember(selectedMemberId);
            showSuccess("Member deactivated.");
            clearForm(); loadAllMembers();
        } catch (Exception ex) { showError(ex.getMessage()); }
    }

    private void searchMembers() {
        try {
            populateTable(memberService.searchMembers(searchField.getText()));
        } catch (Exception ex) { showError(ex.getMessage()); }
    }

    private void loadAllMembers() {
        populateTable(memberService.getAllMembers());
    }

    private void populateTable(List<Member> members) {
        tableModel.setRowCount(0);
        for (Member m : members) {
            tableModel.addRow(new Object[]{
                m.getMemberId(), m.getFullName(), m.getEmail(),
                m.getPhone(), m.getAddress(), m.getJoinedDate(),
                m.isActive() ? "Active" : "Inactive"
            });
        }
    }

    private void populateFormFromTable() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        selectedMemberId = (int) tableModel.getValueAt(row, 0);
        nameField.setText   (tableModel.getValueAt(row, 1).toString());
        emailField.setText  (tableModel.getValueAt(row, 2) != null ? tableModel.getValueAt(row, 2).toString() : "");
        phoneField.setText  (tableModel.getValueAt(row, 3) != null ? tableModel.getValueAt(row, 3).toString() : "");
        addressField.setText(tableModel.getValueAt(row, 4) != null ? tableModel.getValueAt(row, 4).toString() : "");
    }

    private void clearForm() {
        selectedMemberId = -1;
        nameField.setText(""); emailField.setText("");
        phoneField.setText(""); addressField.setText("");
        table.clearSelection();
    }

    private JTextField addField(JPanel panel, String label) {
        panel.add(new JLabel(label));
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(f);
        return f;
    }

    private JButton makeButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg); b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void showSuccess(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
