package CLI.Helpers;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthCodeAndSalaryTest {

    @Test
    void TestConstructor() {
        String authCode = "1234";
        double salary = 50_000;

        AuthCodeAndSalary authCodeAndSalary = new AuthCodeAndSalary(authCode, salary);

        assertEquals(authCode, authCodeAndSalary.authCode);
        assertEquals(salary, authCodeAndSalary.salary);
    }

    @Test
    void TestOf() {
        String authCode = "1234";
        double salary = 50_000;

        AuthCodeAndSalary authCodeAndSalary = AuthCodeAndSalary.of(authCode, salary);

        assertEquals(authCode, authCodeAndSalary.authCode);
        assertEquals(salary, authCodeAndSalary.salary);
    }
}
