package models.value.errors;

import models.value.Lexeme;

public class ClassMethodError extends SemanticParseErrors {

    private Lexeme lexeme;
    private String type;

    public ClassMethodError(String type, Lexeme lexeme) {
        this.lexeme = lexeme;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Semanthic Error " + this.type + " not supported on method " + this.lexeme.getFile() + "_"
                + this.lexeme.getLineNumber() + ":" + this.lexeme.getColumn() + " --> " + this.lexeme.getValue();
    }
}
