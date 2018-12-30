package models.value.errors;

import models.value.Lexeme;

import java.util.HashSet;

public class IncorrectTypeError extends SemanticParseErrors {
    private HashSet<String> error;
    private Lexeme lexeme;

    public IncorrectTypeError(HashSet<String> error, Lexeme lexeme) {
        this.error = error;
        this.lexeme = lexeme;
    }

    @Override
    public String toString() {
        if (this.lexeme != null) {
            return "Semantic Error:" + this.lexeme.getFile() + " " + this.lexeme.getLineNumber() + ":"
                    + this.lexeme.getColumn() + " >> Expected one of the following types " + this.error + ": get " + this.lexeme.getValue();
        }
        return "Semantic Error:" + " >> Expected one of the following tokens " + this.error;
    }

    public Lexeme getLexeme() {
        return this.lexeme;
    }
}
