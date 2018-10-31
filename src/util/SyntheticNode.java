package util;

import models.value.Token;

import java.util.ArrayList;
import java.util.List;

public class SyntheticNode {
    private List<SyntheticNode> nodeList;
    private Token token;

    public SyntheticNode(Token token) {
        this.nodeList = new ArrayList<>();
        this.token = token;
    }

    public void add(SyntheticNode syntheticNode) {
        if (syntheticNode != null) {
            this.nodeList.add(syntheticNode);
        }
    }

    public Token getToken() {
        return token;
    }

    public List<SyntheticNode> getNodeList() {
        return nodeList;
    }
}
