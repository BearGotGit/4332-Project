package CLI.Models;

import static org.junit.jupiter.api.Assertions.*;

import net.jqwik.api.*;
import net.jqwik.api.lifecycle.BeforeTry;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PurchasingTest {

    Purchasing purchasing;

    @BeforeEach @BeforeTry
    void setUp() {
        purchasing = new Purchasing();
    }

    // Specification Tests
    @Test
    public void testGetBookPriceTheSame() {
        // Making sure the same book details give the same price each time
        double exampleBookPrice1 = purchasing.getBookPrice("The Great Gatsby", "F. Scott Fitzgerald", 1925, "Fiction", 12345);
        double exampleBookPrice2 = purchasing.getBookPrice("The Great Gatsby", "F. Scott Fitzgerald", 1925, "Fiction", 12345);

        assertEquals(exampleBookPrice1, exampleBookPrice2, "The same book parameters should return the consistent price");
    }

    @Test
    public void testGetBookPriceBounds() {
        // Let's try a few different books to check the price limits
        double exampleBookPrice1 = purchasing.getBookPrice("The Great Gatsby", "F. Scott Fitzgerald", 1925, "Fiction", 12345);
        double exampleBookPrice2 = purchasing.getBookPrice("To Kill a Mockingbird", "Harper Lee", 1960, "Fiction", 23456);
        double exampleBookPrice3 = purchasing.getBookPrice("1984", "George Orwell", 1949, "Science Fiction", 34567);

        // Books shouldn't be cheaper than $10
        assertEquals(true, exampleBookPrice1 >= 10.0, "Book price should be at least $10");
        assertEquals(true, exampleBookPrice2 >= 10.0, "Book price should be at least $10");
        assertEquals(true, exampleBookPrice3 >= 10.0, "Book price should be at least $10");

        // Shouldn't cost more than $100
        assertEquals(true, exampleBookPrice1 <= 100.0, "Book price should be at most $100");
        assertEquals(true, exampleBookPrice2 <= 100.0, "Book price should be at most $100");
        assertEquals(true, exampleBookPrice3 <= 100.0, "Book price should be at most $100");
    }

    @Test
    public void testGetBookPriceDecimalPlaces() {
        double exampleBookPrice = purchasing.getBookPrice("The Hobbit", "J.R.R. Tolkien", 1937, "Fantasy", 45678);

        // Checking if we're getting proper prices with cents (2 decimal places)
        double roundedPrice = Math.round(exampleBookPrice * 100.0) / 100.0;
        assertEquals(exampleBookPrice, roundedPrice, "Price should be rounded to 2 decimal places");
    }

    // Property Tests

    @Property
    public void testGetBookPriceTheSame(@ForAll("bookGenerator") List<Book> books) {

        // Deep copy
        List<Book> books2 = books.stream().map(b -> new Book(b.Name, b.Author, b.Year, b.Genre, b.ISBN, b.BookID)).toList();

        // Making sure the same book details give the same price each time
        List<Double> prices = books.stream().map(b -> purchasing.getBookPrice(b.Name, b.Author, b.Year, b.Genre, b.ISBN)).toList();
        List<Double> prices2 = books2.stream().map(b -> purchasing.getBookPrice(b.Name, b.Author, b.Year, b.Genre, b.ISBN)).toList();

        assertEquals(prices.toString(), prices2.toString(), "The same book parameters should return the consistent price");
    }

    @Property
    public void testGetBookPriceBounds(@ForAll("bookGenerator") List<Book> books) {

        List<Double> prices = books.stream().map(b -> purchasing.getBookPrice(b.Name, b.Author, b.Year, b.Genre, b.ISBN)).toList();

        prices.forEach(price -> {
            // Books shouldn't be cheaper than $10
            assertTrue(price >= 10.0, "Book price should be at least $10");

            // Shouldn't cost more than $100
            assertTrue(price <= 100.0, "Book price should be at most $100");
        });
    }

    @Property
    public void testGetBookPriceDecimalPlaces(@ForAll("bookGenerator") List<Book> books) {

        List<Double> prices = books.stream().map(b -> purchasing.getBookPrice(b.Name, b.Author, b.Year, b.Genre, b.ISBN)).toList();

        // Checking if we're getting proper prices with cents (2 decimal places)
        prices.forEach(price -> {
            double roundedPrice = Math.round(price * 100.0) / 100.0;
            assertEquals(price, roundedPrice, "Price should be rounded to 2 decimal places");
        });
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
