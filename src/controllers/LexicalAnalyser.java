package controllers;

import models.business.FileManager;
import models.value.Lexeme;
import models.value.ParseErrors;
import models.value.Token;
import util.TokenType;
import util.TokensInformation;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class LexicalAnalyser {

    private static LexicalAnalyser instance = null;
    private HashMap<String, List<ParseErrors>> parseErrors;
    private HashMap<String, List<Token>> tokenList;

    private LexicalAnalyser() {
        this.parseErrors = new HashMap<>();
        this.tokenList = new HashMap<>();
    }

    public static LexicalAnalyser getInstance() {
        if (instance == null) {
            instance = new LexicalAnalyser();
        }
        return instance;
    }

    Token analyse(Lexeme lexeme) {
        String token = lexeme.getValue();
        HashMap<TokenType, HashSet<String>> predefinedTokens = TokensInformation.getInstance().allTokens();
        for (TokenType tokenType : predefinedTokens.keySet()) {
            if (predefinedTokens.get(tokenType).contains(token)) {
                return new Token(tokenType, lexeme);
            }
        }
        if (token.matches("(-)?\\s*[0-9]([0-9]*\\.?[0-9]+)?")) {  /*verify if the number is correct*/
            return new Token(TokenType.NUMBER, lexeme);
        } else if (token.matches("[_]?(([a-z]|[A-Z]|_)+[0-9]*)+(([a-z]|[A-Z]|[0-9]|_)*)*")) {
            return new Token(TokenType.IDENTIFIER, lexeme);  /*verify if the identifier is correct*/
        } else if (token.matches("\"(.)*\"")) {  /*verify if the string is correct*/
            boolean validCharacters = true;
            for (int index = 1; index < lexeme.getValue().length() - 1; index += 1) {
                char character = lexeme.getValue().charAt(index);
                if (!(character >= 32 && character <= 126)) {
                    validCharacters = false;
                }
            }
            if (validCharacters) {
                return new Token(TokenType.STRING, lexeme);
            }
        } else if (token.matches("/\\*(.|\\n)*\\*/") || token.matches("//.*")) {
            return new Token(TokenType.COMMENT, lexeme); /*verify if comment is correct*/
        }

        return null;
    }

    private StringBuilder verifyToken(List<Token> tokenList, StringBuilder currentLexeme, String line, int lineCounter, short columnCounter, String filename) {
        if (currentLexeme.length() > 0) {   /*if have a true lexeme*/
            String value = currentLexeme.toString();
            Lexeme lexeme = new Lexeme(value, line, lineCounter, columnCounter, filename);
            Token generatedToken = this.analyse(lexeme);
            if (generatedToken != null) {
                tokenList.add(generatedToken);
            } else {
                this.parseErrors.get(filename).add(new ParseErrors("Lexical Error", "Unrecognized Token", lexeme));
            }
        }
        return new StringBuilder();
    }

    public List<Token> parse(String filename) throws FileNotFoundException {
        FileManager fileManager = new FileManager(filename);
        if (!this.parseErrors.containsKey(filename)) {
            this.parseErrors.put(filename, new LinkedList<>());
        }
        if (!this.tokenList.containsKey(filename)) {
            this.tokenList.put(filename, new LinkedList<>());
        }

        StringBuilder currentLexeme = new StringBuilder();
        StringBuilder nextLexeme = new StringBuilder();
        int lineCounter = 1;
        short column = 0;
        byte openComment = 0;
        boolean openString = false;
        char previous = 0;
        for (String line : fileManager) {
            short columnCounter = 0;
            for (char character : fileManager.forLine(line)) {
                if (nextLexeme.length() > 0) {
                    currentLexeme = nextLexeme;
                    nextLexeme = new StringBuilder();
                }
                boolean completedLexeme = false;
                if (character == '\\' && previous == '\\') {    /*nullify previous if have two backslash*/
                    previous = 0;
                }
                if (!openString && ((previous == '/' && (character == '*' || character == '/')) || openComment > 0)) {
                    currentLexeme.append(character);    /*Here is the verification for comments*/
                    if (openComment == 0 && character == '/') {
                        openComment = 1;
                    } else if (openComment == 0) {
                        openComment = 2;
                    } else if (openComment == 2 && (previous == '*' && character == '/')) {
                        openComment = 0;
                        completedLexeme = true;
                    } else if (openComment == 1 && columnCounter == line.length() - 1) {
                        openComment = 0;
                        completedLexeme = true;
                    }
                } else if (!openString && previous == '*' && character == '/') {    /*only to verify if has a unopened comment*/
                    currentLexeme.append(character);
                    Lexeme lexeme = new Lexeme(currentLexeme.toString(), "", lineCounter, column, filename);
                    this.parseErrors.get(filename).add(new ParseErrors("Lexical Error", "Comment not opened", lexeme));
                    currentLexeme = new StringBuilder();
                } else if (character == '\"' || openString) {  /*start of string verification*/
                    if (!openString && previous != '\\') {  /*if string was not open and " was not followed by slash*/
                        openString = true;
                        completedLexeme = true; /*finish previous lexeme*/
                    } else if (previous != '\\' && character == '\"') { /*if string was open and find another "*/
                        completedLexeme = true; /*complete string lexeme*/
                        currentLexeme.insert(0, '\"');  /*insert " at beginning of lexeme*/
                        currentLexeme.append('\"');  /*insert " at ending of lexeme*/
                        openString = false; /*close string*/
                    } else {
                        currentLexeme.append(character);    /*add all characters inside the string*/
                    }
                } else if (character == 9 || character == 32) { /*verify spaces to break words*/
                    completedLexeme = true;
                } else if (TokensInformation.getInstance().split().contains(character)) {   /*Here will start token split*/
                    if (((previous >= '0' && previous <= '9') && character == '.')) {
                        currentLexeme.append(character);
                    } else {
                        completedLexeme = true; /*if contains a split character, complete previous lexeme*/
                        nextLexeme.append(character);   /*next lexeme will contain split character*/
                        HashMap<Character, Character> canTogether = TokensInformation.getInstance().canTogether();
                        if (canTogether.containsKey(previous)) {
                            if (canTogether.get(previous) == character) {   /*if current character can be together with other*/
                                if (currentLexeme.length() > 0) {   /*need to work with ------++*/
                                    nextLexeme.deleteCharAt(nextLexeme.length() - 1);
                                    currentLexeme.append(character);
                                }
                            }
                        }
                    }
                } else if (TokensInformation.getInstance().split().contains(previous)) {    /*end of spliced tokens verification*/
                    if (((character >= '0' && character <= '9') && previous == '.')) {
                        currentLexeme.append(character);
                    } else {
                        completedLexeme = true; /*complete lexeme if previous token is a split token like*/
                        nextLexeme.append(character);
                    }
                } else if (columnCounter == line.length() - 1) {    /*verify if character is at end of line*/
                    currentLexeme.append(character);
                    completedLexeme = true;
                } else {
                    currentLexeme.append(character);
                }
                if (completedLexeme) {
                    currentLexeme = this.verifyToken(this.tokenList.get(filename), currentLexeme, line, lineCounter, columnCounter, filename);
                }
                columnCounter += 1;
                column = columnCounter;
                previous = character;
            }
            if (openString) {
                Lexeme lexeme = new Lexeme(currentLexeme.toString(), "", lineCounter, column, filename);
                this.parseErrors.get(filename).add(new ParseErrors("Lexical Error", "String not closed", lexeme));
                openString = false;
                currentLexeme = new StringBuilder();
            } else if (openComment == 2) {
                currentLexeme.append('\n');
            }
            lineCounter += 1;
        }
        if (openComment > 0) {
            Lexeme lexeme = new Lexeme(currentLexeme.toString(), "", lineCounter, column, filename);
            this.parseErrors.get(filename).add(new ParseErrors("Lexical Error", "Comment not closed", lexeme));
        } else if (currentLexeme.length() > 0) {
            this.verifyToken(this.tokenList.get(filename), currentLexeme, "", lineCounter - 1, column, filename);
        } else if (nextLexeme.length() > 0) {
            this.verifyToken(this.tokenList.get(filename), nextLexeme, "", lineCounter - 1, column, filename);
        }
        return this.tokenList.get(filename);
    }

    public HashMap<String, List<ParseErrors>> getParseErrors() {
        return this.parseErrors;
    }

    public HashMap<String, List<Token>> getTokenList() {
        return this.tokenList;
    }
}
