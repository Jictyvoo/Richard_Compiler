package util;

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

    private HashSet<String> reservedWords() {
        return new HashSet<>(Arrays.asList(
                "class", "const", "variables", "method", "return", "main", "if", "then", "else", "while", "read",
                "write", "void", "int", "float", "bool", "string", "true", "false", "extends"
        ));
    }

    public HashSet<String> delimiters() {
        return new HashSet<>(Arrays.asList(";", ",", "(", ")", "[", "]", "{", "}", "."));
    }

    private HashSet<String> relationalOperators() {
        return new HashSet<>(Arrays.asList("!=", "==", "<", "<=", ">", ">=", "="));
    }

    private HashSet<String> logicOperators() {
        return new HashSet<>(Arrays.asList("!", "&&", "||"));
    }

    private HashSet<String> arithmeticOperators() {
        return new HashSet<>(Arrays.asList("+", "-", "*", "/", "++", "--"));
    }

    public HashMap<String, HashMap<Character, Character>> commentTokens() {
        HashMap<String, HashMap<Character, Character>> tokens = new HashMap<>();
        tokens.put("line", new HashMap<>());
        tokens.get("line").put('/', '/');
        tokens.put("beginning", new HashMap<>());
        tokens.get("beginning").put('/', '*');
        tokens.put("ending", new HashMap<>());
        tokens.get("ending").put('*', '/');
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

    public HashMap<TokenType, HashSet<String>> allTokens() {
        HashMap<TokenType, HashSet<String>> tokenHash = new HashMap<>();
        tokenHash.put(TokenType.RESERVED, this.reservedWords());
        tokenHash.put(TokenType.DELIMITER, this.delimiters());
        tokenHash.put(TokenType.RELATIONAL, this.relationalOperators());
        tokenHash.put(TokenType.LOGIC, this.logicOperators());
        tokenHash.put(TokenType.ARITHMETIC, this.arithmeticOperators());
        return tokenHash;
    }
}
