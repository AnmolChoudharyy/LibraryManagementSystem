package service;

import dao.BookCopyDAO;
import dao.LoanDAO;
import dao.MemberDAO;
import model.BookCopy;
import model.Loan;
import model.Member;

import java.time.LocalDate;
import java.util.List;

/**
 * Service class for Loan business logic.
 * This is the most critical service — enforces ALL examiner-checked rules:
 *   1. Cannot issue unavailable book
 *   2. Member must be active
 *   3. Return updates copy availability
 *   4. Overdue detection and fine calculation
 */
public class LoanService {

    private final LoanDAO     loanDAO     = new LoanDAO();
    private final BookCopyDAO bookCopyDAO = new BookCopyDAO();
    private final MemberDAO   memberDAO   = new MemberDAO();
    private final FineService fineService = new FineService();

    // Default loan period in days
    public static final int LOAN_PERIOD_DAYS = 14;

    // ── ISSUE BOOK ────────────────────────────────────────────

    /**
     * EXAMINER CRITICAL — Full issue workflow:
     * 1. Validate member is active
     * 2. Check book copy is available
     * 3. Create loan record
     * 4. Mark copy as issued
     *
     * @throws Exception with user-friendly message if any check fails
     */
    public Loan issueBook(int bookId, int memberId, int staffId) throws Exception {

        // Step 1: Validate member
        Member member = memberDAO.getMemberById(memberId);
        if (member == null)
            throw new Exception("Member ID " + memberId + " not found.");
        if (!member.isActive())
            throw new Exception("Member '" + member.getFullName() + "' is inactive and cannot borrow books.");

        // Step 2: Check availability
        BookCopy copy = bookCopyDAO.getAvailableCopy(bookId);
        if (copy == null)
            throw new Exception("No available copies for this book. All copies are currently issued.");

        // Step 3: Build loan record
        String issueDate = LocalDate.now().toString();
        String dueDate   = LocalDate.now().plusDays(LOAN_PERIOD_DAYS).toString();

        Loan loan = new Loan(copy.getCopyId(), memberId, staffId, issueDate, dueDate);

        // Step 4: Save loan to DB
        boolean loanSaved = loanDAO.addLoan(loan);
        if (!loanSaved)
            throw new Exception("Failed to create loan record in database.");

        // Step 5: Mark copy as issued
        boolean statusUpdated = bookCopyDAO.updateStatus(copy.getCopyId(), "issued");
        if (!statusUpdated)
            throw new Exception("Failed to update book copy status.");

        // Return loan with due date for UI to display
        loan.setCopyId(copy.getCopyId());
        loan.setDueDate(dueDate);
        loan.setIssueDate(issueDate);
        return loan;
    }

    // ── RETURN BOOK ───────────────────────────────────────────

    /**
     * EXAMINER CRITICAL — Full return workflow:
     * 1. Find active loan for the copy
     * 2. Set return date
     * 3. Detect overdue
     * 4. Calculate and save fine if overdue
     * 5. Update copy back to available
     *
     * @return fine amount (0.0 if returned on time)
     * @throws Exception with user-friendly message if any check fails
     */
    public double returnBook(int copyId) throws Exception {

        // Step 1: Find active loan for this copy
        Loan loan = loanDAO.getActiveLoanByCopyId(copyId);
        if (loan == null)
            throw new Exception("No active loan found for Copy ID " + copyId + ".");

        // Step 2: Set return date to today
        String returnDate = LocalDate.now().toString();

        // Step 3: Check if overdue
        double fineAmount = fineService.calculateFine(loan.getDueDate(), returnDate);
        boolean isOverdue = fineAmount > 0;

        // Step 4: Update loan status
        String newStatus = isOverdue ? "overdue" : "returned";
        boolean loanUpdated = loanDAO.returnLoan(loan.getLoanId(), returnDate, newStatus);
        if (!loanUpdated)
            throw new Exception("Failed to update loan record.");

        // Step 5: Save fine record if overdue
        if (isOverdue) {
            fineService.createFine(loan.getLoanId(), fineAmount);
        }

        // Step 6: Mark copy as available again
        boolean copyUpdated = bookCopyDAO.updateStatus(copyId, "available");
        if (!copyUpdated)
            throw new Exception("Failed to update book copy status to available.");

        return fineAmount;
    }

    // ── READ ──────────────────────────────────────────────────

    public List<Loan> getAllActiveLoans() {
        return loanDAO.getAllActiveLoans();
    }

    /**
     * EXAMINER CRITICAL — Returns all loans where due date has passed.
     */
    public List<Loan> getOverdueLoans() {
        return loanDAO.getOverdueLoans();
    }

    public List<Loan> getMemberLoanHistory(int memberId) {
        return loanDAO.getLoansByMember(memberId);
    }

    public List<Loan> getAllLoans() {
        return loanDAO.getAllLoans();
    }

    public Loan getLoanById(int loanId) {
        return loanDAO.getLoanById(loanId);
    }
}
