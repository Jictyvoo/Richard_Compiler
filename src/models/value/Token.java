package models.value;

public class Token {
    private String name;
    private String type;
    private Lexeme lexeme;

    public Token(String name, String type, Lexeme lexeme) {
        this.name = name;
        this.type = type;
        this.lexeme = lexeme;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Lexeme getLexeme() {
        return lexeme;
    }
}
