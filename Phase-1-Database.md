# Phase 1 — Database Schema and Project Setup

## Overview
This phase sets up the complete project structure and SQLite database schema with all 6 required tables.

---

## Project Folder Structure

```
LibraryManagementSystem/
├── src/
│   ├── model/       Entity/POJO classes
│   ├── dao/         Data Access Objects
│   ├── service/     Business logic
│   ├── ui/          Java Swing UI
│   └── util/        DBConnection, utilities
├── sql/
│   └── schema.sql   Database schema + seed data
├── test/            JUnit test cases
├── docs/            UML and documentation
├── lib/             SQLite JDBC jar
├── README.md
└── .gitignore
```

---

## Database Tables

### 1. `staff`
Stores librarian and admin login details.

| Column     | Type    | Constraint          |
|------------|---------|---------------------|
| staff_id   | INTEGER | PRIMARY KEY         |
| full_name  | TEXT    | NOT NULL            |
| username   | TEXT    | NOT NULL UNIQUE      |
| password   | TEXT    | NOT NULL            |
| role       | TEXT    | admin / librarian   |
| email      | TEXT    |                     |
| phone      | TEXT    |                     |
| created_at | TEXT    | DEFAULT datetime()  |

---

### 2. `members`
Library card holders who borrow books.

| Column      | Type    | Constraint         |
|-------------|---------|--------------------|
| member_id   | INTEGER | PRIMARY KEY        |
| full_name   | TEXT    | NOT NULL           |
| email       | TEXT    | UNIQUE             |
| phone       | TEXT    |                    |
| address     | TEXT    |                    |
| joined_date | TEXT    | DEFAULT date()     |
| is_active   | INTEGER | 1=active, 0=suspended |

---

### 3. `books`
Master catalogue — one row per book title.

| Column       | Type    | Constraint     |
|--------------|---------|----------------|
| book_id      | INTEGER | PRIMARY KEY    |
| title        | TEXT    | NOT NULL       |
| author       | TEXT    | NOT NULL       |
| isbn         | TEXT    | UNIQUE         |
| publisher    | TEXT    |                |
| year         | INTEGER |                |
| genre        | TEXT    |                |
| total_copies | INTEGER | DEFAULT 1      |

---

### 4. `book_copies`
Each physical copy of a book title.

| Column      | Type    | Constraint                              |
|-------------|---------|------------------------------------------|
| copy_id     | INTEGER | PRIMARY KEY                              |
| book_id     | INTEGER | FOREIGN KEY → books                      |
| copy_number | INTEGER | DEFAULT 1                                |
| status      | TEXT    | available / issued / lost / damaged      |

---

### 5. `loans`
Records every issue and return transaction.

| Column      | Type    | Constraint                        |
|-------------|---------|-----------------------------------|
| loan_id     | INTEGER | PRIMARY KEY                       |
| copy_id     | INTEGER | FOREIGN KEY → book_copies         |
| member_id   | INTEGER | FOREIGN KEY → members             |
| staff_id    | INTEGER | FOREIGN KEY → staff               |
| issue_date  | TEXT    | DEFAULT date()                    |
| due_date    | TEXT    | issue_date + 14 days              |
| return_date | TEXT    | NULL until returned               |
| status      | TEXT    | active / returned / overdue       |

---

### 6. `fines`
One row per overdue loan.

| Column    | Type    | Constraint              |
|-----------|---------|-------------------------|
| fine_id   | INTEGER | PRIMARY KEY             |
| loan_id   | INTEGER | FOREIGN KEY → loans, UNIQUE |
| amount    | REAL    | calculated by service   |
| is_paid   | INTEGER | 0=unpaid, 1=paid        |
| paid_date | TEXT    | NULL until paid         |

---

## Entity Relationship

```
books ──< book_copies >── loans >── fines
                              |
members ──────────────────────┘
staff ─────────────────────────┘
```

---

## Seed Data Included
- 2 staff accounts (admin + librarian)
- 3 sample members
- 3 sample books with 9 total copies
