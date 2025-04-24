package CLI.Main;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import CLI.Models.*;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;


public class CLITest {
    Library mockedLibrary;
    LibraryAccounts mockedAccounts;
    Librarians mockedLibrarians;
    CLI cli;

    @BeforeEach
    public void setup() {
        mockedLibrary = mock(Library.class);
        mockedAccounts = mock(LibraryAccounts.class);
        mockedLibrarians = mock(Librarians.class);
    }

    /* Specification Tests */
    /* Note: All specification and structural tests have significant overlap since interface is so simple... */

//    OPTION 1)

//    Book not in library yet; add it (happy case)
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
        cli = new CLI(new StringReader(userInput), mockedLibrary, mockedAccounts, mockedLibrarians);

        // Mock that library.addBook() returns a dummy Book
        Book fakeBook = mock(Book.class);
        when(mockedLibrary.addBook(anyString(), anyString(), anyInt(), anyString(), anyInt())).thenReturn(fakeBook);

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

    //    Book in library already or otherwise library can't make book
    @Test
    void testAddBookCantMake() {
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
        cli = new CLI(new StringReader(userInput), mockedLibrary, mockedAccounts, mockedLibrarians);

        // Mock that library.addBook() returns a dummy Book
        when(mockedLibrary.addBook(anyString(), anyString(), anyInt(), anyString(), anyInt())).thenReturn(null);

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

    /* Structural Testing */

    //    Empty field (bookName)
    @Test
    void emptyBookName() {
        //       Qualities of book
        String bookName = "";
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

        cli = new CLI(new StringReader(userInput), mockedLibrary, mockedAccounts, mockedLibrarians);

        cli.run();

        verifyNoInteractions(mockedLibrary);
    }

    //    Empty field (author)
    @Test
    void emptyAuthor() {
        //       Qualities of book
        String bookName = "My Book";
        String author = "";
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

        cli = new CLI(new StringReader(userInput), mockedLibrary, mockedAccounts, mockedLibrarians);

        cli.run();

        verifyNoInteractions(mockedLibrary);
    }

    //    Empty field (year)
    @Test
    void emptyYear() {
        //       Qualities of book
        String bookName = "My Book";
        String author = "Jane Doe";
        String year = "";
        String genre = "Fiction";
        int isbn = 123456;

        // Fake user input: 1 (add book), then book fields, then anything to continue
        String userInput = String.join("\n",
                "1",              // Select option 1 (Add Book)
                bookName,               // Book name
                author,                 // Author
                year,           // Year
                genre,                  // Genre
                String.valueOf(isbn),         // ISBN
                "",                     // (Empty input to get to menu)
                "10"                    // Exit (after adding book, immediately exit)
        );

        cli = new CLI(new StringReader(userInput), mockedLibrary, mockedAccounts, mockedLibrarians);

        cli.run();

        verifyNoInteractions(mockedLibrary);
    }

    //    Bad field (year)
    @Test
    void badYear() {
        //       Qualities of book
        String bookName = "My Book";
        String author = "Jane Doe";
        String year = "bad";
        String genre = "Fiction";
        int isbn = 123456;

        // Fake user input: 1 (add book), then book fields, then anything to continue
        String userInput = String.join("\n",
                "1",              // Select option 1 (Add Book)
                bookName,               // Book name
                author,                 // Author
                year,           // Year
                genre,                  // Genre
                String.valueOf(isbn),         // ISBN
                "",                     // (Empty input to get to menu)
                "10"                    // Exit (after adding book, immediately exit)
        );

        cli = new CLI(new StringReader(userInput), mockedLibrary, mockedAccounts, mockedLibrarians);

        cli.run();

        verifyNoInteractions(mockedLibrary);
    }

    //    Empty field (genre)
    @Test
    void emptyGenre() {
        //       Qualities of book
        String bookName = "My Book";
        String author = "Jane Doe";
        int year = 2024;
        String genre = "";
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

        cli = new CLI(new StringReader(userInput), mockedLibrary, mockedAccounts, mockedLibrarians);

        cli.run();

        verifyNoInteractions(mockedLibrary);
    }

    //    Bad field (isbn)
    @Test
    void badIsbn() {
        //       Qualities of book
        String bookName = "My Book";
        String author = "Jane Doe";
        int year = 2024;
        String genre = "Fiction";
        String isbn = "bad";

        // Fake user input: 1 (add book), then book fields, then anything to continue
        String userInput = String.join("\n",
                "1",              // Select option 1 (Add Book)
                bookName,               // Book name
                author,                 // Author
                String.valueOf(year),           // Year
                genre,                  // Genre
                isbn,         // ISBN
                "",                     // (Empty input to get to menu)
                "10"                    // Exit (after adding book, immediately exit)
        );

        cli = new CLI(new StringReader(userInput), mockedLibrary, mockedAccounts, mockedLibrarians);

        cli.run();

        verifyNoInteractions(mockedLibrary);
    }


    //    Empty field (isbn)
    @Test
    void emptyIsbn() {
        //       Qualities of book
        String bookName = "My Book";
        String author = "Jane Doe";
        int year = 2024;
        String genre = "Fiction";
        String isbn = "";

        // Fake user input: 1 (add book), then book fields, then anything to continue
        String userInput = String.join("\n",
                "1",              // Select option 1 (Add Book)
                bookName,               // Book name
                author,                 // Author
                String.valueOf(year),           // Year
                genre,                  // Genre
                isbn,         // ISBN
                "",                     // (Empty input to get to menu)
                "10"                    // Exit (after adding book, immediately exit)
        );

        cli = new CLI(new StringReader(userInput), mockedLibrary, mockedAccounts, mockedLibrarians);

        cli.run();

        verifyNoInteractions(mockedLibrary);
    }

//    OPTION 2) Remove Book

    /* Specification Testing */

//    Remove book (in library)
    @Test
    void testRemoveBook() {
//        Setup for verifying actions during cli
        String bookId = "bookId";
        doNothing().when(mockedLibrary).removeBook(anyString());
        when(mockedLibrary.findBookIdByName(anyString())).thenReturn(bookId);

//       run cli
//       Qualities of book

        // Fake user input: 1 (add book), then book fields, then anything to continue
        String userInput = String.join("\n",
                "2",              // Select option 2 (Remove Book)
                "La Despidida",               // Book name
                "",                     // (Empty input to get to menu)
                "10"                    // Exit (after adding book, immediately exit)
        );

        cli = new CLI(new StringReader(userInput), mockedLibrary, mockedAccounts, mockedLibrarians);

        cli.run();

        verify(mockedLibrary).removeBook(bookId);
    }


    //    Remove book (not in library)
    @Test
    void testRemoveBookNotFound() {
        //        Setup for verifying actions during cli
        when(mockedLibrary.findBookIdByName(anyString())).thenReturn(null);

        // Fake user input: 2 (remove book), then book name, then anything to continue
        String userInput = String.join("\n",
                "2",              // Select option 2 (Remove Book)
                "La Despidida",               // Book name
                "",                     // (Empty input to get to menu)
                "10"                    // Exit (after adding book, immediately exit)
        );

        cli = new CLI(new StringReader(userInput), mockedLibrary, mockedAccounts, mockedLibrarians);

        cli.run();

        verify(mockedLibrary).findBookIdByName(anyString());
        verifyNoMoreInteractions(mockedLibrary);
    }

//    Empty book name
    @Test
    void testEmptyBook() {
        String bookName = "";

        // Fake user input: 2 (remove book), then book name, then anything to continue
        String userInput = String.join("\n",
                "2",              // Select option 2 (Remove Book)
                bookName,               // Book name
                "",                     // (Empty input to get to menu)
                "10"                    // Exit (after adding book, immediately exit)
        );

        cli = new CLI(new StringReader(userInput), mockedLibrary, mockedAccounts, mockedLibrarians);

        cli.run();

        verifyNoInteractions(mockedLibrary);
    }

//     OPTION 3) Check Book Availability

    /* Specification Tests */

//    Book can't be found
@Test
void bookNotFound() {
        when(mockedLibrary.findBookIdByName(anyString())).thenReturn(null);

        String bookName = "My Book";

        String userInput = String.join("\n",
                "3",
                bookName,
                "",
                "10");

        cli = new CLI(new StringReader(userInput), mockedLibrary, mockedAccounts, mockedLibrarians);
        cli.run();

        verify(mockedLibrary).findBookIdByName(anyString());
        verifyNoMoreInteractions(mockedLibrary);

    }

    //    Book is available
    @Test
    void bookIsAvailable() {
        when(mockedLibrary.findBookIdByName(anyString())).thenReturn("1");
        when(mockedLibrary.bookAvailability(anyString())).thenReturn(true);

        String bookName = "My Book";

        String userInput = String.join("\n",
                "3",
                bookName,
                "",
                "10");

        cli = new CLI(new StringReader(userInput), mockedLibrary, mockedAccounts, mockedLibrarians);
        cli.run();

        verify(mockedLibrary).findBookIdByName(anyString());
        verify(mockedLibrary).bookAvailability(anyString());
        verifyNoMoreInteractions(mockedLibrary);

    }

//    Book is not available
    @Test
    void bookIsNotAvailable() {
        when(mockedLibrary.findBookIdByName(anyString())).thenReturn("1");
        when(mockedLibrary.bookAvailability(anyString())).thenReturn(false);

        String bookName = "My Book";

        String userInput = String.join("\n",
                "3",
                bookName,
                "",
                "10");

        cli = new CLI(new StringReader(userInput), mockedLibrary, mockedAccounts, mockedLibrarians);
        cli.run();

        verify(mockedLibrary).findBookIdByName(anyString());
        verify(mockedLibrary).bookAvailability(anyString());
        verifyNoMoreInteractions(mockedLibrary);
    }

    /* Structural Tests */

    //    Bad Book Name
    @Test
    void badBookName() {
        String bookName = "";

        String userInput = String.join("\n",
                "3",
                bookName,
                "",
                "10");

        cli = new CLI(new StringReader(userInput), mockedLibrary, mockedAccounts, mockedLibrarians);
        cli.run();

        verifyNoInteractions(mockedLibrary);
    }


//    OPTION 4)

    @Test
    void testCheckoutBook() {
        String memberId = "1";
        String bookName = "Test Book";

        // Simulated user input: 4 (checkout book), member ID, book name, "", 10 (exit)
        String userInput = String.join("\n",
                "4",          // Select option 4: Checkout Book
                memberId,     // Member ID
                bookName,     // Book name
                "",           // Continue
                "10"          // Exit
        );

        // Create CLI with fake input and mocked library
        cli = new CLI(new StringReader(userInput), mockedLibrary, mockedAccounts, mockedLibrarians);

        // Run
        cli.run();

        // Verify that the library's checkoutBook method was called
        verify(mockedLibrary).checkoutBook(memberId, bookName);
    }

    @Test
    void testCheckoutBook_emptyMemberId_triggersValidation() {
        // Simulated input: 4 (checkout), then empty string for member ID, then "10" to exit
        String userInput = String.join("\n",
                "4",
                "",            // Empty member ID (should trigger validation)
                "Book1",   // Won't be read
                "",            // Continue
                "10"
        );

        cli = new CLI(new StringReader(userInput), mockedLibrary, mockedAccounts, mockedLibrarians);
        //Run
        cli.run();

        // Verify that checkoutBook was never called
        verify(mockedLibrary, never()).checkoutBook(anyString(), anyString());
    }


    @Test
    void testCheckoutBook_emptyBookName_triggersValidation() {
        String userInput = String.join("\n",
                "4",
                "1",          // Valid member ID
                "",           // Empty book name (should trigger validation)
                "",           // Continue
                "10"
        );

        cli = new CLI(new StringReader(userInput), mockedLibrary, mockedAccounts, mockedLibrarians);
        //Run
        cli.run();

        verify(mockedLibrary, never()).checkoutBook(anyString(), anyString());
    }

    @Test
    void testReturnBook() {
        // Arrange
        String memberId = "1";
        String bookName = "Some Book";
        String resolvedBookID = "B001";

        // Simulated user input: 5 (return), member ID, book name, "", 10 (exit)
        String userInput = String.join("\n",
                "5",
                memberId,
                bookName,
                "",     // Continue
                "10"    // Exit
        );

        // Mock findBookIdByName to return the correct ID
        when(mockedLibrary.findBookIdByName(bookName)).thenReturn(resolvedBookID);

        cli = new CLI(new StringReader(userInput), mockedLibrary, mockedAccounts, mockedLibrarians);

        // Run
        cli.run();

        verify(mockedLibrary).findBookIdByName(bookName);
        verify(mockedLibrary).returnBook(memberId, resolvedBookID);
    }

    @Test
    void testReturnBook_emptyMemberId_triggersValidation() {
        String userInput = String.join("\n",
                "5",
                "",            // Invalid (empty) member ID
                "Some Book",
                "", "10"
        );

        cli = new CLI(new StringReader(userInput), mockedLibrary, mockedAccounts, mockedLibrarians);

        //Run
        cli.run();

        verify(mockedLibrary, never()).returnBook(anyString(), anyString());
    }

    @Test
    void testReturnBook_emptyBookName_triggersValidation() {
        String userInput = String.join("\n",
                "5",
                "1",           // Valid member ID
                "",            // Empty book name
                "", "10"
        );

        cli = new CLI(new StringReader(userInput), mockedLibrary, mockedAccounts, mockedLibrarians);
        //Run
        cli.run();

        verify(mockedLibrary, never()).returnBook(anyString(), anyString());
    }

    @Test
    void testReturnBook_bookNotFound_triggersGuardClause() {
        String userInput = String.join("\n",
                "5",
                "1",
                "Nonexistent Book",
                "", "10"
        );

        // Book name lookup but, returns null
        when(mockedLibrary.findBookIdByName("Nonexistent Book")).thenReturn(null);

        cli = new CLI(new StringReader(userInput), mockedLibrary, mockedAccounts, mockedLibrarians);
        //Run
        cli.run();

        verify(mockedLibrary, never()).returnBook(anyString(), anyString());
    }

    @Test
    void testViewAllBooks_withBooks() {
        // Use a real library so we can control the field
        Library realLibrary = new Library();

        Book book1 = mock(Book.class);
        Book book2 = mock(Book.class);
        when(book1.getBookInfo()).thenReturn("Book 1 Info");
        when(book2.getBookInfo()).thenReturn("Book 2 Info");

        realLibrary.AllBooksInLibrary.add(book1);
        realLibrary.AllBooksInLibrary.add(book2);

        String userInput = String.join("\n",
                "6", "", "10"
        );

        CLI cli = new CLI(new StringReader(userInput), realLibrary, mockedAccounts, mockedLibrarians);
        cli.run();

        verify(book1).getBookInfo();
        verify(book2).getBookInfo();
    }


    @Test
    void testViewAllBooks_whenLibraryIsEmpty() {
        Library realLibrary = new Library();  // AllBooksInLibrary is empty by default

        String userInput = String.join("\n",
                "6", "", "10"
        );

        CLI cli = new CLI(new StringReader(userInput), realLibrary, mockedAccounts, mockedLibrarians);
        cli.run();

        //Checks to make sure  library is empty
        assertEquals(0, realLibrary.AllBooksInLibrary.size());
    }




//    OPTION 7-9


    // Specification tests

    @Test
    void addMemberTestShouldSucceed() {
        // Chooses Add Member and submits valid input

        // Arrange
        String name = "testName";
        String email = "testEmail";

        // Fake user input: 7 (add member), then member fields, then anything to continue
        String userInput = String.join("\n",
                "7",              // Select option 7 (Add Member)
                name,
                email,
                "",                     // (Empty input to get to menu)
                "10"                    // Exit (after adding book, immediately exit)
        );

        // Create CLI with fake input
        cli = new CLI(new StringReader(userInput), mockedLibrary, mockedAccounts, mockedLibrarians);

        // Stub that library.addMember() returns a dummy Member
        Member mockedMember = mock(Member.class);
        when(mockedLibrary.addMember(anyString(), anyString())).thenReturn(mockedMember);

        // Act
        cli.run();

        // Assert that addMember was called correctly and printMemberInfo was called
        verify(mockedLibrary).addMember(name, email);
        verify(mockedMember).printMemberInfo();
    }

    @Test
    void revokeMembershipTestShouldSucceed() {
        // Chooses Revoke Membership and submits an existing member to revoke

        // Arrange
        String memberID = "testMemberID";

        // Fake user input: 8 (revoke membership), then memberID, then anything to continue
        String userInput = String.join("\n",
                "8",
                memberID,
                "",                     // (Empty input to get to menu)
                "10"                    // Exit (after adding book, immediately exit)
        );

        // Create CLI with fake input
        cli = new CLI(new StringReader(userInput), mockedLibrary, mockedAccounts, mockedLibrarians);

        // Stub that library.addMember() returns true
        when(mockedLibrary.revokeMembership(anyString())).thenReturn(true);

        // Act
        cli.run();

        // Assert that revokeMembership was called with the correct ID
        verify(mockedLibrary).revokeMembership(memberID);
    }

    @Property
    void viewAllMembersTestShouldSucceed(@ForAll @IntRange(min = 1, max = 20) int size) {
        // Chooses View All Members, members exist, should return all members

        // Arrange
        List<Member> members = IntStream.range(0, size)
                .mapToObj(i -> mock(Member.class))
                .toList();

        // Fake user input: 9 (View All Members), then anything to continue
        String userInput = String.join("\n",
                "9",
                "",                     // (Empty input to get to menu)
                "10"                    // Exit (after adding book, immediately exit)
        );

        // Create CLI with fake input
        Library mockedLibrary = mock(Library.class); // jqwik cannot use @BeforeEach from junit
        cli = new CLI(new StringReader(userInput), mockedLibrary, mockedAccounts, mockedLibrarians);

        // Stub that library.getAllMembers() returns my list of members
        when(mockedLibrary.getAllMembers()).thenReturn(members);

        // Act
        cli.run();

        // Assert that each member correctly called printMemberInfo
        members.forEach(m -> verify(m).printMemberInfo());
    }

    @Test
    void exitTestShouldSucceed() {
        // Chooses Exit and program exits

        // Arrange
        // Fake user input: 10 (Exit), then anything to continue
        String userInput = String.join("\n",
                "10" // Program should end after exit is requested
        );

        // Create CLI with fake input
        cli = new CLI(new StringReader(userInput), mockedLibrary, mockedAccounts, mockedLibrarians);

        // Act
        cli.run();

        // Assert that Exit was called and set exit to true
        assertEquals(true, cli.exit);
    }

    // Structural test

    @Test
    void addMemberTestEmptyInputShouldFail() {
        // Chooses Add Member and submits empty input

        // Arrange
        // Fake user input: 7 (add member), then empty member fields, then anything to continue
        String userInput = String.join("\n",
                "7",              // Select option 7 (Add Member)
                "",
                "",
                "",                     // (Empty input to get to menu)
                "10"                    // Exit (after adding book, immediately exit)
        );

        // Create CLI with fake input
        cli = new CLI(new StringReader(userInput), mockedLibrary, mockedAccounts, mockedLibrarians);

        // Act
        cli.run();

        // Assert that addMember() was never called
        verify(mockedLibrary, never()).addMember(anyString(), anyString());
    }

    @Test
    void revokeMembershipTestShouldFail() {
        // Chooses Revoke Membership and submits an empty member ID

        // Arrange
        // Fake user input: 8 (revoke membership), then memberID, then anything to continue
        String userInput = String.join("\n",
                "8",
                "",
                "",                     // (Empty input to get to menu)
                "10"                    // Exit (after adding book, immediately exit)
        );

        // Create CLI with fake input
        cli = new CLI(new StringReader(userInput), mockedLibrary, mockedAccounts, mockedLibrarians);

        // Act
        cli.run();

        // Assert that revokeMembership was never called with empty input
        verify(mockedLibrary, never()).revokeMembership(anyString());
    }

}
