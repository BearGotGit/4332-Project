package CLI.Models;

import java.util.*;

public class Librarians {

    private Map<String, String> authCodes = new HashMap<>();
    private Map<String, Float> withdraws = new HashMap<>();
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
            withdraws.put(l, 0.0f);
            purchases.put(l, new ArrayList<>());
        }

//        FIXME: Test
        authCodes.put("A", "111111");
        authCodes.put("B", "222222");
        authCodes.put("C", "333333");
    }

    public Boolean authLibrarian (String name, String authTry) {
        String authActual = authCodes.get(name);
        return authCodes.containsKey(name) && authTry.equals(authActual);
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
