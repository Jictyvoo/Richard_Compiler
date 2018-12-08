package util;

public class TableOfSymbols {
    private static TableOfSymbols instance;
    
    public static TableOfSymbols getInstace(){
        if (instance == null) {
            instance = new TableOfSymbols();
        }
        return instance;
    }
}