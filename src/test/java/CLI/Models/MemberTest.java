package CLI.Models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import net.jqwik.api.*;
import net.jqwik.api.constraints.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

import CLI.Models.*;

import java.util.ArrayList;

public class MemberTest {

    private Member member;
    private final String name = "nameTest";
    private final String email = "emailTest";
    private final String ID  = "1";

    @BeforeEach
    void setUp() {
        member = new Member(name, email, ID);
    }

    // Structural testing ran coverage with JaCoCo (100% on Member class)

    // Specification testing
    @Test
    void printMemberInfoTest() {
        // The printMemberInfo function should return the correct info in the correct format.

        // Arrange
        String expectedMemberInfo = String.format("Name: %s, Email: %s, MemberID: %s",
                member.Name, member.Email, member.MemberID);

        // Act
        member.printMemberInfo();

        // Assert
        assertEquals(expectedMemberInfo, member.testableMemberInfo);
    }

    @Test
    void updateMemberInfoTest() {
        // The updateMemberInfo function should correctly update the info of the member.

        // Arrange
        String expectedName = "expectedName";
        String expectedEmail = "expectedEmail";

        // Act
        member.updateMemberInfo(expectedName, expectedEmail);

        // Assert
        assertEquals(expectedName, member.Name);
        assertEquals(expectedEmail, member.Email);
    }


    // Property testing
    @Property
    void getBorrowedBookListTest(@ForAll @Size(min = 0, max = 20) List<@StringLength(max = 10) String> expectedBorrowedBookList) {
        // For any list of borrowed books, getBorrowedBookList() should return the expected list

        // Arrange
        Member member = new Member(name, email, ID);
        member.BorrowedBookList = new ArrayList<>(expectedBorrowedBookList);

        // Act
        List<String> borrowedBookList = member.getBorrowedBookList();

        // Assert
        assertEquals(expectedBorrowedBookList.size(), borrowedBookList.size());
        assertEquals(expectedBorrowedBookList.toString(), borrowedBookList.toString());
    }

    @Property
    void addBorrowedBookTestWhenBookDoesntExist(@ForAll @Size(min = 0, max = 20) List<@StringLength(max = 10) String> bookIDs,
                                                @ForAll @StringLength(max = 10) String newBookID) {
        Assume.that(!bookIDs.contains(newBookID)); // ignore case when it's already there

        // addBorrowedBook() should add the bookID because it doesn't contain it yet.

        // Arrange
        Member member = new Member(name, email, ID);
        member.BorrowedBookList = new ArrayList<>(bookIDs); // Does NOT include bookID
        String bookID = "testBookID";
        List<String> expectedBorrowedBookList = new ArrayList<>(bookIDs);
        expectedBorrowedBookList.add(bookID);

        // Act
        member.addBorrowedBook(bookID);

        // Assert
        assertEquals(expectedBorrowedBookList.size(), member.BorrowedBookList.size());
        assertEquals(expectedBorrowedBookList.toString(), member.BorrowedBookList.toString());
    }

    @Property
    void addBorrowedBookTestWhenBookExists(@ForAll @Size(min = 0, max = 20) List<@StringLength(max = 10) String> bookIDs,
                                           @ForAll @StringLength(max = 10) String newBookID) {
        Assume.that(!bookIDs.contains(newBookID)); // ignore case when it's already there

        // addBorrowedBook() should NOT add the duplicate bookID because it is already in the list.

        // Arrange
        Member member = new Member(name, email, ID);
        String bookID = "testBookID";
        List<String> expectedBorrowedBookList = new ArrayList<>(bookIDs);
        expectedBorrowedBookList.add(bookID);
        member.BorrowedBookList = new ArrayList<>(expectedBorrowedBookList); // Includes bookID

        // Act
        member.addBorrowedBook(bookID);

        // Assert
        assertEquals(expectedBorrowedBookList.size(), member.BorrowedBookList.size());
        assertEquals(expectedBorrowedBookList.toString(), member.BorrowedBookList.toString());
    }

    @Property
    void removeBorrowedBookTestWhenBookExists(@ForAll @Size(min = 0, max = 20) List<@StringLength(max = 10) String> bookIDs,
                                              @ForAll @StringLength(max = 10) String newBookID) {
        Assume.that(!bookIDs.contains(newBookID)); // ignore case when it's already there

        // removeBorrowedBook() should remove the bookID because it exists in the list.

        // Arrange
        Member member = new Member(name, email, ID);
        String bookID = "testBookID";
        member.BorrowedBookList = new ArrayList<>(bookIDs);
        member.BorrowedBookList.add(bookID); // Now has the bookID

        // Act
        member.removeBorrowedBook(bookID);

        // Assert
        assertEquals(bookIDs.size(), member.BorrowedBookList.size());
        assertEquals(bookIDs.toString(), member.BorrowedBookList.toString());
    }

    @Property
    void removeBorrowedBookTestWhenBookDoesntExist(@ForAll @Size(min = 0, max = 20) List<@StringLength(max = 10) String> bookIDs,
                                                   @ForAll @StringLength(max = 10) String newBookID) {
        Assume.that(!bookIDs.contains(newBookID)); // ignore case when it's already there

        // removeBorrowedBook() should do nothing because the bookID is not in the list.

        // Arrange
        Member member = new Member(name, email, ID);
        String bookID = "testBookID";
        member.BorrowedBookList = new ArrayList<>(bookIDs);

        // Act
        member.removeBorrowedBook(bookID);

        // Assert
        assertEquals(bookIDs.size(), member.BorrowedBookList.size());
        assertEquals(bookIDs.toString(), member.BorrowedBookList.toString());
    }
}
