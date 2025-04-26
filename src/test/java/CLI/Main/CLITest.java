package CLI.Main;

import net.jqwik.api.*;
import net.jqwik.api.constraints.*;
import net.jqwik.api.lifecycle.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import CLI.Models.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.List;
import java.util.stream.IntStream;


public class CLITest {
    Library mockedLibrary;
    LibraryAccounts mockedAccounts;
    Librarians mockedLibrarians;
    PrintStream mockedSystemOut;
    CLI cli;
    String cliExitOption = "13";

    String testLibrarianUser = "A";
    String testLibrarianAuthCode = "111111";

    @BeforeEach
    @BeforeTry
    public void setup() {
        mockedLibrary = mock(Library.class);
        mockedAccounts = mock(LibraryAccounts.class);
        mockedLibrarians = mock(Librarians.class);
        mockedSystemOut = mock(System.out.getClass());
    }

    // ***** Specification Tests *****

    // Auth

    @Test
    void authenticateTestShouldBeFullTime() {
        // authenticate() should return FULL_TIME

        String userInput = String.join("\n",
                testLibrarianUser,
                testLibrarianAuthCode,
                ""
        );

        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
        when(mockedLibrarians.authLibrarian(anyString(), anyString())).thenReturn(Librarians.AuthType.FULL_TIME);

        CLI.AuthResult authResult = cli.authenticate();

        assertEquals(Librarians.AuthType.FULL_TIME, authResult.authType);
    }

    @Test
    void authenticateTestShouldBePartTime() {
        // authenticate() should return PART_TIME

        String userInput = String.join("\n",
                testLibrarianUser,
                "",
                ""
        );

        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
        when(mockedLibrarians.authLibrarian(anyString(), isNull())).thenReturn(Librarians.AuthType.PART_TIME);

        CLI.AuthResult authResult = cli.authenticate();

        assertEquals(Librarians.AuthType.PART_TIME, authResult.authType);
    }

    @Test
    void authenticateTestShouldBeNotAuthorized() {
        // authenticate() should return NOT_AUTHORIZED

        String userInput = String.join("\n",
                testLibrarianUser,
                "incorrect auth code",
                ""
        );

        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
        when(mockedLibrarians.authLibrarian(anyString(), anyString())).thenReturn(Librarians.AuthType.NOT_AUTHORIZED);

        CLI.AuthResult authResult = cli.authenticate();

        assertEquals(Librarians.AuthType.NOT_AUTHORIZED, authResult.authType);
    }

    //    OPTION 1: Order Book

    //    Book not in library yet; add it (happy case)
    @Test
    void testOrderBook() {
        String bookName = "My Book";
        String author = "Jane Doe";
        int year = 2024;
        String genre = "Fiction";
        int ISBN = 123456;

        // Fake user input
        String userInput = String.join("\n",
                "1",              // Select option 1 (Add Book)
                testLibrarianUser,
                testLibrarianAuthCode,
                bookName,
                author,
                String.valueOf(year),
                genre,
                String.valueOf(ISBN),
                "",
                this.cliExitOption
        );

        // Create CLI with fake input
        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
        when(mockedLibrarians.authLibrarian(testLibrarianUser, testLibrarianAuthCode)).thenReturn(Librarians.AuthType.FULL_TIME);
        when(mockedAccounts.orderNewBook(bookName, author, year, genre, ISBN)).thenReturn(true);

        Book fakeBook = mock(Book.class);
        when(mockedLibrary.addBook(anyString(), anyString(), anyInt(), anyString(), anyInt())).thenReturn(fakeBook);

        // Run
        cli.run();

        // Verify that addBook was called correctly
        verify(mockedLibrary).addBook(bookName, author, year, genre, ISBN);
    }

    //    Book in library already or otherwise library can't make book
    @Test
    void testAddBookCantMake() {
        String bookName = "My Book";
        String author = "Jane Doe";
        int year = 2024;
        String genre = "Fiction";
        int ISBN = 123456;

        // Fake user input
        String userInput = String.join("\n",
                "1",              // Select option 1 (Add Book)
                testLibrarianUser,
                testLibrarianAuthCode,
                bookName,
                author,
                String.valueOf(year),
                genre,
                String.valueOf(ISBN),
                "",
                this.cliExitOption
        );

        // Create CLI with fake input
        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
        when(mockedLibrarians.authLibrarian(testLibrarianUser, testLibrarianAuthCode)).thenReturn(Librarians.AuthType.FULL_TIME);
        when(mockedAccounts.orderNewBook(bookName, author, year, genre, ISBN)).thenReturn(true);

        when(mockedLibrary.addBook(anyString(), anyString(), anyInt(), anyString(), anyInt())).thenReturn(null);

        // Run
        cli.run();

        // Verify that addBook was called correctly
        verify(mockedLibrary).addBook(bookName, author, year, genre, ISBN);
    }

    //    OPTION 2: Remove Book

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
                this.cliExitOption                    // Exit (after adding book, immediately exit)
        );

        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);

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
                this.cliExitOption                    // Exit (after adding book, immediately exit)
        );

        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);

        cli.run();

        verify(mockedLibrary).findBookIdByName(anyString());
        verifyNoMoreInteractions(mockedLibrary);
    }

    //     OPTION 3: Check Book Availability

    //    Book can't be found
    @Test
    void bookNotFound() {
        when(mockedLibrary.findBookIdByName(anyString())).thenReturn(null);

        String bookName = "My Book";

        String userInput = String.join("\n",
                "3",
                bookName,
                "",
                this.cliExitOption);

        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
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
                this.cliExitOption);

        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
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
                this.cliExitOption);

        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
        cli.run();

        verify(mockedLibrary).findBookIdByName(anyString());
        verify(mockedLibrary).bookAvailability(anyString());
        verifyNoMoreInteractions(mockedLibrary);
    }

    //    OPTION 4: Checkout Book

    @Test
    void testCheckoutBook() {
        String memberId = "1";
        String bookName = "Test Book";

        // Simulated user input: 4 (checkout book), member ID, book name, "", 10 (exit)
        String userInput = String.join("\n",
                "4",          // Select option 4 (Checkout Book)
                memberId,     // Member ID
                bookName,     // Book name
                "",           // Continue
                this.cliExitOption          // Exit
        );

        // Create CLI with fake input and mocked library
        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);

        // Run
        cli.run();

        // Verify that the library's checkoutBook method was called
        verify(mockedLibrary).checkoutBook(memberId, bookName);
    }

    //    OPTION 5: Return Book

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
                this.cliExitOption    // Exit
        );

        // Mock findBookIdByName to return the correct ID
        when(mockedLibrary.findBookIdByName(bookName)).thenReturn(resolvedBookID);

        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);

        // Run
        cli.run();

        verify(mockedLibrary).findBookIdByName(bookName);
        verify(mockedLibrary).returnBook(memberId, resolvedBookID);
    }

    @Test
    void testReturnBook_bookNotFound_triggersGuardClause() {
        String userInput = String.join("\n",
                "5",
                "1",
                "Nonexistent Book",
                "", this.cliExitOption
        );

        when(mockedLibrary.findBookIdByName("Nonexistent Book")).thenReturn(null);

        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
        cli.run();

        verify(mockedLibrary, never()).returnBook(anyString(), anyString());
    }

    //    OPTION 6: View All Books

    @Test
    void testViewAllBooks_withBooks() {
        Library realLibrary = new Library();

        Book book1 = mock(Book.class);
        Book book2 = mock(Book.class);
        when(book1.getBookInfo()).thenReturn("Book 1");
        when(book2.getBookInfo()).thenReturn("Book 2");

        realLibrary.AllBooksInLibrary.add(book1);
        realLibrary.AllBooksInLibrary.add(book2);

        String userInput = String.join("\n",
                "6", "", this.cliExitOption
        );

        CLI cli = new CLI(new StringReader(userInput), mockedSystemOut, realLibrary, mockedAccounts, mockedLibrarians);
        cli.run();

        verify(book1).getBookInfo();
        verify(book2).getBookInfo();
    }


    @Test
    void testViewAllBooks_whenLibraryIsEmpty() {
        String userInput = String.join("\n",
                "6", "", this.cliExitOption
        );

        CLI cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
        cli.run();

        verify(mockedSystemOut).println("There are no books in the library");
    }

    //    OPTION 7: Add Member

    @Test
    void addMemberTestShouldSucceed() {
        // Fake user input: 7 (add member), auth, then member fields, anything to continue, then exit
        String name = "testName";
        String email = "testEmail";
        String userInput = String.join("\n",
                "7",
                testLibrarianUser,
                testLibrarianAuthCode,
                name,
                email,
                "",
                this.cliExitOption
        );

        // Member prints info only when created
        Member mockedMember = mock(Member.class);
        when(mockedLibrary.addMember(anyString(), anyString())).thenReturn(mockedMember);
//        When authenticating, it's important that the librarian is auth and also FULL_TIME
        when(mockedLibrarians.authLibrarian(testLibrarianUser, testLibrarianAuthCode)).thenReturn(Librarians.AuthType.FULL_TIME);

        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
        cli.run();

//        Member is added, then their info's printed
        verify(mockedLibrary).addMember(name, email);
        verify(mockedMember).printMemberInfo();
    }

    @Test
    void addMemberTestFail_CannotCreate() {
        // Fake user input: 7 (add member), auth, then member fields, anything to continue, then exit
        String name = "testName";
        String email = "testEmail";
        String userInput = String.join("\n",
                "7",
                testLibrarianUser,
                testLibrarianAuthCode,
                name,
                email,
                "",
                this.cliExitOption
        );

        when(mockedLibrary.addMember(anyString(), anyString())).thenReturn(null);
//        When authenticating, it's important that the librarian is auth and also FULL_TIME
        when(mockedLibrarians.authLibrarian(testLibrarianUser, testLibrarianAuthCode)).thenReturn(Librarians.AuthType.FULL_TIME);

        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
        cli.run();

//        Member is added, then their info's printed
        verify(mockedSystemOut).println("New member couldn't be created");
    }

    //    OPTION 8: Revoke Membership

    @Test
    void revokeMembershipTestShouldSucceed() {
        // Chooses Revoke Membership and submits an existing member to revoke
        String memberID = "testMemberID";
        // Fake user input: 8 (revoke membership), auth, then memberID, then menu, exit
        String userInput = String.join("\n",
                "8",
                testLibrarianUser,
                testLibrarianAuthCode,
                memberID,
                "",
                this.cliExitOption
        );

//        Mock to revoke
        when(mockedLibrary.revokeMembership(anyString())).thenReturn(true);
//        When authenticating, it's important that the librarian is auth and also FULL_TIME
        when(mockedLibrarians.authLibrarian(testLibrarianUser, testLibrarianAuthCode)).thenReturn(Librarians.AuthType.FULL_TIME);
//        CLI and run code
        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);

        cli.run();

        verify(mockedLibrary).revokeMembership(memberID);
    }

    @Test
    void revokeMembershipTestShouldFail() {
        // Chooses Revoke Membership and submits an existing member to revoke
        String memberID = "testMemberID";
        // Fake user input: 8 (revoke membership), auth, then memberID, then menu, exit
        String userInput = String.join("\n",
                "8",
                testLibrarianUser,
                testLibrarianAuthCode,
                memberID,
                "",
                this.cliExitOption
        );

//        Mock to revoke
        when(mockedLibrary.revokeMembership(anyString())).thenReturn(false);
//        When authenticating, it's important that the librarian is auth and also FULL_TIME
        when(mockedLibrarians.authLibrarian(testLibrarianUser, testLibrarianAuthCode)).thenReturn(Librarians.AuthType.FULL_TIME);
//        CLI and run code
        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);

        cli.run();

        verify(mockedSystemOut).println("\nFailed to revoke membership of member with ID: testMemberID");
    }

    //    OPTION 9: View All Members
        // None

    //    OPTION 10: Hire Part-Time Librarian

    //    OPTION 11: Withdraw Salary

    //    OPTION 12: Donate to Library

    //    OPTION 13: Exit

    @Test
    void exitTestShouldSucceed() {
        // Chooses Exit and program exits

        // Arrange
        // Fake user input: 10 (Exit), then anything to continue
        String userInput = String.join("\n",
                this.cliExitOption // Program should end after exit is requested
        );

        // Create CLI with fake input
        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);

        // Act
        cli.run();

        // Assert that Exit was called and set exit to true
        assertEquals(true, cli.exit);
    }

    // ***** Property Tests *****

    //    OPTION 9: View All Members

    @Property
    void viewAllMembersTestShouldSucceed(@ForAll @IntRange(min = 1, max = 5) int size) {
        // Chooses View All Members, members exist, should return all members
        String userInput = String.join("\n",
                "9",
                "",
                this.cliExitOption
        );

        // Create CLI with fake input
        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);

        // Stub that library.getAllMembers() returns my list of members
        List<Member> members = IntStream.range(0, size)
                .mapToObj(i -> mock(Member.class))
                .toList();
        when(mockedLibrary.getAllMembers()).thenReturn(members);

        // Act
        cli.run();

        // Assert that each member correctly called printMemberInfo
        members.forEach(m -> verify(m).printMemberInfo());
    }

    // ***** Structural Tests *****

    // Auth

    @Test
    void authenticateTestShouldFail() {
        // authenticate() should return NOT_AUTHORIZED

        String userInput = String.join("\n",
                "",
                ""
        );

        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
        when(mockedLibrarians.authLibrarian(anyString(), anyString())).thenReturn(Librarians.AuthType.NOT_AUTHORIZED);

        CLI.AuthResult authResult = cli.authenticate();

        assertEquals(Librarians.AuthType.NOT_AUTHORIZED, authResult.authType);
    }

    //    OPTION 1: Order Book

    //    Empty field (bookName)
    @Test
    void emptyBookName() {
        String bookName = "";
        String author = "Jane Doe";
        int year = 2024;
        String genre = "Fiction";
        int ISBN = 123456;

        // Fake user input
        String userInput = String.join("\n",
                "1",              // Select option 1 (Add Book)
                testLibrarianUser,
                testLibrarianAuthCode,
                bookName,
                author,
                String.valueOf(year),
                genre,
                String.valueOf(ISBN),
                "",
                this.cliExitOption
        );

        // Create CLI with fake input
        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
        when(mockedLibrarians.authLibrarian(testLibrarianUser, testLibrarianAuthCode)).thenReturn(Librarians.AuthType.FULL_TIME);

        // Run
        cli.run();

        verifyNoInteractions(mockedLibrary);
    }

    //    Empty field (autho)
    @Test
    void emptyAuthor() {
        String bookName = "My Book";
        String author = "";
        int year = 2024;
        String genre = "Fiction";
        int ISBN = 123456;

        // Fake user input
        String userInput = String.join("\n",
                "1",              // Select option 1 (Add Book)
                testLibrarianUser,
                testLibrarianAuthCode,
                bookName,
                author,
                String.valueOf(year),
                genre,
                String.valueOf(ISBN),
                "",
                this.cliExitOption
        );

        // Create CLI with fake input
        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
        when(mockedLibrarians.authLibrarian(testLibrarianUser, testLibrarianAuthCode)).thenReturn(Librarians.AuthType.FULL_TIME);

        // Run
        cli.run();

        verifyNoInteractions(mockedLibrary);
    }

    //    Empty field (year)
    @Test
    void emptyYear() {
        String bookName = "My Book";
        String author = "Jane Doe";
        String year = "";
        String genre = "Fiction";
        int ISBN = 123456;

        // Fake user input
        String userInput = String.join("\n",
                "1",              // Select option 1 (Add Book)
                testLibrarianUser,
                testLibrarianAuthCode,
                bookName,
                author,
                year,
                genre,
                String.valueOf(ISBN),
                "",
                this.cliExitOption
        );

        // Create CLI with fake input
        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
        when(mockedLibrarians.authLibrarian(testLibrarianUser, testLibrarianAuthCode)).thenReturn(Librarians.AuthType.FULL_TIME);

        // Run
        cli.run();

        verifyNoInteractions(mockedLibrary);
    }

    //    Bad field (year)
    @Test
    void badYear() {
        String bookName = "My Book";
        String author = "Jane Doe";
        String year = "not a number";
        String genre = "Fiction";
        int ISBN = 123456;

        // Fake user input
        String userInput = String.join("\n",
                "1",              // Select option 1 (Add Book)
                testLibrarianUser,
                testLibrarianAuthCode,
                bookName,
                author,
                year,
                genre,
                String.valueOf(ISBN),
                "",
                this.cliExitOption
        );

        // Create CLI with fake input
        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
        when(mockedLibrarians.authLibrarian(testLibrarianUser, testLibrarianAuthCode)).thenReturn(Librarians.AuthType.FULL_TIME);

        // Run
        cli.run();

        verifyNoInteractions(mockedLibrary);
    }

    //    Empty field (genre)
    @Test
    void emptyGenre() {
        String bookName = "My Book";
        String author = "Jane Doe";
        int year = 2024;
        String genre = "";
        int ISBN = 123456;

        // Fake user input
        String userInput = String.join("\n",
                "1",              // Select option 1 (Add Book)
                testLibrarianUser,
                testLibrarianAuthCode,
                bookName,
                author,
                String.valueOf(year),
                genre,
                String.valueOf(ISBN),
                "",
                this.cliExitOption
        );

        // Create CLI with fake input
        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
        when(mockedLibrarians.authLibrarian(testLibrarianUser, testLibrarianAuthCode)).thenReturn(Librarians.AuthType.FULL_TIME);

        // Run
        cli.run();

        verifyNoInteractions(mockedLibrary);
    }

    //    Empty field (isbn)
    @Test
    void emptyIsbn() {
        String bookName = "My Book";
        String author = "Jane Doe";
        int year = 2024;
        String genre = "Fiction";
        String ISBN = "";

        // Fake user input
        String userInput = String.join("\n",
                "1",              // Select option 1 (Add Book)
                testLibrarianUser,
                testLibrarianAuthCode,
                bookName,
                author,
                String.valueOf(year),
                genre,
                ISBN,
                "",
                this.cliExitOption
        );

        // Create CLI with fake input
        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
        when(mockedLibrarians.authLibrarian(testLibrarianUser, testLibrarianAuthCode)).thenReturn(Librarians.AuthType.FULL_TIME);

        // Run
        cli.run();

        verifyNoInteractions(mockedLibrary);
    }

    //    Bad field (isbn)
    @Test
    void badIsbn() {
        String bookName = "My Book";
        String author = "Jane Doe";
        int year = 2024;
        String genre = "Fiction";
        String ISBN = "not a number";

        // Fake user input
        String userInput = String.join("\n",
                "1",              // Select option 1 (Add Book)
                testLibrarianUser,
                testLibrarianAuthCode,
                bookName,
                author,
                String.valueOf(year),
                genre,
                ISBN,
                "",
                this.cliExitOption
        );

        // Create CLI with fake input
        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
        when(mockedLibrarians.authLibrarian(testLibrarianUser, testLibrarianAuthCode)).thenReturn(Librarians.AuthType.FULL_TIME);

        // Run
        cli.run();

        verifyNoInteractions(mockedLibrary);
    }

    //    OPTION 2: Remove Book

    //    Empty book name
    @Test
    void testEmptyBook() {
        String bookName = "";

        // Fake user input: 2 (remove book), then book name, then anything to continue
        String userInput = String.join("\n",
                "2",              // Select option 2 (Remove Book)
                bookName,               // Book name
                "",                     // (Empty input to get to menu)
                this.cliExitOption                    // Exit (after adding book, immediately exit)
        );

        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);

        cli.run();

        verifyNoInteractions(mockedLibrary);
    }

    //     OPTION 3: Check Book Availability

    //    Bad Book Name
    @Test
    void badBookName() {
        String bookName = "";

        String userInput = String.join("\n",
                "3",
                bookName,
                "",
                this.cliExitOption);

        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
        cli.run();

        verifyNoInteractions(mockedLibrary);
    }

    //    OPTION 4: Checkout Book

    @Test
    void testCheckoutBook_emptyMemberId_triggersValidation() {
        // Simulated input: 4 (checkout), then empty string for member ID, then this.menuOption to exit
        String userInput = String.join("\n",
                "4",
                "",            // Empty member ID (should trigger validation)
                "Book1",   // Won't be read
                "",            // Continue
                this.cliExitOption
        );

        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
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
                this.cliExitOption
        );

        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
        //Run
        cli.run();

        verify(mockedLibrary, never()).checkoutBook(anyString(), anyString());
    }

    //    OPTION 5: Return Book

    @Test
    void testReturnBook_emptyMemberId_triggersValidation() {
        String userInput = String.join("\n",
                "5",
                "",            // Invalid (empty) member ID
                "Some Book",
                "", this.cliExitOption
        );

        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);

        //Run
        cli.run();

        verify(mockedLibrary, never()).returnBook(anyString(), anyString());
    }

    @Test
    void testReturnBook_emptyBookName_triggersValidation() {
        String userInput = String.join("\n",
                "5",
                "1",
                "",
                "", this.cliExitOption
        );

        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
        cli.run();

        verify(mockedLibrary, never()).returnBook(anyString(), anyString());
    }

    //    OPTION 6: View All Books
        // No structural tests made for this one

    //    OPTION 7: Add Member

    @Test
    void addMemberTestFail_NameEmpty() {
        // Fake user input: 7 (add member), auth, then member fields, anything to continue, then exit
        String name = "";
        String email = "testEmail";
        String userInput = String.join("\n",
                "7",
                testLibrarianUser,
                testLibrarianAuthCode,
                name,
                email,
                "",
                this.cliExitOption
        );

//        When authenticating, it's important that the librarian is auth and also FULL_TIME
        when(mockedLibrarians.authLibrarian(testLibrarianUser, testLibrarianAuthCode)).thenReturn(Librarians.AuthType.FULL_TIME);

        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
        cli.run();

//        Member is added, then their info's printed
        verify(mockedSystemOut).println("\nNot a valid name");
    }

    @Test
    void addMemberTestFail_EmailEmpty() {
        // Fake user input: 7 (add member), auth, then member fields, anything to continue, then exit
        String name = "testName";
        String email = "";
        String userInput = String.join("\n",
                "7",
                testLibrarianUser,
                testLibrarianAuthCode,
                name,
                email,
                "",
                this.cliExitOption
        );

//
        Member mockedMember = mock(Member.class);
        when(mockedLibrary.addMember(anyString(), anyString())).thenReturn(mockedMember);
//        When authenticating, it's important that the librarian is auth and also FULL_TIME
        when(mockedLibrarians.authLibrarian(testLibrarianUser, testLibrarianAuthCode)).thenReturn(Librarians.AuthType.FULL_TIME);

        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
        cli.run();

//        Member is added, then their info's printed
        verify(mockedSystemOut).println("\nNot a valid email");
    }

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
                this.cliExitOption                    // Exit (after adding book, immediately exit)
        );

        // Create CLI with fake input
        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);

        // Act
        cli.run();

        // Assert that addMember() was never called
        verify(mockedLibrary, never()).addMember(anyString(), anyString());
    }

    //    OPTION 8: Revoke Membership

    @Test
    void revokeMembershipTest_EmptyIDisBad() {
        // Chooses Revoke Membership and submits an existing member to revoke
        String memberID = "";
        // Fake user input: 8 (revoke membership), auth, then memberID, then menu, exit
        String userInput = String.join("\n",
                "8",
                testLibrarianUser,
                testLibrarianAuthCode,
                memberID,
                "",
                this.cliExitOption
        );

//        When authenticating, it's important that the librarian is auth and also FULL_TIME
        when(mockedLibrarians.authLibrarian(testLibrarianUser, testLibrarianAuthCode)).thenReturn(Librarians.AuthType.FULL_TIME);
//        CLI and run code
        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);

        cli.run();

        verify(mockedSystemOut).println("\nNot a valid ID");
    }

    @Test
    void revokeMembershipEmptyTestShouldFail() {
        // Chooses Revoke Membership and submits an empty member ID

        // Arrange
        // Fake user input: 8 (revoke membership), then memberID, then anything to continue
        String userInput = String.join("\n",
                "8",
                "",
                "",                     // (Empty input to get to menu)
                this.cliExitOption                    // Exit (after adding book, immediately exit)
        );

        // Create CLI with fake input
        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);

        // Act
        cli.run();

        // Assert that revokeMembership was never called with empty input
        verify(mockedLibrary, never()).revokeMembership(anyString());
    }

    //    OPTION 9: View All Members

    @Test
    void viewAllMembers_Null() {
        // Chooses View All Members, members exist, should return all members
        String userInput = String.join("\n",
                "9",
                "",
                this.cliExitOption
        );
        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);

        when(mockedLibrary.getAllMembers()).thenReturn(null);
        cli.run();
        verify(mockedSystemOut).println("There are no members in the library");
    }

    @Test
    void viewAllMembers_Empty() {
        // Chooses View All Members, members exist, should return all members
        String userInput = String.join("\n",
                "9",
                "",
                this.cliExitOption
        );
        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);

        when(mockedLibrary.getAllMembers()).thenReturn(List.of());
        cli.run();
        verify(mockedSystemOut).println("There are no members in the library");
    }

    //    OPTION 10: Hire Part-Time Librarian

    //    OPTION 11: Withdraw Salary

    //    OPTION 12: Donate to Library

    //    OPTION 13: Exit
        // None
}
