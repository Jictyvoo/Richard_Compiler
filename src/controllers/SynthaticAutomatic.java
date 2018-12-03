package controllers;

import models.value.Lexeme;
import models.value.SynthaticParseErrors;
import models.value.Token;
import util.ChainedCall;
import util.FirstFollow;
import util.SynthaticNode;
import util.TokensInformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

public class SynthaticAutomatic extends ChainedCall {

    private static SynthaticAutomatic instance;
    private HashMap<String, List<List<String>>> productions;
    private List<SynthaticParseErrors> errors;

    private SynthaticAutomatic() {
        super();
        this.productions = FirstFollow.getInstance().getProductions();
        this.errors = new ArrayList<>();
    }

    public static SynthaticAutomatic getInstance() {
        if (instance == null) {
            instance = new SynthaticAutomatic();
        }
        return instance;
    }

    private boolean isProduction(String name) {
        return name.matches("<.*>");
    }

    private boolean isSynchronizationToken(Token token, String derivation) {
        return this.follow.get(derivation.replaceAll("[<|>]", "")).contains(token.getLexeme().getValue())
                || TokensInformation.getInstance().delimiters().contains(token.getLexeme().getValue());
    }

    private boolean isLastEmpty(SynthaticNode synthaticNode) {
        return synthaticNode.isEmpty();
    }

    private SynthaticNode automatic(String production, Queue<Token> queue) {
        if (queue.peek() == null) {
            if (production.equals(FirstFollow.getInstance().StartSymbol)) {
                return new SynthaticNode();
            }
            return null;
        }
        int index = 0;
        for (List<String> produces : this.productions.get(production)) {
            SynthaticNode hasConsumed = new SynthaticNode();
            int count = 0;
            for (String derivation : produces) {
                /*System.out.println("Entering Production: " + queue.peek() + " __" + production + " deriv." + derivation);*/
                if (this.isProduction(derivation)) {    /*has a problem when receiving a node with empty content*/
                    boolean hasError = false;
                    if (this.predict(derivation.replaceAll("[<|>]", ""), queue.peek())) {
                        SynthaticNode synthaticNode = this.automatic(derivation, queue);
                        if (synthaticNode != null) {
                            if (isLastEmpty(synthaticNode) && index < this.productions.get(production).size() - 1) {
                                count += 1;
                                continue;
                            }
                            hasConsumed.add(synthaticNode);
                        } else if (!this.first.get(derivation.replaceAll("[<|>]", "")).contains("")) {
                            hasError = true;
                        }
                    } else if (count >= 1) {
                        hasError = true;
                        Token consume = queue.peek();
                        while (consume != null && !this.isSynchronizationToken(consume, derivation)) {
                            queue.remove();
                            consume = queue.peek();
                            this.errors.add(new SynthaticParseErrors(this.first.get(production.replaceAll("[<|>]", "")), consume != null ? consume.getLexeme() : null));
                        }
                        if (queue.peek() != null) {
                            consume = queue.remove();
                            this.errors.add(new SynthaticParseErrors(this.first.get(production.replaceAll("[<|>]", "")), consume != null ? consume.getLexeme() : null));
                        }
                    }
                    if (hasError) {
                        Token token = queue.peek();
                        Lexeme lexeme = token != null ? token.getLexeme() : null;
                        this.errors.add(new SynthaticParseErrors(this.first.get(derivation.replaceAll("[<|>]", "")), lexeme));
                    }
                } else {
                    Token token = queue.peek();
                    if (token != null) {
                        if (token.getLexeme().getValue().equals(derivation.replace("\'", ""))) {
                            hasConsumed.add(new SynthaticNode(queue.remove()));
                        } else if (this.predict(derivation.replace("\'", ""), token)) {
                            hasConsumed.add(new SynthaticNode(queue.remove()));
                        } else if ("".equals(derivation)) {
                            hasConsumed.add(new SynthaticNode());
                            count += 1;
                            continue;
                        } else if (count > 0) {
                            if (count == 1 && this.first.get(production.replaceAll("[<|>]", "")).contains("")) {
                                break;
                            } else {
                                this.errors.add(new SynthaticParseErrors(this.first.get(production.replaceAll("[<|>]", "")), token.getLexeme()));
                                return null;
                            }
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
            index += 1;
        }
        return null;
    }

    public SynthaticNode start(Queue<Token> queue) {
        while (!queue.isEmpty()) {
            SynthaticNode received = this.automatic(FirstFollow.getInstance().StartSymbol, queue);
            if (received != null) {
                return received;
            }
            queue.remove();
        }
        return null;
    }

    public List<SynthaticParseErrors> getErrors() {
        return errors;
    }

    public void clearErrors() {
        this.errors = new ArrayList<>();
    }
}
