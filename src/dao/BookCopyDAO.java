package dao;

import model.BookCopy;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for BookCopy entity.
 * Handles physical copy availability — critical for issue/return logic.
 */
public class BookCopyDAO {

    // ── CREATE ────────────────────────────────────────────────

    public boolean addCopy(BookCopy copy) {
        String sql = "INSERT INTO book_copies (book_id, copy_number, status) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt   (1, copy.getBookId());
            ps.setInt   (2, copy.getCopyNumber());
            ps.setString(3, copy.getStatus());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("BookCopyDAO.addCopy error: " + e.getMessage());
            return false;
        }
    }

    // ── READ ──────────────────────────────────────────────────

    public BookCopy getCopyById(int copyId) {
        String sql = "SELECT * FROM book_copies WHERE copy_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, copyId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            System.err.println("BookCopyDAO.getCopyById error: " + e.getMessage());
        }
        return null;
    }

    public List<BookCopy> getCopiesByBookId(int bookId) {
        List<BookCopy> copies = new ArrayList<>();
        String sql = "SELECT * FROM book_copies WHERE book_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) copies.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("BookCopyDAO.getCopiesByBookId error: " + e.getMessage());
        }
        return copies;
    }

    /**
     * KEY METHOD — finds one available copy for a given book.
     * Called by LoanService before issuing a book.
     */
    public BookCopy getAvailableCopy(int bookId) {
        String sql = "SELECT * FROM book_copies WHERE book_id = ? AND status = 'available' LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            System.err.println("BookCopyDAO.getAvailableCopy error: " + e.getMessage());
        }
        return null;  // null = no available copy
    }

    public int countAvailableCopies(int bookId) {
        String sql = "SELECT COUNT(*) FROM book_copies WHERE book_id = ? AND status = 'available'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            System.err.println("BookCopyDAO.countAvailableCopies error: " + e.getMessage());
        }
        return 0;
    }

    // ── UPDATE STATUS ─────────────────────────────────────────

    public boolean updateStatus(int copyId, String status) {
        String sql = "UPDATE book_copies SET status = ? WHERE copy_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt   (2, copyId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("BookCopyDAO.updateStatus error: " + e.getMessage());
            return false;
        }
    }

    // ── DELETE ────────────────────────────────────────────────

    public boolean deleteCopy(int copyId) {
        String sql = "DELETE FROM book_copies WHERE copy_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, copyId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("BookCopyDAO.deleteCopy error: " + e.getMessage());
            return false;
        }
    }

    // ── HELPER ────────────────────────────────────────────────

    private BookCopy mapRow(ResultSet rs) throws SQLException {
        return new BookCopy(
            rs.getInt   ("copy_id"),
            rs.getInt   ("book_id"),
            rs.getInt   ("copy_number"),
            rs.getString("status")
        );
    }
}
