package models.value;

import java.util.HashSet;

public class SynthaticParseErrors {
    private HashSet<String> error;
    private Lexeme lexeme;

    public SynthaticParseErrors(HashSet<String> error, Lexeme lexeme) {
        this.error = error;
        this.lexeme = lexeme;
    }

    @Override
    public String toString() {
        return "Synthatic Error:" + this.lexeme.getFile() + " " + this.lexeme.getLineNumber() + ":"
                + this.lexeme.getColumn() + " >> Expected one of the following tokens " + this.error + ": get " + this.lexeme.getValue();
    }
}
