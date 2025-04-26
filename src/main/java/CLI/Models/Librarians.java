package CLI.Models;

import CLI.Helpers.AuthCodeAndSalary;

import java.util.*;
import java.util.stream.Collectors;

public class Librarians {

    private Map<String, String> authCodes = new HashMap<>();

    // Package level so tests can see them
    Map<String, Double> withdraws = new HashMap<>();
    Map<String, List<Book>> purchases = new HashMap<>();
    Map<String, Double> salaries = new HashMap<>();

    public Librarians(Map<String, AuthCodeAndSalary> librarians) {
        for (String librarian : librarians.keySet()) {
            withdraws.put(librarian, 0.0);
            purchases.put(librarian, new ArrayList<>());
            salaries.put(librarian, librarians.get(librarian).salary);
        }

        this.authCodes.putAll(
            librarians.entrySet().stream()
                .collect(Collectors.toMap(
                    e -> e.getKey(),
                    e -> e.getValue().authCode
                ))
        );
    }

    public enum AuthType {
        NOT_AUTHORIZED, PART_TIME, FULL_TIME
    }

    public AuthType authLibrarian(String username, String authTry) {
        if (!authCodes.containsKey(username)) return AuthType.NOT_AUTHORIZED; // not a librarian

        String authCode = authCodes.get(username);
        // No code for the librarian (part-time) and no code passed in (part-time)
        if (authCode == null && (authTry == null || authTry.isBlank())) {
            return AuthType.PART_TIME; // part-time, no auth
        }
        else if (authCode == null) {
            // Librarian has no code, but code was passed in
            return AuthType.NOT_AUTHORIZED;
        }

        return authCode.equals(authTry) ? AuthType.FULL_TIME : AuthType.NOT_AUTHORIZED; // full-time, check match
    }

    public void hirePartTimeLibrarian(String username, String authTry, String newUsername) {
        if (authLibrarian(username, authTry) != AuthType.FULL_TIME) {
            System.out.println("Failed to authorize");
            return;
        }
        if (authCodes.containsKey(newUsername)) {
            System.out.println("Librarian already exists!");
            return;
        }
        authCodes.put(newUsername, null); // no auth code for part-time
        System.out.println("New part-time librarian: " + newUsername + " has been hired!");
    }

    public void librarianPurchasedBook(String username, String authTry, Book book) {
        if (authLibrarian(username, authTry) != AuthType.FULL_TIME) {
            System.out.println("Failed to authorize");
            return;
        }
        purchases.computeIfAbsent(username, k -> new ArrayList<>()).add(book);
    }

    public Double getSalary(String username, String authTry) {
        if (authLibrarian(username, authTry) != AuthType.FULL_TIME) {
            System.out.println("Failed to authorize");
            return null;
        }
        return salaries.getOrDefault(username, 0.0);
    }

    // Has to take in salary parameter because the library accounts might not hav the full salary in the bank
    public void librarianWithdrewSalary(String username, String authTry, double salary) {
        if (authLibrarian(username, authTry) != AuthType.FULL_TIME) {
            System.out.println("Failed to authorize");
            return;
        }
        double currSalary = withdraws.getOrDefault(username, 0.0);
        withdraws.put(username, currSalary+salary);
    }
}