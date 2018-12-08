package util;

import models.value.Token;

public class TableOfSymbols {
    private static TableOfSymbols instance;
    //If necessary remove or add more
    private Token token;
    private String categoria;
    private String tipo;

    public TableOfSymbols(Token token, String categoria, String tipo) {
        this.token = token;
        this.categoria = categoria;
        this.tipo = tipo;
    }

    public TableOfSymbols() {
    }
    
    public static TableOfSymbols getInstace(){
        if (instance == null) {
            instance = new TableOfSymbols();
        }
        return instance;
    }

    public static TableOfSymbols getInstance() {
        return instance;
    }

    public static void setInstance(TableOfSymbols instance) {
        TableOfSymbols.instance = instance;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String toString(){
        return "Categoria -> " + this.categoria + " -> " + this.token.toString();
    }
}