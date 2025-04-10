package Models;

import java.util.ArrayList;
import java.util.List;

public class Member {

    public String Name;
    public String Email;
    public String MemberID;
    public List<String> BorrowedBookList = new ArrayList<String>(); // Holds book IDs

    public Member(String Name, String Email, String MemberID) {
        this.Name = Name;
        this.Email = Email;
        this.MemberID = MemberID;
    }

    public void printMemberInfo(){
        String formatted = String.format("Name: %s, Email: %s, MemberID: %s", Name, Email, MemberID);
        System.out.println(formatted);
    }

    public List<String> getBorrowedBookList(){
        return BorrowedBookList;
    }

    public void addBorrowedBook(String BookID) {
        // if book exists, return false
        if (BorrowedBookList.contains(BookID)) {
            System.out.println("There is already a Book Borrowed!");
            return;
        }
        BorrowedBookList.add(BookID);
    }

    public void removeBorrowedBook(String bookID) {
        BorrowedBookList.remove(bookID);
    }

    public void updateMemberInfo(String Name, String Email, String MemberID) {
        this.Name = Name;
        this.Email = Email;
        this.MemberID = MemberID;
    }

}
