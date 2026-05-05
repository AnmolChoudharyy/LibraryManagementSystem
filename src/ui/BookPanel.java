package ui;

import model.Book;
import service.BookService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * UI Panel for Book management.
 * Add, update, delete, search books.
 */
public class BookPanel extends JPanel {

    private final BookService bookService = new BookService();

    private JTable            table;
    private DefaultTableModel tableModel;

    private JTextField titleField, authorField, isbnField,
                       publisherField, yearField, genreField, copiesField, searchField;

    private int selectedBookId = -1;

    public BookPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 245, 250));
        add(buildTopSearchBar(), BorderLayout.NORTH);
        add(buildTable(),        BorderLayout.CENTER);
        add(buildFormPanel(),    BorderLayout.SOUTH);
        loadAllBooks();
    }

    // ── SEARCH BAR ────────────────────────────────────────────

    private JPanel buildTopSearchBar() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        p.setOpaque(false);
        p.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        p.add(searchField);
        JButton searchBtn = makeButton("Search", new Color(70, 130, 180));
        searchBtn.addActionListener(e -> searchBooks());
        p.add(searchBtn);
        JButton showAllBtn = makeButton("Show All", new Color(100, 100, 100));
        showAllBtn.addActionListener(e -> loadAllBooks());
        p.add(showAllBtn);
        return p;
    }

    // ── TABLE ─────────────────────────────────────────────────

    private JScrollPane buildTable() {
        String[] cols = {"ID", "Title", "Author", "ISBN", "Genre", "Year", "Total Copies", "Available"};
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

    // ── FORM ──────────────────────────────────────────────────

    private JPanel buildFormPanel() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 6));
        wrapper.setOpaque(false);

        JPanel fields = new JPanel(new GridLayout(2, 7, 6, 6));
        fields.setOpaque(false);

        titleField     = addField(fields, "Title *");
        authorField    = addField(fields, "Author *");
        isbnField      = addField(fields, "ISBN");
        publisherField = addField(fields, "Publisher");
        yearField      = addField(fields, "Year");
        genreField     = addField(fields, "Genre");
        copiesField    = addField(fields, "Copies *");

        wrapper.add(fields, BorderLayout.CENTER);
        wrapper.add(buildButtonRow(), BorderLayout.SOUTH);
        return wrapper;
    }

    private JPanel buildButtonRow() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 4));
        p.setOpaque(false);

        JButton addBtn    = makeButton("Add Book",    new Color(34, 139, 34));
        JButton updateBtn = makeButton("Update",      new Color(70, 130, 180));
        JButton deleteBtn = makeButton("Delete",      new Color(178, 34, 34));
        JButton clearBtn  = makeButton("Clear Form",  new Color(120, 120, 120));

        addBtn.addActionListener(e    -> addBook());
        updateBtn.addActionListener(e -> updateBook());
        deleteBtn.addActionListener(e -> deleteBook());
        clearBtn.addActionListener(e  -> clearForm());

        p.add(addBtn); p.add(updateBtn); p.add(deleteBtn); p.add(clearBtn);
        return p;
    }

    // ── ACTIONS ───────────────────────────────────────────────

    private void addBook() {
        try {
            Book book = buildBookFromForm();
            bookService.addBook(book);
            showSuccess("Book added successfully!");
            clearForm();
            loadAllBooks();
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void updateBook() {
        if (selectedBookId == -1) { showError("Select a book from the table first."); return; }
        try {
            Book book = buildBookFromForm();
            book.setBookId(selectedBookId);
            bookService.updateBook(book);
            showSuccess("Book updated successfully!");
            loadAllBooks();
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void deleteBook() {
        if (selectedBookId == -1) { showError("Select a book from the table first."); return; }
        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete this book and all its copies?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            bookService.deleteBook(selectedBookId);
            showSuccess("Book deleted successfully!");
            clearForm();
            loadAllBooks();
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void searchBooks() {
        try {
            List<Book> books = bookService.searchBooks(searchField.getText());
            populateTable(books);
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    // ── TABLE LOAD ────────────────────────────────────────────

    private void loadAllBooks() {
        populateTable(bookService.getAllBooks());
    }

    private void populateTable(List<Book> books) {
        tableModel.setRowCount(0);
        for (Book b : books) {
            int available = bookService.getAvailableCopyCount(b.getBookId());
            tableModel.addRow(new Object[]{
                b.getBookId(), b.getTitle(), b.getAuthor(),
                b.getIsbn(), b.getGenre(), b.getYear(),
                b.getTotalCopies(), available
            });
        }
    }

    private void populateFormFromTable() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        selectedBookId  = (int) tableModel.getValueAt(row, 0);
        titleField.setText    ((String) tableModel.getValueAt(row, 1));
        authorField.setText   ((String) tableModel.getValueAt(row, 2));
        isbnField.setText     (tableModel.getValueAt(row, 3) != null ? tableModel.getValueAt(row, 3).toString() : "");
        genreField.setText    (tableModel.getValueAt(row, 4) != null ? tableModel.getValueAt(row, 4).toString() : "");
        yearField.setText     (tableModel.getValueAt(row, 5).toString());
        copiesField.setText   (tableModel.getValueAt(row, 6).toString());
        publisherField.setText("");
    }

    // ── HELPERS ───────────────────────────────────────────────

    private Book buildBookFromForm() throws Exception {
        String title   = titleField.getText().trim();
        String author  = authorField.getText().trim();
        String isbn    = isbnField.getText().trim();
        String pub     = publisherField.getText().trim();
        String genre   = genreField.getText().trim();
        int    year    = 0;
        int    copies  = 1;
        try { year   = Integer.parseInt(yearField.getText().trim());   } catch (Exception ignored) {}
        try { copies = Integer.parseInt(copiesField.getText().trim()); } catch (Exception ignored) {}
        return new Book(title, author, isbn, pub, year, genre, copies);
    }

    private void clearForm() {
        selectedBookId = -1;
        titleField.setText(""); authorField.setText(""); isbnField.setText("");
        publisherField.setText(""); yearField.setText(""); genreField.setText("");
        copiesField.setText(""); table.clearSelection();
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
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
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
