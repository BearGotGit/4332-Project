package CLI.Models;

import java.util.ArrayList;
import java.util.List;

public class Member {

    public String Name;
    public String Email;
    public String MemberID;
    public List<String> BorrowedBookList = new ArrayList<String>(); // Holds book IDs

    String testableMemberInfo; // only visible to the same package (only tests can see it)

    public Member(String Name, String Email, String MemberID) {
        if (Name == null || Name.isEmpty()) {
            System.out.println("ERROR: Name is null or empty");
            return;
        }
        if (Email == null) {
            System.out.println("ERROR: Email is null");
            return;
        }
        if (MemberID == null || MemberID.isEmpty()) {
            System.out.println("ERROR: MemberID is null or empty");
            return;
        }
        this.Name = Name;
        this.Email = Email;
        this.MemberID = MemberID;
    }

    public void printMemberInfo() {
        String formatted = String.format("Name: %s, Email: %s, MemberID: %s",
                Name, Email, MemberID);
        System.out.println(formatted);
        testableMemberInfo = formatted;
    }

    public List<String> getBorrowedBookList(){
        return BorrowedBookList;
    }

    public void addBorrowedBook(String bookID) {
        if (bookID == null || bookID.isEmpty()) {
            System.out.println("ERROR: BookID is null or empty");
            return;
        }
        // if book exists, return false
        if (BorrowedBookList.contains(bookID)) {
            System.out.println("There is already a Book Borrowed!");
            return;
        }
        BorrowedBookList.add(bookID);
    }

    public void removeBorrowedBook(String bookID) {
        if (bookID == null || bookID.isEmpty()) {
            System.out.println("ERROR: BookID is null or empty");
            return;
        }
        BorrowedBookList.remove(bookID);
    }

    public void updateMemberInfo(String Name, String Email) {
        if (Name == null || Name.isEmpty()) {
            System.out.println("ERROR: Name is null or empty");
            return;
        }
        if (Email == null || Email.isEmpty()) {
            System.out.println("ERROR: Name is null or empty");
            return;
        }
        this.Name = Name;
        this.Email = Email;
    }

}
