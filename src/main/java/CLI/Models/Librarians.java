package CLI.Models;

import java.util.*;

public class Librarians {

    private Map<String, String> authCodes = new HashMap<>();
    private Map<String, Double> withdraws = new HashMap<>();
    private Map<String, List<Book>> purchases = new HashMap<>();

    public Librarians() {
        List<String> librarians = List.of(
                "A",
                "B",
                "C"
//                "Ube",
//                "Wolfeschlegelsteinhausenbergerdorff",
//                "Ottovordemgentschenfelde"
        );

        for (String l : librarians) {
//            authCodes.put(l, makeAuthCode());
            withdraws.put(l, 0.0);
            purchases.put(l, new ArrayList<>());
        }

//        FIXME: Test
        authCodes.put("A", "111111");
        authCodes.put("B", "222222");
        authCodes.put("C", "333333");
    }

    public enum AuthType {
        NOT_AUTHORIZED, PART_TIME, FULL_TIME
    }

    public AuthType authLibrarian(String username, String authTry) {
        if (!authCodes.containsKey(username)) return AuthType.NOT_AUTHORIZED; // not a librarian

        String authCode = authCodes.get(username);
        if (authCode == null) return AuthType.PART_TIME; // part-time, no auth

        return authCode.equals(authTry) ? AuthType.FULL_TIME : AuthType.NOT_AUTHORIZED; // full-time, check match
    }

    public void hirePartTimeLibrarian(String username, String authTry, String newUsername) {
        if (authLibrarian(username, authTry) != AuthType.FULL_TIME) {
            System.out.println("Failed to authorize");
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
        return withdraws.getOrDefault(username, 0.0);
    }

    public void librarianWithdrewSalary(String username, String authTry, double salary) {
        if (authLibrarian(username, authTry) != AuthType.FULL_TIME) {
            System.out.println("Failed to authorize");
            return;
        }
        double currSalary = withdraws.getOrDefault(username, 0.0);
        withdraws.put(username, currSalary+salary);
    }

//    private String makeAuthCode() {
//        Random rand = new Random();
//        String authCode = null;
//        while (authCode == null) {
//            authCode = rand.ints(6, 0,10).toString();
//            if (authCodes.containsValue(authCode)) {
//                authCode = null;
//            }
//        }
//        return authCode;
//    }
}