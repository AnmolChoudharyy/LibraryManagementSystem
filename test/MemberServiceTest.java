import model.Member;
import service.MemberService;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit tests for MemberService.
 * Tests: register member, validation rules, isActiveMember check.
 */
public class MemberServiceTest {

    private MemberService memberService;

    @Before
    public void setUp() {
        memberService = new MemberService();
    }

    // ── TEST 1: Register valid member ────────────────────────

    @Test
    public void testRegisterValidMember() {
        Member m = new Member("Test User", "testuser@email.com",
                              "9876543210", "Test City");
        try {
            boolean result = memberService.registerMember(m);
            assertTrue("Valid member should register successfully", result);
        } catch (Exception e) {
            fail("No exception expected: " + e.getMessage());
        }
    }

    // ── TEST 2: Empty name rejected ──────────────────────────

    @Test
    public void testRegisterMemberEmptyNameThrowsException() {
        Member m = new Member("", "email@test.com", "9876543210", "City");
        try {
            memberService.registerMember(m);
            fail("Exception expected for empty name");
        } catch (Exception e) {
            assertEquals("Member name cannot be empty.", e.getMessage());
        }
    }

    // ── TEST 3: Empty phone rejected ─────────────────────────

    @Test
    public void testRegisterMemberEmptyPhoneThrowsException() {
        Member m = new Member("Valid Name", "email@test.com", "", "City");
        try {
            memberService.registerMember(m);
            fail("Exception expected for empty phone");
        } catch (Exception e) {
            assertEquals("Phone number cannot be empty.", e.getMessage());
        }
    }

    // ── TEST 4: Short phone rejected ─────────────────────────

    @Test
    public void testRegisterMemberShortPhoneThrowsException() {
        Member m = new Member("Valid Name", "email@test.com", "12345", "City");
        try {
            memberService.registerMember(m);
            fail("Exception expected for short phone number");
        } catch (Exception e) {
            assertEquals("Phone number must be at least 10 digits.", e.getMessage());
        }
    }

    // ── TEST 5: isActiveMember returns false for invalid ID ──

    @Test
    public void testIsActiveMemberReturnsFalseForInvalidId() {
        // ID 99999 should not exist in DB
        boolean result = memberService.isActiveMember(99999);
        assertFalse("Non-existent member should return false", result);
    }

    // ── TEST 6: Get all members not null ─────────────────────

    @Test
    public void testGetAllMembersNotNull() {
        assertNotNull("getAllMembers() should not return null",
                      memberService.getAllMembers());
    }

    // ── TEST 7: Deactivate non-existent member fails ─────────

    @Test
    public void testDeactivateNonExistentMemberThrowsException() {
        try {
            memberService.deactivateMember(99999);
            fail("Exception expected for non-existent member");
        } catch (Exception e) {
            assertEquals("Member not found.", e.getMessage());
        }
    }
}
