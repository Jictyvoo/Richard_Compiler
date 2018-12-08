package controllers;

public class SemanticAnalyser {
    private static SemanticAnalyser instance;
    
    public static SemanticAnalyser getInstance(){
        if (instance == null) {
            instance = new SemanticAnalyser();
        }
        return instance;
    }
}