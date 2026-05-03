package dao;

import model.Fine;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for Fine entity.
 * All SQL related to the 'fines' table lives here.
 */
public class FineDAO {

    // ── CREATE ────────────────────────────────────────────────

    public boolean addFine(Fine fine) {
        String sql = "INSERT INTO fines (loan_id, amount, is_paid) VALUES (?, ?, 0)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt   (1, fine.getLoanId());
            ps.setDouble(2, fine.getAmount());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("FineDAO.addFine error: " + e.getMessage());
            return false;
        }
    }

    // ── READ ──────────────────────────────────────────────────

    public Fine getFineByLoanId(int loanId) {
        String sql = "SELECT * FROM fines WHERE loan_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, loanId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            System.err.println("FineDAO.getFineByLoanId error: " + e.getMessage());
        }
        return null;
    }

    public List<Fine> getAllUnpaidFines() {
        List<Fine> fines = new ArrayList<>();
        String sql = "SELECT * FROM fines WHERE is_paid = 0";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) fines.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("FineDAO.getAllUnpaidFines error: " + e.getMessage());
        }
        return fines;
    }

    public List<Fine> getAllFines() {
        List<Fine> fines = new ArrayList<>();
        String sql = "SELECT * FROM fines ORDER BY fine_id DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) fines.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("FineDAO.getAllFines error: " + e.getMessage());
        }
        return fines;
    }

    // ── UPDATE — mark fine as paid ────────────────────────────

    public boolean markAsPaid(int loanId, String paidDate) {
        String sql = "UPDATE fines SET is_paid = 1, paid_date = ? WHERE loan_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, paidDate);
            ps.setInt   (2, loanId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("FineDAO.markAsPaid error: " + e.getMessage());
            return false;
        }
    }

    // ── HELPER ────────────────────────────────────────────────

    private Fine mapRow(ResultSet rs) throws SQLException {
        return new Fine(
            rs.getInt   ("fine_id"),
            rs.getInt   ("loan_id"),
            rs.getDouble("amount"),
            rs.getInt   ("is_paid") == 1,
            rs.getString("paid_date")
        );
    }
}
