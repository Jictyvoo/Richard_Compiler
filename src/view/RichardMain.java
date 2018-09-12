package view;

import controllers.LexicalAnalyser;

import java.io.FileNotFoundException;

public class RichardMain {
    public static void main(String ... args){
        LexicalAnalyser lexicalAnalyser = LexicalAnalyser.getInstance();
        try {
            lexicalAnalyser.parse("README.md");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
