package models.value;

public class Lexeme {
    private String value;
    private String line;
    private int lineNumber;
    private short column;
    private String file;

    public Lexeme(String value, String line, int lineNumber, short column, String file) {
        this.value = value;
        this.line = line;
        this.lineNumber = lineNumber;
        this.column = column;
        this.file = file;
    }

    public String getValue() {
        return value;
    }

    public String getLine() {
        return line;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public short getColumn() {
        return column;
    }

    public String getFile() {
        return file;
    }
}
