package controllers;

import models.value.Lexeme;
import models.value.SynthaticParseErrors;
import models.value.Token;
import util.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

public class SynthaticAnalyser extends ChainedCall {

    private static SynthaticAnalyser instance;
    private HashMap<String, List<List<String>>> productions;
    private List<SynthaticParseErrors> errors;

    private SynthaticAnalyser() {
        super();
        this.productions = FirstFollow.getInstance().getProductions();
        this.errors = new ArrayList<>();
        
        /*Connections for terminals that derive from non-terminals*/
        this.functions.put("Identifier", tokens -> {
            Token token = tokens.peek();
            if (token != null && token.getType() == TokenType.IDENTIFIER) {
                return new SynthaticNode(tokens.remove());
            }
            return null;
        });
        
        this.functions.put("StringLiteral", tokens -> {
            Token token = tokens.peek();
            if (token != null && token.getType() == TokenType.STRING) {
                return new SynthaticNode(tokens.remove());
            }
            return null;
        });
        
        this.functions.put("NumberTerminal", tokens -> {
            Token token = tokens.peek();
            if (token != null && token.getType() == TokenType.NUMBER) {
                return new SynthaticNode(tokens.remove());
            }
            return null;
        });
        
        this.functions.put("Type", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                String value = token.getLexeme().getValue();
                
                if (value.equals("string") || value.equals("int") || value.equals("float") || value.equals("bool") || value.equals("void")) {
                    return new SynthaticNode(tokens.remove());
                }else if(token.getType() == TokenType.IDENTIFIER){
                    return new SynthaticNode(tokens.remove());
                }
            }
            return null;
        });
        
        this.functions.put("Value", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                if (token.getType() == TokenType.STRING || token.getType() == TokenType.NUMBER || token.getType() == TokenType.RESERVED) {
                    return new SynthaticNode(tokens.remove());
                }
            }
            return null;
        });
        
        this.functions.put("Relational Operator", tokens -> {
            Token token = tokens.peek();
            if (token != null && token.getType() == TokenType.RELATIONAL) {
                return new SynthaticNode(tokens.remove());
            }
            return null;
        });
        
        this.functions.put("Logic Operator", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                if(token.getType() == TokenType.LOGIC || !token.getLexeme().getValue().equals("!")){
                    return new SynthaticNode(tokens.remove());
                }
            }
            return null;
        });
        
        this.functions.put("Negate", tokens -> {
            Token token = tokens.remove();
            if (token != null) {
                if ("!".equals(token.getLexeme().getValue())) {
                    SynthaticNode node = new SynthaticNode(tokens.remove());
                    node.add(this.call("Negate", tokens).getTokenNode());
                    return node;
                }
            }
            return null;
        });
        
        /*END Terminals*/
        
        this.functions.put("Program", tokens -> {
            while (tokens.size() > 0) {
                SynthaticNode tokenMap = this.call("Class", tokens).call("Constants").getTokenNode();
                if (tokenMap == null) {
                    tokens.remove();
                } else {
                    SynthaticNode map = this.call("Program", tokens).getTokenNode();
                    if (map != null) {
                        tokenMap.add(map);
                    }
                    return tokenMap;
                }
            }
            return null;
        });
        
        this.functions.put("Class", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                if ("class".equals(token.getLexeme().getValue())) {
                    SynthaticNode node = new SynthaticNode(tokens.remove());
                    node.add(this.call("Identifier", tokens).getTokenNode());
                    node.add(this.call("Extends", tokens).getTokenNode());
                    node.add(this.call("Class Code", tokens).getTokenNode());
                    return node;
                }
            }
            return null;
        });
        
        this.functions.put("Extends", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                if ("extends".equals(token.getLexeme().getValue())) {
                    SynthaticNode node = new SynthaticNode(tokens.remove());
                    node.add(this.call("Identifier", tokens).getTokenNode());
                    return node;
                }
            }
            return null;
        });
        
        this.functions.put("Constants", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                if ("const".equals(token.getLexeme().getValue())) {
                    SynthaticNode node = new SynthaticNode(tokens.remove());
                    node.add(this.call("Constant Assignment", tokens).getTokenNode());
                    return node;
                }
            }
            return null;
        });
        
        
        this.functions.put("Constant Assignment", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                SynthaticNode node = new SynthaticNode(tokens.remove());
                node.add(this.call("Declaration", tokens).getTokenNode());
                node.add(this.call("Expression", tokens).getTokenNode());
                node.add(this.call("Initialize Constant", tokens).getTokenNode());
                node.add(this.call("Constant Assignment", tokens).getTokenNode());
                return node;
            }
            return null;
        });
        
        this.functions.put("Declaration", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                SynthaticNode node = new SynthaticNode(tokens.remove());
                node.add(this.call("Type", tokens).getTokenNode());
                node.add(this.call("Valid Identifier").getTokenNode());
                return node;
            }
            return null;
        });
        
        this.functions.put("Expression", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                SynthaticNode node = new SynthaticNode(tokens.remove());
                node.add(this.call("Expr Arit", tokens).getTokenNode());
                return node;
            }
            return null;
        });
        
        this.functions.put("Expr Arit", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                SynthaticNode node = new SynthaticNode(tokens.remove());
                node.add(this.call("Mult Exp", tokens).getTokenNode());
                node.add(this.call("Arithmetic", tokens).getTokenNode());
                return node;
            }
            return null;
        });
        
        this.functions.put("Arithmetic", tokens -> {
            Token token = tokens.peek();
            if (tokens != null) {
                if ("+".equals(token.getLexeme().getValue()) || "-".equals(token.getLexeme().getValue())) {
                    SynthaticNode node = new SynthaticNode(tokens.remove());
                    node.add(this.call("Expression", tokens).getTokenNode());
                    return node;
                }
            }
            return null;
        });
        
        this.functions.put("Mult Exp", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                SynthaticNode node = new SynthaticNode(tokens.remove());
                node.add(this.call("Negate Exp", tokens).getTokenNode());
                node.add(this.call("Expression", tokens).getTokenNode());
                return node;
            }
            return null;
        });
        
        this.functions.put("Negate Exp", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                if("-".equals(token.getLexeme().getValue())){
                    SynthaticNode node = new SynthaticNode(tokens.remove());
                    node.add(this.call("Initial Value", tokens).getTokenNode());
                    return node;
                }
            }
            return null;
        });
        
        this.functions.put("Initial Value", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                SynthaticNode node = new SynthaticNode(tokens.remove());
                node.add(this.call("Init Array", tokens).getTokenNode());
                node.add(this.call("Valid Values", tokens).getTokenNode());
                node.add(this.call("Increment-Decrement", tokens).getTokenNode());
                node.add(this.call("Mehotd Call", tokens).getTokenNode());
                return node;
            }
            return null;
        });
        
        this.functions.put("Init Array", tokens -> {
            Token token = tokens.remove();
            if (token != null) {
                if("{".equals(token.getLexeme().getValue())){
                    SynthaticNode node = new SynthaticNode(tokens.remove());
                    node.add(this.call("Init Array_2", tokens).getTokenNode());
                    return node;
                }
            }
            return null;
        });
        
        this.functions.put("Init Array_2", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                if ("(".equals(token.getLexeme().getValue())) {
                    SynthaticNode node = new SynthaticNode(tokens.remove());
                    node.add(this.call("Init Array_3", tokens).getTokenNode());
                    node.add(this.call("Init Array_2", tokens).getTokenNode());
                    return node;
                }
            }
            return null;
        });
        
        this.functions.put("Init Array_3", tokens -> {
            Token token = tokens.remove();
            if (token != null) {
                SynthaticNode node = new SynthaticNode(tokens.remove());
                node.add(this.call("Initial Value", tokens).getTokenNode());
                node.add(this.call("Init Array_3", tokens).getTokenNode());
                return node;
            }
            return null;
        });
        
        this.functions.put("Initialize Constant", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                SynthaticNode node = new SynthaticNode(tokens.remove());
                node.add(this.call("Multiple Identifier", tokens).getTokenNode());
                node.add(this.call("Expression", tokens).getTokenNode());
                node.add(this.call("Initialize Constant", tokens).getTokenNode());
                return node;
            }
            return null;
        });
        
        this.functions.put("Class Code", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                SynthaticNode node = new SynthaticNode(tokens.remove());
                node.add(this.call("Variables", tokens).getTokenNode());
                node.add(this.call("Class Code", tokens).getTokenNode());
                node.add(this.call("Methods", tokens).getTokenNode());
                node.add(this.call("Class Code", tokens).getTokenNode());
                return node;
            }
            return null;
        });
        
        this.functions.put("Variables", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                if ("variables".equals(token.getLexeme().getValue())) {
                    SynthaticNode node = new SynthaticNode(tokens.remove());
                    node.add(this.call("Variable Assignment", tokens).getTokenNode());
                    return node;
                }
            }
            return null;
        });
        
        this.functions.put("Variable Assignment", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                SynthaticNode node = new SynthaticNode(tokens.remove());
                node.add(this.call("Declaration", tokens).getTokenNode());
                node.add(this.call("Initialize", tokens).getTokenNode());
                node.add(this.call("Initialize Variable", tokens).getTokenNode());
                node.add(this.call("Variable Assignment", tokens).getTokenNode());
                return node;
            }
            return null;
        });
        
        this.functions.put("Initialize", tokens -> {
            Token token = tokens.remove();
            if (token != null) {
                if ("=".equals(token.getLexeme().getValue())) {
                    SynthaticNode node = new SynthaticNode(tokens.remove());
                    node.add(this.call("Expression", tokens).getTokenNode());
                    return node;
                }
            }
            return null;
        });
        
        this.functions.put("Initialize Variable", tokens -> {
            Token token  = tokens.peek();
            if (token != null) {
                SynthaticNode node = new SynthaticNode(tokens.remove());
                node.add(this.call("Multiple Identifier", tokens).getTokenNode());
                node.add(this.call("Initialize", tokens).getTokenNode());
                return node;
            }
            return null;
        });
        
        this.functions.put("Methods", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                if ("method".equals(token.getLexeme().getValue())) {
                    SynthaticNode node = new SynthaticNode(tokens.remove());
                    node.add(this.call("Declaration", tokens).getTokenNode());
                    node.add(this.call("Parameters", tokens).getTokenNode());
                    node.add(this.call("Code Block", tokens).getTokenNode());
                    return node;
                }
            }
            return null;
        });
        
        this.functions.put("Code Block", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                SynthaticNode node = new SynthaticNode(tokens.remove());
                node.add(this.call("Code Statements", tokens).getTokenNode());
                return node;
            }
            return null;
        });
        
        this.functions.put("Code Statements", tokens -> {
            Token token = tokens.peek();
            if(token != null){
                SynthaticNode node = new SynthaticNode(tokens.remove());
                node.add(this.call("If-Block", tokens).getTokenNode());
                node.add(this.call("Code Statements", tokens).getTokenNode());
                node.add(this.call("Looping-Block", tokens).getTokenNode());
                node.add(this.call("Line Code", tokens).getTokenNode());
                node.add(this.call("Variables", tokens).getTokenNode());
                return node;
            }
            return null;
        });

        this.functions.put("If-Block", tokens -> {
            return null;
        });
        
        this.functions.put("Looping-Block", tokens -> {
            return null;
        });
        
        this.functions.put("Line Code", tokens -> {
            return null;
        });
    }

    public static SynthaticAnalyser getInstance() {
        if (instance == null) {
            instance = new SynthaticAnalyser();
        }
        return instance;
    }

    public SynthaticNode start(Queue<Token> queue) {
        return this.functions.get("Program").run(queue);
    }

    private boolean isProduction(String name) {
        return name.matches("<.*>");
    }

    private boolean isSynchronizationToken(Token token, String derivation) {
        return this.follow.get(derivation.replaceAll("[<|>]", "")).contains(token.getLexeme().getValue())
                || TokensInformation.getInstance().delimiters().contains(token.getLexeme().getValue());
    }

    private SynthaticNode automatic(String production, Queue<Token> queue) {
        for (List<String> produces : this.productions.get(production)) {
            SynthaticNode hasConsumed = new SynthaticNode();
            int count = 0;
            for (String derivation : produces) {
                System.out.println("Entering Production: " + queue.peek() + " __" + production + " deriv." + derivation);
                if (this.isProduction(derivation)) {
                    boolean hasError = false;
                    if (this.predict(derivation.replaceAll("[<|>]", ""), queue.peek())) {
                        SynthaticNode synthaticNode = this.automatic(derivation, queue);
                        if (synthaticNode != null) {
                            hasConsumed.add(synthaticNode);
                        } else if (!this.first.get(production.replaceAll("[<|>]", "")).contains("")) {
                            hasError = true;
                        }
                    } else if (count >= 1) {
                        hasError = true;
                        Token consume = queue.peek();
                        while (consume != null && !this.isSynchronizationToken(consume, derivation)) {
                            System.out.println("Consumed " + derivation + "__> " + queue.remove() + " __" + production);
                            //queue.remove();
                            consume = queue.peek();
                        }
                        if (queue.peek() != null) {
                            queue.remove();
                        }
                    }
                    if (hasError) {
                        System.out.println(production + " --> " + queue.peek() + " __" + derivation);
                        Token token = queue.peek();
                        Lexeme lexeme = token != null ? token.getLexeme() : null;
                        this.errors.add(new SynthaticParseErrors(this.first.get(derivation.replaceAll("[<|>]", "")), lexeme));
                    }
                } else {
                    Token token = queue.peek();
                    if (token != null) {
                        /*treatment to errors in identifier and other types*/
                        if (token.getLexeme().getValue().equals(derivation.replace("\'", ""))) {
                            hasConsumed.add(new SynthaticNode(queue.remove()));
                        } else if (this.predict(derivation.replace("\'", ""), token)) {
                            hasConsumed.add(new SynthaticNode(queue.remove()));
                        } else if ("".equals(derivation)) {
                            hasConsumed.add(new SynthaticNode());
                            count += 1;
                            break;
                        }
                    }
                }
                count += 1;
                if (hasConsumed.isEmpty()) {
                    break;
                }
            }
            if (!hasConsumed.isEmpty() && count != produces.size()) {
                return null;
            } else if (!hasConsumed.isEmpty()) {
                return hasConsumed;
            }
        }
        return null;
    }

    public SynthaticNode startAutomatic(Queue<Token> queue) {
        while (!queue.isEmpty()) {
            SynthaticNode received = this.automatic(FirstFollow.getInstance().StartSymbol, queue);
            if (received != null) {
                return received;
            }
            queue.remove();
        }
        return null;
    }

    public void showDerivation(SynthaticNode node) {
        if (node != null) {
            if (node.getNodeList().isEmpty()) {
                System.out.println(node.getToken() != null ? node.getToken() : "Empty");
            } else {
                for (SynthaticNode synthaticNode : node.getNodeList()) {
                    this.showDerivation(synthaticNode);
                }
            }
        }
    }
}
