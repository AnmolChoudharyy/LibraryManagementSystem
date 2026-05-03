package model;

/**
 * Model class representing a Loan transaction.
 * Created when a book is issued; updated when returned.
 * status: "active" | "returned" | "overdue"
 */
public class Loan {

    private int    loanId;
    private int    copyId;
    private int    memberId;
    private int    staffId;
    private String issueDate;
    private String dueDate;
    private String returnDate;   // null if not yet returned
    private String status;

    // ── Constructors ──────────────────────────────────────────

    public Loan() {}

    public Loan(int loanId, int copyId, int memberId, int staffId,
                String issueDate, String dueDate, String returnDate, String status) {
        this.loanId     = loanId;
        this.copyId     = copyId;
        this.memberId   = memberId;
        this.staffId    = staffId;
        this.issueDate  = issueDate;
        this.dueDate    = dueDate;
        this.returnDate = returnDate;
        this.status     = status;
    }

    // Constructor for new issue (no ID, no return date)
    public Loan(int copyId, int memberId, int staffId,
                String issueDate, String dueDate) {
        this.copyId    = copyId;
        this.memberId  = memberId;
        this.staffId   = staffId;
        this.issueDate = issueDate;
        this.dueDate   = dueDate;
        this.status    = "active";
    }

    // ── Getters & Setters ─────────────────────────────────────

    public int    getLoanId()     { return loanId; }
    public void   setLoanId(int loanId) { this.loanId = loanId; }

    public int    getCopyId()     { return copyId; }
    public void   setCopyId(int copyId) { this.copyId = copyId; }

    public int    getMemberId()   { return memberId; }
    public void   setMemberId(int memberId) { this.memberId = memberId; }

    public int    getStaffId()    { return staffId; }
    public void   setStaffId(int staffId) { this.staffId = staffId; }

    public String getIssueDate()  { return issueDate; }
    public void   setIssueDate(String issueDate) { this.issueDate = issueDate; }

    public String getDueDate()    { return dueDate; }
    public void   setDueDate(String dueDate) { this.dueDate = dueDate; }

    public String getReturnDate() { return returnDate; }
    public void   setReturnDate(String returnDate) { this.returnDate = returnDate; }

    public String getStatus()     { return status; }
    public void   setStatus(String status) { this.status = status; }

    // ── Helpers ───────────────────────────────────────────────

    public boolean isReturned() { return "returned".equalsIgnoreCase(status); }
    public boolean isOverdue()  { return "overdue".equalsIgnoreCase(status);  }
    public boolean isActive()   { return "active".equalsIgnoreCase(status);   }

    // ── toString ──────────────────────────────────────────────

    @Override
    public String toString() {
        return "Loan{" +
               "loanId=" + loanId +
               ", copyId=" + copyId +
               ", memberId=" + memberId +
               ", issueDate='" + issueDate + '\'' +
               ", dueDate='" + dueDate + '\'' +
               ", returnDate='" + returnDate + '\'' +
               ", status='" + status + '\'' +
               '}';
    }
}
