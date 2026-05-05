package service;

import dao.FineDAO;
import model.Fine;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Service class for Fine calculation and management.
 * Fine rate: Rs. 2 per day overdue.
 */
public class FineService {

    private final FineDAO fineDAO = new FineDAO();

    // Fine rate — change this constant to adjust rate
    public static final double FINE_PER_DAY = 2.0;

    // ── CALCULATE ─────────────────────────────────────────────

    /**
     * KEY METHOD — calculates fine based on overdue days.
     * Called by LoanService during return.
     * Returns 0.0 if returned on time.
     */
    public double calculateFine(String dueDateStr, String returnDateStr) {
        try {
            LocalDate dueDate    = LocalDate.parse(dueDateStr);
            LocalDate returnDate = LocalDate.parse(returnDateStr);

            long daysOverdue = ChronoUnit.DAYS.between(dueDate, returnDate);

            if (daysOverdue <= 0) return 0.0;  // on time, no fine

            return daysOverdue * FINE_PER_DAY;

        } catch (Exception e) {
            System.err.println("FineService.calculateFine error: " + e.getMessage());
            return 0.0;
        }
    }

    // ── CREATE ────────────────────────────────────────────────

    public boolean createFine(int loanId, double amount) throws Exception {
        if (amount <= 0)
            throw new Exception("Fine amount must be greater than 0.");

        Fine fine = new Fine(loanId, amount);
        boolean saved = fineDAO.addFine(fine);
        if (!saved) throw new Exception("Failed to save fine record.");
        return true;
    }

    // ── PAY ───────────────────────────────────────────────────

    public boolean markFineAsPaid(int loanId) throws Exception {
        Fine fine = fineDAO.getFineByLoanId(loanId);
        if (fine == null)
            throw new Exception("No fine found for this loan.");
        if (fine.isPaid())
            throw new Exception("Fine is already paid.");

        String today = LocalDate.now().toString();
        boolean updated = fineDAO.markAsPaid(loanId, today);
        if (!updated) throw new Exception("Failed to mark fine as paid.");
        return true;
    }

    // ── READ ──────────────────────────────────────────────────

    public List<Fine> getAllUnpaidFines() {
        return fineDAO.getAllUnpaidFines();
    }

    public List<Fine> getAllFines() {
        return fineDAO.getAllFines();
    }

    public Fine getFineForLoan(int loanId) {
        return fineDAO.getFineByLoanId(loanId);
    }
}
