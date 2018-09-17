package models.value;

import util.TokenType;

public class Token {
    private TokenType type;
    private Lexeme lexeme;

    public Token(TokenType type, Lexeme lexeme) {
        this.type = type;
        this.lexeme = lexeme;
    }

    public TokenType getType() {
        return type;
    }

    public Lexeme getLexeme() {
        return lexeme;
    }

    @Override
    public String toString() {
        return this.lexeme.getFile() + "-" + this.lexeme.getLineNumber() + ":" + this.lexeme.getColumn() + " >> Type: " +
                this.type.getName() + " -> Value: " + this.lexeme.getValue();
    }
}
