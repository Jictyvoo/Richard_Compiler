package controllers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import models.value.Token;
import util.TableOfSymbols;
import util.TokenType;

public class SemanticAnalyser {
    private static SemanticAnalyser instance;
    private LinkedList<TableOfSymbols> listTables = new LinkedList<>();
    
    public static SemanticAnalyser getInstance(){
        if (instance == null) {
            instance = new SemanticAnalyser();
        }
        return instance;
    }
    
    public void CreateTableSymb(Queue<Token> queue){
        if (queue != null && !queue.isEmpty()) {
        }
    }
}