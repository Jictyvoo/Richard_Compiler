package models.value;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class TokensInformation {

    private static TokensInformation instance = null;

    private TokensInformation() {
    }

    public static TokensInformation getInstance() {
        if (instance == null) {
            instance = new TokensInformation();
        }
        return instance;
    }

    public HashSet<String> reservedWords() {
        return new HashSet<>(Arrays.asList(
                "class", "const", "variables", "method", "return", "main", "if", "then", "else", "while", "read",
                "write", "void", "int", "float", "bool", "string", "true", "false", "extends"
        ));
    }

    public HashSet<String> delimiters() {
        return new HashSet<>(Arrays.asList(";", ",", "(", ")", "[", "]", "{", "}", "."));
    }

    public HashSet<String> relationalOperators() {
        return new HashSet<>(Arrays.asList("!=", "==", "<", "<=", ">", ">=", "="));
    }

    public HashSet<String> logicOperators() {
        return new HashSet<>(Arrays.asList("!", "&&", "||"));
    }

    public HashSet<String> arithmeticOperators() {
        return new HashSet<>(Arrays.asList("+", "-", "*", "/", "++", "--"));
    }

    public HashMap<String, String> commentTokens() {
        HashMap<String, String> tokens = new HashMap<>();
        tokens.put("line", "//");
        tokens.put("beginning", "/*");
        tokens.put("ending", "*/");
        return tokens;
    }

    public HashMap<Character, Character> canTogether() {
        HashMap<Character, Character> hashMap = new HashMap<>();
        hashMap.put('&', '&');
        hashMap.put('|', '|');
        hashMap.put('+', '+');
        hashMap.put('-', '-');
        hashMap.put('=', '=');
        hashMap.put('!', '=');
        hashMap.put('>', '=');
        hashMap.put('<', '=');
        return hashMap;
    }

    public HashSet<Character> split() {
        return new HashSet<>(Arrays.asList('(', ')', '[', ']', '{', ',', '.', ';', ':', '}',
                '?', '*', '+', '-', '=', '\'', '\\', '/', '&', '|', '!', '>', '<'));
    }
}
