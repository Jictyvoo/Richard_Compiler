package view;

import controllers.LexicalAnalyser;
import controllers.SynthaticAnalyser;
import models.business.FileManager;
import models.value.ParseErrors;
import models.value.Token;
import util.exception.FileNotExistsException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class RichardMain {

    private static ArrayList<String> allPaths = new ArrayList<>();

    private static void readDirectory(String path) throws FileNotExistsException {
        allPaths.add(path);
        LexicalAnalyser lexicalAnalyser = LexicalAnalyser.getInstance();
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
            readDirectory(rootPath);
            makeDirectories(rootPath);
            LexicalAnalyser lexicalAnalyser = LexicalAnalyser.getInstance();
            for (String filename : lexicalAnalyser.getTokenList().keySet()) {
                System.out.println(SynthaticAnalyser.getInstance().start((LinkedList<Token>)lexicalAnalyser.getTokenList().get(filename)));
                String newFilename = filename.replace(rootPath, "output/");
                try {
                    //noinspection ResultOfMethodCallIgnored
                    (new File(newFilename)).createNewFile();
                    PrintWriter writer = new PrintWriter(newFilename, "ASCII");
                    for (Token token : lexicalAnalyser.getTokenList().get(filename)) {
                        writer.println(token.toString());
                    }
                    if (lexicalAnalyser.getParseErrors().get(filename).isEmpty()) {
                        writer.println("\n\nSuccess, all lexical analyse defined your code fine!");
                    } else {
                        writer.println("\n\nErrors Bellow\n\n");
                        for (ParseErrors parseErrors : lexicalAnalyser.getParseErrors().get(filename)) {
                            writer.println(parseErrors.toString());
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
