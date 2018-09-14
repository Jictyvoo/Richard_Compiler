package controllers;

import models.business.FileManager;
import models.value.Lexeme;
import models.value.ParseErrors;
import models.value.Token;
import models.value.TokensInformation;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class LexicalAnalyser {

    private static LexicalAnalyser instance = null;
    private List<ParseErrors> parseErrors;

    private LexicalAnalyser() {
        this.parseErrors = new LinkedList<>();
    }

    public static LexicalAnalyser getInstance() {
        if (instance == null) {
            instance = new LexicalAnalyser();
        }
        return instance;
    }

    Token analyse(Lexeme lexeme) {
        String token = lexeme.getValue();
        if (TokensInformation.getInstance().reservedWords().contains(token)) {
            return new Token(lexeme.getValue(), "reserved", lexeme);
        } else if (TokensInformation.getInstance().delimiters().contains(token)) {
            return new Token(lexeme.getValue(), "delimiter", lexeme);
        } else if (TokensInformation.getInstance().relationalOperators().contains(token)) {
            return new Token(lexeme.getValue(), "relational", lexeme);
        } else if (TokensInformation.getInstance().logicOperators().contains(token)) {
            return new Token(lexeme.getValue(), "logic", lexeme);
        } else if (TokensInformation.getInstance().arithmeticOperators().contains(token)) {
            return new Token(lexeme.getValue(), "arithmetic", lexeme);
        } else if (token.matches("(-)?\\s*[0-9]([0-9]*\\.?[0-9]+)?")) {  /*verify if the number is correct*/
            return new Token(lexeme.getValue(), "number", lexeme);
        } else if (token.matches("[_]?(([a-z]|[A-Z]|_)+[0-9]*)+(([a-z]|[A-Z]|[0-9]|_)*)*")) {
            return new Token(lexeme.getValue(), "identifier", lexeme);  /*verify if the identifier is correct*/
        } else if (token.matches("\"(.)*\"")) {  /*verify if the string is correct*/
            boolean validCharacters = true;
            for (int index = 1; index < lexeme.getValue().length() - 1; index += 1) {
                char character = lexeme.getValue().charAt(index);
                if (!(character >= 32 && character <= 126)) {
                    validCharacters = false;
                }
            }
            if (validCharacters) {
                return new Token(lexeme.getValue(), "string", lexeme);
            }
        } else if (token.matches("/\\*.*\\*/") || token.matches("//.*")) {
            return new Token(lexeme.getValue(), "comment", lexeme); /*verify if comment is correct*/
        }

        return null;
    }

    public List<Token> parse(String filename) throws FileNotFoundException {
        FileManager fileManager = new FileManager(filename);
        LinkedList<Token> tokenList = new LinkedList<>();

        StringBuilder currentLexeme = new StringBuilder();
        StringBuilder nextLexeme = new StringBuilder();
        int lineCounter = 1;
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
                if (character == '\"' || openString) {  /*start of string verification*/
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
                    completedLexeme = true; /*if contains a split character, complete previous lexeme*/
                    nextLexeme.append(character);   /*next lexeme will contain split character*/
                    HashMap<Character, Character> canTogether = TokensInformation.getInstance().canTogether();
                    if (canTogether.containsKey(previous)) {
                        if (canTogether.get(previous) == character) {   /*if current character can be together with other*/
                            nextLexeme.deleteCharAt(nextLexeme.length() - 1);
                            currentLexeme.append(character);
                        }
                    }
                } else if (TokensInformation.getInstance().split().contains(previous)) {    /*end of splited tokens verification*/
                    completedLexeme = true; /*complete lexeme if previous token is a split token like*/
                    nextLexeme.append(character);
                } else if (columnCounter == line.length() - 1) {    /*verify if character is at end of line*/
                    currentLexeme.append(character);
                    completedLexeme = true;
                } else {
                    currentLexeme.append(character);
                }
                if (completedLexeme) {
                    if (currentLexeme.length() > 0) {   /*if have a true lexeme*/
                        String value = currentLexeme.toString();
                        Lexeme lexeme = new Lexeme(value, line, lineCounter, columnCounter, filename);
                        Token generatedToken = this.analyse(lexeme);
                        if (generatedToken != null) {
                            tokenList.add(generatedToken);
                        } else {
                            this.parseErrors.add(new ParseErrors("Lexical Error", "Unrecognized Token", lexeme));
                        }
                        currentLexeme = new StringBuilder();
                    }
                }
                columnCounter += 1;
                previous = character;
            }
            lineCounter += 1;
        }
        if (openString) {
            Lexeme lexeme = new Lexeme(currentLexeme.toString(), "", lineCounter, (short) 0, filename);
            this.parseErrors.add(new ParseErrors("Lexical Error", "String not closed", lexeme));
        }
        return tokenList;
    }

    public List<ParseErrors> getParseErrors() {
        return this.parseErrors;
    }

}
