package CLI.Models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

import net.jqwik.api.*;
import net.jqwik.api.constraints.*;
import java.util.*;

import CLI.Models.*;

public class LibraryTest {
    @Test
    void testSetupWorks() {

        Book b = new Book("lala", "bg", 2003, "a", 1, "1");
        assertEquals(b.BookID, "1");
    }

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
        Member member = new Member("John Doe", "john@example.com", "M001");
        member.BorrowedBookList.add("B001");
        library.Members.add(member);
        library.LoanedBooksIDs.add("B001");

        assertEquals("M001", library.whoHasBook("B001").MemberID);
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

    // Member exists but does NOT have the book
    @Test
    void whoHasBook_loanedButNoMemberHasIt() {
        library.AllBooksInLibrary.add(new Book("Book_NotTrigger", "AuthorX", 2023, "Genre", 9999, "L123"));
        library.LoanedBooksIDs.add("L123");

        Member m = new Member("Email", "m5@example.com", "M5");
        library.Members.add(m);

        Member result = library.whoHasBook("L123");
        assertEquals(null, result);
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

        Member member = new Member("Jane Doe", "jane@example.com", "M1");
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

    //Test existing member but non-existing book
    @Test
    void checkoutBook_bookNotFound() {
        Member m = new Member("Name", "test@example.com", "M1");
        library.Members.add(m);

        library.checkoutBook("M1", "Fake Book");

        assertEquals(0, m.BorrowedBookList.size());
    }

    //Checkout for book that has already been borrowed
    @Test
    void checkoutBook_alreadyBorrowedByMember() {
        Member member = new Member("Already Borrowed", "borrow@example.com", "M2");
        member.BorrowedBookList.add("B001");
        library.Members.add(member);
        library.AvailableBookIDs.add("B001");
        library.AllBooksInLibrary.add(new Book("Name_1", "Author", 2000, "Genre", 111, "B001"));
        library.checkoutBook("M2", "Name_1");
        assertEquals(1, member.BorrowedBookList.size());
    }

    //Test to check out non-available book
    @Test
    void checkoutBook_bookNotAvailable() {
        Member member = new Member("Not Available", "na@example.com", "M3");
        library.Members.add(member);
        library.AllBooksInLibrary.add(new Book("Name_4", "Author", 2000, "Genre", 111, "B010"));
        library.LoanedBooksIDs.add("B010");
        library.checkoutBook("M3", "Name_4");
        assertEquals(false, member.BorrowedBookList.contains("B010"));
    }

    //Test for returning a book that was loaned out
    @Test
    void returnBook_successfullyReturnsBook() {
        Member member = new Member("Return Tester", "return@example.com", "M10");
        library.Members.add(member);

        Book book = new Book("ReturnMe", "Author", 2020, "Mystery", 54321, "B100");
        library.AllBooksInLibrary.add(book);
        library.LoanedBooksIDs.add("B100");
        member.BorrowedBookList.add("B100");

        library.returnBook(member.MemberID, "B100");

        assertEquals(true, library.AvailableBookIDs.contains("B100"));
        assertEquals(false, library.LoanedBooksIDs.contains("B100"));
        assertEquals(false, member.BorrowedBookList.contains("B100"));
        assertEquals(true, book.IsAvailable);
    }

    //Test for returning a book when member doesn't exist
    @Test
    void returnBook_memberNotFound() {
        Book book = new Book("NoMember", "Author", 2019, "Adventure", 11111, "B101");
        library.AllBooksInLibrary.add(book);
        library.LoanedBooksIDs.add("B101");

        library.returnBook("NOT_REAL", "B101");

        assertEquals(true, library.LoanedBooksIDs.contains("B101"));
        assertEquals(false, library.AvailableBookIDs.contains("B101"));
    }

    //Test for returning a book that member hasn't borrowed
    @Test
    void returnBook_bookNotBorrowedByMember() {
        Member member = new Member("Wrong Member", "wrong@example.com", "M11");
        library.Members.add(member);

        Book book = new Book("NotYours", "Author", 2018, "Comedy", 22222, "B102");
        library.AllBooksInLibrary.add(book);
        library.LoanedBooksIDs.add("B102");

        library.returnBook("M11", "B102");

        assertEquals(true, library.LoanedBooksIDs.contains("B102"));
        assertEquals(false, library.AvailableBookIDs.contains("B102"));
    }

    //Test for returning a book that doesn't exist
    @Test
    void returnBook_bookNotFound() {
        Member member = new Member("No Book", "nobook@example.com", "M12");
        member.BorrowedBookList.add("B999");
        library.Members.add(member);

        library.returnBook("M12", "B999");

        assertEquals(true, member.BorrowedBookList.contains("B999"));
    }

    //Test for getting all members
    @Test
    void getAllMembers_returnsCorrectList() {
        Member member1 = new Member("Name1", "member1@example.com", "M20");
        Member member2 = new Member("Name2", "member2@example.com", "M21");
        library.Members.add(member1);
        library.Members.add(member2);

        List<Member> result = library.getAllMembers();

        assertEquals(2, result.size());
        assertEquals(true, result.contains(member1));
        assertEquals(true, result.contains(member2));
    }

    //Test for adding a new member successfully
    @Test
    void addMember_successfullyAddsMember() {
        Member member = library.addMember("New Member", "new@example.com");

        assertEquals(false, member == null);
        assertEquals("New Member", member.Name);
        assertEquals("new@example.com", member.Email);
        assertEquals(true, library.Members.contains(member));
    }

    //Test for adding a member with next available ID
    @Test
    void addMember_assignsCorrectID() {
        library.Members.add(new Member("Existing", "existing@example.com", "1"));

        Member newMember = library.addMember("Second Member", "second@example.com");

        assertEquals(false, newMember == null);
        assertEquals("2", newMember.MemberID);
    }

    //Test for adding member after 1000 IDs are taken
    @Test
    void addMember_failsAfter1000Tries() {
        for (int i = 1; i <= 1000; i++) {
            library.Members.add(new Member("Block_" + i, "block" + i + "@example.com", String.valueOf(i)));
        }

        Member result = library.addMember("TooMany", "toomany@example.com");

        assertEquals(null, result);
    }

    //Test for revoking membership successfully
    @Test
    void revokeMembership_successfullyRevokesMembership() {
        Member member = new Member("Revoke Me", "revoke@example.com", "M30");
        library.Members.add(member);

        boolean result = library.revokeMembership("M30");

        assertEquals(true, result);
        assertEquals(false, library.Members.contains(member));
    }

    //Test for revoking membership when member doesn't exist
    @Test
    void revokeMembership_memberNotFound() {
        boolean result = library.revokeMembership("NOT_REAL");

        assertEquals(false, result);
    }

    //Test for revoking membership and returning borrowed books (multiple)
    @Test
    void revokeMembership_returnsAllBorrowedBooks() {
        Member member = new Member("Book Holder", "holder@example.com", "M31");
        member.BorrowedBookList.add("B201");
        member.BorrowedBookList.add("B202");
        library.Members.add(member);

        Book book1 = new Book("Book1", "Author1", 2010, "Adventure", 33333, "B201");
        Book book2 = new Book("Book2", "Author2", 2011, "Comedy", 44444, "B202");
        book1.IsAvailable = false;
        book2.IsAvailable = false;

        library.AllBooksInLibrary.add(book1);
        library.AllBooksInLibrary.add(book2);
        library.LoanedBooksIDs.add("B201");
        library.LoanedBooksIDs.add("B202");

        library.revokeMembership("M31");

        assertEquals(true, book1.IsAvailable);
        assertEquals(true, book2.IsAvailable);
        assertEquals(true, library.AvailableBookIDs.contains("B201"));
        assertEquals(true, library.AvailableBookIDs.contains("B202"));
        assertEquals(false, library.LoanedBooksIDs.contains("B201"));
        assertEquals(false, library.LoanedBooksIDs.contains("B202"));
    }

    //Test successful checkout workflow
    @Test
    void checkoutBook_successfulCheckout() {
        Member member = new Member("Checkout Tester", "checkout@example.com", "M40");
        library.Members.add(member);

        Book book = new Book("CheckMeOut", "Great Author", 2022, "Fiction", 55555, "B301");
        book.IsAvailable = true;
        library.AllBooksInLibrary.add(book);
        library.AvailableBookIDs.add("B301");

        library.checkoutBook("M40", "CheckMeOut");

        assertEquals(true, member.BorrowedBookList.contains("B301"));
        assertEquals(false, book.IsAvailable);
        assertEquals(true, library.LoanedBooksIDs.contains("B301"));
        assertEquals(false, library.AvailableBookIDs.contains("B301"));
    }

    //Test checkout with book that exists in AllBooksInLibrary but not in AvailableBookIDs
    @Test
    void checkoutBook_bookExistsButNotAvailable() {
        Member member = new Member("Unavailable Tester", "unavail@example.com", "M41");
        library.Members.add(member);

        Book book = new Book("Unavailable", "Some Author", 2021, "Mystery", 66666, "B401");
        book.IsAvailable = false;
        library.AllBooksInLibrary.add(book);

        library.checkoutBook("M41", "Unavailable");

        assertEquals(false, member.BorrowedBookList.contains("B401"));
        assertEquals(false, library.AvailableBookIDs.contains("B401"));
    }

    //Test checkout when book doesn't exist but ID is in AvailableBookIDs
    @Test
    void checkoutBook_idAvailableButNoBook() {
        Member member = new Member("Phantom Book", "phantom@example.com", "M42");
        library.Members.add(member);

        library.AvailableBookIDs.add("B501");
        String bookID = library.findBookIdByName("Phantom");

        assertEquals(null, bookID);

        library.checkoutBook("M42", "Phantom");

        assertEquals(0, member.BorrowedBookList.size());
    }
}
