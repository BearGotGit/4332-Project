package CLI.Main;

import net.jqwik.api.*;
import net.jqwik.api.constraints.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import CLI.Models.*;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CLITest {
    Library mockedLibrary;
    CLI cli;

    @BeforeEach
    public void setup() {
        mockedLibrary = mock(Library.class);
    }

    @Test
    void testAddBook() {
//       Qualities of book
        String bookName = "My Book";
        String author = "Jane Doe";
        int year = 2024;
        String genre = "Fiction";
        int isbn = 123456;

        // Fake user input: 1 (add book), then book fields, then anything to continue
        String userInput = String.join("\n",
                "1",              // Select option 1 (Add Book)
                bookName,               // Book name
                author,                 // Author
                String.valueOf(year),           // Year
                genre,                  // Genre
                String.valueOf(isbn),         // ISBN
                "",                     // (Empty input to get to menu)
                "10"                    // Exit (after adding book, immediately exit)
        );

        // Create CLI with fake input
        cli = new CLI(new StringReader(userInput), mockedLibrary);

        // Mock that library.addBook() returns a dummy Book
        Book fakeBook = mock(Book.class);
        when(mockedLibrary.addBook(anyString(), anyString(), anyInt(), anyString(), anyInt())).thenReturn(fakeBook);

        // Run
        cli.run();

        // Verify that addBook was called correctly
        verify(mockedLibrary).addBook(
                bookName,
                author,
                year,
                genre,
                isbn
        );
    }

    // Specification tests

    @Test
    void addMemberTestShouldSucceed() {
        // Chooses Add Member and submits valid input

        // Arrange
        String name = "testName";
        String email = "testEmail";

        // Fake user input: 7 (add member), then member fields, then anything to continue
        String userInput = String.join("\n",
                "7",              // Select option 7 (Add Member)
                name,
                email,
                "",                     // (Empty input to get to menu)
                "10"                    // Exit (after adding book, immediately exit)
        );

        // Create CLI with fake input
        cli = new CLI(new StringReader(userInput), mockedLibrary);

        // Stub that library.addMember() returns a dummy Member
        Member mockedMember = mock(Member.class);
        when(mockedLibrary.addMember(anyString(), anyString())).thenReturn(mockedMember);

        // Act
        cli.run();

        // Assert that addMember was called correctly and printMemberInfo was called
        verify(mockedLibrary).addMember(name, email);
        verify(mockedMember).printMemberInfo();
    }

    @Test
    void revokeMembershipTestShouldSucceed() {
        // Chooses Revoke Membership and submits an existing member to revoke

        // Arrange
        String memberID = "testMemberID";

        // Fake user input: 8 (revoke membership), then memberID, then anything to continue
        String userInput = String.join("\n",
                "8",
                memberID,
                "",                     // (Empty input to get to menu)
                "10"                    // Exit (after adding book, immediately exit)
        );

        // Create CLI with fake input
        cli = new CLI(new StringReader(userInput), mockedLibrary);

        // Stub that library.addMember() returns true
        when(mockedLibrary.revokeMembership(anyString())).thenReturn(true);

        // Act
        cli.run();

        // Assert that addMember was called correctly and printMemberInfo was called
        verify(mockedLibrary).revokeMembership(memberID);
    }

    @Property
    void viewAllMembersTestShouldSucceed(@ForAll @IntRange(min = 1, max = 20) int size) {
        // Chooses View All Members, members exist, should return all members

        // Arrange
        List<Member> members = IntStream.range(0, size)
                .mapToObj(i -> mock(Member.class))
                .toList();

        // Fake user input: 9 (View All Members), then anything to continue
        String userInput = String.join("\n",
                "9",
                "",                     // (Empty input to get to menu)
                "10"                    // Exit (after adding book, immediately exit)
        );

        // Create CLI with fake input
        Library mockedLibrary = mock(Library.class); // jqwik cannot use @BeforeEach from junit
        cli = new CLI(new StringReader(userInput), mockedLibrary);

        // Stub that library.getAllMembers() returns my list of members
        when(mockedLibrary.getAllMembers()).thenReturn(members);

        // Act
        cli.run();

        // Assert that each member correctly called printMemberInfo
        members.forEach(m -> verify(m).printMemberInfo());
    }

    // Structural test

    @Test
    void addMemberTestEmptyInputShouldFail() {
        // Chooses Add Member and submits empty input

        // Arrange
        // Fake user input: 7 (add member), then empty member fields, then anything to continue
        String userInput = String.join("\n",
                "7",              // Select option 7 (Add Member)
                "",
                "",
                "",                     // (Empty input to get to menu)
                "10"                    // Exit (after adding book, immediately exit)
        );

        // Create CLI with fake input
        cli = new CLI(new StringReader(userInput), mockedLibrary);

        // Act
        cli.run();

        // Assert that addMember() was never called
        verify(mockedLibrary, never()).addMember(anyString(), anyString());
    }

    @Test
    void revokeMembershipTestShouldFail() {
        // Chooses Revoke Membership and submits an empty member ID

        // Arrange
        // Fake user input: 8 (revoke membership), then memberID, then anything to continue
        String userInput = String.join("\n",
                "8",
                "",
                "",                     // (Empty input to get to menu)
                "10"                    // Exit (after adding book, immediately exit)
        );

        // Create CLI with fake input
        cli = new CLI(new StringReader(userInput), mockedLibrary);

        // Act
        cli.run();

        // Assert that addMember was called correctly and printMemberInfo was called
        verify(mockedLibrary, never()).revokeMembership(anyString());
    }
}