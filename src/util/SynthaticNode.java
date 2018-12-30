package util;

import models.value.Token;

import java.util.ArrayList;
import java.util.List;

public class SynthaticNode {
    private List<SynthaticNode> nodeList;
    private Token token;
    private boolean empty;
    private String production;

    public SynthaticNode(Token token, String production) {
        this.nodeList = new ArrayList<>();
        this.token = token;
        empty = false;
        this.production = production;
    }

    public SynthaticNode() {
        this.nodeList = new ArrayList<>();
        this.token = null;
        empty = true;
    }

    public SynthaticNode(String production) {
        this.nodeList = new ArrayList<>();
        this.token = null;
        empty = true;
        this.production = production;
    }

    public void add(SynthaticNode synthaticNode) {
        if (synthaticNode != null) {
            if (this.empty) {
                this.empty = false;
            }
            this.nodeList.add(synthaticNode);
        }
    }

    public Token getToken() {
        return token;
    }

    public List<SynthaticNode> getNodeList() {
        return nodeList;
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public String getProduction() {
        return production;
    }

    @Override
    public String toString() {
        return "SynthaticNode{" + nodeList + ", " + production + '}';
    }
}
