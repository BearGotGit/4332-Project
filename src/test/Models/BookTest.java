package Models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BookTest {
    @Test
    void testSetupWorks() {

        Book b = new Book("lala", "bg", 2003, "a", 1, "1");
        assertEquals(b.BookID, "1");
    }
}
