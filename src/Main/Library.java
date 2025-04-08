package Main;

import java.util.ArrayList;
import java.util.List;

public class Library {

    public List<String> AvailableBookIds;
    public List<Book> AllBooksInLibrary;
    public List<Book> LoanedBooks;
    public List<String> MemberIDs;

//

    public Library() {
        AvailableBookIds = new ArrayList<>();
        AllBooksInLibrary = new ArrayList<>();
        LoanedBooks = new ArrayList<>();
        MemberIDs = new ArrayList<>();
    }

    public Library(List<String> AvailableBooksIds, List<Book> AllBooksInLibrary, List<Book> LoanedBooks, List<String> MemberIDs) {
        this.AvailableBookIds = AvailableBooksIds;
        this.AllBooksInLibrary = AllBooksInLibrary;
        this.LoanedBooks = LoanedBooks;
        this.MemberIDs = MemberIDs;
    }

//

    public void addBook(Book book) {
        if (AllBooksInLibrary.contains(book)) {
            System.out.println("Book already exists! Choose a new BookID.");
            return;
        }
        AvailableBookIds.add(book.BookID);
        AllBooksInLibrary.add(book);
    }

    public void removeBook(Book book) {
        if (AllBooksInLibrary.contains(book)) {
            AllBooksInLibrary.remove(book);
            AvailableBookIds.remove(book.BookID);
            LoanedBooks.remove(book);
            System.out.println("Book has been successfully removed!");
            return;
        }
        System.out.println("Book could not be removed!");
    }

    public void checkoutBook(Book book) {
        if (AvailableBookIds.contains(book.BookID)) {
            AvailableBookIds.remove(book.BookID);
            LoanedBooks.add(book);
            System.out.println("Book has been successfully checked out!");
            return;
        }
        System.out.println("Book could not be checked out!");
    }


    // Add a new member to the library
    public void addMember(String memberId) {
        if (!MemberIDs.contains(memberId)) {
            MemberIDs.add(memberId);
            System.out.println("Member added successfully.");
        } else {
            System.out.println("Member already exists.");
        }
    }

    // Revoke membership of a member
    public void revokeMembership(String memberId) {
        if (MemberIDs.contains(memberId)) {
            MemberIDs.remove(memberId);
            System.out.println("Membership revoked.");
        } else {
            System.out.println("Member not found.");
        }
    }

    // Check if a book is available
    public Boolean bookAvailability(String bookID) {
        return AvailableBookIds.contains(bookID);
    }

    // Find which member has a particular book
    public Member whoHasBook(String bookID) {
        for (Book book : LoanedBooks) {
            if (book.BookID.equals(bookID)) {
                return new Member("FIXME Member", "member@gmail.com", "lalala");
            }
        }
        return null; // Book not found
    }

    // Get all library members
    public List<String> getAllMembers() {
        return MemberIDs;
    }

    // Find a book by its name
    public Book findBookIdByName(String bookName) {
        for (Book book : AllBooksInLibrary) {
            if (book.Name.equals(bookName)) {
                return book;
            }
        }
        return null; // Book not found
    }

    // Return a book to the library
    public void returnBook(Book book) {
        if (LoanedBooks.contains(book)) {
            LoanedBooks.remove(book);
            AvailableBookIds.add(book.BookID);
            System.out.println("Book returned successfully.");
        } else {
            System.out.println("This book was not checked out.");
        }
    }

}

