package util;

import models.value.Token;

public class TableOfSymbols {
    private String category;
    private Token type;
    private Token token;

    public TableOfSymbols(Token token, Token type) {
        this.token = token;
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Token getType() {
        return type;
    }

    public void setType(Token type) {
        this.type = type;
    }

    public TableOfSymbols() {
    }
    
    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Category: " + category + " -> " + type.getLexeme().getValue() + " " + token.getLexeme().getValue();
    }
}