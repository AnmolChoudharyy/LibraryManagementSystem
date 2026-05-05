import service.FineService;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit tests for FineService.
 * Tests: fine calculation logic, on-time return, overdue detection.
 */
public class FineServiceTest {

    private FineService fineService;

    @Before
    public void setUp() {
        fineService = new FineService();
    }

    // ── TEST 1: No fine for on-time return ───────────────────
    // EXAMINER CRITICAL

    @Test
    public void testNoFineForOnTimeReturn() {
        // Due: 2026-05-10, Returned: 2026-05-10 (same day)
        double fine = fineService.calculateFine("2026-05-10", "2026-05-10");
        assertEquals("On-time return should have zero fine", 0.0, fine, 0.001);
    }

    // ── TEST 2: No fine for early return ─────────────────────

    @Test
    public void testNoFineForEarlyReturn() {
        // Due: 2026-05-10, Returned: 2026-05-08 (2 days early)
        double fine = fineService.calculateFine("2026-05-10", "2026-05-08");
        assertEquals("Early return should have zero fine", 0.0, fine, 0.001);
    }

    // ── TEST 3: Fine calculated for 1 day overdue ────────────
    // EXAMINER CRITICAL

    @Test
    public void testFineForOneDayOverdue() {
        // Due: 2026-05-10, Returned: 2026-05-11 (1 day late)
        double fine = fineService.calculateFine("2026-05-10", "2026-05-11");
        assertEquals("1 day overdue should give Rs.2 fine",
                     2.0, fine, 0.001);
    }

    // ── TEST 4: Fine calculated for 7 days overdue ───────────

    @Test
    public void testFineForSevenDaysOverdue() {
        // Due: 2026-05-01, Returned: 2026-05-08 (7 days late)
        double fine = fineService.calculateFine("2026-05-01", "2026-05-08");
        assertEquals("7 days overdue should give Rs.14 fine",
                     14.0, fine, 0.001);
    }

    // ── TEST 5: Fine calculated for 14 days overdue ──────────

    @Test
    public void testFineForFourteenDaysOverdue() {
        // Due: 2026-04-01, Returned: 2026-04-15 (14 days late)
        double fine = fineService.calculateFine("2026-04-01", "2026-04-15");
        assertEquals("14 days overdue should give Rs.28 fine",
                     28.0, fine, 0.001);
    }

    // ── TEST 6: Fine rate is Rs.2 per day ────────────────────

    @Test
    public void testFineRateIsCorrect() {
        assertEquals("Fine rate should be Rs.2 per day",
                     2.0, FineService.FINE_PER_DAY, 0.001);
    }

    // ── TEST 7: Get all fines returns list ───────────────────

    @Test
    public void testGetAllFinesNotNull() {
        assertNotNull("getAllFines() should not return null",
                      fineService.getAllFines());
    }

    // ── TEST 8: Get unpaid fines returns list ────────────────

    @Test
    public void testGetUnpaidFinesNotNull() {
        assertNotNull("getAllUnpaidFines() should not return null",
                      fineService.getAllUnpaidFines());
    }

    // ── TEST 9: Mark paid for non-existent loan fails ────────

    @Test
    public void testMarkPaidNonExistentLoanFails() {
        try {
            fineService.markFineAsPaid(99999);
            fail("Exception expected for non-existent loan fine");
        } catch (Exception e) {
            assertEquals("No fine found for this loan.", e.getMessage());
        }
    }
}
