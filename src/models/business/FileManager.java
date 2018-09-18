package models.business;

import java.io.*;
import java.util.Iterator;
import java.io.File;

public class FileManager implements Iterable<String> {
    
    private File file;
    private FileReader fileReader;

    public FileManager(String filename) throws FileNotFoundException {
        this.file = new File(filename);
        this.fileReader = new FileReader(this.file);
    }

    public FileManager() {
    }
    
    @Override
    public Iterator<String> iterator() {
        return new Iterator<String>() {

            private BufferedReader bufferedReader = new BufferedReader(fileReader);
            private String line;

            @Override
            public boolean hasNext() {
                this.line = null;
                try {
                    this.line = this.bufferedReader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return this.line != null;
            }

            @Override
            public String next() {
                return this.line;
            }
        };
    }

    public Iterable<Character> forLine(String iterateLine) {
        return new Iterable<Character>() {
            @Override
            public Iterator<Character> iterator() {
                return new Iterator<Character>() {

                    private int position = 0;
                    private String line = iterateLine;

                    @Override
                    public boolean hasNext() {
                        return this.position < this.line.length();
                    }

                    @Override
                    public Character next() {
                        char character = 0;
                        if (this.line.length() > 0) {
                            character = this.line.charAt(this.position);
                            this.position += 1;
                        }
                        return character;
                    }
                };
            }
        };
    }

    public static String[] filesName(String root){
        String[] filesName;
        File dir = new File(root);
        
        if (dir.isDirectory()) {
            filesName = dir.list();
            return filesName;
        }
        
        return null;
    }
    
    public void destroy() {
        this.fileReader = null;
        this.file = null;
    }

}
