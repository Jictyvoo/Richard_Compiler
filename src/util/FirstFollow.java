package util;

import java.util.HashSet;
import java.util.Arrays;
import java.util.HashMap;

public class FirstFollow {
    public final String Version;
    public final String StartSymbol;
    public final String About;
    public final String Author;
    public final String Name;
    public final String CaseSensitive;
    private HashMap<String, HashSet<String>> first;
    private static FirstFollow instance;

    private FirstFollow() {
        this.Version = "The version of the grammar and/or language";
        this.StartSymbol = "<Program>";
        this.About = "A short description of the grammar";
        this.Author = "João Victor, Eduardo Marques, Gustavo Henrique, Marcos Aldrey, Marcos Vinicius, Alyson, Alisson, André, Gilvaney, Nielson, Lucas Cardoso";
        this.Name = "RICHARD";
        this.CaseSensitive = "True";
        this.first = new HashMap<>();
        this.first.put("Value", new HashSet<>(Arrays.asList("true", "false", "StringLiteral", "NumberTerminal")));
        this.first.put("Write Parameters", new HashSet<>(Arrays.asList("false", "StringLiteral", "true", "NumberTerminal", "Identifier")));
        this.first.put("Valid Values", new HashSet<>(Arrays.asList("false", "StringLiteral", "true", "NumberTerminal", "Identifier")));
        this.first.put("Expr Arit", new HashSet<>(Arrays.asList("false", "StringLiteral", "-", "true", "{", "NumberTerminal", "Identifier")));
        this.first.put("Return Statement", new HashSet<>(Arrays.asList("", "!", "(")));
        this.first.put("Relational Logic", new HashSet<>(Arrays.asList("<=", "||", ">=", "==", "<", "&&", ">", "", "!=")));
        this.first.put("Type", new HashSet<>(Arrays.asList("bool", "Identifier", "float", "int", "void", "string")));
        this.first.put("Constants", new HashSet<>(Arrays.asList("const")));
        this.first.put("Post-Else-Block", new HashSet<>(Arrays.asList("if", "", "{")));
        this.first.put("Increment-Decrement", new HashSet<>(Arrays.asList("--", "", "++")));
        this.first.put("If-Block", new HashSet<>(Arrays.asList("if")));
        this.first.put("Array Position", new HashSet<>(Arrays.asList("Identifier", "-", "{", "(", "false", "StringLiteral", "true", "NumberTerminal", "")));
        this.first.put("Program", new HashSet<>(Arrays.asList("const", "", "class")));
        this.first.put("Negate Exp", new HashSet<>(Arrays.asList("false", "StringLiteral", "-", "true", "{", "NumberTerminal", "Identifier")));
        this.first.put("Constant Assignment", new HashSet<>(Arrays.asList("bool", "Identifier", "float", "", "int", "void", "string")));
        this.first.put("Array", new HashSet<>(Arrays.asList("", "[")));
        this.first.put("Expression", new HashSet<>(Arrays.asList("Identifier", "false", "StringLiteral", "-", "true", "{", "NumberTerminal", "(")));
        this.first.put("Methods", new HashSet<>(Arrays.asList("method")));
        this.first.put("Declaration", new HashSet<>(Arrays.asList("bool", "Identifier", "float", "int", "string", "void")));
        this.first.put("Init Array_3", new HashSet<>(Arrays.asList("false", "StringLiteral", "true", "{", "NumberTerminal", "Identifier")));
        this.first.put("Init Array_2", new HashSet<>(Arrays.asList("(")));
        this.first.put("Initialize Variable", new HashSet<>(Arrays.asList(",", "")));
        this.first.put("Condition", new HashSet<>(Arrays.asList("", "!", "(")));
        this.first.put("Parameter", new HashSet<>(Arrays.asList(",", "")));
        this.first.put("Extends", new HashSet<>(Arrays.asList("extends", "")));
        this.first.put("Arguments", new HashSet<>(Arrays.asList("false", "StringLiteral", "", "true", "{", "NumberTerminal", "Identifier")));
        this.first.put("Looping-Block", new HashSet<>(Arrays.asList("while")));
        this.first.put("Attributition", new HashSet<>(Arrays.asList("--", "", "++", "=")));
        this.first.put("Class", new HashSet<>(Arrays.asList("class")));
        this.first.put("Negate", new HashSet<>(Arrays.asList("!", "")));
        this.first.put("Attribute Access", new HashSet<>(Arrays.asList(".", "")));
        this.first.put("Initialize Constant", new HashSet<>(Arrays.asList(",", "")));
        this.first.put("Mult Exp", new HashSet<>(Arrays.asList("false", "StringLiteral", "-", "true", "{", "Identifier", "NumberTerminal")));
        this.first.put("Write", new HashSet<>(Arrays.asList("write")));
        this.first.put("Argument", new HashSet<>(Arrays.asList(",", "")));
        this.first.put("Initialize", new HashSet<>(Arrays.asList("", "=")));
        this.first.put("Read", new HashSet<>(Arrays.asList("read")));
        this.first.put("Logic Operator", new HashSet<>(Arrays.asList("||", "&&")));
        this.first.put("Relational Operator", new HashSet<>(Arrays.asList("<=", "!=", "==", "<", ">", ">=")));
        this.first.put("Code Statements", new HashSet<>(Arrays.asList("", "write", "variables", "return", "Identifier", "while", "read", "if")));
        this.first.put("Method Call", new HashSet<>(Arrays.asList("Identifier")));
        this.first.put("Class Code", new HashSet<>(Arrays.asList("", "method", "variables")));
        this.first.put("Parameters", new HashSet<>(Arrays.asList("bool", "Identifier", "float", "", "int", "void", "string")));
        this.first.put("Variables", new HashSet<>(Arrays.asList("variables")));
        this.first.put("Line Code", new HashSet<>(Arrays.asList("write", "Identifier", "read", "return")));
        this.first.put("To-Write", new HashSet<>(Arrays.asList(",", "")));
        this.first.put("Variable Assignment", new HashSet<>(Arrays.asList("bool", "Identifier", "float", "", "int", "void", "string")));
        this.first.put("Multiple Identifier", new HashSet<>(Arrays.asList(",", "")));
        this.first.put("Read Parameters", new HashSet<>(Arrays.asList(",", "")));
        this.first.put("Initial Value", new HashSet<>(Arrays.asList("false", "StringLiteral", "true", "{", "Identifier", "NumberTerminal")));
        this.first.put("Else-Block", new HashSet<>(Arrays.asList("else", "")));
        this.first.put("Code Block", new HashSet<>(Arrays.asList("{")));
        this.first.put("Valid Identifier", new HashSet<>(Arrays.asList("Identifier")));
        this.first.put("Init Array", new HashSet<>(Arrays.asList("{")));
    }

    public HashMap<String, HashSet<String>> getFirst() {
        return this.first;
    }

    public static FirstFollow getInstance() {
        if (instance == null) {
            instance = new FirstFollow();
        }
        return instance;
    }
}
