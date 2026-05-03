package model;

/**
 * Model class representing a Book title in the library catalogue.
 * One Book can have multiple BookCopy instances.
 */
public class Book {

    private int    bookId;
    private String title;
    private String author;
    private String isbn;
    private String publisher;
    private int    year;
    private String genre;
    private int    totalCopies;

    // ── Constructors ──────────────────────────────────────────

    public Book() {}

    public Book(int bookId, String title, String author, String isbn,
                String publisher, int year, String genre, int totalCopies) {
        this.bookId      = bookId;
        this.title       = title;
        this.author      = author;
        this.isbn        = isbn;
        this.publisher   = publisher;
        this.year        = year;
        this.genre       = genre;
        this.totalCopies = totalCopies;
    }

    // Constructor without ID (for INSERT operations)
    public Book(String title, String author, String isbn,
                String publisher, int year, String genre, int totalCopies) {
        this.title       = title;
        this.author      = author;
        this.isbn        = isbn;
        this.publisher   = publisher;
        this.year        = year;
        this.genre       = genre;
        this.totalCopies = totalCopies;
    }

    // ── Getters & Setters ─────────────────────────────────────

    public int    getBookId()      { return bookId; }
    public void   setBookId(int bookId) { this.bookId = bookId; }

    public String getTitle()       { return title; }
    public void   setTitle(String title) { this.title = title; }

    public String getAuthor()      { return author; }
    public void   setAuthor(String author) { this.author = author; }

    public String getIsbn()        { return isbn; }
    public void   setIsbn(String isbn) { this.isbn = isbn; }

    public String getPublisher()   { return publisher; }
    public void   setPublisher(String publisher) { this.publisher = publisher; }

    public int    getYear()        { return year; }
    public void   setYear(int year) { this.year = year; }

    public String getGenre()       { return genre; }
    public void   setGenre(String genre) { this.genre = genre; }

    public int    getTotalCopies() { return totalCopies; }
    public void   setTotalCopies(int totalCopies) { this.totalCopies = totalCopies; }

    // ── toString ──────────────────────────────────────────────

    @Override
    public String toString() {
        return "Book{" +
               "bookId=" + bookId +
               ", title='" + title + '\'' +
               ", author='" + author + '\'' +
               ", isbn='" + isbn + '\'' +
               ", year=" + year +
               ", totalCopies=" + totalCopies +
               '}';
    }
}
