package CLI.Models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import CLI.Models.*;

public class MemberTest {

//    private Library library;
//
//    MemberTest(Library _library) {
//        library = _library;
//    }

    @Test
    void testSetupWorks() {

//        Book b = new Book("lala", "bg", 2003, "a", 1, "1");
//        assertEquals(b.BookID, "1");
    }

    @Test
    void printMemberInfoTest() {
        // Arrange
        Library library = new Library();
        String name = "nameTest";
        String email = "emailTest";
        Member newMember = library.addMember(name, email);
        String expectedMemberInfo = String.format("Name: %s, Email: %s, MemberID: %s",
                name, email, newMember.MemberID);

        // Act
        newMember.printMemberInfo();

        // Assert
        assertEquals(expectedMemberInfo, newMember.testableMemberInfo);
    }
}
