package dao;

import model.Staff;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for Staff entity.
 * Handles login authentication and staff management.
 */
public class StaffDAO {

    // ── AUTH — login ──────────────────────────────────────────

    /** Used by login screen — returns Staff if credentials match, null otherwise */
    public Staff authenticate(String username, String password) {
        String sql = "SELECT * FROM staff WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            System.err.println("StaffDAO.authenticate error: " + e.getMessage());
        }
        return null;  // null = login failed
    }

    // ── CREATE ────────────────────────────────────────────────

    public boolean addStaff(Staff staff) {
        String sql = "INSERT INTO staff (full_name, username, password, role, email, phone) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, staff.getFullName());
            ps.setString(2, staff.getUsername());
            ps.setString(3, staff.getPassword());
            ps.setString(4, staff.getRole());
            ps.setString(5, staff.getEmail());
            ps.setString(6, staff.getPhone());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("StaffDAO.addStaff error: " + e.getMessage());
            return false;
        }
    }

    // ── READ ──────────────────────────────────────────────────

    public Staff getStaffById(int staffId) {
        String sql = "SELECT * FROM staff WHERE staff_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, staffId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            System.err.println("StaffDAO.getStaffById error: " + e.getMessage());
        }
        return null;
    }

    public List<Staff> getAllStaff() {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT * FROM staff ORDER BY full_name";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) staffList.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("StaffDAO.getAllStaff error: " + e.getMessage());
        }
        return staffList;
    }

    // ── UPDATE ────────────────────────────────────────────────

    public boolean updateStaff(Staff staff) {
        String sql = "UPDATE staff SET full_name=?, username=?, password=?, role=?, email=?, phone=? " +
                     "WHERE staff_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, staff.getFullName());
            ps.setString(2, staff.getUsername());
            ps.setString(3, staff.getPassword());
            ps.setString(4, staff.getRole());
            ps.setString(5, staff.getEmail());
            ps.setString(6, staff.getPhone());
            ps.setInt   (7, staff.getStaffId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("StaffDAO.updateStaff error: " + e.getMessage());
            return false;
        }
    }

    // ── DELETE ────────────────────────────────────────────────

    public boolean deleteStaff(int staffId) {
        String sql = "DELETE FROM staff WHERE staff_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, staffId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("StaffDAO.deleteStaff error: " + e.getMessage());
            return false;
        }
    }

    // ── HELPER ────────────────────────────────────────────────

    private Staff mapRow(ResultSet rs) throws SQLException {
        return new Staff(
            rs.getInt   ("staff_id"),
            rs.getString("full_name"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("role"),
            rs.getString("email"),
            rs.getString("phone"),
            rs.getString("created_at")
        );
    }
}
