package view;

import controllers.LexicalAnalyser;
import models.business.FileManager;
import models.value.ParseErrors;
import models.value.Token;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class RichardMain {

    private static void readDirectory(String path) {
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

    public static void main(String... args) {
        String rootPath = args.length > 0 ? args[0] : "inputDemo/";
        readDirectory(rootPath);
        LexicalAnalyser lexicalAnalyser = LexicalAnalyser.getInstance();
        for (String filename : lexicalAnalyser.getTokenList().keySet()) {
            String newFilename = filename.replace(rootPath, "output/");
            String[] directories = newFilename.split("/");
            for (int index = 0; index < directories.length - 1; index += 1) {
                if (index > 0) {
                    String path = directories[index - 1] + "/" + directories[index];
                    if ((new File(path)).mkdir()) {
                        System.out.println(path + " created");
                    }
                } else {
                    if ((new File(directories[index])).mkdir()) {
                        System.out.println(directories[index] + " created");
                    }
                }
            }
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

    }
}
