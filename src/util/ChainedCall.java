package util;

import models.value.Token;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;

public abstract class ChainedCall {

    private Queue<Token> tokenList;
    private SynthaticNode tokenNode;
    private HashMap<String, HashSet<String>> first;
    protected HashMap<String, Production> functions;

    public ChainedCall() {
        this.tokenList = null;
        this.functions = new HashMap<>();
        this.first = FirstFollow.getInstance().getFirst();
    }

    private boolean predict(String productionName, Token token) {   /*Something is wrong with predict*/
        if (token != null) {
            if (this.first.get(productionName) != null) {
                return this.first.get(productionName).contains(token.getLexeme().getValue());
            } else if ("NumberTerminal".equals(productionName) && token.getType() == TokenType.NUMBER) {
                return true;
            } else if ("Identifier".equals(productionName) && token.getType() == TokenType.IDENTIFIER) {
                return true;
            } else return "StringLiteral".equals(productionName) && token.getType() == TokenType.STRING;
        }
        return false;
    }

    protected ChainedCall call(String productionName, Queue<Token> tokenList) {
        this.tokenList = tokenList;
        this.tokenNode = null;
        if (this.functions.containsKey(productionName) && this.predict(productionName, tokenList.peek())) {
            this.tokenNode = this.functions.get(productionName).run(tokenList);
        }
        return this;
    }

    public ChainedCall call(String productionName) {
        if (this.tokenNode == null && this.tokenList != null && this.functions.containsKey(productionName)) {
            if (this.predict(productionName, this.tokenList.peek())) {
                SynthaticNode map = this.functions.get(productionName).run(tokenList);
                if (map != null) {
                    this.tokenNode = map;
                }
            }
        }
        return this;
    }

    public SynthaticNode getTokenNode() {
        return tokenNode;
    }
}