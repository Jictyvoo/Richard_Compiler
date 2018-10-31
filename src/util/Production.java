package util;

import models.value.Token;

import java.util.List;
import java.util.Map;

public interface Production {
    Map<String, Token> run(List<Token> tokens);
}
