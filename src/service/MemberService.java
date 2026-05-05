package service;

import dao.MemberDAO;
import model.Member;

import java.util.List;

/**
 * Service class for Member business logic.
 * Validates member data before any DB operation.
 */
public class MemberService {

    private final MemberDAO memberDAO = new MemberDAO();

    // ── REGISTER ──────────────────────────────────────────────

    public boolean registerMember(Member member) throws Exception {
        if (member.getFullName() == null || member.getFullName().trim().isEmpty())
            throw new Exception("Member name cannot be empty.");
        if (member.getPhone() == null || member.getPhone().trim().isEmpty())
            throw new Exception("Phone number cannot be empty.");
        if (member.getPhone().length() < 10)
            throw new Exception("Phone number must be at least 10 digits.");

        boolean saved = memberDAO.addMember(member);
        if (!saved) throw new Exception("Failed to register member. Email may already exist.");
        return true;
    }

    // ── UPDATE ────────────────────────────────────────────────

    public boolean updateMember(Member member) throws Exception {
        if (member.getFullName() == null || member.getFullName().trim().isEmpty())
            throw new Exception("Member name cannot be empty.");

        boolean updated = memberDAO.updateMember(member);
        if (!updated) throw new Exception("Failed to update member.");
        return true;
    }

    // ── DEACTIVATE ────────────────────────────────────────────

    public boolean deactivateMember(int memberId) throws Exception {
        Member member = memberDAO.getMemberById(memberId);
        if (member == null)
            throw new Exception("Member not found.");
        if (!member.isActive())
            throw new Exception("Member is already inactive.");

        member.setActive(false);
        boolean updated = memberDAO.updateMember(member);
        if (!updated) throw new Exception("Failed to deactivate member.");
        return true;
    }

    // ── READ ──────────────────────────────────────────────────

    public List<Member> getAllMembers() {
        return memberDAO.getAllMembers();
    }

    public Member getMemberById(int memberId) {
        return memberDAO.getMemberById(memberId);
    }

    public List<Member> searchMembers(String keyword) throws Exception {
        if (keyword == null || keyword.trim().isEmpty())
            throw new Exception("Search keyword cannot be empty.");
        return memberDAO.searchMembers(keyword.trim());
    }

    // ── VALIDATION — called by LoanService ───────────────────

    /**
     * KEY VALIDATION: Member must exist AND be active to borrow.
     * Called before every book issue.
     */
    public boolean isActiveMember(int memberId) {
        Member member = memberDAO.getMemberById(memberId);
        return member != null && member.isActive();
    }
}
