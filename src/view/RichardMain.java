package view;

//Authors: João Victor & Eduardo Marques

import controllers.LexicalAnalyser;
import controllers.SynthaticAnalyser;
import controllers.SynthaticAutomatic;
import models.business.FileManager;
import models.value.LexicalParseErrors;
import models.value.SynthaticParseErrors;
import models.value.Token;
import util.SynthaticNode;
import util.exception.FileNotExistsException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;

public class RichardMain {

    private static ArrayList<String> allPaths = new ArrayList<>();

    /*Method that read the project's root folder*/
    private static void readDirectory(String path) throws FileNotExistsException {
        allPaths.add(path); //Add directory an list
        LexicalAnalyser lexicalAnalyser = LexicalAnalyser.getInstance(); //Received instance of lexical analyser
        /*Performs directory reading*/
        try {
            FileManager fileManager = new FileManager(path);
            for (String filename : fileManager) {
                filename = path + filename;
                File file = new File(filename);
                if (file.isDirectory()) {
                    readDirectory(filename + "/");
                } else {
                    lexicalAnalyser.parse(filename);
                }
            }
            fileManager.destroy();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*Create output directories*/
    private static void makeDirectories(String rootPath) {
        for (String path : allPaths) {
            path = path.replace(rootPath, "output/");
            if ((new File(path)).mkdir()) {
                System.out.println(path + " created");
            }
        }
    }

    public static void main(String... args) {
        String rootPath = args.length > 0 ? args[0] : "src/";
        try {
            readDirectory(rootPath); /*Performs directory reading*/
            makeDirectories(rootPath); /*Create output directories*/
            LexicalAnalyser lexicalAnalyser = LexicalAnalyser.getInstance(); //Receives lexical parser instance

            for (String filename : lexicalAnalyser.getTokenList().keySet()) {
                /*Create output directory*/
                String newFilename = filename.replace(rootPath, "output/");
                try {
                    //noinspection ResultOfMethodCallIgnored
                    (new File(newFilename)).createNewFile();
                    PrintWriter writer = new PrintWriter(newFilename, "ASCII");
                    for (Token token : lexicalAnalyser.getTokenList().get(filename)) {
                        writer.println(token.toString());
                    }

                    /*Syntax analyzer output*/
                    if (lexicalAnalyser.getParseErrors().get(filename).isEmpty()) {
                        writer.println("\n\nSuccess, all lexical analyse defined your code fine!");
                        SynthaticNode node = SynthaticAutomatic.getInstance().start((LinkedList<Token>) lexicalAnalyser.getTokenList().get(filename));
                        writer.println("\n");
                        for (SynthaticParseErrors synthaticParseErrors : SynthaticAutomatic.getInstance().getErrors()) {
                            if (synthaticParseErrors.getLexeme() != null)
                                writer.println(synthaticParseErrors.toString());
                            //SynthaticNode node = SynthaticAnalyser.getInstance().start((LinkedList<Token>) lexicalAnalyser.getTokenList().get(filename));
                            //SynthaticAnalyser.getInstance().showDerivation(node);
                        }
                        SynthaticAutomatic.getInstance().clearErrors();
                    } else {
                        writer.println("\n\nErrors Bellow\n\n");
                        for (LexicalParseErrors lexicalParseErrors : lexicalAnalyser.getParseErrors().get(filename)) {
                            writer.println(lexicalParseErrors.toString());
                        }
                    }
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotExistsException e) {
            System.err.println("Cannot find " + rootPath);
        }
    }
}
