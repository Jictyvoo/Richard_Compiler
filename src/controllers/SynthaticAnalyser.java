package controllers;

import models.value.Token;
import util.ChainedCall;
import util.SynthaticNode;
import util.TokenType;

import java.util.Queue;

public class SynthaticAnalyser extends ChainedCall {

    private static SynthaticAnalyser instance;

    private SynthaticAnalyser() {
        super();
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

}
