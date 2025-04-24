package CLI.Models;

import java.util.Random;

public class Purchasing {

    // Uses a hash function to consistently return the same price for each book ID
    public double getBookPrice(String bookID) {
        Random rand = new Random(bookID.hashCode()); // Hash is deterministic

        // Min is 10, so start with 10, then add 0-90$

        // 90$ is 9000 cents
        int cents = rand.nextInt(9000);

        // Adds 10$ then divides by 100 to convert to $
        double price = 10 + cents / 100.0;

        // Round to 2 decimal places
        return Math.round(price * 100.0) / 100.0; // between 10 and 100
    }
}
