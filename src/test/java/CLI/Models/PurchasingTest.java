package CLI.Models;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PurchasingTest {
    @Test
    public void testGetBookPriceTheSame() {
        Purchasing purchasing = new Purchasing();

        // Making sure the same book details give the same price each time
        double exampleBookPrice1 = purchasing.getBookPrice("The Great Gatsby", "F. Scott Fitzgerald", 1925, "Fiction", 12345);
        double exampleBookPrice2 = purchasing.getBookPrice("The Great Gatsby", "F. Scott Fitzgerald", 1925, "Fiction", 12345);

        assertEquals(exampleBookPrice1, exampleBookPrice2, "The same book parameters should return the consistent price");
    }

    @Test
    public void testGetBookPriceBounds() {
        Purchasing purchasing = new Purchasing();

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
        Purchasing purchasing = new Purchasing();

        double exampleBookPrice = purchasing.getBookPrice("The Hobbit", "J.R.R. Tolkien", 1937, "Fantasy", 45678);

        // Checking if we're getting proper prices with cents (2 decimal places)
        double roundedPrice = Math.round(exampleBookPrice * 100.0) / 100.0;
        assertEquals(exampleBookPrice, roundedPrice, "Price should be rounded to 2 decimal places");
    }

    @Test
    public void testGetBookPriceDifferentBooks() {
        Purchasing purchasing = new Purchasing();

        // Different books with different prices
        double exampleBookPrice1 = purchasing.getBookPrice("The Great Gatsby", "F. Scott Fitzgerald", 1925, "Fiction", 12345);
        double exampleBookPrice2 = purchasing.getBookPrice("To Kill a Mockingbird", "Harper Lee", 1960, "Fiction", 23456);

        // The price of book 1 and 2 aren't going to be the same
        assertEquals(true, exampleBookPrice1 != exampleBookPrice2, "Different books should typically have different prices");
    }
}
