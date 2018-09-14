package view;

import controllers.LexicalAnalyser;
import models.value.ParseErrors;
import models.value.Token;

import java.io.FileNotFoundException;
import java.util.List;

public class RichardMain {
    public static void main(String... args) {
        LexicalAnalyser lexicalAnalyser = LexicalAnalyser.getInstance();
        try {
            List<Token> tokens = lexicalAnalyser.parse("inputDemo/testFile.rchrd");
            for (Token token : tokens) {
                System.out.println(token);
            }
            for (List<ParseErrors> parseErrors : lexicalAnalyser.getParseErrors().values()) {
                for (ParseErrors error : parseErrors) {
                    System.out.println(error);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
