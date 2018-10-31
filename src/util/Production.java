package util;

import models.value.Token;

import java.util.Queue;

public interface Production {
    SyntheticNode run(Queue<Token> tokens);
}
