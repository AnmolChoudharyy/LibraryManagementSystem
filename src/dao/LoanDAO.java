package dao;

import model.Loan;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for Loan entity.
 * Handles issue, return, and overdue queries — heart of the system.
 */
public class LoanDAO {

    // ── CREATE — issue a book ─────────────────────────────────

    public boolean addLoan(Loan loan) {
        String sql = "INSERT INTO loans (copy_id, member_id, staff_id, issue_date, due_date, status) " +
                     "VALUES (?, ?, ?, ?, ?, 'active')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt   (1, loan.getCopyId());
            ps.setInt   (2, loan.getMemberId());
            ps.setInt   (3, loan.getStaffId());
            ps.setString(4, loan.getIssueDate());
            ps.setString(5, loan.getDueDate());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("LoanDAO.addLoan error: " + e.getMessage());
            return false;
        }
    }

    // ── READ ──────────────────────────────────────────────────

    public Loan getLoanById(int loanId) {
        String sql = "SELECT * FROM loans WHERE loan_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, loanId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            System.err.println("LoanDAO.getLoanById error: " + e.getMessage());
        }
        return null;
    }

    /** Get the active loan for a specific copy (used during return) */
    public Loan getActiveLoanByCopyId(int copyId) {
        String sql = "SELECT * FROM loans WHERE copy_id = ? AND status = 'active'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, copyId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            System.err.println("LoanDAO.getActiveLoanByCopyId error: " + e.getMessage());
        }
        return null;
    }

    public List<Loan> getLoansByMember(int memberId) {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans WHERE member_id = ? ORDER BY issue_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, memberId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) loans.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("LoanDAO.getLoansByMember error: " + e.getMessage());
        }
        return loans;
    }

    public List<Loan> getAllActiveLoans() {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans WHERE status = 'active' ORDER BY due_date";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) loans.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("LoanDAO.getAllActiveLoans error: " + e.getMessage());
        }
        return loans;
    }

    /** Returns all loans where due_date has passed and still active — for overdue detection */
    public List<Loan> getOverdueLoans() {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans WHERE status = 'active' AND due_date < date('now')";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) loans.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("LoanDAO.getOverdueLoans error: " + e.getMessage());
        }
        return loans;
    }

    public List<Loan> getAllLoans() {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans ORDER BY issue_date DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) loans.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("LoanDAO.getAllLoans error: " + e.getMessage());
        }
        return loans;
    }

    // ── UPDATE — return a book ────────────────────────────────

    public boolean returnLoan(int loanId, String returnDate, String status) {
        String sql = "UPDATE loans SET return_date = ?, status = ? WHERE loan_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, returnDate);
            ps.setString(2, status);
            ps.setInt   (3, loanId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("LoanDAO.returnLoan error: " + e.getMessage());
            return false;
        }
    }

    public boolean updateStatus(int loanId, String status) {
        String sql = "UPDATE loans SET status = ? WHERE loan_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt   (2, loanId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("LoanDAO.updateStatus error: " + e.getMessage());
            return false;
        }
    }

    // ── HELPER ────────────────────────────────────────────────

    private Loan mapRow(ResultSet rs) throws SQLException {
        return new Loan(
            rs.getInt   ("loan_id"),
            rs.getInt   ("copy_id"),
            rs.getInt   ("member_id"),
            rs.getInt   ("staff_id"),
            rs.getString("issue_date"),
            rs.getString("due_date"),
            rs.getString("return_date"),
            rs.getString("status")
        );
    }
}
