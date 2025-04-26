package CLI.Models;

import net.jqwik.api.lifecycle.*;
import org.junit.jupiter.api.*;
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

    @BeforeEach @BeforeTry
    void setUp() {
        member = new Member(name, email, ID);
    }

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

    @Test
    void addRemoveAndGetBooksTest() {
        // The member should be able to borrow a book and then return it.

        // Arrange
        String bookID = "book123";
        List<String> expectedBorrowedBookList = new ArrayList<>();

        // Act
        member.addBorrowedBook(bookID);
        member.removeBorrowedBook(bookID);
        List<String> borrowedBookList = member.getBorrowedBookList();

        // Assert
        assertEquals(expectedBorrowedBookList.size(), borrowedBookList.size());
        assertEquals(expectedBorrowedBookList.toString(), borrowedBookList.toString());
    }

    // Property testing
    @Property
    void getBorrowedBookListTest(@ForAll @Size(min = 0, max = 20) List<@StringLength(min = 1, max = 10) String> bookIDs) {
        // For any list of borrowed books, getBorrowedBookList() should return the expected list

        // Arrange
        member.BorrowedBookList = new ArrayList<>(bookIDs);
        List<String> expectedBorrowedBookList = new ArrayList<>(bookIDs);

        // Act
        List<String> borrowedBookList = member.getBorrowedBookList();

        // Assert
        assertEquals(expectedBorrowedBookList.size(), borrowedBookList.size());
        assertEquals(expectedBorrowedBookList.toString(), borrowedBookList.toString());
    }

    @Property
    void addBorrowedBookTestWhenBookDoesntExist(@ForAll @Size(min = 0, max = 20) List<@AlphaChars @NumericChars @StringLength(min = 1, max = 10) String> bookIDs,
                                                @ForAll @AlphaChars @NumericChars @StringLength(min = 1, max = 10) String newBookID) {
        Assume.that(!bookIDs.contains(newBookID)); // ignore case when it's already there

        // addBorrowedBook() should add the bookID because it doesn't contain it yet.

        // Arrange
        member.BorrowedBookList = new ArrayList<>(bookIDs); // Does NOT include bookID
        List<String> expectedBorrowedBookList = new ArrayList<>(bookIDs);
        expectedBorrowedBookList.add(newBookID);

        // Act
        member.addBorrowedBook(newBookID);

        // Assert
        assertEquals(expectedBorrowedBookList.size(), member.BorrowedBookList.size());
        assertEquals(expectedBorrowedBookList.toString(), member.BorrowedBookList.toString());
    }

    @Property
    void addBorrowedBookTestWhenBookExists(@ForAll @Size(min = 0, max = 20) List<@StringLength(min = 1, max = 10) String> bookIDs,
                                           @ForAll @StringLength(min = 1, max = 10) String newBookID) {
        Assume.that(!bookIDs.contains(newBookID)); // ignore case when it's already there

        // addBorrowedBook() should NOT add the duplicate bookID because it is already in the list.

        // Arrange
        List<String> expectedBorrowedBookList = new ArrayList<>(bookIDs);
        expectedBorrowedBookList.add(newBookID);
        member.BorrowedBookList = new ArrayList<>(expectedBorrowedBookList); // Includes bookID

        // Act
        member.addBorrowedBook(newBookID);

        // Assert
        assertEquals(expectedBorrowedBookList.size(), member.BorrowedBookList.size());
        assertEquals(expectedBorrowedBookList.toString(), member.BorrowedBookList.toString());
    }

    @Property
    void removeBorrowedBookTestWhenBookExists(@ForAll @Size(min = 0, max = 20) List<@AlphaChars @NumericChars @StringLength(min = 1, max = 10) String> bookIDs,
                                              @ForAll @AlphaChars @NumericChars @StringLength(min = 1, max = 10) String newBookID) {
        Assume.that(!bookIDs.contains(newBookID)); // ignore case when it's already there

        // removeBorrowedBook() should remove the bookID because it exists in the list.

        // Arrange
        member.BorrowedBookList = new ArrayList<>(bookIDs);
        member.BorrowedBookList.add(newBookID); // Now has the bookID

        // Act
        member.removeBorrowedBook(newBookID);

        // Assert
        assertEquals(bookIDs.size(), member.BorrowedBookList.size());
        assertEquals(bookIDs.toString(), member.BorrowedBookList.toString());
    }

    @Property
    void removeBorrowedBookTestWhenBookDoesntExist(@ForAll @Size(min = 0, max = 20) List<@StringLength(min = 1, max = 10) String> bookIDs,
                                                   @ForAll @StringLength(min = 1, max = 10) String newBookID) {
        Assume.that(!bookIDs.contains(newBookID)); // ignore case when it's already there

        // removeBorrowedBook() should do nothing because the bookID is not in the list.

        // Arrange
        member.BorrowedBookList = new ArrayList<>(bookIDs);

        // Act
        member.removeBorrowedBook(newBookID);

        // Assert
        assertEquals(bookIDs.size(), member.BorrowedBookList.size());
        assertEquals(bookIDs.toString(), member.BorrowedBookList.toString());
    }

    // Structural testing

    @Test
    void addNullBookIDTestDoesNothing() {
        // addBorrowedBook() should do nothing if the bookID is null.

        // Arrange
        // member created in beforeEach

        // Act
        member.addBorrowedBook(null);

        // Assert
        assertEquals(true,  member.getBorrowedBookList().isEmpty());
    }

    @Test
    void addEmptyBookIDTestDoesNothing() {
        // addBorrowedBook() should do nothing if the bookID is empty.

        // Arrange
        String empty = "";

        // Act
        member.addBorrowedBook(empty);

        // Assert
        assertEquals(true,  member.getBorrowedBookList().isEmpty());
    }

    @Test
    void removeNullBookIDTestDoesNothing() {
        // removeBorrowedBook() should do nothing if the bookID is null.

        // Arrange
        member.BorrowedBookList = List.of("book");

        // Act
        member.removeBorrowedBook(null);

        // Assert
        assertEquals(1, member.getBorrowedBookList().size());
    }

    @Test
    void removeEmptyBookIDTestDoesNothing() {
        // removeBorrowedBook() should do nothing if the bookID is empty.

        // Arrange
        member.BorrowedBookList = List.of("book");
        String empty = "";

        // Act
        member.removeBorrowedBook(empty);

        // Assert
        assertEquals(1, member.getBorrowedBookList().size());
    }

    @Test
    void updateMemberInfoWithNullNameTestDoesNothing() {
        // updateMemberInfo() should not update if the name is null.

        // Arrange
        // member created in beforeEach

        // Act
        member.updateMemberInfo(null, "new@email.com");

        // Assert
        assertEquals(name, member.Name);
        assertEquals(email, member.Email);
    }

    @Test
    void updateMemberInfoWithNullEmailTestDoesNothing() {
        // updateMemberInfo() should not update if the email is null.

        // Arrange
        // member created in beforeEach

        // Act
        member.updateMemberInfo("newName", null);

        // Assert
        assertEquals(name, member.Name);
        assertEquals(email, member.Email);
    }

    @Test
    void updateMemberInfoWithEmptyNameTestDoesNothing() {
        // updateMemberInfo() should not update if the name is empty.

        // Arrange
        String empty = "";

        // Act
        member.updateMemberInfo(empty, "new@email.com");

        // Assert
        assertEquals(name, member.Name);
        assertEquals(email, member.Email);
    }

    @Test
    void updateMemberInfoWithEmptyEmailTestDoesNothing() {
        // updateMemberInfo() should not update if the email is empty.

        // Arrange
        String empty = "";

        // Act
        member.updateMemberInfo("newName", empty);

        // Assert
        assertEquals(name, member.Name);
        assertEquals(email, member.Email);
    }

    @Test
    void removeBookFromEmptyListTestDoesNothing() {
        // updateMemberInfo() should do nothing when removing a book from an empty list.

        // Arrange
        // member created in beforeEach

        // Act
        member.removeBorrowedBook("nonexistent");

        // Assert
        assertEquals(true,  member.getBorrowedBookList().isEmpty());
    }

    @Test
    void createMemberWithEmptyFieldsTest() {
        // The constructor should reject empty fields.

        // Arrange
        String empty = "";

        // Act
        Member m = new Member(empty, empty, empty);

        // Assert
        assertEquals(null, m.Name);
        assertEquals(null, m.Email);
        assertEquals(null, m.MemberID);
    }

    @Test
    void createMemberWithNullFieldsTest() {
        // The constructor should reject null fields.

        // Arrange
        // member created in beforeEach

        // Act
        Member m = new Member(null, null, null);

        // Assert
        assertEquals(null, m.Name);
        assertEquals(null, m.Email);
        assertEquals(null, m.MemberID);
    }

    @Test
    void createMemberWithNullEmailTest() {
        // The constructor should reject a null email.

        // Arrange
        // member created in beforeEach

        // Act
        Member m = new Member("validName", null, "validID");

        // Assert
        assertEquals(null, m.Name);
        assertEquals(null, m.Email);
        assertEquals(null, m.MemberID);
    }

    @Test
    void createMemberWithEmptyMemberIDTest() {
        // The constructor should reject an empty ID.

        // Arrange
        String empty = "";

        // Act
        Member m = new Member("validName", "valid@email.com", empty);

        // Assert
        assertEquals(null, m.Name);
        assertEquals(null, m.Email);
        assertEquals(null, m.MemberID);
    }

    @Test
    void createMemberWithNullMemberIDTest() {
        // The constructor should reject a null ID.

        // Arrange
        // member created in beforeEach

        // Act
        Member m = new Member("validName", "valid@email.com", null);

        // Assert
        assertEquals(null, m.Name);
        assertEquals(null, m.Email);
        assertEquals(null, m.MemberID);
    }
}
