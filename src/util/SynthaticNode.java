package util;

import models.value.Token;

import java.util.ArrayList;
import java.util.List;

public class SynthaticNode {
    private List<SynthaticNode> nodeList;
    private Token token;
    private boolean empty;

    public SynthaticNode(Token token) {
        this.nodeList = new ArrayList<>();
        this.token = token;
        empty = false;
    }

    public SynthaticNode() {
        this.nodeList = new ArrayList<>();
        this.token = null;
        empty = true;
    }

    public void add(SynthaticNode synthaticNode) {
        if (synthaticNode != null) {
            if(this.empty){
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
}
