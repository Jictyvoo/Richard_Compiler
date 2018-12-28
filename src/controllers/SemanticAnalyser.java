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
            while (!queue.isEmpty()) {
                if ("const".equals(queue.peek().getLexeme().getValue())) {
                    tableConst(queue);
                }else if("class".equals(queue.peek().getLexeme().getValue())){
                    tableClass(queue);
                }else{
                    queue.remove();
                }
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
            
            while (control) {
                TableOfSymbols t = new TableOfSymbols();
                t.setCategory("const");
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
        HashMap<String, TableOfSymbols> tClass = new HashMap<>();
        String nameClass = null;
        
        if ("class".equals(queue.peek().getLexeme().getValue())) {
            queue.remove();
            nameClass = queue.remove().getLexeme().getValue();
            
            boolean control = true;
            
            while (control && !queue.isEmpty()) {
                if (queue.peek().getLexeme().getValue().equals("variables")) {
                    tableVariables(queue, tClass, nameClass);
                }else if(queue.peek().getLexeme().getValue().equals("method")){
                    tableMethod(queue, tClass, nameClass);
                }else{
                    queue.remove();
                }
            }
        }
        table.put(nameClass, tClass);
    }
    
    public void tableVariables(Queue<Token> queue, HashMap<String, TableOfSymbols> tClass, String nameClass){
        queue.remove(); //Remove token variables
        boolean control = true;
        int chaves = 0;
        
        if (queue.peek().getLexeme().getValue().equals("{")) {
            queue.remove();
        }
        
        while (control) {
            TableOfSymbols t = new TableOfSymbols();
            t.setCategory(nameClass + "-> variables");
            
            if (queue.peek().getLexeme().getValue().equals("{")) {
                queue.remove();
                chaves++;
            }else if(queue.peek().getLexeme().getValue().equals("}")){
                if (chaves > 0) {
                    chaves--;
                }else{
                    control = false;
                }
                queue.remove();
            }else if(queue.peek().getType() == TokenType.RESERVED){
                t.setType(queue.remove());
                
                if (queue.peek().getType() == TokenType.IDENTIFIER) {
                    t.setToken(queue.remove());
                    tClass.put(t.getToken().getLexeme().getValue(), t);
                }
            }else{
                queue.remove();
            }
        }
    }
    
    public void tableMethod(Queue<Token> queue, HashMap<String, TableOfSymbols> tClass, String nameClass){
        queue.remove(); //Remove token method
        boolean control = true;
        int chaves = 0;
        
        //GAMBIARRA
        if (queue.peek().getType() == TokenType.RESERVED) {
            queue.remove();
            queue.remove();
        }
        
        while (control) {
            TableOfSymbols t = new TableOfSymbols();
            t.setCategory(nameClass + "-> method");
            
            if (queue.peek().getLexeme().getValue().equals("{")) {
                queue.remove();
                chaves++;
            }else if(queue.peek().getLexeme().getValue().equals("}")){
                if (chaves > 1) {
                    control = false;
                }else{
                    chaves --;
                }
                queue.remove();
            }else if(queue.peek().getType() == TokenType.RESERVED){
                t.setType(queue.remove());
                if (queue.peek().getType() == TokenType.IDENTIFIER) {
                    t.setToken(queue.remove());
                    tClass.put(t.getToken().getLexeme().getValue(), t);
                }
            }else{
                queue.remove();
            }
        }
    }
    
    public void test(){
        for (HashMap<String, TableOfSymbols> hash : table.values()) {
            for(TableOfSymbols key : hash.values()){
                System.out.println(key.toString());
            }
        }
    }
}