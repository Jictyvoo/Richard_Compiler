package util;

import models.value.Token;

import java.util.ArrayList;
import java.util.List;

public class SynthaticNode {
    private List<SynthaticNode> nodeList;
    private Token token;

    public SynthaticNode(Token token) {
        this.nodeList = new ArrayList<>();
        this.token = token;
    }

    public void add(SynthaticNode synthaticNode) {
        if (synthaticNode != null) {
            this.nodeList.add(synthaticNode);
        }
    }

    public Token getToken() {
        return token;
    }

    public List<SynthaticNode> getNodeList() {
        return nodeList;
    }
}