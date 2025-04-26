package CLI.Main;

import CLI.Models.*;

import java.io.PrintStream;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class CLI {
    Scanner scanner;
    PrintStream outStream;
    Library library;
    LibraryAccounts libraryAccounts;
    Librarians librarians;

    Boolean exit = false;
    Boolean skip = false;

    public CLI(Readable input, PrintStream output, Library library, LibraryAccounts libraryAccounts, Librarians librarians) {
        this.scanner = new Scanner(input);
        this.outStream = output;
        this.library = library;
        this.libraryAccounts = libraryAccounts;
        this.librarians = librarians;
    }

    public void run() {
        String stars = "****************************************************";

        String input = "";

        while (input != null) {

            outStream.print("""
                    
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

            outStream.println();

            switch (option) {
                // 1. Order book
                case "1": {
                    orderBook();
                    break;
                }

                // 2. Remove book
                case "2": {
                    outStream.println("You chose to remove a book");
                    if (authenticate().authType != Librarians.AuthType.NOT_AUTHORIZED) {
                        // Only librarians can remove books!
                        outStream.println("\nSuccessfully authorized!");
                    } else {
                        outStream.println("\nFailed to authorize.");
                        break;
                    }

                    outStream.print("Enter book name to remove: ");
                    String book = scanner.nextLine();
                    if (book.isEmpty()) {
                        outStream.println("\nNot a valid book name");
                        break;
                    }
                    String bookID = library.findBookIdByName(book);
                    if (bookID == null) {
                        outStream.println("Couldn't find book " + book + ".");
                        break;
                    }
                    outStream.println();

                    library.removeBook(bookID);
                    break;
                }
                // 3. Check Book Availability
                case "3": {
                    outStream.println("You chose to check a book's availability");
                    outStream.print("Enter book name: ");
                    String book = scanner.nextLine();
                    if (book.isEmpty()) {
                        outStream.println("\nNot a valid genre");
                        break;
                    }

                    String bookID = library.findBookIdByName(book);
                    if (bookID == null) {
                        outStream.println("\nCouldn't find book " + book + ".");
                        break;
                    }

                    boolean available = library.bookAvailability(bookID);
                    if (available) {
                        outStream.println("\nBook " + book + " is available to checkout!");
                        break;
                    } else {
                        outStream.println("\nBook " + book + " is not available to checkout.");
                    }
                    break;
                }

                // 4. Checkout Book
                case "4": {
                    outStream.println("You chose to checkout a new book");
                    outStream.print("Enter your member ID: ");
                    String memberID = scanner.nextLine();
                    if (memberID == null || memberID.isEmpty()) {
                        outStream.println("\nNot a valid ID");
                        break;
                    }
                    if (library.getAllMembers().stream().noneMatch(m -> m.MemberID.equals(memberID))) {
                        outStream.println("\nMember with ID " + memberID + " does not exist.");
                        break;
                    }
                    outStream.print("Enter book name: ");
                    String bookName = scanner.nextLine();
                    if (bookName == null || bookName.isEmpty()) {
                        outStream.println("\nNot a valid book name");
                        break;
                    }

                    String bookID = library.findBookIdByName(bookName);
                    if (bookID == null) {
                        outStream.println("\nCouldn't find book " + bookName + ".");
                        outStream.println("\nHowever, if you enter full-time librarian credentials, you can order the book.");

                        outStream.print("""
                                
                                You have these options:
                                
                                1. Order Book
                                2. Cancel
                                
                                Enter the number of the option you want:""" + " ");
                        String checkoutOption = scanner.nextLine();

                        outStream.println();
                        if (checkoutOption.equals("1")) {
                            AuthResult auth = authenticate();
                            if (auth.authType == Librarians.AuthType.FULL_TIME) {
                                outStream.println("\nYou are authorized as a full-time librarian!");
                                Book book = orderBook(auth.username, auth.authCode, bookName);
                                if (book != null) {
                                    outStream.println();
                                    library.checkoutBook(memberID, book.Name);
                                }
                            }
                            else {
                                outStream.println("\nYou failed to authorize as a full-time librarian.");
                            }
                        }
                        break; // exit switch no matter what
                    }

                    outStream.println();
                    library.checkoutBook(memberID, bookName);
                    break;
                }

                // 5. Return Book
                case "5": {
                    outStream.println("You chose to return a book");
                    outStream.print("Enter your member ID: ");
                    String memberID = scanner.nextLine();
                    if (memberID == null || memberID.isEmpty()) {
                        outStream.println("\nNot a valid ID");
                        break;
                    }
                    outStream.print("Enter book name: ");
                    String book = scanner.nextLine();
                    if (book == null || book.isEmpty()) {
                        outStream.println("\nNot a valid book name");
                        break;
                    }
                    String bookID = library.findBookIdByName(book);
                    if (bookID == null) {
                        outStream.println("Couldn't find book " + book + ".");
                        break;
                    }
                    outStream.println();
                    library.returnBook(memberID, bookID);
                    break;
                }

                // 6. View All Books
                case "6": {
                    if (library.AllBooksInLibrary == null || library.AllBooksInLibrary.size() <= 0) {
                        outStream.println("There are no books in the library");
                        break;
                    }
                    for (Book book : library.AllBooksInLibrary) {
                        outStream.println(book.getBookInfo());
                    }
                    break;
                }

                // 7. Add Member
                case "7": {
                    outStream.println("You chose to add a new member");
                    if (authenticate().authType == Librarians.AuthType.FULL_TIME) {
                        outStream.println("\nSuccessfully authorized!");
                    } else {
                        outStream.println("\nFailed to authorize.");
                        break;
                    }
                    
                    outStream.print("Enter member name: ");
                    String name = scanner.nextLine();
                    if (name.isEmpty()) {
                        outStream.println("\nNot a valid name");
                        break;
                    }
                    outStream.print("Enter member email: ");
                    String email = scanner.nextLine();
                    if (email.isEmpty()) {
                        outStream.println("\nNot a valid email");
                        break;
                    }

                    outStream.println();
                    Member newMember = library.addMember(name, email);
                    if (newMember == null) {
                        outStream.println("New member couldn't be created");
                        break;
                    }
                    outStream.println("Successfully added a new member with ID: " + newMember.MemberID);
                    newMember.printMemberInfo();
                    break;
                }
                // 8. Revoke Membership
                case "8": {
                    outStream.println("You chose to revoke a membership");
                    if (authenticate().authType == Librarians.AuthType.FULL_TIME) {
                        outStream.println("\nSuccessfully authorized!");
                    } else {
                        outStream.println("\nFailed to authorize.");
                        break;
                    }

                    outStream.print("Enter member ID to revoke: ");
                    String memberID = scanner.nextLine();
                    if (memberID.isEmpty()) {
                        outStream.println("\nNot a valid ID");
                        break;
                    }

                    var success = library.revokeMembership(memberID);
                    if (success) {
                        outStream.println("\nSuccessfully revoked membership of member with ID: " + memberID);
                        break;
                    }
                    outStream.println("\nFailed to revoke membership of member with ID: " + memberID);
                    break;
                }
                // 9. View All Members
                case "9": {
                    List<Member> allMembers = library.getAllMembers();
                    if (allMembers == null || allMembers.size() <= 0) {
                        outStream.println("There are no members in the library");
                        break;
                    }
                    for (Member member : allMembers) {
                        member.printMemberInfo();
                    }
                    break;
                }
                // 10. Hire Part-Time Librarian
                case "10": {
                    outStream.println("You chose to hire a part-time librarian");
                    outStream.println("First, you need to authenticate");
                    AuthResult auth = authenticate();
                    if (auth.authType == Librarians.AuthType.FULL_TIME) {
                        outStream.println("\nSuccessfully authorized!");
                    } else {
                        outStream.println("\nFailed to authorize.");
                        break;
                    }

                    outStream.print("Enter the new username: ");
                    String newUsername = scanner.nextLine();
                    if (newUsername.isEmpty()) {
                        outStream.println("\nNot a valid username");
                        break;
                    }
                    librarians.hirePartTimeLibrarian(auth.username, auth.authCode, newUsername);
                }
                // 11. Withdraw Salary
                case "11": {
                    outStream.println("You chose to withdraw your salary");
                    AuthResult auth = authenticate();
                    if (auth.authType == Librarians.AuthType.FULL_TIME) {
                        outStream.println("\nSuccessfully authorized!");
                    } else {
                        outStream.println("\nFailed to authorize.");
                        break;
                    }

                    Double expectedSalary = librarians.getSalary(auth.username, auth.authCode);
                    if (expectedSalary == null) {
                        outStream.println("Salary couldn't be found.");
                        break;
                    }
                    double salary = libraryAccounts.withdrawSalary(expectedSalary);
                    if (salary < expectedSalary) {
                        outStream.println("Salary: $" + salary + " is less than expected: $" + expectedSalary);
                    }
                    librarians.librarianWithdrewSalary(auth.username, auth.authCode, salary);
                }
                // 12. Donate to Library
                case "12": {
                    outStream.println("You chose to donate to the library");
                    AuthResult auth = authenticate();
                    if (auth.authType == Librarians.AuthType.FULL_TIME) {
                        outStream.println("\nSuccessfully authorized!");
                    } else {
                        outStream.println("\nFailed to authorize.");
                        break;
                    }

                    outStream.print("How much would you like to donate?: ");
                    String donationStr = scanner.nextLine();
                    if (donationStr.isEmpty()) {
                        outStream.println("\nNot a valid donation");
                        break;
                    }
                    double donation = -1;
                    try {
                        donation = Double.parseDouble(donationStr);
                    } catch (Exception e) {
                        outStream.println("Donation " + donationStr + "must be a number.");
                        break;
                    }

                    libraryAccounts.depositDonation(donation);

                    outStream.println("Thank you for donating $" + donation + "!");
                }
                // 13. EXIT
                case "13": {
                    outStream.println("Goodbye!\n");
                    exit = true;
                    break;
                }
                default: {
                    // ?. Handle bad option
                    skip = true;
                    outStream.println("Invalid option");
                }
            }
            if (exit) break;

            if (!skip) {
                outStream.print("\nPress any key to continue: ");
                input = scanner.nextLine();
            }
        }
        scanner.close();
    }

    public AuthResult authenticate() {
        outStream.print("Enter your librarian username: ");
        String username = scanner.nextLine();
        if (username.isEmpty()) {
            outStream.println("\nNot a valid username");
            return new AuthResult(username, null, Librarians.AuthType.NOT_AUTHORIZED);
        }
        outStream.print("Enter your auth code: ");
        String authCode = scanner.nextLine();
        if (authCode.isEmpty()) {
            outStream.println("\nNot a valid authCode");
            return new AuthResult(username, authCode, Librarians.AuthType.NOT_AUTHORIZED);
        }
        return new AuthResult(username, authCode, librarians.authLibrarian(username, authCode));
    }

    private void orderBook() {
        orderBook(null, null, null);
    }

    private Book orderBook(String librarianUsername, String authCode, String bookName) {
        outStream.println("You chose to order " + (bookName == null ? "a new book" : "the book: " + bookName));
        AuthResult auth = null;
        if (librarianUsername == null || librarianUsername.isEmpty() ||
                authCode == null || authCode.isEmpty()) {
            auth = authenticate();
            if (auth.authType == Librarians.AuthType.FULL_TIME) {
                outStream.println("\nSuccessfully authorized!");
            } else {
                outStream.println("\nFailed to authorize.");
                return null;
            }
        }
        else {
            auth = new AuthResult(librarianUsername, authCode, librarians.authLibrarian(librarianUsername, authCode));
        }

        String name = null;
        if (bookName == null || bookName.isEmpty()) {
            outStream.print("Enter book name: ");
            name = scanner.nextLine();
            if (name.isEmpty()) {
                outStream.println("\nNot a valid name");
                return null;
            }
        }
        else {
            name = bookName;
            outStream.println("You are ordering the book: " + name);
        }

        outStream.print("Enter author: ");
        String author = scanner.nextLine();
        if (author.isEmpty()) {
            outStream.println("\nNot a valid author");
            return null;
        }
        outStream.print("Enter year: ");
        String yearString = scanner.nextLine();
        if (yearString.isEmpty()) {
            outStream.println("\nNot a valid year");
            return null;
        }
        int year = -1;
        try {
            year = Integer.parseInt(yearString);
        } catch (Exception e) {
            outStream.println("Year " + yearString + "must be a number.");
            return null;
        }
        outStream.print("Enter Genre: ");
        String genre = scanner.nextLine();
        if (genre.isEmpty()) {
            outStream.println("\nNot a valid genre");
            return null;
        }
        outStream.print("Enter ISBN: ");
        String isbnString = scanner.nextLine();
        if (isbnString.isEmpty()) {
            outStream.println("\nNot a valid ISBN");
            return null;
        }
        int isbn = -1;
        try {
            isbn = Integer.parseInt(isbnString);
        } catch (Exception e) {
            outStream.println("ISBN " + isbnString + "must be a number.");
            return null;
        }
        outStream.println();

        Boolean bookSucccess = libraryAccounts.orderNewBook(name, author, year, genre, isbn);
        Book newBook = null;
        if (bookSucccess) {
            newBook = library.addBook(name, author, year, genre, isbn);
            if (newBook == null) { return null; }
            librarians.librarianPurchasedBook(auth.username, auth.authCode, newBook);
        } else {
            return null;
        }

        outStream.println("Successfully added a new book!");
        outStream.println(newBook.getBookInfo());
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
