package util;

import java.util.HashSet;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FirstFollow {
    public final String Version;
    public final String About;
    public final String StartSymbol;
    public final String Name;
    public final String CaseSensitive;
    public final String Author;
    private HashMap<String, HashSet<String>> first;
    private HashMap<String, HashSet<String>> follow;
    private static FirstFollow instance;

    private FirstFollow() {
        this.Version = "The version of the grammar and/or language";
        this.About = "A short description of the grammar";
        this.StartSymbol = "<Program>";
        this.Name = "RICHARD";
        this.CaseSensitive = "True";
        this.Author = "João Victor, Eduardo Marques, Gustavo Henrique, Marcos Aldrey, Marcos Vinicius, Alyson, Alisson, André, Gilvaney, Nielson, Lucas Cardoso";
        this.first = new HashMap<>();
        this.first.put("Value", new HashSet<>(Arrays.asList("NumberTerminal", "false", "StringLiteral", "true")));
        this.first.put("Call Arguments", new HashSet<>(Arrays.asList("", "(")));
        this.first.put("Increment-Decrement", new HashSet<>(Arrays.asList("", "++", "--")));
        this.first.put("Class Code", new HashSet<>(Arrays.asList("", "variables", "method")));
        this.first.put("Valid Identifier", new HashSet<>(Arrays.asList("Identifier")));
        this.first.put("Negate Exp", new HashSet<>(Arrays.asList("-", "Identifier", "{", "true", "false", "StringLiteral", "NumberTerminal")));
        this.first.put("Declaration", new HashSet<>(Arrays.asList("void", "int", "float", "string", "Identifier", "bool")));
        this.first.put("Expression", new HashSet<>(Arrays.asList("NumberTerminal", "-", "Identifier", "(", "false", "StringLiteral", "true", "{")));
        this.first.put("Read", new HashSet<>(Arrays.asList("read")));
        this.first.put("Expr Arit", new HashSet<>(Arrays.asList("-", "Identifier", "{", "true", "StringLiteral", "false", "NumberTerminal")));
        this.first.put("Argument", new HashSet<>(Arrays.asList(",", "")));
        this.first.put("Init Array_2", new HashSet<>(Arrays.asList("(")));
        this.first.put("Initialize Constant", new HashSet<>(Arrays.asList("", "=", ",")));
        this.first.put("Variables", new HashSet<>(Arrays.asList("variables")));
        this.first.put("Type", new HashSet<>(Arrays.asList("void", "int", "float", "Identifier", "bool", "string")));
        this.first.put("Multiple Identifier", new HashSet<>(Arrays.asList(",", "")));
        this.first.put("Relational Logic", new HashSet<>(Arrays.asList(">=", "<=", "==", "!=", "", "&&", ">", "<", "||")));
        this.first.put("Method Call", new HashSet<>(Arrays.asList("Identifier")));
        this.first.put("Condition", new HashSet<>(Arrays.asList("Identifier", "(", "false", "NumberTerminal", "-", "StringLiteral", "true", "!", "{")));
        this.first.put("Constant Assignment", new HashSet<>(Arrays.asList("", "void", "int", "float", "bool", "Identifier", "string")));
        this.first.put("Write", new HashSet<>(Arrays.asList("write")));
        this.first.put("Multiple", new HashSet<>(Arrays.asList("/", "*", "")));
        this.first.put("Array", new HashSet<>(Arrays.asList("", "[")));
        this.first.put("Init Array", new HashSet<>(Arrays.asList("{")));
        this.first.put("Line Code", new HashSet<>(Arrays.asList("Identifier", "return", "read", "write")));
        this.first.put("Array Position", new HashSet<>(Arrays.asList("Identifier", "(", "StringLiteral", "NumberTerminal", "", "-", "false", "true", "{")));
        this.first.put("Initialize Variable", new HashSet<>(Arrays.asList(",")));
        this.first.put("Initialize", new HashSet<>(Arrays.asList("", "=")));
        this.first.put("Return Statement", new HashSet<>(Arrays.asList("Identifier", "(", "StringLiteral", "NumberTerminal", "", "-", "false", "!", "true", "{")));
        this.first.put("Program", new HashSet<>(Arrays.asList("class", "const", "")));
        this.first.put("Extends", new HashSet<>(Arrays.asList("", "extends")));
        this.first.put("Variable Assignment", new HashSet<>(Arrays.asList("", "void", "int", "float", "bool", "Identifier", "string")));
        this.first.put("Identifier With Attributes", new HashSet<>(Arrays.asList("Identifier")));
        this.first.put("Read Parameters", new HashSet<>(Arrays.asList(",", "")));
        this.first.put("Parameters", new HashSet<>(Arrays.asList("", "void", "int", "float", "bool", "Identifier", "string")));
        this.first.put("Execute Line", new HashSet<>(Arrays.asList("++", "=", "--", "(")));
        this.first.put("Parameter", new HashSet<>(Arrays.asList(",", "")));
        this.first.put("To-Write", new HashSet<>(Arrays.asList(",", "")));
        this.first.put("Mult Exp", new HashSet<>(Arrays.asList("-", "Identifier", "NumberTerminal", "StringLiteral", "false", "true", "{")));
        this.first.put("Relational Operator", new HashSet<>(Arrays.asList(">=", "<=", "==", ">", "!=", "<")));
        this.first.put("Attribute Access", new HashSet<>(Arrays.asList(".", "")));
        this.first.put("Negate", new HashSet<>(Arrays.asList("!", "")));
        this.first.put("Code Statements", new HashSet<>(Arrays.asList("", "variables", "Identifier", "write", "return", "if", "read", "while")));
        this.first.put("Code Block", new HashSet<>(Arrays.asList("{")));
        this.first.put("Arguments", new HashSet<>(Arrays.asList("", "Identifier", "{", "true", "false", "StringLiteral", "NumberTerminal")));
        this.first.put("Write Parameters", new HashSet<>(Arrays.asList("Identifier", "NumberTerminal", "StringLiteral", "false", "true")));
        this.first.put("Methods", new HashSet<>(Arrays.asList("method")));
        this.first.put("If-Block", new HashSet<>(Arrays.asList("if")));
        this.first.put("Class", new HashSet<>(Arrays.asList("class")));
        this.first.put("Else-Block", new HashSet<>(Arrays.asList("", "else")));
        this.first.put("Post-Else-Block", new HashSet<>(Arrays.asList("", "if", "{")));
        this.first.put("Arithmetic", new HashSet<>(Arrays.asList("", "-", "+")));
        this.first.put("Constants", new HashSet<>(Arrays.asList("const")));
        this.first.put("Logic Operator", new HashSet<>(Arrays.asList("&&", "||")));
        this.first.put("Attributition", new HashSet<>(Arrays.asList("=", "--", "++")));
        this.first.put("Looping-Block", new HashSet<>(Arrays.asList("while")));
        this.first.put("Valid Values", new HashSet<>(Arrays.asList("Identifier", "true", "StringLiteral", "false", "NumberTerminal")));
        this.first.put("Init Array_3", new HashSet<>(Arrays.asList("Identifier", "{", "true", "false", "StringLiteral", "NumberTerminal")));
        this.first.put("Initial Value", new HashSet<>(Arrays.asList("Identifier", "NumberTerminal", "false", "StringLiteral", "true", "{")));
        this.follow = new HashMap<>();
        this.follow.put("Value", new HashSet<>(Arrays.asList(",", "++", "--", "read")));
        this.follow.put("Multiple", new HashSet<>(Arrays.asList("-", "+")));
        this.follow.put("Increment-Decrement", new HashSet<>(Arrays.asList("/", "*", ",", ";")));
        this.follow.put("Class Code", new HashSet<>(Arrays.asList("class")));
        this.follow.put("Valid Identifier", new HashSet<>(Arrays.asList(".", "=", ",", "method")));
        this.follow.put("Negate Exp", new HashSet<>(Arrays.asList("/", "*")));
        this.follow.put("Declaration", new HashSet<>(Arrays.asList("=", ",", "method")));
        this.follow.put("Constant Assignment", new HashSet<>(Arrays.asList("const")));
        this.follow.put("Init Array", new HashSet<>(Arrays.asList("/", "*", ",")));
        this.follow.put("Expr Arit", new HashSet<>(Arrays.asList("!", "float", "void", "=", "(", "bool", "false", "[", "true", "int", "-", ",", "Identifier", "StringLiteral", "NumberTerminal", "string", "{")));
        this.follow.put("Valid Values", new HashSet<>(Arrays.asList("--", "++", "read", ",")));
        this.follow.put("Init Array_2", new HashSet<>(Arrays.asList("{")));
        this.follow.put("Initialize Constant", new HashSet<>(Arrays.asList("Identifier", "void", "int", "float", "string", "const", "bool")));
        this.follow.put("Variables", new HashSet<>(Arrays.asList("read", "while", "Identifier", "method", "if", "variables", "write", "return")));
        this.follow.put("Program", new HashSet<>(Arrays.asList("$")));
        this.follow.put("Multiple Identifier", new HashSet<>(Arrays.asList("=")));
        this.follow.put("Relational Logic", new HashSet<>(Arrays.asList("!", "(", "if", "NumberTerminal", ";", "-", "while", "StringLiteral", "true", "Identifier", "false", "{")));
        this.follow.put("Initialize Variable", new HashSet<>(Arrays.asList("string", "void", "int", "float", "variables", "Identifier", "bool")));
        this.follow.put("Condition", new HashSet<>(Arrays.asList("!", "(", "false", "NumberTerminal", "if", "-", "while", "{", "Identifier", "true", "StringLiteral", ";")));
        this.follow.put("Extends", new HashSet<>(Arrays.asList("class")));
        this.follow.put("Attributition", new HashSet<>(Arrays.asList(";")));
        this.follow.put("Type", new HashSet<>(Arrays.asList("Identifier")));
        this.follow.put("Array", new HashSet<>(Arrays.asList(".", "=", ",", "method")));
        this.follow.put("Arithmetic", new HashSet<>(Arrays.asList("NumberTerminal", "-", "Identifier", "(", "StringLiteral", "false", "true", "{")));
        this.follow.put("Line Code", new HashSet<>(Arrays.asList(";")));
        this.follow.put("Else-Block", new HashSet<>(Arrays.asList("return", "while", "write", "variables", "if", "read", "Identifier")));
        this.follow.put("Parameters", new HashSet<>(Arrays.asList("method")));
        this.follow.put("Parameter", new HashSet<>(Arrays.asList("method")));
        this.follow.put("Relational Operator", new HashSet<>(Arrays.asList("Identifier", "(", "StringLiteral", "NumberTerminal", "-", "false", "!", "true", "{")));
        this.follow.put("Array Position", new HashSet<>(Arrays.asList("[")));
        this.follow.put("Code Statements", new HashSet<>(Arrays.asList("{")));
        this.follow.put("Attribute Access", new HashSet<>(Arrays.asList("++", "=", "--", "(")));
        this.follow.put("Identifier With Attributes", new HashSet<>(Arrays.asList("=", "++", "--", "(")));
        this.follow.put("Read Parameters", new HashSet<>(Arrays.asList("read")));
        this.follow.put("Methods", new HashSet<>(Arrays.asList("method", "variables")));
        this.follow.put("Execute Line", new HashSet<>(Arrays.asList(";")));
        this.follow.put("Code Block", new HashSet<>(Arrays.asList("return", "write", "while", "method", "Identifier", "if", "read", "variables")));
        this.follow.put("To-Write", new HashSet<>(Arrays.asList("write")));
        this.follow.put("Mult Exp", new HashSet<>(Arrays.asList("+", "-")));
        this.follow.put("Write", new HashSet<>(Arrays.asList(";")));
        this.follow.put("If-Block", new HashSet<>(Arrays.asList("while", "Identifier", "read", "if", "variables", "write", "return")));
        this.follow.put("Negate", new HashSet<>(Arrays.asList("{", "-", "Identifier", "(", "true", "false", "StringLiteral", "NumberTerminal")));
        this.follow.put("Expression", new HashSet<>(Arrays.asList("Identifier", "float", "void", "=", "int", "!", "(", "{", "StringLiteral", "const", "string", "true", "-", ",", "+", "false", "[", "NumberTerminal", "bool")));
        this.follow.put("Variable Assignment", new HashSet<>(Arrays.asList("variables")));
        this.follow.put("Read", new HashSet<>(Arrays.asList(";")));
        this.follow.put("Write Parameters", new HashSet<>(Arrays.asList("write")));
        this.follow.put("Initialize", new HashSet<>(Arrays.asList("string", "void", "int", "float", "variables", "Identifier", "bool")));
        this.follow.put("Return Statement", new HashSet<>(Arrays.asList("!", "(", "false", "NumberTerminal", "-", ";", "StringLiteral", "true", "Identifier", "{")));
        this.follow.put("Class", new HashSet<>(Arrays.asList("class", "const")));
        this.follow.put("Method Call", new HashSet<>(Arrays.asList(",", "write")));
        this.follow.put("Post-Else-Block", new HashSet<>(Arrays.asList("Identifier", "while", "read", "if", "return", "write", "variables")));
        this.follow.put("Arguments", new HashSet<>(Arrays.asList("++", "Identifier", "(", "read", "--", ",")));
        this.follow.put("Constants", new HashSet<>(Arrays.asList("class", "const")));
        this.follow.put("Logic Operator", new HashSet<>(Arrays.asList("Identifier", "(", "StringLiteral", "NumberTerminal", "-", "false", "!", "true", "{")));
        this.follow.put("Call Arguments", new HashSet<>(Arrays.asList("++", ",", "--", "read", ";")));
        this.follow.put("Looping-Block", new HashSet<>(Arrays.asList("while", "Identifier", "read", "if", "variables", "write", "return")));
        this.follow.put("Argument", new HashSet<>(Arrays.asList("Identifier", "(")));
        this.follow.put("Init Array_3", new HashSet<>(Arrays.asList("(")));
        this.follow.put("Initial Value", new HashSet<>(Arrays.asList("*", ",", "(", "/", "Identifier")));
    }

    public HashMap<String, List<List<String>>> getProductions() {
        HashMap<String, List<List<String>>> productions = new HashMap<>();
        productions.put("<Value>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("'true'")), new ArrayList<>(Arrays.asList("'false'")), new ArrayList<>(Arrays.asList("StringLiteral")), new ArrayList<>(Arrays.asList("NumberTerminal")))));
        productions.put("<Call Arguments>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("'('", "<Arguments>", "')'")), new ArrayList<>(Arrays.asList("")))));
        productions.put("<Increment-Decrement>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("'++'", "<Increment-Decrement>")), new ArrayList<>(Arrays.asList("'--'", "<Increment-Decrement>")), new ArrayList<>(Arrays.asList("")))));
        productions.put("<Class Code>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("<Variables>", "<Class Code>")), new ArrayList<>(Arrays.asList("<Methods>", "<Class Code>")), new ArrayList<>(Arrays.asList("")))));
        productions.put("<Valid Identifier>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("Identifier", "<Array>")))));
        productions.put("<Negate Exp>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("'-'", "<Initial Value>")), new ArrayList<>(Arrays.asList("<Initial Value>")))));
        productions.put("<Declaration>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("<Type>", "<Valid Identifier>")))));
        productions.put("<Expression>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("'('", "<Expr Arit>", "')'")), new ArrayList<>(Arrays.asList("<Expr Arit>")))));
        productions.put("<Constants>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("'const'", "'{'", "<Constant Assignment>", "'}'")))));
        productions.put("<Expr Arit>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("<Mult Exp>", "<Arithmetic>")))));
        productions.put("<Valid Values>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("<Value>")), new ArrayList<>(Arrays.asList("<Identifier With Attributes>", "<Call Arguments>")))));
        productions.put("<To-Write>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("','", "<Valid Values>", "<To-Write>")), new ArrayList<>(Arrays.asList("','", "<Method Call>", "<To-Write>")), new ArrayList<>(Arrays.asList("")))));
        productions.put("<Initialize Constant>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("<Multiple Identifier>", "'='", "<Expression>", "<Initialize Constant>")), new ArrayList<>(Arrays.asList("")))));
        productions.put("<Variables>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("'variables'", "'{'", "<Variable Assignment>", "'}'")))));
        productions.put("<Write>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("'write'", "'('", "<Write Parameters>", "')'")))));
        productions.put("<Multiple Identifier>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("','", "<Valid Identifier>", "<Multiple Identifier>")), new ArrayList<>(Arrays.asList("")))));
        productions.put("<Argument>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("','", "<Initial Value>", "<Argument>")), new ArrayList<>(Arrays.asList("")))));
        productions.put("<Method Call>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("<Identifier With Attributes>", "'('", "<Arguments>", "')'")))));
        productions.put("<Condition>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("'('", "<Condition>", "')'", "<Relational Logic>")), new ArrayList<>(Arrays.asList("<Negate>", "<Expression>", "<Relational Logic>")))));
        productions.put("<Read>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("'read'", "'('", "<Valid Values>", "<Read Parameters>", "')'")))));
        productions.put("<Attributition>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("'='", "<Expression>")), new ArrayList<>(Arrays.asList("<Increment-Decrement>")))));
        productions.put("<Looping-Block>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("'while'", "'('", "<Condition>", "')'", "<Code Block>")))));
        productions.put("<Array>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("'['", "<Array Position>", "']'", "<Array>")), new ArrayList<>(Arrays.asList("")))));
        productions.put("<Arithmetic>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("'+'", "<Expression>")), new ArrayList<>(Arrays.asList("'-'", "<Expression>")), new ArrayList<>(Arrays.asList("")))));
        productions.put("<Line Code>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("'return'", "<Return Statement>")), new ArrayList<>(Arrays.asList("<Read>")), new ArrayList<>(Arrays.asList("<Write>")), new ArrayList<>(Arrays.asList("<Identifier With Attributes>", "<Execute Line>")))));
        productions.put("<Multiple>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("'*'", "<Expression>")), new ArrayList<>(Arrays.asList("'/'", "<Expression>")), new ArrayList<>(Arrays.asList("")))));
        productions.put("<Else-Block>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("'else'", "<Post-Else-Block>")), new ArrayList<>(Arrays.asList("")))));
        productions.put("<If-Block>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("'if'", "'('", "<Condition>", "')'", "'then'", "<Code Block>", "<Else-Block>")))));
        productions.put("<Code Block>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("'{'", "<Code Statements>", "'}'")))));
        productions.put("<Program>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("<Class>", "<Program>")), new ArrayList<>(Arrays.asList("<Constants>", "<Program>")), new ArrayList<>(Arrays.asList("")))));
        productions.put("<Return Statement>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("<Condition>")), new ArrayList<>(Arrays.asList("'('", "<Return Statement>", "')'")), new ArrayList<>(Arrays.asList("")))));
        productions.put("<Arguments>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("<Initial Value>", "<Argument>")), new ArrayList<>(Arrays.asList("")))));
        productions.put("<Identifier With Attributes>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("<Valid Identifier>", "<Attribute Access>")))));
        productions.put("<Read Parameters>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("','", "<Valid Values>", "<Read Parameters>")), new ArrayList<>(Arrays.asList("")))));
        productions.put("<Code Statements>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("<If-Block>", "<Code Statements>")), new ArrayList<>(Arrays.asList("<Looping-Block>", "<Code Statements>")), new ArrayList<>(Arrays.asList("<Line Code>", "';'", "<Code Statements>")), new ArrayList<>(Arrays.asList("<Variables>", "<Code Statements>")), new ArrayList<>(Arrays.asList("")))));
        productions.put("<Execute Line>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("<Call Arguments>")), new ArrayList<>(Arrays.asList("<Attributition>")))));
        productions.put("<Methods>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("'method'", "<Declaration>", "'('", "<Parameters>", "')'", "<Code Block>")))));
        productions.put("<Attribute Access>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("'.'", "<Identifier With Attributes>")), new ArrayList<>(Arrays.asList("")))));
        productions.put("<Relational Operator>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("'!='")), new ArrayList<>(Arrays.asList("'=='")), new ArrayList<>(Arrays.asList("'<'")), new ArrayList<>(Arrays.asList("'<='")), new ArrayList<>(Arrays.asList("'>'")), new ArrayList<>(Arrays.asList("'>='")))));
        productions.put("<Parameter>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("','", "<Parameters>")), new ArrayList<>(Arrays.asList("")))));
        productions.put("<Parameters>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("<Declaration>", "<Parameter>")), new ArrayList<>(Arrays.asList("")))));
        productions.put("<Negate>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("'!'", "<Negate>")), new ArrayList<>(Arrays.asList("")))));
        productions.put("<Type>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("'string'")), new ArrayList<>(Arrays.asList("'int'")), new ArrayList<>(Arrays.asList("'float'")), new ArrayList<>(Arrays.asList("'bool'")), new ArrayList<>(Arrays.asList("'void'")), new ArrayList<>(Arrays.asList("Identifier")))));
        productions.put("<Variable Assignment>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("<Declaration>", "<Initialize>", "<Initialize Variable>", "';'", "<Variable Assignment>")), new ArrayList<>(Arrays.asList("")))));
        productions.put("<Extends>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("'extends'", "Identifier")), new ArrayList<>(Arrays.asList("")))));
        productions.put("<Write Parameters>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("<Valid Values>", "<To-Write>")), new ArrayList<>(Arrays.asList("<Method Call>", "<To-Write>")))));
        productions.put("<Initialize>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("'='", "<Expression>")), new ArrayList<>(Arrays.asList("")))));
        productions.put("<Initialize Variable>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("<Multiple Identifier>", "<Initialize>")))));
        productions.put("<Class>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("'class'", "Identifier", "<Extends>", "'{'", "<Class Code>", "'}'")))));
        productions.put("<Array Position>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("<Expression>")), new ArrayList<>(Arrays.asList("")))));
        productions.put("<Post-Else-Block>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("<If-Block>")), new ArrayList<>(Arrays.asList("<Code Block>")), new ArrayList<>(Arrays.asList("")))));
        productions.put("<Constant Assignment>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("<Declaration>", "'='", "<Expression>", "<Initialize Constant>", "';'", "<Constant Assignment>")), new ArrayList<>(Arrays.asList("")))));
        productions.put("<Relational Logic>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("<Relational Operator>", "<Condition>")), new ArrayList<>(Arrays.asList("<Logic Operator>", "<Condition>")), new ArrayList<>(Arrays.asList("")))));
        productions.put("<Logic Operator>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("'&&'")), new ArrayList<>(Arrays.asList("'||'")))));
        productions.put("<Mult Exp>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("<Negate Exp>", "<Multiple>")))));
        productions.put("<Init Array_2>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("'('", "<Init Array_3>", "')'")), new ArrayList<>(Arrays.asList("'('", "<Init Array_3>", "')'", "<Init Array_2>")))));
        productions.put("<Init Array>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("'{'", "<Init Array_2>", "'}'")))));
        productions.put("<Init Array_3>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("<Initial Value>")), new ArrayList<>(Arrays.asList("<Initial Value>", "','", "<Init Array_3>")))));
        productions.put("<Initial Value>", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("<Init Array>")), new ArrayList<>(Arrays.asList("<Valid Values>", "<Increment-Decrement>")))));
        return productions;
    }

    public HashMap<String, HashSet<String>> getFirst() {
        return this.first;
    }

    public HashMap<String, HashSet<String>> getFollow() {
        return this.follow;
    }

    public static FirstFollow getInstance() {
        if (instance == null) {
            instance = new FirstFollow();
        }
        return instance;
    }
}
