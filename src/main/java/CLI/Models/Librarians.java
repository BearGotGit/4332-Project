package CLI.Models;

import java.util.*;

public class Librarians {

    private Map<String, Integer> authCodes = new HashMap<>();
    private Map<String, Float> withdraws = new HashMap<>();
    private Map<String, List<Book>> purchases = new HashMap<>();

    public Librarians() {
        List<String> librarians = List.of(
                "Ube",
                "Wolfeschlegelsteinhausenbergerdorff",
                "Ottovordemgentschenfelde"
        );

        for (String l : librarians) {
            authCodes.put(l, 0);
            withdraws.put(l, 0.0f);
            purchases.put(l, new ArrayList<>());
        }
    }
}
