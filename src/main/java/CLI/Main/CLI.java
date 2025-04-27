package CLI.Main;

import CLI.Models.*;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CLI {
    Scanner scanner;
    PrintStream outStream;
    Library library;
    LibraryAccounts libraryAccounts;
    Librarians librarians;

    AuthResult currentLibrarian = null;

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
        String stars = "*******************************************************************";

        System.out.print("\n" + stars + "\nBuilt by: Alex Brodsky, Berend Grandt, Shawn Russell, & Bruce Brown");

        String input = "";

        while (input != null) {

            String userHeader = currentLibrarian == null ? "" : "\n\tCurrent Librarian: " + currentLibrarian.username + " | Employment Level: " + prettify(currentLibrarian.authType) + "\n";

            outStream.print("""
                    
                    """ + stars + (currentLibrarian == null ? "" : userHeader + stars) + """
                
                
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
                13. Log In
                14. Log Out
                15. EXIT
                
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
                    // They can be part-time, so they don't need to request to be full-time
                    if (authenticate().authType != Librarians.AuthType.NOT_AUTHORIZED) {
                        // Only librarians can remove books! (Including Part-Time)
                        outStream.println("\nSuccessfully authorized!");
                    } else {
                        outStream.println("\nFailed to authorize.");
                        break;
                    }

                    outStream.print("Enter book name to remove: ");
                    String book = scanner.nextLine();
                    if (book.isBlank()) {
                        outStream.println("\nNot a valid book name");
                        break;
                    }
                    String bookID = library.findBookIdByName(book);
                    if (bookID == null) {
                        outStream.println("\nCouldn't find book " + book + ".");
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
                    if (book.isBlank()) {
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
                    if (memberID.isBlank()) {
                        outStream.println("\nNot a valid ID");
                        break;
                    }
                    if (library.getAllMembers().stream().noneMatch(m -> m.MemberID.equals(memberID))) {
                        outStream.println("\nMember with ID " + memberID + " does not exist.");
                        break;
                    }
                    outStream.print("Enter book name: ");
                    String bookName = scanner.nextLine();
                    if (bookName.isBlank()) {
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
                            AuthResult auth = currentLibrarian == null || currentLibrarian.authType != Librarians.AuthType.FULL_TIME ? authenticate(true) : currentLibrarian;
                            if (auth.authType == Librarians.AuthType.FULL_TIME) {
                                outStream.println("\nYou are authorized as a full-time librarian!");
                                Book book = orderBook(auth.username, auth.authCode, bookName);
                                if (book == null) break;
                                outStream.println();
                                library.checkoutBook(memberID, book.Name);
                            }
                            else {
                                outStream.println("\nYou failed to authorize as a full-time librarian.");
                                break;
                            }
                        }
                        break;
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
                    if (memberID.isBlank()) {
                        outStream.println("\nNot a valid ID");
                        break;
                    }
                    outStream.print("Enter book name: ");
                    String book = scanner.nextLine();
                    if (book.isBlank()) {
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
                    if (authenticate(true).authType == Librarians.AuthType.FULL_TIME) {
                        outStream.println("\nSuccessfully authorized!\n");
                    } else {
                        outStream.println("\nFailed to authorize. Must be a full-time librarian.\n");
                        break;
                    }
                    
                    outStream.print("Enter member name: ");
                    String name = scanner.nextLine();
                    if (name.isBlank()) {
                        outStream.println("\nNot a valid name");
                        break;
                    }
                    outStream.print("Enter member email: ");
                    String email = scanner.nextLine();
                    if (email.isBlank()) {
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
                    if (authenticate(true).authType == Librarians.AuthType.FULL_TIME) {
                        outStream.println("\nSuccessfully authorized!\n");
                    } else {
                        outStream.println("\nFailed to authorize. Must be a full-time librarian.\n");
                        break;
                    }

                    outStream.print("Enter member ID to revoke: ");
                    String memberID = scanner.nextLine();
                    if (memberID.isBlank()) {
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
                    AuthResult auth = currentLibrarian;
                    if (auth == null || auth.authType != Librarians.AuthType.FULL_TIME) {
                        if (auth != null && auth.authType == Librarians.AuthType.PART_TIME) {
                            outStream.println("\nYou must be a full-time librarian to hire a new librarian");
                        }
                        outStream.println("First, you need to authenticate");
                        auth = authenticate(true);
                        if (auth.authType == Librarians.AuthType.FULL_TIME) {
                            outStream.println("\nSuccessfully authorized!");
                        } else {
                            outStream.println("\nFailed to authorize. Must be a full-time librarian.");
                            break;
                        }
                    }

                    outStream.print("Enter the new username: ");
                    String newUsername = scanner.nextLine();
                    if (newUsername.isBlank()) {
                        outStream.println("\nNot a valid username");
                        break;
                    }

                    System.out.println();
                    librarians.hirePartTimeLibrarian(auth.username, auth.authCode, newUsername);
                    break;
                }
                // 11. Withdraw Salary
                case "11": {
                    outStream.println("You chose to withdraw your salary");
                    AuthResult auth = authenticate(true);
                    if (auth.authType == Librarians.AuthType.FULL_TIME) {
                        outStream.println("\nSuccessfully authorized!\n");
                    } else {
                        outStream.println("\nFailed to authorize. Must be a full-time librarian.\n");
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
                    break;
                }
                // 12. Donate to Library
                case "12": {
                    outStream.println("You chose to donate to the library");
                    AuthResult auth = authenticate(true);
                    if (auth.authType == Librarians.AuthType.FULL_TIME) {
                        outStream.println("\nSuccessfully authorized!");
                    } else {
                        outStream.println("\nFailed to authorize. Must be a full-time librarian.");
                        break;
                    }

                    outStream.print("How much would you like to donate?: ");
                    String donationStr = scanner.nextLine();
                    if (donationStr.isBlank()) {
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
                    break;
                }
                // 13. Log In
                case "13": {
                    if (currentLibrarian != null && currentLibrarian.authType != Librarians.AuthType.NOT_AUTHORIZED) {
                        outStream.println("You must log out before you can log in.");
                        break;
                    }
                    currentLibrarian = null; // Just in case
                    AuthResult auth = authenticate();
                    if (auth.authType == Librarians.AuthType.FULL_TIME) {
                        outStream.println("\nLogged in as a full-time librarian!");
                    }
                    else if (auth.authType == Librarians.AuthType.PART_TIME) {
                        outStream.println("\nLogged in as a part-time librarian!");
                    }
                    else {
                        outStream.println("\nFailed to log in.");
                    }
                    break;
                }
                // 14. Log Out
                case "14": {
                    if (currentLibrarian == null) {
                        outStream.println("You are already logged out!");
                        break;
                    }
                    currentLibrarian = null;
                    outStream.println("Successfully logged out!");
                    break;
                }
                // 15. EXIT
                case "15", "x", "X", "q", "Q": {
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
            if (exit) {
                break;
            }

            if (!skip) {
                outStream.print("\nPress any key to continue: ");
                input = scanner.nextLine();
            }
            skip = false;
        }
        scanner.close();
    }

    AuthResult authenticate() {
        return authenticate(false);
    }

    AuthResult authenticate(Boolean requestFullTime) {
        if (currentLibrarian != null) {
            // If not null, then either full time or part-time, or requesting full time
            if (currentLibrarian.authType == Librarians.AuthType.FULL_TIME) {
                return currentLibrarian;
            }
            else if (!requestFullTime && currentLibrarian.authType == Librarians.AuthType.PART_TIME) {
                return currentLibrarian;
            }
        }

        if (requestFullTime) {
            outStream.println("You must log in as a full-time librarian!\n");
        }
        outStream.print("Enter your librarian username: ");
        String username = scanner.nextLine();
        if (username.isBlank()) {
            outStream.println("\nNot a valid username");
            currentLibrarian = null;
            return new AuthResult(username, null, Librarians.AuthType.NOT_AUTHORIZED);
        }
        // Check if they are Part-Time
        Librarians.AuthType checkPartTime = librarians.authLibrarian(username, null);
        if (checkPartTime == Librarians.AuthType.PART_TIME) {
            currentLibrarian = new AuthResult(username, null, Librarians.AuthType.PART_TIME);
            return currentLibrarian;
        }

        outStream.print("Enter your authentication code: ");
        String authCode = scanner.nextLine();
        authCode = authCode.isBlank() ? null : authCode;
        AuthResult result = new AuthResult(username, authCode, librarians.authLibrarian(username, authCode));
        currentLibrarian = result.authType == Librarians.AuthType.NOT_AUTHORIZED ? null : result;
        return result;
    }

     void orderBook() {
        orderBook(null, null, null);
    }

    Book orderBook(String librarianUsername, String authCode, String bookName) {
        outStream.println("You chose to order " + (bookName == null ? "a new book" : "the book: " + bookName));
        AuthResult auth = null;
        if (librarianUsername == null || librarianUsername.isBlank() ||
            authCode == null || authCode.isBlank()
        ) {
            auth = authenticate(true);
        }
        else {
            auth = new AuthResult(librarianUsername, authCode, librarians.authLibrarian(librarianUsername, authCode));
        }
        if (auth.authType == Librarians.AuthType.FULL_TIME) {
            outStream.println("\nSuccessfully authorized!");
        } else {
            outStream.println("\nFailed to authorize. Must be a full-time librarian.");
            return null;
        }

        String name = null;
        if (bookName == null || bookName.isBlank()) {
            outStream.print("Enter book name: ");
            name = scanner.nextLine();
            if (name.isBlank()) {
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
        if (author.isBlank()) {
            outStream.println("\nNot a valid author");
            return null;
        }
        outStream.print("Enter year: ");
        String yearString = scanner.nextLine();
        if (yearString.isBlank()) {
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
        if (genre.isBlank()) {
            outStream.println("\nNot a valid genre");
            return null;
        }
        outStream.print("Enter ISBN: ");
        String isbnString = scanner.nextLine();
        if (isbnString.isBlank()) {
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

        Boolean bookSuccess = libraryAccounts.orderNewBook(name, author, year, genre, isbn);
        Book newBook = null;
        if (bookSuccess) {
            newBook = library.addBook(name, author, year, genre, isbn);
            if (newBook == null) { return null; }
            librarians.librarianPurchasedBook(auth.username, auth.authCode, newBook);
        } else {
            outStream.println("Failed to order book: " + name + ".");
            return null;
        }

        outStream.println("Successfully ordered book: " + name + "!");
        outStream.println(newBook.getBookInfo());
        return newBook;
    }

    String prettify(Librarians.AuthType type) {
        String[] split = type.toString().split("_");
        String[] capitalized = Arrays.stream(split)
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase())
                .toArray(String[]::new);
        return String.join(" ", capitalized);
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
