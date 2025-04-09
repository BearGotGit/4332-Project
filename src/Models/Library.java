package Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Library {

    public List<String> AvailableBookIds;
    public List<Book> AllBooksInLibrary;
    public List<String> LoanedBooks;
    // public List<String> MemberIDs;
    public List<Member> Members;

    public Library() {
        AvailableBookIds = new ArrayList<String>();
        AllBooksInLibrary = new ArrayList<Book>();
        LoanedBooks = new ArrayList<String>();
        // MemberIDs = new ArrayList<>();
        Members = new ArrayList<Member>();
    }

    // public Library(List<String> AvailableBooksIds, List<Book> AllBooksInLibrary, List<String> LoanedBooks, List<Member> Members) {
    //     this.AvailableBookIds = AvailableBooksIds;
    //     this.AllBooksInLibrary = AllBooksInLibrary;
    //     this.LoanedBooks = LoanedBooks;
    //     // this.MemberIDs = MemberIDs;
    //     this.Members = Members;
    // }

    public void addBook(Book book) {
        if (AllBooksInLibrary.contains(book)) {
            System.out.println("Book already exists! Choose a new BookID.");
            return;
        }
        AvailableBookIds.add(book.BookID);
        AllBooksInLibrary.add(book);
    }
    public Book addBook(String name, String author, int year, String genre, int isbn) {
        int count = 0;
        int newBookID = 1;

        while (true) {
            String idString = String.valueOf(newBookID);
            if (!AllBooksInLibrary.stream().anyMatch(b -> b.BookID.equals(idString))) { 
                // Found uniqueID
                break; 
            }
            if (count >= 1000) {
                System.out.println("Book could not be created.");
                return null;
            }
            newBookID++;
            count++;
        }
        Book newBook = new Book(name, author, year, genre, isbn, String.valueOf(newBookID));
        AllBooksInLibrary.add(newBook);
        AvailableBookIds.add(newBook.BookID);
        System.out.println("Book " + newBook.BookID + " added successfully.");
        return newBook;
    }

    public void removeBook(Book book) {
        if (AllBooksInLibrary.contains(book)) {
            AllBooksInLibrary.remove(book);
            AvailableBookIds.remove(book.BookID);
            LoanedBooks.remove(book.BookID);
            System.out.println("Book has been successfully removed!");
            return;
        }
        System.out.println("Book could not be removed!");
    }
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
        else { removeBook(bookToRemove);}
    }


    // Add a new member to the library
    public Member addMember(String name, String email) {
        int newMemberID = 1;
        int count = 0;
        while (true) {
            String idString = String.valueOf(newMemberID);
            if (!Members.stream().anyMatch(m -> m.MemberID.equals(idString))) {
                // Found uniqueID
                break;
            }
            if (count >= 1000) {
                System.out.println("Member could not be created.");
                return null;
            }
            newMemberID++;
            count++;
        }
        Member newMember = new Member(name, email, String.valueOf(newMemberID));
        // MemberIDs.add(newMember.MemberID);
        Members.add(newMember);
        System.out.println("Member " + newMember.MemberID + " added successfully.");
        return newMember;
    }

    // Revoke membership of a member
    public boolean revokeMembership(String memberId) {
        // if (MemberIDs.contains(memberId)) {
        //     MemberIDs.remove(memberId);
        //     System.out.println("Membership revoked.");
        //     return true;
        // }
        Member member = Members.stream().filter(m -> m.MemberID.equals(memberId)).findFirst().orElse(null);
        if (member != null) {
            Members.remove(member);
            System.out.println("Membership revoked.");
            return true;
        }
        else {
            System.out.println("Member not found.");
            return false;
        }
    }

    // Check if a book is available
    public Boolean bookAvailability(String bookID) {
        return AvailableBookIds.contains(bookID);
    }

    // Find which member has a particular book
    public Member whoHasBook(String bookID) {
        if (!AllBooksInLibrary.stream().anyMatch(b -> b.BookID.equals(bookID))) {
            System.out.println("Book with ID: " + bookID + " does not exist in the library.");
            return null;
        }
        else if (LoanedBooks.contains(bookID)) {
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

    // Get all library members
    public List<Member> getAllMembers() {
        return Members;
    }

    // Find a book by its name
    public String findBookIdByName(String bookName) {
        for (Book book : AllBooksInLibrary) {
            if (book.Name.equals(bookName)) {
                return book.BookID;
            }
        }
        return null; // Book not found
    }

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
        if (AllBooksInLibrary.stream().anyMatch(b -> b.BookID.equals(bookID))) {
            if (member.BorrowedBookList.contains(bookID)) {
                System.out.println("Member with ID: " + member.MemberID + " has already checkout out book with ID: " + bookID);
                return;
            }
            AvailableBookIds.remove(bookID);
            LoanedBooks.add(bookID);
            member.BorrowedBookList.add(bookID);
            System.out.println("Book has been successfully checked out!");
            return;
        }
        System.out.println("Book with ID: " + bookID + " is already checkout out!");
    }

    // Return a book to the library
    public void returnBook(String memberID, String bookID) {
        Member member = Members.stream()
                                .filter(m -> m.MemberID.equals(memberID))
                                .findFirst()
                                .orElse(null);
        if (member == null) {
            System.out.println("No member with ID: " + memberID + " exists.");
            return;
        }
        if (LoanedBooks.contains(bookID) && member.BorrowedBookList.contains(bookID)) {
            LoanedBooks.remove(bookID);
            AvailableBookIds.add(bookID);
            System.out.println("Book with ID: " + bookID + " returned successfully.");
        } else {
            System.out.println("The book with ID: " + bookID + " was not checked out by member with ID: " + memberID + ".");
        }
    }

}

