package CLI.Main;

import CLI.Models.*;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class CLI {
    Scanner scanner;
    Library library;
    LibraryAccounts libraryAccounts;
    Librarians librarians;

    Boolean exit = false;
    Boolean skip = false;

    public CLI(Readable input, Library library, LibraryAccounts libraryAccounts, Librarians librarians) {
        this.scanner = new Scanner(input);
        this.library = library;
        this.libraryAccounts = libraryAccounts;
        this.librarians = librarians;
    }

    public void run() {
        String stars = "****************************************************";

        String input = "";

        while (input != null) {

            System.out.print("""
                    
                    """ + stars + """
                    
                    
                    You have these options:
                    
                    1. Order Book
                    2. Remove Book
                    3. Check Book Availability
                    4. Checkout Book
                    5. Return Book
                    6. View All Books
                    7. Add Member
                    8. Revoke Membership
                    9. View All Members
                    10. Hire Part-Time Librarian
                    11. Withdraw Salary
                    12. Donate to Library
                    13. EXIT
                    
                    Enter the number of the option you want to select:""" + " ");
            String option = scanner.nextLine();

            System.out.println();

            switch (option) {
                // 1. Order book
                case "1": {
                    orderBook();
                    break;
                }

                // 2. Remove book
                case "2": {
                    System.out.println("You chose to remove a book");
                    if (authenticate().authType != Librarians.AuthType.NOT_AUTHORIZED) {
                        // Only librarians can remove books!
                        System.out.println("\nSuccessfully authorized!");
                    } else {
                        System.out.println("\nFailed to authorize.");
                        break;
                    }

                    System.out.print("Enter book name to remove: ");
                    String book = scanner.nextLine();
                    if (book.isEmpty()) {
                        System.out.println("\nNot a valid book name");
                        break;
                    }
                    String bookID = library.findBookIdByName(book);
                    if (bookID == null) {
                        System.out.println("Couldn't find book " + book + ".");
                        break;
                    }
                    System.out.println();

                    library.removeBook(bookID);
                    break;
                }
                // 3. Check Book Availability
                case "3": {
                    System.out.println("You chose to check a book's availability");
                    System.out.print("Enter book name: ");
                    String book = scanner.nextLine();
                    if (book.isEmpty()) {
                        System.out.println("\nNot a valid genre");
                        break;
                    }

                    String bookID = library.findBookIdByName(book);
                    if (bookID == null) {
                        System.out.println("\nCouldn't find book " + book + ".");
                        break;
                    }

                    boolean available = library.bookAvailability(bookID);
                    if (available) {
                        System.out.println("\nBook " + book + " is available to checkout!");
                        break;
                    } else {
                        System.out.println("\nBook " + book + " is not available to checkout.");
                    }
                    break;
                }

                // 4. Checkout Book
                case "4": {
                    System.out.println("You chose to checkout a new book");
                    System.out.print("Enter your member ID: ");
                    String memberID = scanner.nextLine();
                    if (memberID == null || memberID.isEmpty()) {
                        System.out.println("\nNot a valid ID");
                        break;
                    }
                    if (library.getAllMembers().stream().noneMatch(m -> m.MemberID.equals(memberID))) {
                        System.out.println("\nMember with ID " + memberID + " does not exist.");
                        break;
                    }
                    System.out.print("Enter book name: ");
                    String bookName = scanner.nextLine();
                    if (bookName == null || bookName.isEmpty()) {
                        System.out.println("\nNot a valid book name");
                        break;
                    }

                    String bookID = library.findBookIdByName(bookName);
                    if (bookID == null) {
                        System.out.println("\nCouldn't find book " + bookName + ".");
                        System.out.println("\nHowever, if you enter full-time librarian credentials, you can order the book.");

                        System.out.print("""
                                
                                You have these options:
                                
                                1. Order Book
                                2. Cancel
                                
                                Enter the number of the option you want:""" + " ");
                        String checkoutOption = scanner.nextLine();

                        System.out.println();
                        if (checkoutOption.equals("1")) {
                            AuthResult auth = authenticate();
                            if (auth.authType == Librarians.AuthType.FULL_TIME) {
                                System.out.println("\nYou are authorized as a full-time librarian!");
                                Book book = orderBook(auth.username, auth.authCode, bookName);
                                if (book != null) {
                                    System.out.println();
                                    library.checkoutBook(memberID, book.Name);
                                }
                            }
                            else {
                                System.out.println("\nYou failed to authorize as a full-time librarian.");
                            }
                        }
                        break; // exit switch no matter what
                    }

                    System.out.println();
                    library.checkoutBook(memberID, bookName);
                    break;
                }

                // 5. Return Book
                case "5": {
                    System.out.println("You chose to return a book");
                    System.out.print("Enter your member ID: ");
                    String memberID = scanner.nextLine();
                    if (memberID == null || memberID.isEmpty()) {
                        System.out.println("\nNot a valid ID");
                        break;
                    }
                    System.out.print("Enter book name: ");
                    String book = scanner.nextLine();
                    if (book == null || book.isEmpty()) {
                        System.out.println("\nNot a valid book name");
                        break;
                    }
                    String bookID = library.findBookIdByName(book);
                    if (bookID == null) {
                        System.out.println("Couldn't find book " + book + ".");
                        break;
                    }
                    System.out.println();
                    library.returnBook(memberID, bookID);
                    break;
                }

                // 6. View All Books
                case "6": {
                    if (library.AllBooksInLibrary == null || library.AllBooksInLibrary.size() <= 0) {
                        System.out.println("There are no books in the library");
                        break;
                    }
                    for (Book book : library.AllBooksInLibrary) {
                        System.out.println(book.getBookInfo());
                    }
                    break;
                }

                // 7. Add Member
                case "7": {
                    System.out.println("You chose to add a new member");
                    if (authenticate().authType == Librarians.AuthType.FULL_TIME) {
                        System.out.println("\nSuccessfully authorized!");
                    } else {
                        System.out.println("\nFailed to authorize.");
                        break;
                    }
                    
                    System.out.print("Enter member name: ");
                    String name = scanner.nextLine();
                    if (name == null || name.isEmpty()) {
                        System.out.println("\nNot a valid name");
                        break;
                    }
                    System.out.print("Enter member email: ");
                    String email = scanner.nextLine();
                    if (email == null || email.isEmpty()) {
                        System.out.println("\nNot a valid email");
                        break;
                    }

                    System.out.println();
                    Member newMember = library.addMember(name, email);
                    if (newMember == null) {
                        System.out.println("New member couldn't be created");
                        break;
                    }
                    System.out.println("Successfully added a new member with ID: " + newMember.MemberID);
                    newMember.printMemberInfo();
                    break;
                }
                // 8. Revoke Membership
                case "8": {
                    System.out.println("You chose to revoke a membership");
                    if (authenticate().authType == Librarians.AuthType.FULL_TIME) {
                        System.out.println("\nSuccessfully authorized!");
                    } else {
                        System.out.println("\nFailed to authorize.");
                        break;
                    }

                    System.out.print("Enter member ID to revoke: ");
                    String memberID = scanner.nextLine();
                    if (memberID == null || memberID.isEmpty()) {
                        System.out.println("\nNot a valid ID");
                        break;
                    }

                    var success = library.revokeMembership(memberID);
                    if (success) {
                        System.out.println("\nSuccessfully revoked membership of member with ID: " + memberID);
                        break;
                    }
                    System.out.println("\nFailed to revoke membership of member with ID: " + memberID);
                    break;
                }
                // 9. View All Members
                case "9": {
                    List<Member> allMembers = library.getAllMembers();
                    if (allMembers == null || allMembers.size() <= 0) {
                        System.out.println("There are no members of this library");
                        break;
                    }
                    for (Member member : allMembers) {
                        member.printMemberInfo();
                    }
                    break;
                }
                // 10. Hire Part-Time Librarian
                case "10": {
                    System.out.println("You chose to hire a part-time librarian");
                    System.out.println("First, you need to authenticate");
                    AuthResult auth = authenticate();
                    if (auth.authType == Librarians.AuthType.FULL_TIME) {
                        System.out.println("\nSuccessfully authorized!");
                    } else {
                        System.out.println("\nFailed to authorize.");
                        break;
                    }

                    System.out.print("Enter the new username: ");
                    String newUsername = scanner.nextLine();
                    if (newUsername.isEmpty()) {
                        System.out.println("\nNot a valid username");
                        break;
                    }
                    librarians.hirePartTimeLibrarian(auth.username, auth.authCode, newUsername);
                }
                // 11. Withdraw Salary
                case "11": {
                    System.out.println("You chose to withdraw your salary");
                    AuthResult auth = authenticate();
                    if (auth.authType == Librarians.AuthType.FULL_TIME) {
                        System.out.println("\nSuccessfully authorized!");
                    } else {
                        System.out.println("\nFailed to authorize.");
                        break;
                    }

                    Double expectedSalary = librarians.getSalary(auth.username, auth.authCode);
                    if (expectedSalary == null) {
                        System.out.println("Salary couldn't be found.");
                        break;
                    }
                    double salary = libraryAccounts.withdrawSalary(expectedSalary);
                    if (salary < expectedSalary) {
                        System.out.println("Salary: $" + salary + " is less than expected: $" + expectedSalary);
                    }
                    librarians.librarianWithdrewSalary(auth.username, auth.authCode, salary);
                }
                // 12. Donate to Library
                case "12": {
                    System.out.println("You chose to donate to the library");
                    AuthResult auth = authenticate();
                    if (auth.authType == Librarians.AuthType.FULL_TIME) {
                        System.out.println("\nSuccessfully authorized!");
                    } else {
                        System.out.println("\nFailed to authorize.");
                        break;
                    }

                    System.out.print("How much would you like to donate?: ");
                    String donationStr = scanner.nextLine();
                    if (donationStr.isEmpty()) {
                        System.out.println("\nNot a valid donation");
                        break;
                    }
                    double donation = -1;
                    try {
                        donation = Double.parseDouble(donationStr);
                    } catch (Exception e) {
                        System.out.println("Donation " + donationStr + "must be a number.");
                        break;
                    }

                    libraryAccounts.depositDonation(donation);

                    System.out.println("Thank you for donating $" + donation + "!");
                }
                // 13. EXIT
                case "13": {
                    System.out.println("Goodbye!\n");
                    exit = true;
                    break;
                }
                default: {
                    // ?. Handle bad option
                    skip = true;
                    System.out.println("Invalid option");
                }
            }
            if (exit) break;

            if (!skip) {
                System.out.print("\nPress any key to continue: ");
                input = scanner.nextLine();
            }
        }
        scanner.close();
    }

    private AuthResult authenticate() {
        System.out.print("Enter your librarian username: ");
        String username = scanner.nextLine();
        if (username.isEmpty()) {
            System.out.println("\nNot a valid username");
            return new AuthResult(username, null, Librarians.AuthType.NOT_AUTHORIZED);
        }
        System.out.print("Enter your auth code: ");
        String authCode = scanner.nextLine();
        if (authCode.isEmpty()) {
            System.out.println("\nNot a valid authCode");
            return new AuthResult(username, authCode, Librarians.AuthType.NOT_AUTHORIZED);
        }
        return new AuthResult(username, authCode, librarians.authLibrarian(username, authCode));
    }

    private Book orderBook() {
        return orderBook(null, null, null);
    }

    private Book orderBook(String librarianUsername, String authCode, String bookName) {
        System.out.println("You chose to order " + (bookName == null ? "a new book" : "the book: " + bookName));
        AuthResult auth = null;
        if (librarianUsername == null || librarianUsername.isEmpty() ||
                authCode == null || authCode.isEmpty()) {
            auth = authenticate();
            if (auth.authType == Librarians.AuthType.FULL_TIME) {
                System.out.println("\nSuccessfully authorized!");
            } else {
                System.out.println("\nFailed to authorize.");
                return null;
            }
        }
        else {
            auth = new AuthResult(librarianUsername, authCode, librarians.authLibrarian(librarianUsername, authCode));
        }

        String name = null;
        if (bookName == null || bookName.isEmpty()) {
            System.out.print("Enter book name: ");
            name = scanner.nextLine();
            if (name.isEmpty()) {
                System.out.println("\nNot a valid name");
                return null;
            }
        }
        else {
            name = bookName;
            System.out.println("You are ordering the book: " + name);
        }

        System.out.print("Enter author: ");
        String author = scanner.nextLine();
        if (author.isEmpty()) {
            System.out.println("\nNot a valid author");
            return null;
        }
        System.out.print("Enter year: ");
        String yearString = scanner.nextLine();
        if (yearString.isEmpty()) {
            System.out.println("\nNot a valid year");
            return null;
        }
        int year = -1;
        try {
            year = Integer.parseInt(yearString);
        } catch (Exception e) {
            System.out.println("Year " + yearString + "must be a number.");
            return null;
        }
        System.out.print("Enter Genre: ");
        String genre = scanner.nextLine();
        if (genre.isEmpty()) {
            System.out.println("\nNot a valid genre");
            return null;
        }
        System.out.print("Enter ISBN: ");
        String isbnString = scanner.nextLine();
        if (isbnString.isEmpty()) {
            System.out.println("\nNot a valid ISBN");
            return null;
        }
        int isbn = -1;
        try {
            isbn = Integer.parseInt(isbnString);
        } catch (Exception e) {
            System.out.println("ISBN " + isbnString + "must be a number.");
            return null;
        }
        System.out.println();

        Boolean bookSucccess = libraryAccounts.orderNewBook(name, author, year, genre, isbn);
        Book newBook = null;
        if (bookSucccess) {
            newBook = library.addBook(name, author, year, genre, isbn);
            if (newBook == null) { return null; }
            librarians.librarianPurchasedBook(auth.username, auth.authCode, newBook);
        } else {
            return null;
        }

        System.out.println("Successfully added a new book!");
        System.out.println(newBook.getBookInfo());
        return newBook;
    }

    public static class AuthResult {
        public String username;
        public String authCode;
        public Librarians.AuthType authType;

        public AuthResult(String username, String authCode, Librarians.AuthType authType) {
            this.username = username;
            this.authCode = authCode;
            this.authType = authType;
        }
    }
}
