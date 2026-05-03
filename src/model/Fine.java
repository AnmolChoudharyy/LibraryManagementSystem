package model;

/**
 * Model class representing a Fine for an overdue Loan.
 * One Fine per Loan (enforced by UNIQUE constraint in DB).
 */
public class Fine {

    private int     fineId;
    private int     loanId;
    private double  amount;
    private boolean isPaid;
    private String  paidDate;   // null if not yet paid

    // ── Constructors ──────────────────────────────────────────

    public Fine() {}

    public Fine(int fineId, int loanId, double amount, boolean isPaid, String paidDate) {
        this.fineId   = fineId;
        this.loanId   = loanId;
        this.amount   = amount;
        this.isPaid   = isPaid;
        this.paidDate = paidDate;
    }

    // Constructor for new fine (no ID, unpaid)
    public Fine(int loanId, double amount) {
        this.loanId = loanId;
        this.amount = amount;
        this.isPaid = false;
    }

    // ── Getters & Setters ─────────────────────────────────────

    public int    getFineId()   { return fineId; }
    public void   setFineId(int fineId) { this.fineId = fineId; }

    public int    getLoanId()   { return loanId; }
    public void   setLoanId(int loanId) { this.loanId = loanId; }

    public double getAmount()   { return amount; }
    public void   setAmount(double amount) { this.amount = amount; }

    public boolean isPaid()     { return isPaid; }
    public void    setPaid(boolean paid) { isPaid = paid; }

    public String getPaidDate() { return paidDate; }
    public void   setPaidDate(String paidDate) { this.paidDate = paidDate; }

    // ── toString ──────────────────────────────────────────────

    @Override
    public String toString() {
        return "Fine{" +
               "fineId=" + fineId +
               ", loanId=" + loanId +
               ", amount=" + amount +
               ", isPaid=" + isPaid +
               ", paidDate='" + paidDate + '\'' +
               '}';
    }
}
