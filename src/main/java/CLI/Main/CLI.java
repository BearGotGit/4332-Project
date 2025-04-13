package CLI.Main;

import CLI.Models.*;

import java.util.Scanner;

public class CLI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Library library = new Library();
        seedLibrary(library); // optional

        String stars = "****************************************************";

        String input = "";

        while (input != null) {

            System.out.print("""

            """+stars+"""


            You have these options:
            
            1. Add Book
            2. Remove Book
            3. Check Book Availability
            4. Checkout Book
            5. Return Book
            6. View All Books
            7. Add Member
            8. Revoke Membership
            9. View All Members
            10. EXIT
            
            Enter the number of the option you want to select: """ + " ");
            String option = scanner.nextLine();

            Boolean exit = false;
            Boolean skip = false;

            System.out.println();

            switch (option) {
                // 1. Add book
                case "1": {
                    System.out.println("You chose to add a new book");
                    System.out.print("Enter book name: ");
                    String name = scanner.nextLine();
                    if (name == null || name.isEmpty()) {
                        System.out.println("\nNot a valid name");
                        break;
                    }
                    System.out.print("Enter author: ");
                    String author = scanner.nextLine();
                    if (author == null || author.isEmpty()) {
                        System.out.println("\nNot a valid author");
                        break;
                    }
                    System.out.print("Enter year: ");
                    String yearString = scanner.nextLine();
                    if (yearString == null || yearString.isEmpty()) {
                        System.out.println("\nNot a valid year");
                        break;
                    }
                    int year = -1;
                    try {
                        year = Integer.parseInt(yearString);
                    } catch (Exception e) {
                        System.out.println("Year " + yearString + "must be a number.");
                        break;
                    }
                    System.out.print("Enter Genre: ");
                    String genre = scanner.nextLine();
                    if (genre == null || genre.isEmpty()) {
                        System.out.println("\nNot a valid genre");
                        break;
                    }
                    System.out.print("Enter ISBN: ");
                    String isbnString = scanner.nextLine();
                    if (isbnString == null || isbnString.isEmpty()) {
                        System.out.println("\nNot a valid ISBN");
                        break;
                    }
                    int isbn = -1;
                    try {
                        isbn = Integer.parseInt(isbnString);
                    } catch (Exception e) {
                        System.out.println("ISBN " + isbnString + "must be a number.");
                        break;
                    }
                    System.out.println();

                    Book newBook = library.addBook(name, author, year, genre, isbn);
                    if (newBook == null) { break; }

                    System.out.println("Successfully added a new book!");
                    System.out.println(newBook.getBookInfo());
                    break;
                }

                // 2. Remove book
                case "2": {
                    System.out.println("You chose to remove a book");
                    System.out.print("Enter book name to remove: ");
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

                    library.removeBook(bookID);
                    break;
                }
                // 3. Check Book Availability
                case "3": {
                    System.out.println("You chose to check a book's availability");
                    System.out.print("Enter book name: ");
                    String book = scanner.nextLine();
                    if (book == null || book.isEmpty()) {
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
                    }
                    else {
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
                    System.out.print("Enter book name: ");
                    String book = scanner.nextLine();
                    if (book == null || book.isEmpty()) {
                        System.out.println("\nNot a valid book name");
                        break;
                    }

                    System.out.println();
                    library.checkoutBook(memberID, book);
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
                    if (library.Members == null || library.Members.size() <= 0) {
                        System.out.println("There are no members of this library");
                        break;
                    }
                    for (Member member : library.Members) {
                       member.printMemberInfo();
                    }
                    break;
                }
                // 10. EXIT
                case "10": {
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

    private static void seedLibrary(Library library) {
        // Seed books
        library.addBook("1984", "George Orwell", 1949, "Dystopian", 1);
        library.addBook("To Kill a Mockingbird", "Harper Lee", 1960, "Fiction", 2);
        library.addBook("The Great Gatsby", "F. Scott Fitzgerald", 1925, "Classic", 3);
        library.addBook("The Hobbit", "J.R.R. Tolkien", 1937, "Fantasy", 3);
        library.addBook("Pride and Prejudice", "Jane Austen", 1813, "Romance", 4);
        library.addBook("Moby Dick", "Herman Melville", 1851, "Adventure", 6);
        library.addBook("The Catcher in the Rye", "J.D. Salinger", 1951, "Fiction", 7);
        library.addBook("Brave New World", "Aldous Huxley", 1932, "Dystopian", 8);
        library.addBook("The Alchemist", "Paulo Coelho", 1988, "Philosophical", 9);
        library.addBook("The Road", "Cormac McCarthy", 2006, "Post-apocalyptic", 10);

        // Seed members
        library.addMember("Alex", "brodsky.alex22@gmail.com");
        library.addMember("Berend", "idk");
        library.addMember("Shawn", "idk");
        library.addMember("Bruce", "idk");

        // Clear the screen
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
    }
}
