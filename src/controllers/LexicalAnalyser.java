package controllers;

import models.business.FileManager;
import models.value.Lexeme;
import models.value.Token;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

public class LexicalAnalyser {

    private static LexicalAnalyser instance = null;

    private LexicalAnalyser() {
    }

    public static LexicalAnalyser getInstance() {
        if (instance == null) {
            instance = new LexicalAnalyser();
        }
        return instance;
    }

    private Token analyse(Lexeme lexeme) {
        return null;
    }

    public List<Token> parse(String filename) throws FileNotFoundException {
        FileManager fileManager = new FileManager(filename);
        LinkedList<Token> tokenList = new LinkedList<>();

        StringBuilder currentLexeme = new StringBuilder();
        int lineCounter = 0;
        boolean openString = false;
        char previous = 0;
        for (String line : fileManager) {
            short columnCounter = 0;
            for (char character : fileManager.forLine(line)) {
                boolean completedLexeme = false;
                /*if (character == '\"' || openString) {
                    if (!openString && previous != '\\') {
                        openString = true;
                    } else if (!openString) {
                        openString = true;
                    } else if(previous != '\\'){
                        completedLexeme = true;
                    }
                    currentLexeme.append(character);
                } else */if (character == ' ') {
                    completedLexeme = true;
                } else if (columnCounter == line.length() - 1) {
                    currentLexeme.append(character);
                    completedLexeme = true;
                } else {
                    currentLexeme.append(character);
                }
                if (completedLexeme) {
                    String value = currentLexeme.toString();
                    System.out.println(value);
                    Lexeme lexeme = new Lexeme(value, line, lineCounter, columnCounter, filename);
                    Token generatedToken = this.analyse(lexeme);
                    if (generatedToken != null) {
                        tokenList.add(generatedToken);
                    }
                    currentLexeme = new StringBuilder();
                }
                columnCounter += 1;
                previous = character;
            }
            lineCounter += 1;
        }

        return tokenList;
    }

}
