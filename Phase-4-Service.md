# Phase 4 — Service Layer (Business Logic)

## Overview
This phase implements the **Service layer** which sits between the UI and DAO layers. All business rules, validations, and logic live here. The UI never talks to DAO directly.

---

## Package: `service`

---

### 1. `BookService.java`
Business logic for book management.

| Method | Validation/Logic |
|--------|-----------------|
| `addBook(Book)` | Validates title, author not empty; auto-creates copies |
| `updateBook(Book)` | Validates fields before update |
| `deleteBook(int)` | Checks no active loans before delete |
| `getAllBooks()` | Delegates to BookDAO |
| `searchBooks(String)` | Delegates to BookDAO |
| `getAvailableCopyCount(int)` | Returns count from BookCopyDAO |

---

### 2. `MemberService.java`
Business logic for member management.

| Method | Validation/Logic |
|--------|-----------------|
| `registerMember(Member)` | Validates name, phone not empty |
| `updateMember(Member)` | Validates fields |
| `deactivateMember(int)` | Checks no active loans before deactivating |
| `getAllMembers()` | Delegates to MemberDAO |
| `isActiveMember(int)` ⭐ | Returns true only if member exists and is_active=1 |

⭐ = called before every book issue

---

### 3. `LoanService.java`
The most critical service — enforces all examiner-checked rules.

#### `issueBook(int bookId, int memberId, int staffId)`
```
Step 1: Check member exists and is active          → throws if not
Step 2: Find available copy using BookCopyDAO      → throws if none available  
Step 3: Create Loan with issue_date and due_date   → due = issue + 14 days
Step 4: Save loan via LoanDAO.addLoan()
Step 5: Update copy status to 'issued' via BookCopyDAO.updateStatus()
Step 6: Return success
```

#### `returnBook(int copyId, int staffId)`
```
Step 1: Find active loan for this copy             → throws if not found
Step 2: Set return_date = today
Step 3: Check if overdue (return_date > due_date)
Step 4: If overdue → calculate fine, create Fine record, set status='overdue'
Step 5: If on time → set status='returned'
Step 6: Update copy status back to 'available'
Step 7: Return fine amount (0 if no fine)
```

| Method | Description |
|--------|-------------|
| `issueBook(int, int, int)` ⭐ | Full issue workflow with all checks |
| `returnBook(int, int)` ⭐ | Full return workflow with overdue check |
| `getActiveLoans()` | All currently issued books |
| `getOverdueLoans()` ⭐ | All loans past due date |
| `getMemberLoanHistory(int)` | Full history for a member |

---

### 4. `FineService.java`
Calculates and manages overdue fines.

**Fine Rate:** ₹2 per day overdue (configurable constant)

| Method | Description |
|--------|-------------|
| `calculateFine(String dueDate, String returnDate)` ⭐ | Returns fine amount based on days overdue |
| `getAllUnpaidFines()` | List of all pending fines |
| `markFineAsPaid(int loanId)` | Records payment with today's date |
| `getFineForLoan(int loanId)` | Gets fine details for a loan |

**Fine calculation logic:**
```java
long daysOverdue = ChronoUnit.DAYS.between(dueDate, returnDate);
double fineAmount = daysOverdue * FINE_PER_DAY; // ₹2 per day
```

---

## Key Business Rules Enforced

| Rule | Where | How |
|------|-------|-----|
| Cannot issue unavailable book | `LoanService.issueBook()` | Checks `getAvailableCopy()` returns non-null |
| Member must be active to borrow | `LoanService.issueBook()` | Checks `isActiveMember()` |
| Return updates copy to available | `LoanService.returnBook()` | Calls `updateStatus("available")` |
| Overdue auto-detected on return | `LoanService.returnBook()` | Compares return_date vs due_date |
| Fine auto-calculated | `FineService.calculateFine()` | Days × ₹2 per day |

---

## Flow Diagram

```
UI calls LoanService.issueBook(bookId, memberId, staffId)
         │
         ├── MemberDAO.getMemberById()     → member exists?
         ├── member.isActive()             → member active?
         ├── BookCopyDAO.getAvailableCopy()→ copy available?
         ├── LoanDAO.addLoan()             → save transaction
         └── BookCopyDAO.updateStatus()    → mark as 'issued'
```
