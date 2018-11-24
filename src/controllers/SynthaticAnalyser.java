package controllers;

import models.value.Lexeme;
import models.value.SynthaticParseErrors;
import models.value.Token;
import util.ChainedCall;
import util.FirstFollow;
import util.SynthaticNode;
import util.TokenType;

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
        this.functions.put("Identifier", tokens -> {
            Token token = tokens.peek();
            if (token != null && token.getType() == TokenType.IDENTIFIER) {
                return new SynthaticNode(tokens.remove());
            }
            return null;
        });
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
                    node.add(this.call("Class Code", tokens).getTokenNode());
                    return node;
                }
            }
            return null;
        });
        this.functions.put("Class Code", tokens -> {
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

    private SynthaticNode automatic(String production, Queue<Token> queue) {
        for (List<String> produces : this.productions.get(production)) {
            SynthaticNode hasConsumed = new SynthaticNode();
            int count = 0;
            for (String derivation : produces) {
                if (this.isProduction(derivation)) {
                    boolean hasError = false;
                    if (this.predict(derivation.replaceAll("[<|>]", ""), queue.peek())) {
                        SynthaticNode synthaticNode = this.automatic(derivation, queue);
                        if (synthaticNode != null) {
                            hasConsumed.add(synthaticNode);
                        } else {
                            hasError = true;
                        }
                    } else if (count >= 1) {
                        hasError = true;
                    }
                    if (hasError) {
                        Token token = queue.peek();
                        Lexeme lexeme = token != null ? token.getLexeme() : null;
                        this.errors.add(new SynthaticParseErrors(FirstFollow.getInstance().getFirst().get(derivation.replaceAll("[<|>]", "")), lexeme));
                    }
                } else {
                    Token token = queue.peek();
                    if (token != null) {
                        if (token.getLexeme().getValue().equals(derivation.replace("\'", ""))) {
                            hasConsumed.add(new SynthaticNode(queue.remove()));
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
            SynthaticNode received = this.automatic("<Program>", queue);
            if (received != null) {
                return received;
            }
            queue.remove();
        }
        return null;
    }
    
    public void showDerivation(SynthaticNode node){
        if (node != null) {
            System.out.println(node.getToken().toString());
        }else{
            System.out.println("Empty");
        }
    }

}
