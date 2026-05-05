# Phase 0 — Project Proposal and Backlog

**Project Name:** Desktop Library Management System  
**Team Member:** Anmol Choudhary  
**Institution:** CLD Project  
**Technology:** Java Swing, SQLite, JDBC  

---

## Problem Statement

Many small libraries and educational institutions still manage books, members, and loan records manually using registers and paper files. This leads to:
- Difficulty tracking which books are available
- No system to detect overdue returns
- No automatic fine calculation
- Loss of transaction records over time

---

## Proposed Solution

A **Desktop Library Management System** built in Java that allows librarians to:
- Manage the full book catalogue with physical copies
- Register and manage members
- Issue and return books with due date tracking
- Automatically detect overdue loans and calculate fines
- Generate reports on active loans and overdue records

---

## Architecture

The system follows a strict **4-layer architecture**:

```
UI Layer        →  Java Swing (panels and frames)
Service Layer   →  Business logic and validations
DAO Layer       →  All database queries (SQL)
Database        →  SQLite via JDBC
```

---

## Core Entities

| Entity     | Description                              |
|------------|------------------------------------------|
| Book       | Title, author, ISBN, genre               |
| BookCopy   | Physical copy with availability status   |
| Member     | Registered library card holder           |
| Loan       | Issue/return transaction record          |
| Fine       | Overdue fine record                      |
| Staff      | Librarian/admin with login               |

---

## Product Backlog

| ID  | User Story                                                        | Priority |
|-----|-------------------------------------------------------------------|----------|
| US1 | As a librarian, I want to add/edit/delete books                   | High     |
| US2 | As a librarian, I want to register new members                    | High     |
| US3 | As a librarian, I want to issue a book to a member                | High     |
| US4 | As a librarian, I want to return a book and update availability   | High     |
| US5 | As a librarian, I want the system to prevent issuing unavailable books | High |
| US6 | As a librarian, I want to see overdue loans automatically         | High     |
| US7 | As a librarian, I want fines calculated automatically             | Medium   |
| US8 | As an admin, I want to log in securely                            | Medium   |
| US9 | As a librarian, I want to search books by title/author/ISBN       | Medium   |
| US10| As a librarian, I want to view a member's full loan history       | Low      |

---

## Phase Plan

| Phase | Deliverable                        |
|-------|------------------------------------|
| 0     | Proposal + Backlog (this document) |
| 1     | Database schema + project setup    |
| 2     | Model/entity classes               |
| 3     | DAO layer + DB connection          |
| 4     | Service layer + validations        |
| 5     | Java Swing UI + full integration   |
