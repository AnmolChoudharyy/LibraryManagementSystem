package dao;

import model.Book;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for Book entity.
 * All SQL related to the 'books' table lives here.
 */
public class BookDAO {

    // ── CREATE ────────────────────────────────────────────────

    public boolean addBook(Book book) {
        String sql = "INSERT INTO books (title, author, isbn, publisher, year, genre, total_copies) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setString(4, book.getPublisher());
            ps.setInt   (5, book.getYear());
            ps.setString(6, book.getGenre());
            ps.setInt   (7, book.getTotalCopies());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("BookDAO.addBook error: " + e.getMessage());
            return false;
        }
    }

    // ── READ — single ─────────────────────────────────────────

    public Book getBookById(int bookId) {
        String sql = "SELECT * FROM books WHERE book_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            System.err.println("BookDAO.getBookById error: " + e.getMessage());
        }
        return null;
    }

    public Book getBookByIsbn(String isbn) {
        String sql = "SELECT * FROM books WHERE isbn = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            System.err.println("BookDAO.getBookByIsbn error: " + e.getMessage());
        }
        return null;
    }

    // ── READ — all ────────────────────────────────────────────

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books ORDER BY title";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) books.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("BookDAO.getAllBooks error: " + e.getMessage());
        }
        return books;
    }

    public List<Book> searchBooks(String keyword) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR isbn LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ps.setString(3, kw);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) books.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("BookDAO.searchBooks error: " + e.getMessage());
        }
        return books;
    }

    // ── UPDATE ────────────────────────────────────────────────

    public boolean updateBook(Book book) {
        String sql = "UPDATE books SET title=?, author=?, isbn=?, publisher=?, " +
                     "year=?, genre=?, total_copies=? WHERE book_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setString(4, book.getPublisher());
            ps.setInt   (5, book.getYear());
            ps.setString(6, book.getGenre());
            ps.setInt   (7, book.getTotalCopies());
            ps.setInt   (8, book.getBookId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("BookDAO.updateBook error: " + e.getMessage());
            return false;
        }
    }

    // ── DELETE ────────────────────────────────────────────────

    public boolean deleteBook(int bookId) {
        String sql = "DELETE FROM books WHERE book_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("BookDAO.deleteBook error: " + e.getMessage());
            return false;
        }
    }

    // ── HELPER — map ResultSet row to Book object ─────────────

    private Book mapRow(ResultSet rs) throws SQLException {
        return new Book(
            rs.getInt   ("book_id"),
            rs.getString("title"),
            rs.getString("author"),
            rs.getString("isbn"),
            rs.getString("publisher"),
            rs.getInt   ("year"),
            rs.getString("genre"),
            rs.getInt   ("total_copies")
        );
    }
}
