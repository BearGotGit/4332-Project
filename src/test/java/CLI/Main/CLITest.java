package CLI.Main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import CLI.Models.*;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Scanner;

public class CLITest {
    Library mockedLibrary;
    CLI cli;

    @BeforeEach
    public void setup() {
        mockedLibrary = mock(Library.class);
    }

    @Test
    void testAddBook() {
//       Qualities of book
        String bookName = "My Book";
        String author = "Jane Doe";
        int year = 2024;
        String genre = "Fiction";
        int isbn = 123456;

        // Fake user input: 1 (add book), then book fields, then anything to continue
        String userInput = String.join("\n",
                "1",              // Select option 1 (Add Book)
                bookName,               // Book name
                author,                 // Author
                String.valueOf(year),           // Year
                genre,                  // Genre
                String.valueOf(isbn),         // ISBN
                "",                     // (Empty input to get to menu)
                "10"                    // Exit (after adding book, immediately exit)
        );

        // Create CLI with fake input
        cli = new CLI(new StringReader(userInput), mockedLibrary);

        // Mock that library.addBook() returns a dummy Book
        Book fakeBook = mock(Book.class);
        when(mockedLibrary.addBook(anyString(), anyString(), anyInt(), anyString(), anyInt())).thenReturn(fakeBook);
//        when(fakeBook.getBookInfo()).thenReturn("Fake Book Info");

        // Run
        cli.run();

        // Verify that addBook was called correctly
        verify(mockedLibrary).addBook(
                bookName,
                author,
                year,
                genre,
                isbn
        );
    }

//
//    @Test
//    void testAddBookFlow() {
//        // Arrange: Fake user inputs
//        when(mocked_scanner.nextLine())
//                .thenReturn("1")          // select "Add Book"
//                .thenReturn("Test Book")  // book name
//                .thenReturn("Test Author")// author
//                .thenReturn("2020")       // year
//                .thenReturn("Fantasy")    // genre
//                .thenReturn("12345")      // ISBN
//                .thenReturn("10");        // then exit after (simulate pressing 10 to exit)
//
//        // Also, fake the library returning a Book when adding
//        Book fakeBook = mock(Book.class);
//        when(mocked_library.addBook(anyString(), anyString(), anyInt(), anyString(), anyInt())).thenReturn(fakeBook);
//        when(fakeBook.getBookInfo()).thenReturn("Book Info Here");
//
//        // Act
//        cli.run();
//
//        // Assert: Did we call library.addBook with correct stuff?
//        verify(mocked_library).addBook(
//                eq("Test Book"),
//                eq("Test Author"),
//                eq(2020),
//                eq("Fantasy"),
//                eq(12345)
//        );
//    }
}
