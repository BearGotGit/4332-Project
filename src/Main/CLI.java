package Main;

import Models.Book;
import Models.Library;
import Models.Member;

import java.util.Scanner;

public class CLI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Library library = new Library();

        while (true) {

            System.out.println("""
            You have these options:
            
               1. Add book
               2. Remove book
               3. Add Member
               4. Revoke Membership
               5. Checkout Book
               6. Return Book
            
            Enter the number of the option you want to select:
            """);
            String option = scanner.nextLine();


            switch (option) {
//            1. Add book
                case "1": {
                    System.out.println("You chose to add a new book");
                    System.out.println("Enter book name:");
                    String name = scanner.nextLine();
                    System.out.println("Enter author:");
                    String author = scanner.nextLine();
                    System.out.println("Enter year:");
                    String yearString = scanner.nextLine();
                    int year = -1;
                    try {
                        year = Integer.parseInt(yearString);
                    } catch (Exception e) {
                        System.out.println("Year " + yearString + "must be a number.");
                        break;
                    }
                    System.out.println("Enter Genre:");
                    String genre = scanner.nextLine();
                    System.out.println("Enter ISBN:");
                    String isbnString = scanner.nextLine();
                    int isbn = -1;
                    try {
                        isbn = Integer.parseInt(isbnString);
                    } catch (Exception e) {
                        System.out.println("ISBN " + isbnString + "must be a number.");
                        break;
                    }

                    Book newBook = library.addBook(name, author, year, genre, isbn);

                    System.out.println("Successfully added a new book:");
                    System.out.println(newBook.getBookInfo());
                    break;
                }

//            2. Remove book
                case "2": {
                    System.out.println("You chose to remove a book");
                    System.out.println("Enter book name to remove:");
                    String book = scanner.nextLine();
                    String bookID = library.findBookIdByName(book);
                    if (bookID == null) { 
                        System.out.println("Couldn't find book " + book + ".");
                        break;
                    }
                    library.removeBook(bookID);
                    break;
                }
//            3. Add Member
                case "3": {
                    System.out.println("You chose to add a new member");
                    System.out.println("Enter member name:");
                    String name = scanner.nextLine();
                    System.out.println("Enter member email:");
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
//            4. Revoke Membership
                case "4": {
                    System.out.println("You chose to revoke a membership");
                    System.out.println("Enter member ID to revoke:");
                    String memberID = scanner.nextLine();
                    var success = library.revokeMembership(memberID);
                    if (success) {
                        System.out.println("Successfully revoked membership of member with ID: " + memberID);
                        break;
                    }
                    System.out.println("Failed to revoke membership of member with ID: " + memberID);
                    break;
                }
//            5. Checkout Book
                case "5": {
                    System.out.println("You chose to checkout a new book");
                    System.out.println("Enter your member ID:");
                    String memberID = scanner.nextLine();
                    System.out.println("Enter book name:");
                    String book = scanner.nextLine();
                    
                    library.checkoutBook(memberID, book);
                    break;
                }
//            6. Return Book
                case "6": {
                    System.out.println("You chose to return a book");
                    System.out.println("Enter your member ID:");
                    String memberID = scanner.nextLine();
                    System.out.println("Enter book name:");
                    String book = scanner.nextLine();
                    String bookID = library.findBookIdByName(book);
                    if (bookID == null) { 
                        System.out.println("Couldn't find book " + book + ".");
                        break;
                    }
                    
                    library.returnBook(memberID, bookID);
                    break;
                }
                default: {
//            ?. Handle bad option
                    System.out.println("Invalid option");
                }
            }

            System.out.print("Do you want to do something else? (yes/no): ");
            String answer = scanner.nextLine();

            if (!answer.equalsIgnoreCase("yes")) {
                break;
            }
        }

        scanner.close();
    }
}
