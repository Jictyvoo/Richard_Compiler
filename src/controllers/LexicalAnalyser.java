package controllers;

import models.business.FileManager;
import models.value.Lexeme;
import models.value.ParseErrors;
import models.value.Token;
import models.value.TokensInformation;

import java.io.FileNotFoundException;
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
        int lineCounter = 0;
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
                if (character == '\\' && previous == '\\') {
                    previous = 0;
                }
                if (character == '\"' || openString) {
                    if (!openString && previous != '\\') {
                        openString = true;
                        completedLexeme = true;
                    } else if (previous != '\\' && character == '\"') {
                        completedLexeme = true;
                        currentLexeme.insert(0, '\"');
                        currentLexeme.append('\"');
                        openString = false;
                    } else {
                        currentLexeme.append(character);
                    }
                } else if (character == 9 || character == 32) { /*verify spaces to break words*/
                    completedLexeme = true;
                } else if (columnCounter == line.length() - 1) {
                    currentLexeme.append(character);
                    completedLexeme = true;
                } /*else if (TokensInformation.getInstance().canTogether().contains("" + previous)) {
                    if (TokensInformation.getInstance().canTogether().contains("" + character)) {
                        currentLexeme.deleteCharAt(currentLexeme.length() - 1);
                        nextLexeme.append(previous);
                        nextLexeme.append(character);
                        completedLexeme = true;
                    } else {
                        completedLexeme = true;
                        nextLexeme.append(character);
                    }
                }*/ else {
                    currentLexeme.append(character);
                }
                if (completedLexeme) {
                    if (currentLexeme.length() > 0) {
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

        return tokenList;
    }

    public List<ParseErrors> getParseErrors() {
        return this.parseErrors;
    }

}
