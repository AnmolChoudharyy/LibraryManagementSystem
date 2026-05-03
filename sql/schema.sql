-- ============================================================
--  Library Management System — Database Schema
--  Phase 1 | CLD Project
-- ============================================================

-- Drop tables in reverse dependency order (safe re-run)
DROP TABLE IF EXISTS fines;
DROP TABLE IF EXISTS loans;
DROP TABLE IF EXISTS book_copies;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS members;
DROP TABLE IF EXISTS staff;

-- ============================================================
--  TABLE: staff
-- ============================================================
CREATE TABLE staff (
    staff_id    INTEGER PRIMARY KEY AUTOINCREMENT,
    full_name   TEXT    NOT NULL,
    username    TEXT    NOT NULL UNIQUE,
    password    TEXT    NOT NULL,
    role        TEXT    NOT NULL DEFAULT 'librarian'
                        CHECK(role IN ('admin', 'librarian')),
    email       TEXT,
    phone       TEXT,
    created_at  TEXT    NOT NULL DEFAULT (datetime('now'))
);

-- ============================================================
--  TABLE: members
-- ============================================================
CREATE TABLE members (
    member_id   INTEGER PRIMARY KEY AUTOINCREMENT,
    full_name   TEXT    NOT NULL,
    email       TEXT    UNIQUE,
    phone       TEXT,
    address     TEXT,
    joined_date TEXT    NOT NULL DEFAULT (date('now')),
    is_active   INTEGER NOT NULL DEFAULT 1
);

-- ============================================================
--  TABLE: books
-- ============================================================
CREATE TABLE books (
    book_id      INTEGER PRIMARY KEY AUTOINCREMENT,
    title        TEXT    NOT NULL,
    author       TEXT    NOT NULL,
    isbn         TEXT    UNIQUE,
    publisher    TEXT,
    year         INTEGER,
    genre        TEXT,
    total_copies INTEGER NOT NULL DEFAULT 1
);

-- ============================================================
--  TABLE: book_copies
--  status: 'available' | 'issued' | 'lost' | 'damaged'
-- ============================================================
CREATE TABLE book_copies (
    copy_id     INTEGER PRIMARY KEY AUTOINCREMENT,
    book_id     INTEGER NOT NULL,
    copy_number INTEGER NOT NULL DEFAULT 1,
    status      TEXT    NOT NULL DEFAULT 'available'
                        CHECK(status IN ('available','issued','lost','damaged')),
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE
);

-- ============================================================
--  TABLE: loans
-- ============================================================
CREATE TABLE loans (
    loan_id     INTEGER PRIMARY KEY AUTOINCREMENT,
    copy_id     INTEGER NOT NULL,
    member_id   INTEGER NOT NULL,
    staff_id    INTEGER NOT NULL,
    issue_date  TEXT    NOT NULL DEFAULT (date('now')),
    due_date    TEXT    NOT NULL,
    return_date TEXT,
    status      TEXT    NOT NULL DEFAULT 'active'
                        CHECK(status IN ('active','returned','overdue')),
    FOREIGN KEY (copy_id)   REFERENCES book_copies(copy_id),
    FOREIGN KEY (member_id) REFERENCES members(member_id),
    FOREIGN KEY (staff_id)  REFERENCES staff(staff_id)
);

-- ============================================================
--  TABLE: fines
-- ============================================================
CREATE TABLE fines (
    fine_id  INTEGER PRIMARY KEY AUTOINCREMENT,
    loan_id  INTEGER NOT NULL UNIQUE,
    amount   REAL    NOT NULL DEFAULT 0.0,
    is_paid  INTEGER NOT NULL DEFAULT 0,
    paid_date TEXT,
    FOREIGN KEY (loan_id) REFERENCES loans(loan_id)
);

-- ============================================================
--  INDEXES
-- ============================================================
CREATE INDEX idx_loans_member  ON loans(member_id);
CREATE INDEX idx_loans_copy    ON loans(copy_id);
CREATE INDEX idx_loans_status  ON loans(status);
CREATE INDEX idx_copies_book   ON book_copies(book_id);
CREATE INDEX idx_copies_status ON book_copies(status);

-- ============================================================
--  SEED DATA
-- ============================================================
INSERT INTO staff (full_name, username, password, role)
VALUES ('System Admin', 'admin', 'admin123', 'admin');

INSERT INTO staff (full_name, username, password, role, email)
VALUES ('Ravi Kumar', 'ravi', 'lib123', 'librarian', 'ravi@library.com');

INSERT INTO members (full_name, email, phone, address) VALUES
    ('Anjali Singh', 'anjali@email.com', '9876543210', 'Delhi'),
    ('Rohan Mehta',  'rohan@email.com',  '9123456780', 'Mumbai'),
    ('Priya Sharma', 'priya@email.com',  '9988776655', 'Meerut');

INSERT INTO books (title, author, isbn, publisher, year, genre, total_copies) VALUES
    ('The Alchemist',           'Paulo Coelho',     '978-0062315007', 'HarperOne',     1988, 'Fiction',    3),
    ('Clean Code',              'Robert C. Martin', '978-0132350884', 'Prentice Hall', 2008, 'Technology', 2),
    ('Data Structures in Java', 'Mark Weiss',       '978-0132576277', 'Pearson',       2011, 'Education',  4);

INSERT INTO book_copies (book_id, copy_number, status) VALUES
    (1, 1, 'available'), (1, 2, 'available'), (1, 3, 'available'),
    (2, 1, 'available'), (2, 2, 'available'),
    (3, 1, 'available'), (3, 2, 'available'), (3, 3, 'available'), (3, 4, 'available');
