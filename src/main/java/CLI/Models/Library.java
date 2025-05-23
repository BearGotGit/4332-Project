package CLI.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Library {

    public List<Book> AllBooksInLibrary;
    public List<String> AvailableBookIDs;
    public List<String> LoanedBooksIDs;
    public List<Member> Members;

    // Default constructor
    public Library() {
        AllBooksInLibrary = new ArrayList<Book>();
        AvailableBookIDs = new ArrayList<String>();
        LoanedBooksIDs = new ArrayList<String>();
        Members = new ArrayList<Member>();
    }

    // Get a book's ID from its name
    public String findBookIdByName(String bookName) {
        for (Book book : AllBooksInLibrary) {
            if (book.Name.equals(bookName)) {
                return book.BookID;
            }
        }
        return null; // Book not found
    }

    // Check if a book is available
    public Boolean bookAvailability(String bookID) {
        return AvailableBookIDs.contains(bookID);
    }

    // Find which member has a particular book
    public Member whoHasBook(String bookID) {
        if (!AllBooksInLibrary.stream().anyMatch(b -> b.BookID.equals(bookID))) {
            System.out.println("Book with ID: " + bookID + " does not exist in the library.");
            return null;
        }
        else if (LoanedBooksIDs.contains(bookID)) {
            for (Member member : Members) {
                if (member.BorrowedBookList.contains(bookID)) {
                    System.out.println("Member with ID: " + member.MemberID + " has the book with ID: " + bookID);
                    return member;
                }
            }
        }
        System.out.println("Book with ID: " + bookID + " is not checked out by anyone");
        return null;
    }

    // Create and add a new book
    public Book addBook(String name, String author, int year, String genre, int isbn) {
        if (AllBooksInLibrary.stream().anyMatch(b -> b.Name.equals(name))) {
            System.out.println("Book " + name + " already exists.");
            return null;
        }

        for (int newBookID = 1; newBookID <= 1000; newBookID++) {
            String idString = String.valueOf(newBookID);
            if (!AllBooksInLibrary.stream().anyMatch(b -> b.BookID.equals(idString))) {
                Book newBook = new Book(name, author, year, genre, isbn, idString);
                AllBooksInLibrary.add(newBook);
                AvailableBookIDs.add(newBook.BookID);
                System.out.println("Book " + newBook.BookID + " added successfully.");
                return newBook;
            }
        }

        System.out.println("Book could not be created.");
        return null;
    }

    // Remove a book by ID
    public void removeBook(String bookID) {
        Book bookToRemove = null;
        for (Book book : AllBooksInLibrary) {
            if (book.BookID.equals(bookID)) {
                bookToRemove = book;
            }
        }
        if (bookToRemove == null) {
            System.out.println("Book with ID: " + bookID + "could not be found.");
            return;
        }
        AllBooksInLibrary.remove(bookToRemove); // these are same in java
        AvailableBookIDs.remove(bookToRemove.BookID);
        LoanedBooksIDs.remove(bookToRemove.BookID);

        // Remove from many member
        Member member = Members.stream().filter(m -> m.BorrowedBookList.contains(bookID)).findFirst().orElse(null);
        if (member != null) {
            member.BorrowedBookList.remove(bookID);
        }
        System.out.println("Book has been successfully removed!");
    }

    // Checkout a book to a member
    public void checkoutBook(String memberID, String bookName) {
        Member member = Members.stream()
                                .filter(m -> m.MemberID.equals(memberID))
                                .findFirst()
                                .orElse(null);
        if (member == null) {
            System.out.println("No member with ID: " + memberID + " exists.");
            return;
        }
        String bookID = findBookIdByName(bookName);
        if (bookID == null) { 
            System.out.println("Couldn't find book " + bookName + ".");
            return;
        }
        else if (member.BorrowedBookList.contains(bookID)) {
            System.out.println("Member with ID: " + member.MemberID + " has already checked out the book with ID: " + bookID);
            return;
        }
        else if (!AvailableBookIDs.contains(bookID)) {
            System.out.println("Book with ID: " + bookID + " is already checked out!");
            return;
        }

        // Now checkout the book
        Book book = AllBooksInLibrary.stream().filter(b -> b.BookID.equals(bookID)).findFirst().orElse(null);
        AvailableBookIDs.remove(bookID); // remove because if the book doesn't exist, it shouldn't be in the list anyway
        if (book == null) { 
            System.out.println("Could not find book " + bookName + ".");
            return;
        }
        // Update Library
        book.IsAvailable = false;
        LoanedBooksIDs.add(bookID);

        // Update member
        member.BorrowedBookList.add(bookID);

        System.out.println("Book has been successfully checked out!");
        return;
    }

    // Return a book to the library
    public void returnBook(String memberID, String bookID) {
        Member member = Members.stream()
                                .filter(m -> m.MemberID.equals(memberID) && m.BorrowedBookList.contains(bookID))
                                .findFirst()
                                .orElse(null);
        if (member == null) {
            System.out.println("Member with ID: " + memberID + " has either not checked out the book with ID: " + bookID + " or they are not a member.");
            return;
        }
        Book book = AllBooksInLibrary.stream().filter(b -> b.BookID.equals(bookID)).findFirst().orElse(null);
        if (book == null) {
            System.out.println("Book with ID: " + bookID + " does not exist in the library.");
            return;
        }
        book.IsAvailable = true;
        LoanedBooksIDs.remove(book.BookID); // no check needed, this is safe
        AvailableBookIDs.add(bookID);
        member.BorrowedBookList.remove(bookID); // no check needed, this is safe
        System.out.println("Book " + book.Name + " returned successfully.");
    }

    // Library Functions

    // Get all library members
    public List<Member> getAllMembers() {
        return Members;
    }

    // Add a new member to the library
    public Member addMember(String name, String email) {
        for (int newMemberID = 1; newMemberID <= 1000; newMemberID++) {
            String idString = String.valueOf(newMemberID);
            if (!Members.stream().anyMatch(m -> m.MemberID.equals(idString))) {
                Member newMember = new Member(name, email, String.valueOf(newMemberID));
                Members.add(newMember);
                System.out.println("Member " + newMember.MemberID + " added successfully.");
                return newMember;
            }
        }
        System.out.println("Member could not be created.");
        return null;
    }

    // Revoke membership of a member
    public boolean revokeMembership(String memberId) {
        Member member = Members.stream().filter(m -> m.MemberID.equals(memberId)).findFirst().orElse(null);
        if (member != null) {
            Members.remove(member);

            List<Book> books = AllBooksInLibrary.stream().filter(b -> member.BorrowedBookList.contains(b.BookID)).collect(Collectors.toList());
            for (Book book : books) {
                AvailableBookIDs.add(book.BookID);
                LoanedBooksIDs.remove(book.BookID);
                book.IsAvailable = true;
            }

            System.out.println("Membership revoked.");
            return true;
        }
        else {
            System.out.println("Member not found.");
            return false;
        }
    }

}

