package CLI.Main;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import CLI.Models.*;
import CLI.Main.*;

public class CLITest {
    @Test
    void testSetupWorks() {
//
        Book b = new Book("lala", "bg", 2003, "a", 1, "1");
        assertEquals(b.BookID, "1");
    }
}
