package CLI.Models;

import java.util.*;

public class Librarians {

    private Map<String, String> authCodes = new HashMap<>();
    private Map<String, Float> withdraws = new HashMap<>();
    private Map<String, List<Book>> purchases = new HashMap<>();

    public Librarians() {
        List<String> librarians = List.of(
                "Ube",
                "Wolfeschlegelsteinhausenbergerdorff",
                "Ottovordemgentschenfelde"
        );

        for (String l : librarians) {
            authCodes.put(l, makeAuthCode());
            withdraws.put(l, 0.0f);
            purchases.put(l, new ArrayList<>());
        }
    }

    private String makeAuthCode() {
        Random rand = new Random();
        String authCode = null;
        while (authCode == null) {
            authCode = rand.ints(6, 0,10).toString();
            if (authCodes.containsValue(authCode)) {
                authCode = null;
            }
        }
        return authCode;
    }
}
