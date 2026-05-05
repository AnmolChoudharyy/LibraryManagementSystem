# Phase 5 — Java Swing UI and Full Integration

## Overview
This phase implements the complete **Java Swing desktop interface** and integrates all layers. The UI calls only the Service layer — never the DAO directly.

---

## Package: `ui`

---

### `MainFrame.java`
The main application window — a `JFrame` with a `JTabbedPane` containing all panels.

**Tabs:**
1. Books Management
2. Member Management
3. Issue / Return
4. Reports & Fines

**Also contains:** Login dialog on startup — calls `StaffDAO.authenticate()`

---

### `BookPanel.java`
Manages the book catalogue.

**Components:**
- `JTable` — displays all books with columns: ID, Title, Author, ISBN, Genre, Year, Copies
- `JTextField` — search bar (searches by title/author/ISBN)
- Form fields: Title, Author, ISBN, Publisher, Year, Genre, Total Copies
- Buttons: **Add Book**, **Update**, **Delete**, **Search**, **Clear**

**Calls:** `BookService.addBook()`, `updateBook()`, `deleteBook()`, `searchBooks()`

---

### `MemberPanel.java`
Manages library members.

**Components:**
- `JTable` — displays all members: ID, Name, Email, Phone, Status
- Form fields: Full Name, Email, Phone, Address
- Buttons: **Register**, **Update**, **Deactivate**, **Search**, **Clear**

**Calls:** `MemberService.registerMember()`, `updateMember()`, `deactivateMember()`

---

### `IssueReturnPanel.java`
The most important UI panel — handles the core workflow.

**Issue Section:**
- Book ID field + Member ID field + **Issue Book** button
- Shows: book title, member name, due date after issue
- Error message if book unavailable or member inactive

**Return Section:**
- Copy ID field + **Return Book** button
- Shows: return confirmation, days overdue, fine amount if applicable

**Calls:** `LoanService.issueBook()`, `LoanService.returnBook()`

**Validation shown to user:**
```
❌ "Book is not available. All copies are currently issued."
❌ "Member is inactive or not registered."
✅ "Book issued successfully. Due date: 2026-05-17"
✅ "Book returned. Fine: ₹14 (7 days overdue)"
```

---

### `ReportsPanel.java`
Displays reports and fine management.

**Tabs inside:**
1. **Active Loans** — JTable of all currently issued books
2. **Overdue Loans** — JTable of all overdue loans (highlighted in red)
3. **Fines** — JTable of all fines with paid/unpaid status + **Mark Paid** button

**Calls:** `LoanService.getActiveLoans()`, `getOverdueLoans()`, `FineService.getAllUnpaidFines()`, `markFineAsPaid()`

---

## UI Architecture

```
MainFrame (JFrame)
│
├── LoginDialog         → StaffDAO.authenticate()
│
└── JTabbedPane
    ├── BookPanel       → BookService
    ├── MemberPanel     → MemberService
    ├── IssueReturnPanel→ LoanService
    └── ReportsPanel    → LoanService + FineService
```

---

## Full System Integration Flow

```
User clicks "Issue Book"
        ↓
IssueReturnPanel.issueButtonClicked()
        ↓
LoanService.issueBook(bookId, memberId, staffId)
        ↓
MemberDAO.getMemberById()    → validate member
BookCopyDAO.getAvailableCopy() → check availability
LoanDAO.addLoan()            → save to DB
BookCopyDAO.updateStatus()   → mark as issued
        ↓
UI shows success message with due date
```

---

## How to Run

```bash
# 1. Make sure library.db exists (run schema.sql first)
sqlite3 library.db < sql/schema.sql

# 2. Compile all source files
javac -cp "lib/*" -d out/ src/model/*.java src/util/*.java src/dao/*.java src/service/*.java src/ui/*.java

# 3. Run the application
java -cp "lib/*:out/" ui.MainFrame
```

**Default Login:**
```
Username: admin
Password: admin123
```
