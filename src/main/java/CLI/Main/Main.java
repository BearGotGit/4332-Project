package CLI.Main;

import CLI.Models.Librarians;
import CLI.Models.Library;
import CLI.Models.LibraryAccounts;
import CLI.Models.Purchasing;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Purchasing purchasing = new Purchasing();
        CLI cli = new CLI(new InputStreamReader(System.in), new Library(), new LibraryAccounts(purchasing), new Librarians());
        cli.run();
    }
}