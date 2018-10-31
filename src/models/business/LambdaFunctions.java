package models.business;

import models.value.Token;
import util.ChainedCall;
import util.SyntheticNode;
import util.TokenType;

import java.util.Queue;

public class LambdaFunctions extends ChainedCall {

    private static LambdaFunctions instance;

    private LambdaFunctions() {
        super();
        this.functions.put("Identifier", tokens -> {
            Token token = tokens.peek();
            if (token != null && token.getType() == TokenType.IDENTIFIER) {
                return new SyntheticNode(tokens.remove());
            }
            return null;
        });
        this.functions.put("Program", tokens -> {
            while (tokens.size() > 0) {
                SyntheticNode tokenMap = this.call("Class", tokens).call("Constants").getTokenNode();
                if (tokenMap == null) {
                    tokens.remove();
                } else {
                    SyntheticNode map = this.call("Program", tokens).getTokenNode();
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
                    SyntheticNode node = new SyntheticNode(tokens.remove());
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

    public static LambdaFunctions getInstance() {
        if (instance == null) {
            instance = new LambdaFunctions();
        }
        return instance;
    }

    public SyntheticNode start(Queue<Token> queue) {
        return this.call("Program", queue).getTokenNode();
    }

}
