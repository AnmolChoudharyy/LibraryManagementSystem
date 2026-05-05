# Phase 2 — Model / Entity Classes

## Overview
This phase implements all 6 POJO (Plain Old Java Object) model classes that represent the database entities in Java. These classes are used across all layers of the architecture.

---

## Package: `model`

---

### 1. `Book.java`
Represents a book title in the library catalogue.

**Fields:**
| Field        | Type   | Description                  |
|--------------|--------|------------------------------|
| bookId       | int    | Unique book ID               |
| title        | String | Book title                   |
| author       | String | Author name                  |
| isbn         | String | ISBN number (unique)         |
| publisher    | String | Publisher name               |
| year         | int    | Publication year             |
| genre        | String | Genre/category               |
| totalCopies  | int    | Total physical copies        |

---

### 2. `BookCopy.java`
Represents one physical copy of a book.

**Fields:**
| Field       | Type   | Description                            |
|-------------|--------|----------------------------------------|
| copyId      | int    | Unique copy ID                         |
| bookId      | int    | FK → Book                              |
| copyNumber  | int    | Copy number (1st, 2nd copy etc.)       |
| status      | String | available / issued / lost / damaged    |

**Helper method:** `isAvailable()` — returns true if status is "available"

---

### 3. `Member.java`
Represents a registered library member.

**Fields:**
| Field       | Type    | Description              |
|-------------|---------|--------------------------|
| memberId    | int     | Unique member ID         |
| fullName    | String  | Full name                |
| email       | String  | Email address            |
| phone       | String  | Phone number             |
| address     | String  | Home address             |
| joinedDate  | String  | Date of registration     |
| isActive    | boolean | Active or suspended      |

---

### 4. `Loan.java`
Represents a book issue/return transaction — the heart of the system.

**Fields:**
| Field       | Type   | Description                      |
|-------------|--------|----------------------------------|
| loanId      | int    | Unique loan ID                   |
| copyId      | int    | FK → BookCopy                    |
| memberId    | int    | FK → Member                      |
| staffId     | int    | FK → Staff (who processed it)    |
| issueDate   | String | Date book was issued             |
| dueDate     | String | Date book must be returned       |
| returnDate  | String | Actual return date (null if not returned) |
| status      | String | active / returned / overdue      |

**Helper methods:** `isActive()`, `isReturned()`, `isOverdue()`

---

### 5. `Fine.java`
Represents a fine for an overdue loan.

**Fields:**
| Field     | Type    | Description              |
|-----------|---------|--------------------------|
| fineId    | int     | Unique fine ID           |
| loanId    | int     | FK → Loan (UNIQUE)       |
| amount    | double  | Fine amount in ₹         |
| isPaid    | boolean | Paid or unpaid           |
| paidDate  | String  | Date fine was paid       |

---

### 6. `Staff.java`
Represents a librarian or admin who operates the system.

**Fields:**
| Field     | Type   | Description              |
|-----------|--------|--------------------------|
| staffId   | int    | Unique staff ID          |
| fullName  | String | Full name                |
| username  | String | Login username           |
| password  | String | Login password           |
| role      | String | admin / librarian        |
| email     | String | Email address            |
| phone     | String | Phone number             |
| createdAt | String | Account creation date    |

**Helper method:** `isAdmin()` — returns true if role is "admin"

---

## Design Notes
- All classes have a **no-arg constructor** (required for DAO mapping)
- All classes have **constructors with and without ID** (with ID for SELECT, without for INSERT)
- All fields have **getters and setters**
- All classes override **toString()** for easy debugging
