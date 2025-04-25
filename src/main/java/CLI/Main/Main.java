package CLI.Main;

import CLI.Helpers.AuthCodeAndSalary;
import CLI.Models.Librarians;
import CLI.Models.Library;
import CLI.Models.LibraryAccounts;
import CLI.Models.Purchasing;

import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        Purchasing purchasing = new Purchasing();
        Map<String, AuthCodeAndSalary> fullTimeLibrarians = Map.of(
                "A", AuthCodeAndSalary.of("111111", 7_500.00),
                "B", AuthCodeAndSalary.of("222222", 10_000.00),
                "C", AuthCodeAndSalary.of("333333", 5_000.00)
        );

        CLI cli = new CLI(
                new InputStreamReader(System.in),
                new PrintStream(System.out),
                new Library(),
                new LibraryAccounts(purchasing),
                new Librarians(fullTimeLibrarians)
        );
        cli.run();
    }
}