package models.value;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class TokensInformation {
    public HashMap<String, String> reservedWords() {
        return null;
    }

    public HashMap<String, String> commentTokens() {
        HashMap<String, String> tokens = new HashMap<>();
        tokens.put("line", "//");
        tokens.put("beginning", "/*");
        tokens.put("ending", "*/");
        return tokens;
    }

    public HashSet<String> canTogether() {
        return new HashSet<>(Arrays.asList("(", ")", "[", "]", "{", ",", ";", ":", "}", "?", "*", "+", "-", "'"));
    }
}
