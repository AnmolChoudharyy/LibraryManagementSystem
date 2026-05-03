package dao;

import model.Member;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for Member entity.
 * All SQL related to the 'members' table lives here.
 */
public class MemberDAO {

    // ── CREATE ────────────────────────────────────────────────

    public boolean addMember(Member member) {
        String sql = "INSERT INTO members (full_name, email, phone, address) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, member.getFullName());
            ps.setString(2, member.getEmail());
            ps.setString(3, member.getPhone());
            ps.setString(4, member.getAddress());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("MemberDAO.addMember error: " + e.getMessage());
            return false;
        }
    }

    // ── READ ──────────────────────────────────────────────────

    public Member getMemberById(int memberId) {
        String sql = "SELECT * FROM members WHERE member_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, memberId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            System.err.println("MemberDAO.getMemberById error: " + e.getMessage());
        }
        return null;
    }

    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members ORDER BY full_name";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) members.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("MemberDAO.getAllMembers error: " + e.getMessage());
        }
        return members;
    }

    public List<Member> searchMembers(String keyword) {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members WHERE full_name LIKE ? OR email LIKE ? OR phone LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ps.setString(3, kw);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) members.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("MemberDAO.searchMembers error: " + e.getMessage());
        }
        return members;
    }

    public List<Member> getActiveMembers() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members WHERE is_active = 1 ORDER BY full_name";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) members.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("MemberDAO.getActiveMembers error: " + e.getMessage());
        }
        return members;
    }

    // ── UPDATE ────────────────────────────────────────────────

    public boolean updateMember(Member member) {
        String sql = "UPDATE members SET full_name=?, email=?, phone=?, address=?, is_active=? " +
                     "WHERE member_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString (1, member.getFullName());
            ps.setString (2, member.getEmail());
            ps.setString (3, member.getPhone());
            ps.setString (4, member.getAddress());
            ps.setInt    (5, member.isActive() ? 1 : 0);
            ps.setInt    (6, member.getMemberId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("MemberDAO.updateMember error: " + e.getMessage());
            return false;
        }
    }

    // ── DELETE ────────────────────────────────────────────────

    public boolean deleteMember(int memberId) {
        String sql = "DELETE FROM members WHERE member_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, memberId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("MemberDAO.deleteMember error: " + e.getMessage());
            return false;
        }
    }

    // ── HELPER ────────────────────────────────────────────────

    private Member mapRow(ResultSet rs) throws SQLException {
        return new Member(
            rs.getInt    ("member_id"),
            rs.getString ("full_name"),
            rs.getString ("email"),
            rs.getString ("phone"),
            rs.getString ("address"),
            rs.getString ("joined_date"),
            rs.getInt    ("is_active") == 1
        );
    }
}
