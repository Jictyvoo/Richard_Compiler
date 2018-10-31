package models.business;

import models.value.Token;
import util.ChainedCall;

import java.util.List;
import java.util.Map;

public class LambdaFunctions extends ChainedCall {

    private static LambdaFunctions instance;

    private LambdaFunctions() {
        super();
        this.functions.put("Program", tokens -> {
            while (tokens.size() > 0) {
                Map<String, Token> tokenMap = this.call("Class", tokens).call("Constants").getTokenMap();
                if (tokenMap == null) {
                    tokens.remove(0);
                } else {
                    Map<String, Token> map = this.call("Program", tokens).getTokenMap();
                    if (map != null) {
                        tokenMap.putAll(map);
                    }
                    return tokenMap;
                }
            }
            return null;
        });
    }

    public static LambdaFunctions getInstance() {
        if (instance == null) {
            instance = new LambdaFunctions();
        }
        return instance;
    }

}
