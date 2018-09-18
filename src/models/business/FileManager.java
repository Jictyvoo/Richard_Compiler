package models.business;

import java.io.*;
import java.util.Iterator;
import java.io.File;

public class FileManager implements Iterable<String> {

    private File file;
    private FileReader fileReader;

    public FileManager(String filename) throws FileNotFoundException {
        this.file = new File(filename);
        if (file.isFile()) {
            this.fileReader = new FileReader(this.file);
        }
    }

    @Override
    public Iterator<String> iterator() {
        if (this.file.isDirectory()) {
            return new Iterator<String>() {

                private String[] files = file.list();
                private int index = 0;

                @Override
                public boolean hasNext() {
                    return this.index < files.length;
                }

                @Override
                public String next() {
                    String returned = this.files[this.index];
                    this.index += 1;
                    return returned;
                }
            };
        } else {
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

    public void destroy() {
        this.fileReader = null;
        this.file = null;
    }

}
