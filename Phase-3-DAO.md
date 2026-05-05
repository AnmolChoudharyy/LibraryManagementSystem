# Phase 3 — DAO Layer and Database Connection

## Overview
This phase implements the **Data Access Object (DAO)** layer. All SQL queries live here — no SQL exists anywhere else in the project. The Service layer calls these DAOs; the DAOs never contain business logic.

---

## Package: `util`

### `DBConnection.java`
Provides a single shared SQLite database connection using the **Singleton pattern**.

**Key method:**
```java
DBConnection.getConnection()  // returns shared Connection object
DBConnection.closeConnection() // call on app shutdown
```

**Connection URL:** `jdbc:sqlite:library.db`

---

## Package: `dao`

---

### 1. `BookDAO.java`
All CRUD operations for the `books` table.

| Method | Description |
|--------|-------------|
| `addBook(Book)` | Insert new book |
| `getBookById(int)` | Find book by ID |
| `getBookByIsbn(String)` | Find book by ISBN |
| `getAllBooks()` | Get all books ordered by title |
| `searchBooks(String)` | Search by title, author, or ISBN |
| `updateBook(Book)` | Update book details |
| `deleteBook(int)` | Delete book by ID |

---

### 2. `BookCopyDAO.java`
Manages physical copies — critical for issue/return logic.

| Method | Description |
|--------|-------------|
| `addCopy(BookCopy)` | Add a new physical copy |
| `getCopyById(int)` | Get copy by ID |
| `getCopiesByBookId(int)` | Get all copies of a book |
| `getAvailableCopy(int)` ⭐ | Get one available copy for a book |
| `countAvailableCopies(int)` | Count available copies |
| `updateStatus(int, String)` ⭐ | Change copy status (issued/available) |
| `deleteCopy(int)` | Delete a copy |

⭐ = called during issue/return workflow

---

### 3. `MemberDAO.java`
All CRUD operations for the `members` table.

| Method | Description |
|--------|-------------|
| `addMember(Member)` | Register new member |
| `getMemberById(int)` | Find member by ID |
| `getAllMembers()` | Get all members |
| `getActiveMembers()` | Get only active members |
| `searchMembers(String)` | Search by name, email, phone |
| `updateMember(Member)` | Update member details |
| `deleteMember(int)` | Delete member |

---

### 4. `LoanDAO.java`
The most critical DAO — handles all issue/return transactions.

| Method | Description |
|--------|-------------|
| `addLoan(Loan)` ⭐ | Create new loan (issue book) |
| `getLoanById(int)` | Get loan by ID |
| `getActiveLoanByCopyId(int)` ⭐ | Find active loan for a copy (for return) |
| `getLoansByMember(int)` | Full loan history of a member |
| `getAllActiveLoans()` | All currently issued books |
| `getOverdueLoans()` ⭐ | All loans past due date |
| `getAllLoans()` | Complete transaction history |
| `returnLoan(int, String, String)` ⭐ | Update loan on return |
| `updateStatus(int, String)` | Change loan status |

⭐ = examiner-critical methods

---

### 5. `FineDAO.java`
Manages fine records for overdue loans.

| Method | Description |
|--------|-------------|
| `addFine(Fine)` | Create fine record |
| `getFineByLoanId(int)` | Get fine for a specific loan |
| `getAllUnpaidFines()` | All unpaid fines |
| `getAllFines()` | All fine records |
| `markAsPaid(int, String)` | Mark fine as paid |

---

### 6. `StaffDAO.java`
Staff management and login authentication.

| Method | Description |
|--------|-------------|
| `authenticate(String, String)` ⭐ | Login check — returns Staff or null |
| `addStaff(Staff)` | Add new staff account |
| `getStaffById(int)` | Get staff by ID |
| `getAllStaff()` | Get all staff |
| `updateStaff(Staff)` | Update staff details |
| `deleteStaff(int)` | Delete staff |

---

## DAO Design Pattern Used
Every DAO follows this pattern:
```
1. Get connection from DBConnection.getConnection()
2. Prepare SQL statement with PreparedStatement
3. Set parameters
4. Execute query
5. Map ResultSet to model object (mapRow method)
6. Return result
7. Handle SQLException with try-catch
```

This ensures **no SQL injection** and **clean separation** from business logic.
