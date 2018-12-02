package controllers;

import models.value.SynthaticParseErrors;
import models.value.Token;
import util.ChainedCall;
import util.SynthaticNode;
import util.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class SynthaticAnalyser extends ChainedCall {

    private static SynthaticAnalyser instance;
    private List<SynthaticParseErrors> errors;

    private SynthaticAnalyser() {
        super();
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
                } else if (token.getType() == TokenType.IDENTIFIER) {
                    return new SynthaticNode(tokens.remove());
                }
            }
            return null;
        });

        this.functions.put("Value", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                if (token.getType() == TokenType.STRING || token.getType() == TokenType.NUMBER) {
                    return new SynthaticNode(tokens.remove());
                } else if (token.getLexeme().getValue().equals("true") || token.getLexeme().getValue().equals("false")) {
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
                if (token.getType() == TokenType.LOGIC || !token.getLexeme().getValue().equals("!")) {
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
                    if (node != null) {
                        if (tokens.peek().getLexeme().getValue().equals("{")) {
                            node.add(new SynthaticNode(tokens.remove()));
                            node.add(this.call("Constant Assignment", tokens).getTokenNode());
                            if (node != null) {
                                if (tokens.peek() != null && tokens.peek().getLexeme().getValue().equals("}")) {
                                    node.add(new SynthaticNode(tokens.remove()));
                                    return node;
                                }
                            }
                        }
                    }
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

        this.functions.put("Valid Identifier", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                if ("Identifier".equals(token.getLexeme().getValue())) {
                    SynthaticNode node = new SynthaticNode(tokens.remove());
                    node.add(this.call("Array", tokens).getTokenNode());
                    return node;
                }
            }
            return null;
        });

        this.functions.put("Identifier With Attributes", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                SynthaticNode node = new SynthaticNode(tokens.remove());
                node.add(this.call("Valid Identifier", tokens).getTokenNode());
                node.add(this.call("Attribute Access", tokens).getTokenNode());
                return node;
            }
            return null;
        });

        this.functions.put("Attribute Access", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                if (".".equals(token.getLexeme().getValue())) {
                    SynthaticNode node = new SynthaticNode(tokens.remove());
                    node.add(this.call("Identifier With Attributes", tokens).getTokenNode());
                    return node;
                }
            }
            return null;
        });

        this.functions.put("Expression", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                if (token.getLexeme().getValue().equals("(")) {
                    SynthaticNode node = new SynthaticNode(tokens.remove());
                    node.add(this.call("Expr Arit", tokens).getTokenNode());
                    if (node != null && tokens.peek().getLexeme().getValue().equals(")")) {
                        node.add(new SynthaticNode(tokens.remove()));
                        return node;
                    }
                } else {
                    SynthaticNode node = new SynthaticNode(tokens.remove());
                    node.add(this.call("Expr Arit", tokens).getTokenNode());
                    return node;
                }
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
                node.add(this.call("Multiple", tokens).getTokenNode());
                return node;
            }
            return null;
        });

        this.functions.put("Multiple", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                if ("*".equals(token.getLexeme().getValue()) || "/".equals(token.getLexeme().getValue())) {
                    SynthaticNode node = new SynthaticNode(tokens.remove());
                    node.add(this.call("Expression", tokens).getTokenNode());
                    return node;
                }
            }
            return null;
        });

        this.functions.put("Negate Exp", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                if ("-".equals(token.getLexeme().getValue())) {
                    SynthaticNode node = new SynthaticNode(tokens.remove());
                    node.add(this.call("Initial Value", tokens).getTokenNode());
                    return node;
                } else {
                    SynthaticNode node = new SynthaticNode(tokens.remove());
                    node.add(this.call("Initial Value", tokens).getTokenNode());
                    return node;
                }
            }
            return null;
        });

        this.functions.put("Relational Logic", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                SynthaticNode node = new SynthaticNode(tokens.remove());
                node.add(this.call("Relational Operator", tokens).getTokenNode());
                node.add(this.call("Condition", tokens).getTokenNode());
                node.add(this.call("Logic Operator", tokens).getTokenNode());
            }
            return null;
        });

        this.functions.put("Condition", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                if ("(".equals(token.getLexeme().getValue())) {
                    SynthaticNode node = new SynthaticNode(tokens.remove());
                    node.add(this.call("Condition", tokens).getTokenNode());

                    if (node != null && tokens.peek().getLexeme().getValue().equals(")")) {
                        node.add(new SynthaticNode(tokens.remove()));
                        node.add(this.call("Relational Logic", tokens).getTokenNode());
                        return node;
                    }
                } else {
                    SynthaticNode node = new SynthaticNode(tokens.remove());
                    node.add(this.call("Negate", tokens).getTokenNode());
                    node.add(this.call("Expression", tokens).getTokenNode());
                    node.add(this.call("Relational Logic", tokens).getTokenNode());
                    return node;
                }
            }
            return null;
        });

        this.functions.put("Initial Value", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                SynthaticNode node = new SynthaticNode(tokens.remove());
                node.add(this.call("Valid Values", tokens).getTokenNode());
                node.add(this.call("Increment-Decrement", tokens).getTokenNode());
                return node;
            }
            return null;
        });

        this.functions.put("Increment-Decrement", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                if (token.getLexeme().getValue().equals("++") || token.getLexeme().getValue().equals("--")) {
                    SynthaticNode node = new SynthaticNode(tokens.remove());
                    node.add(this.call("Increment-Decrement", tokens).getTokenNode());
                    return node;
                }
            }
            return null;
        });

        this.functions.put("Valid Values", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                if (token.getType() == TokenType.STRING || token.getType() == TokenType.NUMBER) {
                    SynthaticNode node = new SynthaticNode(tokens.remove());
                    node.add(this.call("Value", tokens).getTokenNode());
                    return node;
                } else if (token.getLexeme().getValue().equals("true") || token.getLexeme().getValue().equals("false")) {
                    SynthaticNode node = new SynthaticNode(tokens.remove());
                    node.add(this.call("Value", tokens).getTokenNode());
                    return node;
                } else {
                    SynthaticNode node = new SynthaticNode(tokens.remove());
                    node.add(this.call("Identifier With Attributes", tokens).getTokenNode());
                    node.add(this.call("Call Arguments", tokens).getTokenNode());
                    return node;
                }
            }
            return null;
        });

        this.functions.put("Call Arguments", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                if ("(".equals(token.getLexeme().getValue())) {
                    SynthaticNode node = new SynthaticNode(tokens.remove());
                    node.add(this.call("Arguments", tokens).getTokenNode());
                    if (tokens.peek().getLexeme().getValue().equals(")")) {
                        node.add(new SynthaticNode(tokens.remove()));
                        return node;
                    }
                }
            }
            return null;
        });

        this.functions.put("Arguments", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                SynthaticNode node = new SynthaticNode(tokens.remove());
                node.add(this.call("Initial Value", tokens).getTokenNode());
                node.add(this.call("Argument", tokens).getTokenNode());
                return node;
            }
            return null;
        });

        this.functions.put("Array", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                if ("[".equals(token.getLexeme().getValue())) {
                    SynthaticNode node = new SynthaticNode(tokens.remove());
                    node.add(this.call("Array Position", tokens).getTokenNode());
                    if (node != null) {
                        if (tokens.peek().getLexeme().getValue().equals("]")) {
                            node.add(new SynthaticNode(tokens.remove()));
                            node.add(this.call("Array", tokens).getTokenNode());
                            return node;
                        }
                    }
                }
            }
            return null;
        });

        this.functions.put("Array Position", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                SynthaticNode node = new SynthaticNode(tokens.remove());
                node.add(this.call("Expression", tokens).getTokenNode());
                return node;
            }
            return null;
        });

        this.functions.put("Init Array", tokens -> {
            Token token = tokens.remove();
            if (token != null) {
                if ("{".equals(token.getLexeme().getValue())) {
                    SynthaticNode node = new SynthaticNode(tokens.remove());
                    node.add(this.call("Init Array_2", tokens).getTokenNode());
                    if (node != null) {
                        if (tokens.peek().getLexeme().getValue().equals("}")) {
                            node.add(new SynthaticNode(tokens.remove()));
                            return node;
                        }
                    }
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
                    if (node != null) {
                        if (tokens.peek().getLexeme().getValue().equals(")")) {
                            node.add(new SynthaticNode(tokens.remove()));
                            if (tokens.peek().getLexeme().getValue().equals("(")) {
                                node.add(new SynthaticNode(tokens.remove()));
                                node.add(this.call("Init Array_2", tokens).getTokenNode());
                                if (tokens.peek().getLexeme().getValue().equals(")")) {
                                    node.add(new SynthaticNode(tokens.remove()));
                                    return node;
                                }
                            }
                            return node;
                        }
                    }
                }
            }
            return null;
        });

        this.functions.put("Init Array_3", tokens -> {
            Token token = tokens.remove();
            if (token != null) {
                SynthaticNode node = new SynthaticNode(tokens.remove());
                node.add(this.call("Initial Value", tokens).getTokenNode());
                if (node != null) {
                    if (tokens.peek().getLexeme().getValue().equals(",")) {
                        node.add(new SynthaticNode(tokens.remove()));
                        node.add(this.call("Init Array_3", tokens).getTokenNode());
                        return node;
                    }
                    return node;
                }
            }
            return null;
        });

        this.functions.put("Initialize Constant", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                SynthaticNode node = new SynthaticNode(tokens.remove());
                node.add(this.call("Multiple Identifier", tokens).getTokenNode());
                if (node != null) {
                    if (tokens.peek() != null && tokens.peek().getLexeme().getValue().equals("=")) {
                        node.add(new SynthaticNode(tokens.remove()));
                        node.add(this.call("Expression", tokens).getTokenNode());
                        node.add(this.call("Initialize Constant", tokens).getTokenNode());
                        return node;
                    }
                }
            }
            return null;
        });

        this.functions.put("Class Code", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                SynthaticNode node = new SynthaticNode(tokens.remove());
                if (token.getLexeme().getValue().equals("variables")) {
                    node.add(this.call("Variables", tokens).getTokenNode());
                    node.add(this.call("Class Code", tokens).getTokenNode());
                    return node;
                } else {
                    node.add(this.call("Methods", tokens).getTokenNode());
                    node.add(this.call("Class Code", tokens).getTokenNode());
                    return node;
                }
            }
            return null;
        });

        this.functions.put("Variables", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                if ("variables".equals(token.getLexeme().getValue())) {
                    SynthaticNode node = new SynthaticNode(tokens.remove());
                    if (tokens.peek().getLexeme().getValue().equals("{")) {
                        node.add(new SynthaticNode(tokens.remove()));
                        node.add(this.call("Variable Assignment", tokens).getTokenNode());
                        if (tokens.peek().getLexeme().getValue().equals("}")) {
                            node.add(new SynthaticNode(tokens.remove()));
                            return node;
                        }
                    }
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
                if (node != null) {
                    if (tokens.peek().getLexeme().getValue().equals(";")) {
                        node.add(new SynthaticNode(tokens.remove()));
                        node.add(this.call("Variable Assignment", tokens).getTokenNode());
                        return node;
                    }
                }
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
            Token token = tokens.peek();
            if (token != null) {
                SynthaticNode node = new SynthaticNode(tokens.remove());
                node.add(this.call("Multiple Identifier", tokens).getTokenNode());
                node.add(this.call("Initialize", tokens).getTokenNode());
                return node;
            }
            return null;
        });

        this.functions.put("Multiple Identifier", tokens -> {
            Token token = tokens.peek();
            if (token != null) {
                if (",".equals(token.getLexeme().getValue())) {
                    SynthaticNode node = new SynthaticNode(tokens.remove());
                    node.add(this.call("Valid Identifier", tokens).getTokenNode());
                    node.add(this.call("Multiple Identifier", tokens).getTokenNode());
                    return node;
                }
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
            if (token != null) {
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
}
