package service;

import dao.StaffDAO;
import model.Staff;

import java.util.List;

/**
 * Service class for Staff authentication and management.
 */
public class StaffService {

    private final StaffDAO staffDAO = new StaffDAO();

    // ── LOGIN ─────────────────────────────────────────────────

    /**
     * Authenticates a staff member.
     * @return Staff object if login successful, null if failed
     */
    public Staff login(String username, String password) throws Exception {
        if (username == null || username.trim().isEmpty())
            throw new Exception("Username cannot be empty.");
        if (password == null || password.trim().isEmpty())
            throw new Exception("Password cannot be empty.");

        Staff staff = staffDAO.authenticate(username.trim(), password.trim());
        if (staff == null)
            throw new Exception("Invalid username or password.");

        return staff;
    }

    // ── ADD ───────────────────────────────────────────────────

    public boolean addStaff(Staff staff) throws Exception {
        if (staff.getFullName() == null || staff.getFullName().trim().isEmpty())
            throw new Exception("Staff name cannot be empty.");
        if (staff.getUsername() == null || staff.getUsername().trim().isEmpty())
            throw new Exception("Username cannot be empty.");
        if (staff.getPassword() == null || staff.getPassword().length() < 4)
            throw new Exception("Password must be at least 4 characters.");

        boolean saved = staffDAO.addStaff(staff);
        if (!saved) throw new Exception("Failed to add staff. Username may already exist.");
        return true;
    }

    // ── READ ──────────────────────────────────────────────────

    public List<Staff> getAllStaff() {
        return staffDAO.getAllStaff();
    }

    public Staff getStaffById(int staffId) {
        return staffDAO.getStaffById(staffId);
    }

    // ── UPDATE ────────────────────────────────────────────────

    public boolean updateStaff(Staff staff) throws Exception {
        if (staff.getFullName() == null || staff.getFullName().trim().isEmpty())
            throw new Exception("Staff name cannot be empty.");

        boolean updated = staffDAO.updateStaff(staff);
        if (!updated) throw new Exception("Failed to update staff.");
        return true;
    }

    // ── DELETE ────────────────────────────────────────────────

    public boolean deleteStaff(int staffId) throws Exception {
        boolean deleted = staffDAO.deleteStaff(staffId);
        if (!deleted) throw new Exception("Failed to delete staff.");
        return true;
    }
}
