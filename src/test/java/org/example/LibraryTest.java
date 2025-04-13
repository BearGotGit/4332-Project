package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LibraryTest {

    private Library library;

    @BeforeEach
    void setUp() {
        library = new Library();

        library.AllBooksInLibrary.add(new Book("Name_1", "Author_1", 2000, "Action", 111111, "B001"));
        library.AllBooksInLibrary.add(new Book("Name_2", "Author_2", 2001, "Comedy", 222222, "B002"));
        library.AllBooksInLibrary.add(new Book("Name_3", "Author_3", 2002, "Sci_Fi", 333333, "B003"));
    }

    //Find Books id By using name
    @Test
    void findBookIdByName() {
        String id = library.findBookIdByName("Name_2");
        assertEquals("B002", id);
    }

    //Attempt to find book id, but fail
    @Test
    void failToFindBookIdByName_notFound() {
        assertEquals(null, library.findBookIdByName("NotThere"));
    }

    //Attempt to find book ID by using null attempt
    @Test
    void failToFindBookIdByName_nullInput() {
        assertEquals(null, library.findBookIdByName(null));
    }

    //Uses id to see if book is available
    @Test
    void bookAvailability_found() {
        library.AvailableBookIDs.add("B001");
        assertEquals(true, library.bookAvailability("B001"));
    }

    //Uses non-existent id to attempt to find that a book is not available
    @Test
    void bookAvailability_notFound() {
        assertEquals(false, library.bookAvailability("B999"));
    }

    //Attempts to find book by null input
    @Test
    void bookAvailability_nullInput_notFound() {
        assertEquals(false, library.bookAvailability(null));
    }

    //Creates a member and has him loan out a book, then tests for getting the member's id
    @Test
    void whoHasBook_bookLoaned_memberFound() {
        Member member = new Member("M001", "John Doe", "john@example.com");
        member.BorrowedBookList.add("B001");
        library.Members.add(member);
        library.LoanedBooksIDs.add("B001");

        assertEquals("john@example.com", library.whoHasBook("B001").MemberID);
    }

    //Tries to find member but none is there so returns null
    @Test
    void whoHasBook_bookLoaned_butNoMatchingMember() {
        library.LoanedBooksIDs.add("B002");
        assertEquals(null, library.whoHasBook("B002"));
    }

    //Test for finding an unloaned book
    @Test
    void whoHasBook_bookExists_notLoaned() {
        assertEquals(null, library.whoHasBook("B003"));
    }

    //Test for trying to find nonexistent book
    @Test
    void whoHasBook_bookDoesNotExist() {
        assertEquals(null, library.whoHasBook("B999"));
    }


    //Tests to add book and check to ensure proper storage
    @Test
    void addBook_successfullyAddsNewBook() {
        Book book = library.addBook("NewBook", "AuthorX", 2023, "Mystery", 999999);
        assertEquals(false, book == null);
        assertEquals("NewBook", book.Name);
        assertEquals(true, library.AllBooksInLibrary.contains(book));
        assertEquals(true, library.AvailableBookIDs.contains(book.BookID));
    }

    //Attempts to add already existing book
    @Test
    void addBook_failsIfNameAlreadyExists() {
        assertEquals(null, library.addBook("Name_1", "AnyAuthor", 2025, "Drama", 888888));
    }

    //Test for getting the book ID, id should be in order of added books
    @Test
    void addBook_assignsCorrectBookID() {
        Book book = library.addBook("AnotherBook", "AuthorY", 2024, "Adventure", 777777);
        assertEquals(false, book == null);
        assertEquals("1", book.BookID);
    }

    //Makes sure that if id is already assigned, then the new id is given next available slot
    @Test
    void addBook_skipsUsedIdAndIncrementsCount() {
        library.AllBooksInLibrary.add(new Book("BlockedName", "Blocker", 2020, "Drama", 555555, "1"));
        Book newBook = library.addBook("FreeName", "AuthorZ", 2025, "Action", 666666);
        assertEquals(false, newBook == null);
        assertEquals("2", newBook.BookID);
    }

    //There is a limit to ids, so add book should return null after 1000
    @Test
    void addBook_failsAfter1000Tries() {
        for (int i = 1; i <= 1000; i++) {
            library.AllBooksInLibrary.add(new Book("Block_" + i, "Author", 2000 + i, "Genre", 123000 + i, String.valueOf(i)));
        }
        Book result = library.addBook("BlockedBook", "Exhausted Author", 2099, "LockedOut", 999999);
        assertEquals(null, result);
    }

    //Test to make sure an added book, is removed from all locations
    @Test
    void removeBook_existingAvailableBook_removedEverywhere() {
        Book book = new Book("TempBook", "Author", 2024, "Genre", 123456, "B010");
        library.AllBooksInLibrary.add(book);
        library.AvailableBookIDs.add("B010");

        library.removeBook("B010");

        assertEquals(false, library.AllBooksInLibrary.contains(book));
        assertEquals(false, library.AvailableBookIDs.contains("B010"));
        assertEquals(false, library.LoanedBooksIDs.contains("B010"));
    }

    //Remove a book that has been loaned out
    @Test
    void removeBook_existingLoanedBook_removedFromMember() {
        Book book = new Book("LoanedBook", "Author", 2024, "Genre", 123456, "B020");
        library.AllBooksInLibrary.add(book);
        library.LoanedBooksIDs.add("B020");

        Member member = new Member("M1", "Jane Doe", "jane@example.com");
        member.BorrowedBookList.add("B020");
        library.Members.add(member);

        library.removeBook("B020");

        assertEquals(false, library.AllBooksInLibrary.contains(book));
        assertEquals(false, library.LoanedBooksIDs.contains("B020"));
        assertEquals(false, member.BorrowedBookList.contains("B020"));
    }

    //Test for removing non-existent book, nothing should happen
    @Test
    void removeBook_notFound_nothingHappens() {
        int beforeSize = library.AllBooksInLibrary.size();
        library.removeBook("B999");
        int afterSize = library.AllBooksInLibrary.size();
        assertEquals(beforeSize, afterSize);
    }

    //Test for non-existent member
    @Test
    void checkoutBook_memberNotFound() {

        int loanedBefore = library.LoanedBooksIDs.size();

        library.checkoutBook("NOT_REAL", "Name_1");

        int loanedAfter = library.LoanedBooksIDs.size();
        assertEquals(loanedBefore, loanedAfter);
    }

    //Test existing member but non existing book
    @Test
    void checkoutBook_bookNotFound() {
        Member m = new Member("M1", "Test", "test@example.com");
        library.Members.add(m);

        library.checkoutBook("M1", "Fake Book");

        assertEquals(0, m.BorrowedBookList.size());
    }

    //Checkout for book that has already been borrowed
    @Test
    void checkoutBook_alreadyBorrowedByMember() {
        Member member = new Member("M2", "Already Borrowed", "borrow@example.com");
        member.BorrowedBookList.add("B001");
        library.Members.add(member);
        library.AvailableBookIDs.add("B001");
        library.AllBooksInLibrary.add(new Book("Name_1", "Author", 2000, "Genre", 111, "B001"));
        library.checkoutBook("M2", "Name_1");
        assertEquals(1, member.BorrowedBookList.size());
    }

    //Test to checkout nonavailable book
    @Test
    void checkoutBook_bookNotAvailable() {
        Member member = new Member("M3", "Not Available", "na@example.com");
        library.Members.add(member);
        library.AllBooksInLibrary.add(new Book("Name_4", "Author", 2000, "Genre", 111, "B010"));
        library.LoanedBooksIDs.add("B010");
        library.checkoutBook("M3", "Name_4");
        assertEquals(false, member.BorrowedBookList.contains("B010"));
    }
}
