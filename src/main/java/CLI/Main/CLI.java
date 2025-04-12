package CLI.Main;

import CLI.Models.*;

import java.util.Scanner;

public class CLI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Library library = new Library();

        while (true) {

            System.out.print("""
                
            You have these options:
            
            1. Add Book
            2. Remove Book
            3. Check Book Availability
            4. Checkout Book
            5. Return Book
            6. Add Member
            7. Revoke Membership
            
            Enter the number of the option you want to select: """ + " ");
            String option = scanner.nextLine();

            System.out.println();

            switch (option) {
                // 1. Add book
                case "1": {
                    System.out.println("You chose to add a new book");
                    System.out.print("Enter book name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter author: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter year: ");
                    String yearString = scanner.nextLine();
                    int year = -1;
                    try {
                        year = Integer.parseInt(yearString);
                    } catch (Exception e) {
                        System.out.println("Year " + yearString + "must be a number.");
                        break;
                    }
                    System.out.print("Enter Genre: ");
                    String genre = scanner.nextLine();
                    System.out.print("Enter ISBN: ");
                    String isbnString = scanner.nextLine();
                    int isbn = -1;
                    try {
                        isbn = Integer.parseInt(isbnString);
                    } catch (Exception e) {
                        System.out.println("ISBN " + isbnString + "must be a number.");
                        break;
                    }

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
                    String bookID = library.findBookIdByName(book);
                    if (bookID == null) { 
                        System.out.println("Couldn't find book " + book + ".");
                        break;
                    }
                    library.removeBook(bookID);
                    break;
                }
                // 3. Check Book Availability
                case "3": {
                    System.out.println("You chose to check a book's availability");
                    System.out.print("Enter book name: ");
                    String book = scanner.nextLine();

                    String bookID = library.findBookIdByName(book);
                    if (bookID == null) {
                        System.out.println("Couldn't find book " + book + ".");
                        break;
                    }
                
                    boolean available = library.bookAvailability(bookID);
                    if (available) {
                        System.out.println("Book " + book + " is available to checkout!");
                        break;
                    }
                    else {
                        System.out.println("Book " + book + " is not available to checkout.");
                    }
                    break;
                }
                // 4. Checkout Book
                case "4": {
                    System.out.println("You chose to checkout a new book");
                    System.out.print("Enter your member ID: ");
                    String memberID = scanner.nextLine();
                    System.out.print("Enter book name: ");
                    String book = scanner.nextLine();
                    
                    library.checkoutBook(memberID, book);
                    break;
                }
                // 5. Return Book
                case "5": {
                    System.out.println("You chose to return a book");
                    System.out.print("Enter book name: ");
                    String book = scanner.nextLine();
                    String bookID = library.findBookIdByName(book);
                    if (bookID == null) { 
                        System.out.println("Couldn't find book " + book + ".");
                        break;
                    }
                    
                    library.returnBook(bookID);
                    break;
                }
                // 6. Add Member
                case "6": {
                    System.out.println("You chose to add a new member");
                    System.out.print("Enter member name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter member email: ");
                    String email = scanner.nextLine();
                    Member newMember = library.addMember(name, email);
                    if (newMember == null) {
                        System.out.println("New member couldn't be created");
                        break;
                    }
                    System.out.println("Successfully added a new member with ID: " + newMember.MemberID);
                    newMember.printMemberInfo();
                    break;
                }
                // 7. Revoke Membership
                case "7": {
                    System.out.println("You chose to revoke a membership");
                    System.out.print("Enter member ID to revoke: ");
                    String memberID = scanner.nextLine();
                    var success = library.revokeMembership(memberID);
                    if (success) {
                        System.out.println("Successfully revoked membership of member with ID: " + memberID);
                        break;
                    }
                    System.out.println("Failed to revoke membership of member with ID: " + memberID);
                    break;
                }
                default: {
                // ?. Handle bad option
                    System.out.println("Invalid option");
                }
            }

            System.out.println();
            System.out.print("Do you want to do something else? (yes/no): ");
            String answer = scanner.nextLine();
            System.out.println();

            if (!answer.equalsIgnoreCase("yes")) {
                break;
            }
        }

        scanner.close();
    }
}
