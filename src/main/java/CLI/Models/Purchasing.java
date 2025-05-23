package CLI.Models;

import java.util.Random;

public class Purchasing {

    // Uses a hash function to consistently return the same price for each book ($10-100)
    public double getBookPrice(String bookName, String author, int year, String genre, int ISBN) {
        String str = bookName + author + year + genre + ISBN;
        Random rand = new Random(str.hashCode()); // Hash is deterministic

        // Min is 10, so start with 10, then add 0-90$

        // 90$ is 9000 cents (p.s. upperbound is exclusive)
        int cents = rand.nextInt(0, 9_000 + 1);

        // Adds 10$ then divides by 100 to convert to $
        double price = 10 + cents / 100.0;

        // Round to 2 decimal places
        return Math.round(price * 100.0) / 100.0; // between 10 and 100
    }

}
