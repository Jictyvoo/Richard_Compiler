package models.value;

public class ParseErrors {
    private String type;
    private String error;
    private Lexeme lexeme;

    public ParseErrors(String type, String error, Lexeme lexeme) {
        this.type = type;
        this.error = error;
        this.lexeme = lexeme;
    }

    @Override
    public String toString() {
        return this.type + ": " + this.lexeme.getFile() + " " + this.lexeme.getLineNumber() + ":"
                + this.lexeme.getColumn() + " >> " + this.error + ": " + this.lexeme.getValue();
    }
}
