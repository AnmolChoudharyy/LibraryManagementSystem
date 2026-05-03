# Library Management System

A Desktop Library Management System built with **Java Swing**, following a clean **4-layer architecture** (UI → Service → DAO → Database).

---

## Project Info

| Field        | Detail                          |
|--------------|---------------------------------|
| Language     | Java 17+                        |
| UI Framework | Java Swing                      |
| Database     | SQLite (via JDBC)               |
| Architecture | UI / Service / DAO / Model      |
| Build Tool   | Manual (no Maven required)      |

---

## Architecture

```
UI Layer (Java Swing)
        ↓
Service Layer (Business Logic)
        ↓
DAO Layer (Database Access)
        ↓
SQLite Database
```

---

## Package Structure

```
src/
├── model/          Entity classes (Book, Member, Loan, Fine, Staff, BookCopy)
├── dao/            Data Access Objects — all SQL lives here
├── service/        Business logic — validations, rules
├── ui/             Java Swing panels and frames
└── util/           DBConnection, DateUtil, Constants
sql/
└── schema.sql      Database schema + seed data
test/
└── (JUnit tests — Phase 6)
docs/
└── (UML diagrams — Phase 3)
```

---

## Core Features

- Add / update / delete books and manage physical copies
- Register and manage library members
- Issue books to members (with availability check)
- Return books (updates copy status automatically)
- Due date tracking (default: 14 days loan period)
- Overdue detection and automatic fine calculation
- Reports: active loans, overdue list, member history

---

## Key Business Rules (Examiner-Critical)

| Rule | Where enforced |
|------|---------------|
| Cannot issue an unavailable book | `LoanService` |
| Return updates copy status to available | `LoanService` |
| Overdue detection on return | `LoanService` |
| Fine auto-calculated (₹2/day default) | `FineService` |
| Member must be active to borrow | `MemberService` |

---

## Database Tables

| Table        | Purpose                              |
|--------------|--------------------------------------|
| `books`      | Book catalogue (title, author, ISBN) |
| `book_copies`| Physical copies with availability    |
| `members`    | Registered library members           |
| `loans`      | Issue / return transaction log       |
| `fines`      | Overdue fine records                 |
| `staff`      | Librarian / admin accounts           |

---

## Setup & Run

### 1. Database Setup
```bash
# Run schema (SQLite)
sqlite3 library.db < sql/schema.sql
```

### 2. Add SQLite JDBC Driver
Download `sqlite-jdbc-x.x.x.jar` from https://github.com/xerial/sqlite-jdbc/releases  
Place it in a `lib/` folder.

### 3. Compile
```bash
javac -cp "lib/*" -d out/ src/**/*.java
```

### 4. Run
```bash
java -cp "lib/*:out/" ui.MainFrame
```

---

## Phase Completion

| Phase | Description              | Status  |
|-------|--------------------------|---------|
| 1     | Repo setup + DB schema   | ✅ Done  |
| 2     | Model classes            | 🔲 Next  |
| 3     | DAO layer                | 🔲       |
| 4     | Service layer            | 🔲       |
| 5     | Java Swing UI            | 🔲       |
| 6     | JUnit tests              | 🔲       |

---

## Default Login

| Username | Password  | Role      |
|----------|-----------|-----------|
| admin    | admin123  | Admin     |
| ravi     | lib123    | Librarian |

> Change credentials before submitting final project.
