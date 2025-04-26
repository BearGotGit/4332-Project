//package CLI.Main;
//
//import net.jqwik.api.ForAll;
//import net.jqwik.api.Property;
//import net.jqwik.api.constraints.IntRange;
//import net.jqwik.api.lifecycle.BeforeTry;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.*;
//
//import CLI.Models.*;
//
//import java.io.PrintStream;
//import java.io.StringReader;
//import java.util.List;
//import java.util.stream.IntStream;
//
//
//public class CLITestOLD_UNORGANIZED {
//    Library mockedLibrary;
//    LibraryAccounts mockedAccounts;
//    Librarians mockedLibrarians;
//    PrintStream mockedSystemOut;
//    CLI cli;
//    String cliExitOption = "13";
//
//    String testLibrarianUser = "A";
//    String testLibrarianAuth = "111111";
//
//    @BeforeEach
//    @BeforeTry
//    public void setup() {
//        mockedLibrary = mock(Library.class);
//        mockedAccounts = mock(LibraryAccounts.class);
//        mockedLibrarians = mock(Librarians.class);
//        mockedSystemOut = mock(System.out.getClass());
//    }
//
//    /* Specification Tests */
//    /* Note: All specification and structural tests have significant overlap since interface is so simple... */
//
////    OPTION 1)
//
//    //    Book not in library yet; add it (happy case)
//    @Test
//    void testAddBook() {
////       Qualities of book
//        String bookName = "My Book";
//        String author = "Jane Doe";
//        int year = 2024;
//        String genre = "Fiction";
//        int isbn = 123456;
//
//        // Fake user input: 1 (add book), then book fields, then anything to continue
//        String userInput = String.join("\n",
//                "1",              // Select option 1 (Add Book)
//                bookName,               // Book name
//                author,                 // Author
//                String.valueOf(year),           // Year
//                genre,                  // Genre
//                String.valueOf(isbn),         // ISBN
//                "",                     // (Empty input to get to menu)
//                this.cliExitOption                    // Exit (after adding book, immediately exit)
//        );
//
//        // Create CLI with fake input
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//
//        // Mock that library.addBook() returns a dummy Book
//        Book fakeBook = mock(Book.class);
//        when(mockedLibrary.addBook(anyString(), anyString(), anyInt(), anyString(), anyInt())).thenReturn(fakeBook);
//
//        // Run
//        cli.run();
//
//        // Verify that addBook was called correctly
//        verify(mockedLibrary).addBook(
//                bookName,
//                author,
//                year,
//                genre,
//                isbn
//        );
//    }
//
//    //    Book in library already or otherwise library can't make book
//    @Test
//    void testAddBookCantMake() {
//        //       Qualities of book
//        String bookName = "My Book";
//        String author = "Jane Doe";
//        int year = 2024;
//        String genre = "Fiction";
//        int isbn = 123456;
//
//        // Fake user input: 1 (add book), then book fields, then anything to continue
//        String userInput = String.join("\n",
//                "1",              // Select option 1 (Add Book)
//                bookName,               // Book name
//                author,                 // Author
//                String.valueOf(year),           // Year
//                genre,                  // Genre
//                String.valueOf(isbn),         // ISBN
//                "",                     // (Empty input to get to menu)
//                this.cliExitOption                    // Exit (after adding book, immediately exit)
//        );
//
//
//        // Create CLI with fake input
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//
//        // Mock that library.addBook() returns a dummy Book
//        when(mockedLibrary.addBook(anyString(), anyString(), anyInt(), anyString(), anyInt())).thenReturn(null);
//
//        // Run
//        cli.run();
//
//        // Verify that addBook was called correctly
//        verify(mockedLibrary).addBook(
//                bookName,
//                author,
//                year,
//                genre,
//                isbn
//        );
//    }
//
//    /* Structural Testing */
//
//    //    Empty field (bookName)
//    @Test
//    void emptyBookName() {
//        //       Qualities of book
//        String bookName = "";
//        String author = "Jane Doe";
//        int year = 2024;
//        String genre = "Fiction";
//        int isbn = 123456;
//
//        // Fake user input: 1 (add book), then book fields, then anything to continue
//        String userInput = String.join("\n",
//                "1",              // Select option 1 (Add Book)
//                bookName,               // Book name
//                author,                 // Author
//                String.valueOf(year),           // Year
//                genre,                  // Genre
//                String.valueOf(isbn),         // ISBN
//                "",                     // (Empty input to get to menu)
//                this.cliExitOption                    // Exit (after adding book, immediately exit)
//        );
//
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//
//        cli.run();
//
//        verifyNoInteractions(mockedLibrary);
//    }
//
//    //    Empty field (author)
//    @Test
//    void emptyAuthor() {
//        //       Qualities of book
//        String bookName = "My Book";
//        String author = "";
//        int year = 2024;
//        String genre = "Fiction";
//        int isbn = 123456;
//
//        // Fake user input: 1 (add book), then book fields, then anything to continue
//        String userInput = String.join("\n",
//                "1",              // Select option 1 (Add Book)
//                bookName,               // Book name
//                author,                 // Author
//                String.valueOf(year),           // Year
//                genre,                  // Genre
//                String.valueOf(isbn),         // ISBN
//                "",                     // (Empty input to get to menu)
//                this.cliExitOption                    // Exit (after adding book, immediately exit)
//        );
//
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//
//        cli.run();
//
//        verifyNoInteractions(mockedLibrary);
//    }
//
//    //    Empty field (year)
//    @Test
//    void emptyYear() {
//        //       Qualities of book
//        String bookName = "My Book";
//        String author = "Jane Doe";
//        String year = "";
//        String genre = "Fiction";
//        int isbn = 123456;
//
//        // Fake user input: 1 (add book), then book fields, then anything to continue
//        String userInput = String.join("\n",
//                "1",              // Select option 1 (Add Book)
//                bookName,               // Book name
//                author,                 // Author
//                year,           // Year
//                genre,                  // Genre
//                String.valueOf(isbn),         // ISBN
//                "",                     // (Empty input to get to menu)
//                this.cliExitOption                    // Exit (after adding book, immediately exit)
//        );
//
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//
//        cli.run();
//
//        verifyNoInteractions(mockedLibrary);
//    }
//
//    //    Bad field (year)
//    @Test
//    void badYear() {
//        //       Qualities of book
//        String bookName = "My Book";
//        String author = "Jane Doe";
//        String year = "bad";
//        String genre = "Fiction";
//        int isbn = 123456;
//
//        // Fake user input: 1 (add book), then book fields, then anything to continue
//        String userInput = String.join("\n",
//                "1",              // Select option 1 (Add Book)
//                bookName,               // Book name
//                author,                 // Author
//                year,           // Year
//                genre,                  // Genre
//                String.valueOf(isbn),         // ISBN
//                "",                     // (Empty input to get to menu)
//                this.cliExitOption                    // Exit (after adding book, immediately exit)
//        );
//
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//
//        cli.run();
//
//        verifyNoInteractions(mockedLibrary);
//    }
//
//    //    Empty field (genre)
//    @Test
//    void emptyGenre() {
//        //       Qualities of book
//        String bookName = "My Book";
//        String author = "Jane Doe";
//        int year = 2024;
//        String genre = "";
//        int isbn = 123456;
//
//        // Fake user input: 1 (add book), then book fields, then anything to continue
//        String userInput = String.join("\n",
//                "1",              // Select option 1 (Add Book)
//                bookName,               // Book name
//                author,                 // Author
//                String.valueOf(year),           // Year
//                genre,                  // Genre
//                String.valueOf(isbn),         // ISBN
//                "",                     // (Empty input to get to menu)
//                this.cliExitOption                    // Exit (after adding book, immediately exit)
//        );
//
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//
//        cli.run();
//
//        verifyNoInteractions(mockedLibrary);
//    }
//
//    //    Bad field (isbn)
//    @Test
//    void badIsbn() {
//        //       Qualities of book
//        String bookName = "My Book";
//        String author = "Jane Doe";
//        int year = 2024;
//        String genre = "Fiction";
//        String isbn = "bad";
//
//        // Fake user input: 1 (add book), then book fields, then anything to continue
//        String userInput = String.join("\n",
//                "1",              // Select option 1 (Add Book)
//                bookName,               // Book name
//                author,                 // Author
//                String.valueOf(year),           // Year
//                genre,                  // Genre
//                isbn,         // ISBN
//                "",                     // (Empty input to get to menu)
//                this.cliExitOption                    // Exit (after adding book, immediately exit)
//        );
//
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//
//        cli.run();
//
//        verifyNoInteractions(mockedLibrary);
//    }
//
//
//    //    Empty field (isbn)
//    @Test
//    void emptyIsbn() {
//        //       Qualities of book
//        String bookName = "My Book";
//        String author = "Jane Doe";
//        int year = 2024;
//        String genre = "Fiction";
//        String isbn = "";
//
//        // Fake user input: 1 (add book), then book fields, then anything to continue
//        String userInput = String.join("\n",
//                "1",              // Select option 1 (Add Book)
//                bookName,               // Book name
//                author,                 // Author
//                String.valueOf(year),           // Year
//                genre,                  // Genre
//                isbn,         // ISBN
//                "",                     // (Empty input to get to menu)
//                this.cliExitOption                    // Exit (after adding book, immediately exit)
//        );
//
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//
//        cli.run();
//
//        verifyNoInteractions(mockedLibrary);
//    }
//
////    OPTION 2) Remove Book
//
//    /* Specification Testing */
//
//    //    Remove book (in library)
//    @Test
//    void testRemoveBook() {
////        Setup for verifying actions during cli
//        String bookId = "bookId";
//        doNothing().when(mockedLibrary).removeBook(anyString());
//        when(mockedLibrary.findBookIdByName(anyString())).thenReturn(bookId);
//
////       run cli
////       Qualities of book
//
//        // Fake user input: 1 (add book), then book fields, then anything to continue
//        String userInput = String.join("\n",
//                "2",              // Select option 2 (Remove Book)
//                "La Despidida",               // Book name
//                "",                     // (Empty input to get to menu)
//                this.cliExitOption                    // Exit (after adding book, immediately exit)
//        );
//
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//
//        cli.run();
//
//        verify(mockedLibrary).removeBook(bookId);
//    }
//
//
//    //    Remove book (not in library)
//    @Test
//    void testRemoveBookNotFound() {
//        //        Setup for verifying actions during cli
//        when(mockedLibrary.findBookIdByName(anyString())).thenReturn(null);
//
//        // Fake user input: 2 (remove book), then book name, then anything to continue
//        String userInput = String.join("\n",
//                "2",              // Select option 2 (Remove Book)
//                "La Despidida",               // Book name
//                "",                     // (Empty input to get to menu)
//                this.cliExitOption                    // Exit (after adding book, immediately exit)
//        );
//
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//
//        cli.run();
//
//        verify(mockedLibrary).findBookIdByName(anyString());
//        verifyNoMoreInteractions(mockedLibrary);
//    }
//
//    //    Empty book name
//    @Test
//    void testEmptyBook() {
//        String bookName = "";
//
//        // Fake user input: 2 (remove book), then book name, then anything to continue
//        String userInput = String.join("\n",
//                "2",              // Select option 2 (Remove Book)
//                bookName,               // Book name
//                "",                     // (Empty input to get to menu)
//                this.cliExitOption                    // Exit (after adding book, immediately exit)
//        );
//
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//
//        cli.run();
//
//        verifyNoInteractions(mockedLibrary);
//    }
//
////     OPTION 3) Check Book Availability
//
//    /* Specification Tests */
//
//    //    Book can't be found
//    @Test
//    void bookNotFound() {
//        when(mockedLibrary.findBookIdByName(anyString())).thenReturn(null);
//
//        String bookName = "My Book";
//
//        String userInput = String.join("\n",
//                "3",
//                bookName,
//                "",
//                this.cliExitOption);
//
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//        cli.run();
//
//        verify(mockedLibrary).findBookIdByName(anyString());
//        verifyNoMoreInteractions(mockedLibrary);
//
//    }
//
//    //    Book is available
//    @Test
//    void bookIsAvailable() {
//        when(mockedLibrary.findBookIdByName(anyString())).thenReturn("1");
//        when(mockedLibrary.bookAvailability(anyString())).thenReturn(true);
//
//        String bookName = "My Book";
//
//        String userInput = String.join("\n",
//                "3",
//                bookName,
//                "",
//                this.cliExitOption);
//
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//        cli.run();
//
//        verify(mockedLibrary).findBookIdByName(anyString());
//        verify(mockedLibrary).bookAvailability(anyString());
//        verifyNoMoreInteractions(mockedLibrary);
//
//    }
//
//    //    Book is not available
//    @Test
//    void bookIsNotAvailable() {
//        when(mockedLibrary.findBookIdByName(anyString())).thenReturn("1");
//        when(mockedLibrary.bookAvailability(anyString())).thenReturn(false);
//
//        String bookName = "My Book";
//
//        String userInput = String.join("\n",
//                "3",
//                bookName,
//                "",
//                this.cliExitOption);
//
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//        cli.run();
//
//        verify(mockedLibrary).findBookIdByName(anyString());
//        verify(mockedLibrary).bookAvailability(anyString());
//        verifyNoMoreInteractions(mockedLibrary);
//    }
//
//    /* Structural Tests */
//
//    //    Bad Book Name
//    @Test
//    void badBookName() {
//        String bookName = "";
//
//        String userInput = String.join("\n",
//                "3",
//                bookName,
//                "",
//                this.cliExitOption);
//
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//        cli.run();
//
//        verifyNoInteractions(mockedLibrary);
//    }
//
//
////    OPTION 4)
//
//    @Test
//    void testCheckoutBook() {
//        String memberId = "1";
//        String bookName = "Test Book";
//
//        // Simulated user input: 4 (checkout book), member ID, book name, "", 10 (exit)
//        String userInput = String.join("\n",
//                "4",          // Select option 4: Checkout Book
//                memberId,     // Member ID
//                bookName,     // Book name
//                "",           // Continue
//                this.cliExitOption          // Exit
//        );
//
//        // Create CLI with fake input and mocked library
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//
//        // Run
//        cli.run();
//
//        // Verify that the library's checkoutBook method was called
//        verify(mockedLibrary).checkoutBook(memberId, bookName);
//    }
//
//    @Test
//    void testCheckoutBook_emptyMemberId_triggersValidation() {
//        // Simulated input: 4 (checkout), then empty string for member ID, then this.menuOption to exit
//        String userInput = String.join("\n",
//                "4",
//                "",            // Empty member ID (should trigger validation)
//                "Book1",   // Won't be read
//                "",            // Continue
//                this.cliExitOption
//        );
//
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//        //Run
//        cli.run();
//
//        // Verify that checkoutBook was never called
//        verify(mockedLibrary, never()).checkoutBook(anyString(), anyString());
//    }
//
//
//    @Test
//    void testCheckoutBook_emptyBookName_triggersValidation() {
//        String userInput = String.join("\n",
//                "4",
//                "1",          // Valid member ID
//                "",           // Empty book name (should trigger validation)
//                "",           // Continue
//                this.cliExitOption
//        );
//
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//        //Run
//        cli.run();
//
//        verify(mockedLibrary, never()).checkoutBook(anyString(), anyString());
//    }
//
//    @Test
//    void testReturnBook() {
//        // Arrange
//        String memberId = "1";
//        String bookName = "Some Book";
//        String resolvedBookID = "B001";
//
//        // Simulated user input: 5 (return), member ID, book name, "", 10 (exit)
//        String userInput = String.join("\n",
//                "5",
//                memberId,
//                bookName,
//                "",     // Continue
//                this.cliExitOption    // Exit
//        );
//
//        // Mock findBookIdByName to return the correct ID
//        when(mockedLibrary.findBookIdByName(bookName)).thenReturn(resolvedBookID);
//
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//
//        // Run
//        cli.run();
//
//        verify(mockedLibrary).findBookIdByName(bookName);
//        verify(mockedLibrary).returnBook(memberId, resolvedBookID);
//    }
//
//    @Test
//    void testReturnBook_emptyMemberId_triggersValidation() {
//        String userInput = String.join("\n",
//                "5",
//                "",            // Invalid (empty) member ID
//                "Some Book",
//                "", this.cliExitOption
//        );
//
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//
//        //Run
//        cli.run();
//
//        verify(mockedLibrary, never()).returnBook(anyString(), anyString());
//    }
//
//    @Test
//    void testReturnBook_emptyBookName_triggersValidation() {
//        String userInput = String.join("\n",
//                "5",
//                "1",
//                "",
//                "", this.cliExitOption
//        );
//
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//        cli.run();
//
//        verify(mockedLibrary, never()).returnBook(anyString(), anyString());
//    }
//
//    @Test
//    void testReturnBook_bookNotFound_triggersGuardClause() {
//        String userInput = String.join("\n",
//                "5",
//                "1",
//                "Nonexistent Book",
//                "", this.cliExitOption
//        );
//
//        when(mockedLibrary.findBookIdByName("Nonexistent Book")).thenReturn(null);
//
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//        cli.run();
//
//        verify(mockedLibrary, never()).returnBook(anyString(), anyString());
//    }
//
//    @Test
//    void testViewAllBooks_withBooks() {
//        Library realLibrary = new Library();
//
//        Book book1 = mock(Book.class);
//        Book book2 = mock(Book.class);
//        when(book1.getBookInfo()).thenReturn("Book 1");
//        when(book2.getBookInfo()).thenReturn("Book 2");
//
//        realLibrary.AllBooksInLibrary.add(book1);
//        realLibrary.AllBooksInLibrary.add(book2);
//
//        String userInput = String.join("\n",
//                "6", "", this.cliExitOption
//        );
//
//        CLI cli = new CLI(new StringReader(userInput), mockedSystemOut, realLibrary, mockedAccounts, mockedLibrarians);
//        cli.run();
//
//        verify(book1).getBookInfo();
//        verify(book2).getBookInfo();
//    }
//
//
//    @Test
//    void testViewAllBooks_whenLibraryIsEmpty() {
//        String userInput = String.join("\n",
//                "6", "", this.cliExitOption
//        );
//
//        CLI cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//        cli.run();
//
//        verify(mockedSystemOut).println("There are no books in the library");
//    }
//
//
//
//
////    OPTION 7-9
//
//
//    // Specification tests
//
//    @Test
//    void addMemberTestShouldSucceed() {
//        // Fake user input: 7 (add member), auth, then member fields, anything to continue, then exit
//        String name = "testName";
//        String email = "testEmail";
//        String userInput = String.join("\n",
//                "7",
//                testLibrarianUser,
//                testLibrarianAuth,
//                name,
//                email,
//                "",
//                this.cliExitOption
//        );
//
//// Member prints info only when created
//        Member mockedMember = mock(Member.class);
//        when(mockedLibrary.addMember(anyString(), anyString())).thenReturn(mockedMember);
////        When authenticating, it's important that the librarian is auth and also FULL_TIME
//        when(mockedLibrarians.authLibrarian(testLibrarianUser, testLibrarianAuth)).thenReturn(Librarians.AuthType.FULL_TIME);
//
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//        cli.run();
//
////        Member is added, then their info's printed
//        verify(mockedLibrary).addMember(name, email);
//        verify(mockedMember).printMemberInfo();
//    }
//
//    @Test
//    void addMemberTestFail_NameEmpty() {
//        // Fake user input: 7 (add member), auth, then member fields, anything to continue, then exit
//        String name = "";
//        String email = "testEmail";
//        String userInput = String.join("\n",
//                "7",
//                testLibrarianUser,
//                testLibrarianAuth,
//                name,
//                email,
//                "",
//                this.cliExitOption
//        );
//
////        When authenticating, it's important that the librarian is auth and also FULL_TIME
//        when(mockedLibrarians.authLibrarian(testLibrarianUser, testLibrarianAuth)).thenReturn(Librarians.AuthType.FULL_TIME);
//
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//        cli.run();
//
////        Member is added, then their info's printed
//        verify(mockedSystemOut).println("\nNot a valid name");
//    }
//
//    @Test
//    void addMemberTestFail_EmailEmpty() {
//        // Fake user input: 7 (add member), auth, then member fields, anything to continue, then exit
//        String name = "testName";
//        String email = "";
//        String userInput = String.join("\n",
//                "7",
//                testLibrarianUser,
//                testLibrarianAuth,
//                name,
//                email,
//                "",
//                this.cliExitOption
//        );
//
////
//        Member mockedMember = mock(Member.class);
//        when(mockedLibrary.addMember(anyString(), anyString())).thenReturn(mockedMember);
////        When authenticating, it's important that the librarian is auth and also FULL_TIME
//        when(mockedLibrarians.authLibrarian(testLibrarianUser, testLibrarianAuth)).thenReturn(Librarians.AuthType.FULL_TIME);
//
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//        cli.run();
//
////        Member is added, then their info's printed
//        verify(mockedSystemOut).println("\nNot a valid email");
//    }
//
//    @Test
//    void addMemberTestFail_CannotCreate() {
//        // Fake user input: 7 (add member), auth, then member fields, anything to continue, then exit
//        String name = "testName";
//        String email = "testEmail";
//        String userInput = String.join("\n",
//                "7",
//                testLibrarianUser,
//                testLibrarianAuth,
//                name,
//                email,
//                "",
//                this.cliExitOption
//        );
//
//        when(mockedLibrary.addMember(anyString(), anyString())).thenReturn(null);
////        When authenticating, it's important that the librarian is auth and also FULL_TIME
//        when(mockedLibrarians.authLibrarian(testLibrarianUser, testLibrarianAuth)).thenReturn(Librarians.AuthType.FULL_TIME);
//
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//        cli.run();
//
////        Member is added, then their info's printed
//        verify(mockedSystemOut).println("New member couldn't be created");
//    }
//
//    @Test
//    void revokeMembershipTestShouldSucceed() {
//        // Chooses Revoke Membership and submits an existing member to revoke
//        String memberID = "testMemberID";
//        // Fake user input: 8 (revoke membership), auth, then memberID, then menu, exit
//        String userInput = String.join("\n",
//                "8",
//                testLibrarianUser,
//                testLibrarianAuth,
//                memberID,
//                "",
//                this.cliExitOption
//        );
//
////        Mock to revoke
//        when(mockedLibrary.revokeMembership(anyString())).thenReturn(true);
////        When authenticating, it's important that the librarian is auth and also FULL_TIME
//        when(mockedLibrarians.authLibrarian(testLibrarianUser, testLibrarianAuth)).thenReturn(Librarians.AuthType.FULL_TIME);
////        CLI and run code
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//
//        cli.run();
//
//        verify(mockedLibrary).revokeMembership(memberID);
//    }
//
//    @Test
//    void revokeMembershipTestShouldFailed() {
//        // Chooses Revoke Membership and submits an existing member to revoke
//        String memberID = "testMemberID";
//        // Fake user input: 8 (revoke membership), auth, then memberID, then menu, exit
//        String userInput = String.join("\n",
//                "8",
//                testLibrarianUser,
//                testLibrarianAuth,
//                memberID,
//                "",
//                this.cliExitOption
//        );
//
////        Mock to revoke
//        when(mockedLibrary.revokeMembership(anyString())).thenReturn(false);
////        When authenticating, it's important that the librarian is auth and also FULL_TIME
//        when(mockedLibrarians.authLibrarian(testLibrarianUser, testLibrarianAuth)).thenReturn(Librarians.AuthType.FULL_TIME);
////        CLI and run code
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//
//        cli.run();
//
//        verify(mockedSystemOut).println("\nFailed to revoke membership of member with ID: testMemberID");
//    }
//
//    @Test
//    void revokeMembershipTest_EmptyIDisBad() {
//        // Chooses Revoke Membership and submits an existing member to revoke
//        String memberID = "";
//        // Fake user input: 8 (revoke membership), auth, then memberID, then menu, exit
//        String userInput = String.join("\n",
//                "8",
//                testLibrarianUser,
//                testLibrarianAuth,
//                memberID,
//                "",
//                this.cliExitOption
//        );
//
////        When authenticating, it's important that the librarian is auth and also FULL_TIME
//        when(mockedLibrarians.authLibrarian(testLibrarianUser, testLibrarianAuth)).thenReturn(Librarians.AuthType.FULL_TIME);
////        CLI and run code
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//
//        cli.run();
//
//        verify(mockedSystemOut).println("\nNot a valid ID");
//    }
//
//    @Test
//    void viewAllMembers_Null() {
//        // Chooses View All Members, members exist, should return all members
//        String userInput = String.join("\n",
//                "9",
//                "",
//                this.cliExitOption
//        );
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//
//        when(mockedLibrary.getAllMembers()).thenReturn(null);
//        cli.run();
//        verify(mockedSystemOut).println("There are no members in the library");
//    }
//
//    @Test
//    void viewAllMembers_Empty() {
//        // Chooses View All Members, members exist, should return all members
//        String userInput = String.join("\n",
//                "9",
//                "",
//                this.cliExitOption
//        );
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//
//        when(mockedLibrary.getAllMembers()).thenReturn(List.of());
//        cli.run();
//        verify(mockedSystemOut).println("There are no members in the library");
//    }
//
//    @Property
//    void viewAllMembersTestShouldSucceed(@ForAll @IntRange(min = 1, max = 5) int size) {
//        // Chooses View All Members, members exist, should return all members
//        String userInput = String.join("\n",
//                "9",
//                "",
//                this.cliExitOption
//        );
//
//        // Create CLI with fake input
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//
//        // Stub that library.getAllMembers() returns my list of members
//        List<Member> members = IntStream.range(0, size)
//                .mapToObj(i -> mock(Member.class))
//                .toList();
//        when(mockedLibrary.getAllMembers()).thenReturn(members);
//
//        // Act
//        cli.run();
//
//        // Assert that each member correctly called printMemberInfo
//        members.forEach(m -> verify(m).printMemberInfo());
//    }
//
//    @Test
//    void exitTestShouldSucceed() {
//        // Chooses Exit and program exits
//
//        // Arrange
//        // Fake user input: 10 (Exit), then anything to continue
//        String userInput = String.join("\n",
//                this.cliExitOption // Program should end after exit is requested
//        );
//
//        // Create CLI with fake input
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//
//        // Act
//        cli.run();
//
//        // Assert that Exit was called and set exit to true
//        assertEquals(true, cli.exit);
//    }
//
//    // Structural test
//
//    @Test
//    void addMemberTestEmptyInputShouldFail() {
//        // Chooses Add Member and submits empty input
//
//        // Arrange
//        // Fake user input: 7 (add member), then empty member fields, then anything to continue
//        String userInput = String.join("\n",
//                "7",              // Select option 7 (Add Member)
//                "",
//                "",
//                "",                     // (Empty input to get to menu)
//                this.cliExitOption                    // Exit (after adding book, immediately exit)
//        );
//
//        // Create CLI with fake input
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//
//        // Act
//        cli.run();
//
//        // Assert that addMember() was never called
//        verify(mockedLibrary, never()).addMember(anyString(), anyString());
//    }
//
//    @Test
//    void revokeMembershipTestShouldFail() {
//        // Chooses Revoke Membership and submits an empty member ID
//
//        // Arrange
//        // Fake user input: 8 (revoke membership), then memberID, then anything to continue
//        String userInput = String.join("\n",
//                "8",
//                "",
//                "",                     // (Empty input to get to menu)
//                this.cliExitOption                    // Exit (after adding book, immediately exit)
//        );
//
//        // Create CLI with fake input
//        cli = new CLI(new StringReader(userInput), mockedSystemOut, mockedLibrary, mockedAccounts, mockedLibrarians);
//
//        // Act
//        cli.run();
//
//        // Assert that revokeMembership was never called with empty input
//        verify(mockedLibrary, never()).revokeMembership(anyString());
//    }
//
//}