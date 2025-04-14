package CLI.Models;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;
import net.jqwik.api.*;
import net.jqwik.api.constraints.*;
import java.util.*;
import org.mockito.*;


import CLI.Models.*;

public class BookTest {
    Book book;

    @BeforeEach
    void setUp() {
        book = new Book("lala", "bg", 2003, "a", 1, "1");
    }

    @Test
    void testGetBookInfo_Available() {
        String expected = String.format(
                "Name: lala, Author: bg, Year: 2003, Genre: a, ISBN: 1, Available: Yes"
        );

        String actual = book.getBookInfo();

        assertEquals(expected, actual);
    }

    @Test
    void testGetBookInfo_NotAvailable() {
        String expected = "Name: lala, Author: bg, Year: 2003, Genre: a, ISBN: 1, Available: No";

        book.IsAvailable = false;

        String actual = book.getBookInfo();

        assertEquals(expected, actual);
    }

    @Test
    void testCheckAvailability() {
        assertEquals(true, book.checkAvailability());
    }

    @Test
    void testUpdate() {
        // Expected member variables
        String expectedName = "expectedBook";
        String expectedBookID = "bookID";
        String expectedAuthor = "expectedAuthor";
        boolean expectedAvailability = true;
        String expectedGenre = "expectedGenre";
        int expectedISBN = 123;
        int expectedYear = 2000;

        // Method to test
        book.updateBookInfo(expectedName, expectedAuthor, expectedYear, expectedGenre, expectedISBN, expectedBookID, expectedAvailability);

        // Assertions on class members (observable)
        assertEquals(expectedName, book.Name);
        assertEquals(expectedBookID, book.BookID);
        assertEquals(expectedAvailability, book.IsAvailable);
        assertEquals(expectedAuthor, book.Author);
        assertEquals(expectedGenre, book.Genre);
        assertEquals(expectedISBN, book.ISBN);
        assertEquals(expectedYear, book.Year);
    }
}
