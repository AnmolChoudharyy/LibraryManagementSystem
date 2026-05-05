package service;

import dao.BookCopyDAO;
import dao.BookDAO;
import model.Book;
import model.BookCopy;

import java.util.List;

/**
 * Service class for Book business logic.
 * UI calls this — never calls DAO directly.
 */
public class BookService {

    private final BookDAO     bookDAO     = new BookDAO();
    private final BookCopyDAO bookCopyDAO = new BookCopyDAO();

    // ── ADD ───────────────────────────────────────────────────

    public boolean addBook(Book book) throws Exception {
        if (book.getTitle() == null || book.getTitle().trim().isEmpty())
            throw new Exception("Book title cannot be empty.");
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty())
            throw new Exception("Author name cannot be empty.");
        if (book.getTotalCopies() <= 0)
            throw new Exception("Total copies must be at least 1.");

        boolean saved = bookDAO.addBook(book);
        if (!saved) throw new Exception("Failed to save book to database.");

        // Auto-create BookCopy records for each copy
        Book saved_book = bookDAO.getBookByIsbn(book.getIsbn());
        if (saved_book != null) {
            for (int i = 1; i <= book.getTotalCopies(); i++) {
                bookCopyDAO.addCopy(new BookCopy(saved_book.getBookId(), i, "available"));
            }
        }
        return true;
    }

    // ── UPDATE ────────────────────────────────────────────────

    public boolean updateBook(Book book) throws Exception {
        if (book.getTitle() == null || book.getTitle().trim().isEmpty())
            throw new Exception("Book title cannot be empty.");
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty())
            throw new Exception("Author name cannot be empty.");

        boolean updated = bookDAO.updateBook(book);
        if (!updated) throw new Exception("Failed to update book.");
        return true;
    }

    // ── DELETE ────────────────────────────────────────────────

    public boolean deleteBook(int bookId) throws Exception {
        int available = bookCopyDAO.countAvailableCopies(bookId);
        List<BookCopy> allCopies = bookCopyDAO.getCopiesByBookId(bookId);

        // Only delete if all copies are available (none currently issued)
        if (available < allCopies.size())
            throw new Exception("Cannot delete: some copies are currently issued.");

        boolean deleted = bookDAO.deleteBook(bookId);
        if (!deleted) throw new Exception("Failed to delete book.");
        return true;
    }

    // ── READ ──────────────────────────────────────────────────

    public List<Book> getAllBooks() {
        return bookDAO.getAllBooks();
    }

    public Book getBookById(int bookId) {
        return bookDAO.getBookById(bookId);
    }

    public List<Book> searchBooks(String keyword) throws Exception {
        if (keyword == null || keyword.trim().isEmpty())
            throw new Exception("Search keyword cannot be empty.");
        return bookDAO.searchBooks(keyword.trim());
    }

    public int getAvailableCopyCount(int bookId) {
        return bookCopyDAO.countAvailableCopies(bookId);
    }
}
