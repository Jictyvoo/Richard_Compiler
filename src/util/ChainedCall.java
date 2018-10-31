package util;

import models.value.Token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ChainedCall {

    private List<Token> tokenList;
    private Map<String, Token> tokenMap;
    protected HashMap<String, Production> functions;

    public ChainedCall() {
        this.tokenList = null;
        this.functions = new HashMap<>();
    }

    public ChainedCall call(String productionName, List<Token> tokenList) {
        this.tokenList = tokenList;
        this.tokenMap = null;
        if (this.functions.containsKey(productionName)) {
            this.tokenMap = this.functions.get(productionName).run(tokenList);
        }
        return this;
    }

    public ChainedCall call(String productionName) {
        if (this.tokenMap == null && this.tokenList != null && this.functions.containsKey(productionName)) {
            Map<String, Token> map = this.functions.get(productionName).run(tokenList);
            if (map != null) {
                this.tokenMap = map;
            }
        }
        return this;
    }

    public Map<String, Token> getTokenMap() {
        return tokenMap;
    }
}
