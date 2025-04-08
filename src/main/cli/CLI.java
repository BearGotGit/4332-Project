package main.cli;

import main.model.Book;
import main.model.Library;
import main.model.Member;

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
               4. Remove Member
               5. Checkout Book
               6. Opt: Return Book
            
            Enter the number of the option you want to select:
            """);
            String option = scanner.nextLine();


            switch (option) {
//            1. Add book
                case "1": {
                    System.out.println("You chose to add a new book:");
                    System.out.println("Enter book name:");
                    String name = scanner.nextLine();
                    System.out.println("Enter author:");
                    String author = scanner.nextLine();
                    System.out.println("Enter year:");
                    String year = scanner.nextLine();
                    System.out.println("Enter Genre:");
                    String genre = scanner.nextLine();
                    System.out.println("Enter ISBN:");
                    String isbn = scanner.nextLine();
                    System.out.println("Enter book ID:");
                    String bookId = scanner.nextLine();

                    Book book = new Book(name, author, Integer.parseInt(year), genre, Integer.parseInt(isbn), bookId);

                    System.out.println("Successfully added a new book:");
                    System.out.println(book.getBookInfo());
                }

//            2. Remove book
                case "2": {
                    break;
                }
//            3. Add Member
                case "3": {
                    System.out.println("You chose to add a new member:");
                    System.out.println("Enter member name:");
                    String name = scanner.nextLine();
                    System.out.println("Enter member email:");
                    String email = scanner.nextLine();

//                    FIXME: Make ID unique (just have library generate ID or something
                    Member member = new Member(name, email, "abc");
                    library.addMember(member.MemberID);

                    System.out.println("Successfully added a new member:");
                    member.printMemberInfo();

                    break;
                }
//            4. Remove Member
                case "4": {
                    break;
                }
//            5. Checkout Book
                case "5": {
                    break;
                }
//            6. Opt: Return Book
                case "6": {
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
