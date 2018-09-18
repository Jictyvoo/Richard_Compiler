package view;

import controllers.LexicalAnalyser;
import models.value.ParseErrors;
import models.value.Token;

import java.io.FileNotFoundException;
import java.util.List;
import models.business.FileManager;

public class RichardMain {
    public static void main(String... args) {
        LexicalAnalyser lexicalAnalyser = LexicalAnalyser.getInstance();
        try {
            String[] filesName = new FileManager().filesName("teste");
            
            for(String s : filesName){
                System.out.println("File: "+s); //test
                lexicalAnalyser.parse(s);

                for (List<Token> tokens : lexicalAnalyser.getTokenList().values()) {
                    for (Token token : tokens) {
                        System.out.println(token);
                    }
                }
                for (List<ParseErrors> parseErrors : lexicalAnalyser.getParseErrors().values()) {
                    for (ParseErrors error : parseErrors) {
                        System.out.println(error);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
