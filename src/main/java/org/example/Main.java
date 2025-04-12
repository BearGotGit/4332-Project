package org.example;

import java.util.Scanner;

public class Main {
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
            
            Enter the number of the option you want to select: """);

            String option = scanner.nextLine();
            System.out.println();

            switch (option) {
                case "1" -> {
                    System.out.println("You chose to add a new book");
                    System.out.print("Enter book name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter author: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter year: ");
                    int year;
                    try {
                        year = Integer.parseInt(scanner.nextLine());
                    } catch (Exception e) {
                        System.out.println("Year must be a number.");
                        continue;
                    }
                    System.out.print("Enter Genre: ");
                    String genre = scanner.nextLine();
                    System.out.print("Enter ISBN: ");
                    int isbn;
                    try {
                        isbn = Integer.parseInt(scanner.nextLine());
                    } catch (Exception e) {
                        System.out.println("ISBN must be a number.");
                        continue;
                    }

                    Book newBook = library.addBook(name, author, year, genre, isbn);
                    if (newBook == null) continue;

                    System.out.println("Successfully added a new book!");
                    System.out.println(newBook.getBookInfo());
                }

                case "2" -> {
                    System.out.println("You chose to remove a book");
                    System.out.print("Enter book name to remove: ");
                    String book = scanner.nextLine();
                    String bookID = library.findBookIdByName(book);
                    if (bookID == null) {
                        System.out.println("Couldn't find book " + book + ".");
                        continue;
                    }
                    library.removeBook(bookID);
                }

                case "3" -> {
                    System.out.println("You chose to check a book's availability");
                    System.out.print("Enter book name: ");
                    String book = scanner.nextLine();
                    String bookID = library.findBookIdByName(book);
                    if (bookID == null) {
                        System.out.println("Couldn't find book " + book + ".");
                        continue;
                    }
                    boolean available = library.bookAvailability(bookID);
                    System.out.println("Book " + book + (available ? " is available!" : " is not available."));
                }

                case "4" -> {
                    System.out.println("You chose to checkout a book");
                    System.out.print("Enter your member ID: ");
                    String memberID = scanner.nextLine();
                    System.out.print("Enter book name: ");
                    String book = scanner.nextLine();
                    library.checkoutBook(memberID, book);
                }

                case "5" -> {
                    System.out.println("You chose to return a book");
                    System.out.print("Enter book name: ");
                    String book = scanner.nextLine();
                    String bookID = library.findBookIdByName(book);
                    if (bookID == null) {
                        System.out.println("Couldn't find book " + book + ".");
                        continue;
                    }
                    library.returnBook(bookID);
                }

                case "6" -> {
                    System.out.println("You chose to add a new member");
                    System.out.print("Enter member name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter member email: ");
                    String email = scanner.nextLine();
                    Member newMember = library.addMember(name, email);
                    if (newMember == null) {
                        System.out.println("New member couldn't be created");
                        continue;
                    }
                    System.out.println("Successfully added new member with ID: " + newMember.MemberID);
                    newMember.printMemberInfo();
                }

                case "7" -> {
                    System.out.println("You chose to revoke a membership");
                    System.out.print("Enter member ID to revoke: ");
                    String memberID = scanner.nextLine();
                    boolean success = library.revokeMembership(memberID);
                    System.out.println(success
                            ? "Successfully revoked member ID: " + memberID
                            : "Failed to revoke member ID: " + memberID);
                }

                default -> System.out.println("Invalid option.");
            }

            System.out.print("\nDo you want to do something else? (yes/no): ");
            String answer = scanner.nextLine();
            System.out.println();
            if (!answer.equalsIgnoreCase("yes")) break;
        }

        scanner.close();
    }
}
