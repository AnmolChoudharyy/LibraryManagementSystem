import service.StaffService;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit tests for StaffService.
 * Tests: login validation, wrong credentials, empty fields.
 */
public class StaffServiceTest {

    private StaffService staffService;

    @Before
    public void setUp() {
        staffService = new StaffService();
    }

    // ── TEST 1: Valid login succeeds ─────────────────────────

    @Test
    public void testValidLoginReturnsStaff() {
        try {
            assertNotNull("Valid login should return Staff object",
                          staffService.login("admin", "admin123"));
        } catch (Exception e) {
            fail("No exception expected for valid credentials: " + e.getMessage());
        }
    }

    // ── TEST 2: Wrong password fails ─────────────────────────

    @Test
    public void testWrongPasswordThrowsException() {
        try {
            staffService.login("admin", "wrongpassword");
            fail("Exception expected for wrong password");
        } catch (Exception e) {
            assertEquals("Invalid username or password.", e.getMessage());
        }
    }

    // ── TEST 3: Wrong username fails ─────────────────────────

    @Test
    public void testWrongUsernameThrowsException() {
        try {
            staffService.login("nobody", "admin123");
            fail("Exception expected for wrong username");
        } catch (Exception e) {
            assertEquals("Invalid username or password.", e.getMessage());
        }
    }

    // ── TEST 4: Empty username rejected ──────────────────────

    @Test
    public void testEmptyUsernameThrowsException() {
        try {
            staffService.login("", "admin123");
            fail("Exception expected for empty username");
        } catch (Exception e) {
            assertEquals("Username cannot be empty.", e.getMessage());
        }
    }

    // ── TEST 5: Empty password rejected ──────────────────────

    @Test
    public void testEmptyPasswordThrowsException() {
        try {
            staffService.login("admin", "");
            fail("Exception expected for empty password");
        } catch (Exception e) {
            assertEquals("Password cannot be empty.", e.getMessage());
        }
    }

    // ── TEST 6: Get all staff not null ───────────────────────

    @Test
    public void testGetAllStaffNotNull() {
        assertNotNull("getAllStaff() should not return null",
                      staffService.getAllStaff());
    }

    // ── TEST 7: Librarian login works ────────────────────────

    @Test
    public void testLibrarianLoginReturnsStaff() {
        try {
            assertNotNull("Librarian login should return Staff object",
                          staffService.login("ravi", "lib123"));
        } catch (Exception e) {
            fail("No exception expected for librarian credentials: " + e.getMessage());
        }
    }
}
