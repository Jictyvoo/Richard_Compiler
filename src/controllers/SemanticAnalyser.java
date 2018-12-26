package controllers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import models.value.Token;
import util.TableOfSymbols;
import util.TokenType;

public class SemanticAnalyser {
    private static SemanticAnalyser instance;
    private HashMap<String, HashMap<String, TableOfSymbols>> table = new HashMap<>();
    
    public static SemanticAnalyser getInstance(){
        if (instance == null) {
            instance = new SemanticAnalyser();
        }
        return instance;
    }
    
    public void CreateTableSymb(Queue<Token> queue){
        if (queue != null && !queue.isEmpty()) {
            if ("const".equals(queue.peek().getLexeme().getValue())) {
                tableConst(queue);
            }else if("class".equals(queue.peek().getLexeme().getValue())){
                tableClass(queue);
            }
        }
        test(); //test table
    }
    
    /*Method for reading constants*/
    public void tableConst(Queue<Token> queue){
        if ("const".equals(queue.peek().getLexeme().getValue())) {
            queue.remove();
            HashMap<String, TableOfSymbols> constant = new HashMap<>(); /*Hash for constants*/
            boolean control = true;
            
            if (queue.peek().getLexeme().getValue().equals("{")) {
                queue.remove();
            }
            
            while (control) {
                TableOfSymbols t = new TableOfSymbols();
                if (queue.peek().getType() == TokenType.RESERVED) {
                    t.setType(queue.remove());
                    if (queue.peek().getType() == TokenType.IDENTIFIER) {
                        t.setToken(queue.remove());
                        
                        constant.put(t.getToken().getLexeme().getValue(), t);
                    }
                }else if (queue.peek().getLexeme().getValue().equals("}")) {
                    queue.remove();
                    control = false;
                }else{
                    queue.remove();
                }
            }
            table.put("const", constant);
        }
    }
    
    public void tableClass(Queue<Token> queue){
        if ("class".equals(queue.peek().getLexeme().getValue())) {
            queue.remove();
            HashMap<String, TableOfSymbols> tClass = new HashMap<>();
            boolean control = true;
            
            String nameClass = queue.remove().getLexeme().getValue();
            
            while (control) {
            }
        }
    }
    
    public void test(){
        HashMap<String, TableOfSymbols> test = table.get("const");
        
        for (TableOfSymbols key : test.values()) {
            System.out.println(key.toString());
        }
    }
}