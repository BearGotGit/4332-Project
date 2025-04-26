package CLI.Models;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.assertj.core.api.Assertions.*;

import CLI.Helpers.AuthCodeAndSalary;
import net.jqwik.api.*;
import net.jqwik.api.constraints.*;
import net.jqwik.api.lifecycle.BeforeTry;
import org.junit.jupiter.api.*;

import java.util.*;

public class LibrariansTest {

    Librarians librarians;

    @BeforeEach @BeforeTry
    void setUp() {
        Map<String, AuthCodeAndSalary> fullTimeLibrarians = Map.of(
                "A", AuthCodeAndSalary.of("111111", 7_500.00),
                "B", AuthCodeAndSalary.of("222222", 10_000.00),
                "C", AuthCodeAndSalary.of("333333", 5_000.00)
        );
        librarians = new Librarians(fullTimeLibrarians);
    }

    // Specification tests

    @Test
    void authLibrarianTestShouldBeFullTime() {
        // authLibrarian() should return FULL_TIME

        var authResult = librarians.authLibrarian("A", "111111");

        assertEquals(Librarians.AuthType.FULL_TIME, authResult);
    }

    @Test
    void authLibrarianNullTestShouldBePartTime() {
        // authLibrarian() should return PART_TIME

        librarians.hirePartTimeLibrarian("A", "111111", "X");

        var authResult = librarians.authLibrarian("X", null);

        assertEquals(Librarians.AuthType.PART_TIME, authResult);
    }

    @Test
    void authLibrarianEmptyTestShouldBePartTime() {
        // authLibrarian() should return PART_TIME

        librarians.hirePartTimeLibrarian("A", "111111", "X");

        var authResult = librarians.authLibrarian("X", "");

        assertEquals(Librarians.AuthType.PART_TIME, authResult);
    }

    @Test
    void authLibrarianTestShouldBeNotAuthorized() {
        // authLibrarian() should return NOT_AUTHORIZED

        librarians.hirePartTimeLibrarian("A", "111111", "X");

        var authResult = librarians.authLibrarian("X", "Part-time librarians don't have auth codes");

        assertEquals(Librarians.AuthType.NOT_AUTHORIZED, authResult);
    }

    @Test
    void hirePartTimeLibrarianTestShouldHire() {
        // hirePartTimeLibrarian() should hire them

        librarians.hirePartTimeLibrarian("A", "111111", "X");

        var authResult = librarians.authLibrarian("X", null);

        assertEquals(Librarians.AuthType.PART_TIME, authResult);
    }

    @Test
    void hirePartTimeLibrarianTestShouldFailAlreadyExists() {
        // hirePartTimeLibrarian() should fail because the librarian already exists

        librarians.hirePartTimeLibrarian("A", "111111", "B");

        var authResult = librarians.authLibrarian("B", null);

        // They already exist as a full-time librarian, so it shouldn't be finding a part-time librarian
            // It should return NOT_AUTHORIZED because the auth code was not provided
        assertEquals(Librarians.AuthType.NOT_AUTHORIZED, authResult);
    }

    @Test
    void librarianPurchasesBookTestShouldSucceed() {
        // librarianPurchasedBook() should add the book to purchases

        Book book = mock(Book.class);
        librarians.librarianPurchasedBook("A", "111111", book);

        // Check if book exists and if its the book we just purchased
        assertTrue(librarians.purchases.get("A").stream().findFirst().isPresent());
        assertEquals(book, librarians.purchases.get("A").stream().findFirst().get());
    }

    @Property
    void librarianPurchasesBookTestShouldCountMultiple(@ForAll("bookGenerator") List<Book> books) {
        // librarianPurchasedBook() should add all the books to purchases

        for (Book book : books) {
            librarians.librarianPurchasedBook("A", "111111", book);
        }

        assertThat(books.toArray()).containsExactlyInAnyOrder(librarians.purchases.get("A").toArray());
    }

    @Test
    void getSalaryTestShouldSucceed() {
        // getSalary() should return the correct salary

        Double expectedSalary = 50_0000.00;
        Map<String, AuthCodeAndSalary> fullTimeLibrarians = Map.of(
                "A", AuthCodeAndSalary.of("111111", expectedSalary)
        );
        librarians = new Librarians(fullTimeLibrarians);

        Double salary = librarians.getSalary("A", "111111");

        assertEquals(expectedSalary, salary);
    }

    @Test
    void librarianWithdrewSalaryShouldSucceed() {
        // librarianWithdrewSalary() should withdraw the expected salary

        double expectedSalary = 5_000.00;
        librarians.librarianWithdrewSalary("A", "111111", expectedSalary);

        assertEquals(expectedSalary, librarians.withdraws.get("A"));
    }

    @Property
    void librarianWithdrewSalaryShouldCountMultiple(@ForAll @Size(max = 20)List<@DoubleRange(min = 0.0, max = 100_000.0) Double> salaries) {
        // librarianWithdrewSalary() should count the total amount withdrawn

        for (var salary : salaries) {
            librarians.librarianWithdrewSalary("A", "111111", salary);
        }

        // Round them
        double expected = Math.round(salaries.stream().mapToDouble(d -> d).sum() * 100.0) / 100.0;
        double actual = Math.round(librarians.withdraws.get("A") * 100.0) / 100.0;

        assertEquals(expected, actual);
    }

    @Test
    void librarianWithdrewSalaryShouldIncrease() {
        // librarianWithdrewSalary() should count the total amount withdrawn

        double salary1 = 5_000.00;
        double salary2 = 8_000.00;
        librarians.librarianWithdrewSalary("A", "111111", salary1);
        librarians.librarianWithdrewSalary("A", "111111", salary2);

        assertEquals(salary1 + salary2, librarians.withdraws.get("A"));
    }

    // Structural tests
    @Test
    void authLibrarianNullTestShouldBeNotAuthorized() {
        // authLibrarian() should return NOT_AUTHORIZED

        var authResult = librarians.authLibrarian("X", "123456");

        assertEquals(Librarians.AuthType.NOT_AUTHORIZED, authResult);
    }

    @Test
    void hirePartTimeLibrarianTestShouldFailAuth() {
        // hirePartTimeLibrarian() should fail on authorization

        librarians.hirePartTimeLibrarian("X", "123456", "B");

        var authResult = librarians.authLibrarian("B", null);

        assertEquals(Librarians.AuthType.NOT_AUTHORIZED, authResult);
    }

    @Test
    void librarianPurchasesBookTestShouldFailAuth() {
        // librarianPurchasedBook() should fail authentication

        Book book = mock(Book.class);
        librarians.librarianPurchasedBook("X", "12345", book);

        // Librarian doesn't exist, so it's purchases are null
        assertNull(librarians.purchases.get("X"));
    }

    @Test
    void getSalaryTestShouldFailAuth() {
        // getSalary() should fail auth

        Double salary = librarians.getSalary("X", "123456");

        assertNull(salary);
    }

    @Test
    void librarianWithdrewSalaryShouldFailAuth() {
        // librarianWithdrewSalary() should fail auth

        librarians.librarianWithdrewSalary("X", "123456", 5_000.00);

        // Librarian doesn't exist, so it's withdraws are null
        assertNull(librarians.withdraws.get("X"));
    }


    // Generates a random list of 20 books
    @Provide
    Arbitrary<List<Book>> bookGenerator() {
        Arbitrary<String> name = Arbitraries.strings().withCharRange('A', 'z').ofMinLength(5).ofMaxLength(20);
        Arbitrary<String> author = Arbitraries.strings().withCharRange('A', 'z').ofMinLength(5).ofMaxLength(20);
        Arbitrary<Integer> year = Arbitraries.integers().between(0, 2025);
        Arbitrary<String> genre = Arbitraries.strings().withCharRange('A', 'z').ofMinLength(5).ofMaxLength(20);
        Arbitrary<Integer> isbn = Arbitraries.integers().between(0, 99999);
        Arbitrary<String> bookId = Arbitraries.strings().withCharRange('A', 'Z').ofMinLength(5).ofMaxLength(20);

        Arbitrary<Book> book = Combinators.combine(name, author, year, genre, isbn, bookId).as(Book::new);
        return book.list().ofSize(20);
    }
}