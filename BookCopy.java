package model;

/**
 * Model class representing a single physical copy of a Book.
 * status: "available" | "issued" | "lost" | "damaged"
 */
public class BookCopy {

    private int    copyId;
    private int    bookId;
    private int    copyNumber;
    private String status;

    // ── Constructors ──────────────────────────────────────────

    public BookCopy() {}

    public BookCopy(int copyId, int bookId, int copyNumber, String status) {
        this.copyId     = copyId;
        this.bookId     = bookId;
        this.copyNumber = copyNumber;
        this.status     = status;
    }

    // Constructor without ID (for INSERT)
    public BookCopy(int bookId, int copyNumber, String status) {
        this.bookId     = bookId;
        this.copyNumber = copyNumber;
        this.status     = status;
    }

    // ── Getters & Setters ─────────────────────────────────────

    public int    getCopyId()     { return copyId; }
    public void   setCopyId(int copyId) { this.copyId = copyId; }

    public int    getBookId()     { return bookId; }
    public void   setBookId(int bookId) { this.bookId = bookId; }

    public int    getCopyNumber() { return copyNumber; }
    public void   setCopyNumber(int copyNumber) { this.copyNumber = copyNumber; }

    public String getStatus()     { return status; }
    public void   setStatus(String status) { this.status = status; }

    // ── Helper ────────────────────────────────────────────────

    public boolean isAvailable() {
        return "available".equalsIgnoreCase(status);
    }

    // ── toString ──────────────────────────────────────────────

    @Override
    public String toString() {
        return "BookCopy{" +
               "copyId=" + copyId +
               ", bookId=" + bookId +
               ", copyNumber=" + copyNumber +
               ", status='" + status + '\'' +
               '}';
    }
}
