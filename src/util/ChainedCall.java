package util;

import models.value.Token;

import java.util.HashMap;
import java.util.Queue;

public abstract class ChainedCall {

    private Queue<Token> tokenList;
    private SyntheticNode tokenNode;
    protected HashMap<String, Production> functions;

    public ChainedCall() {
        this.tokenList = null;
        this.functions = new HashMap<>();
    }

    protected ChainedCall call(String productionName, Queue<Token> tokenList) {
        this.tokenList = tokenList;
        this.tokenNode = null;
        if (this.functions.containsKey(productionName)) {
            this.tokenNode = this.functions.get(productionName).run(tokenList);
        }
        return this;
    }

    public ChainedCall call(String productionName) {
        if (this.tokenNode == null && this.tokenList != null && this.functions.containsKey(productionName)) {
            SyntheticNode map = this.functions.get(productionName).run(tokenList);
            if (map != null) {
                this.tokenNode = map;
            }
        }
        return this;
    }

    public SyntheticNode getTokenNode() {
        return tokenNode;
    }
}
