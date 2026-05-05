import model.Book;
import service.BookService;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit tests for BookService.
 * Tests: add book, validate empty fields, delete protection.
 */
public class BookServiceTest {

    private BookService bookService;

    @Before
    public void setUp() {
        bookService = new BookService();
    }

    // ── TEST 1: Add valid book ────────────────────────────────

    @Test
    public void testAddValidBook() {
        Book book = new Book("Test Book", "Test Author",
                             "978-TEST-001", "Test Publisher",
                             2024, "Education", 2);
        try {
            boolean result = bookService.addBook(book);
            assertTrue("Valid book should be added successfully", result);
        } catch (Exception e) {
            fail("No exception expected for valid book: " + e.getMessage());
        }
    }

    // ── TEST 2: Empty title rejected ─────────────────────────

    @Test
    public void testAddBookEmptyTitleThrowsException() {
        Book book = new Book("", "Some Author",
                             "978-TEST-002", "Publisher",
                             2024, "Fiction", 1);
        try {
            bookService.addBook(book);
            fail("Exception expected for empty title");
        } catch (Exception e) {
            assertEquals("Book title cannot be empty.", e.getMessage());
        }
    }

    // ── TEST 3: Empty author rejected ────────────────────────

    @Test
    public void testAddBookEmptyAuthorThrowsException() {
        Book book = new Book("Some Title", "",
                             "978-TEST-003", "Publisher",
                             2024, "Fiction", 1);
        try {
            bookService.addBook(book);
            fail("Exception expected for empty author");
        } catch (Exception e) {
            assertEquals("Author name cannot be empty.", e.getMessage());
        }
    }

    // ── TEST 4: Zero copies rejected ─────────────────────────

    @Test
    public void testAddBookZeroCopiesThrowsException() {
        Book book = new Book("Title", "Author",
                             "978-TEST-004", "Publisher",
                             2024, "Fiction", 0);
        try {
            bookService.addBook(book);
            fail("Exception expected for zero copies");
        } catch (Exception e) {
            assertEquals("Total copies must be at least 1.", e.getMessage());
        }
    }

    // ── TEST 5: Get all books returns list ───────────────────

    @Test
    public void testGetAllBooksReturnsList() {
        assertNotNull("getAllBooks() should not return null",
                      bookService.getAllBooks());
    }

    // ── TEST 6: Search with empty keyword fails ───────────────

    @Test
    public void testSearchBooksEmptyKeywordThrowsException() {
        try {
            bookService.searchBooks("");
            fail("Exception expected for empty search keyword");
        } catch (Exception e) {
            assertEquals("Search keyword cannot be empty.", e.getMessage());
        }
    }

    // ── TEST 7: Search with valid keyword works ───────────────

    @Test
    public void testSearchBooksValidKeyword() {
        try {
            assertNotNull("Search result should not be null",
                          bookService.searchBooks("Java"));
        } catch (Exception e) {
            fail("No exception expected for valid keyword: " + e.getMessage());
        }
    }
}
