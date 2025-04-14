package CLI.Main;

import CLI.Models.Library;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        CLI cli = new CLI(new InputStreamReader(System.in), new Library());
        cli.run();
    }
}