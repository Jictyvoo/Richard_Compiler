package util;

import models.value.Token;

import java.util.Queue;

public interface Production {
    SynthaticNode run(Queue<Token> tokens);
}
