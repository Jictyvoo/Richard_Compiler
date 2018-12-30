package controllers;

import models.value.Lexeme;
import models.value.SemanticParseErrors;
import models.value.Token;
import util.SynthaticNode;

import java.util.*;

public class SemanticAnalyser {
    private static SemanticAnalyser instance;
    private HashSet<SynthaticNode> postAnalysis;
    private HashSet<SynthaticNode> alreadyAnalyzed;
    private List<SemanticParseErrors> errors;
    private HashMap<String, String> inheritance;
    private HashSet<String> validTypes;
    private HashSet<String> classNames;
    private HashMap<String, String> globalConstants;
    private HashMap<SynthaticNode, HashMap<String, String>> scopeElements;

    static SemanticAnalyser getInstance() {
        if (instance == null) {
            instance = new SemanticAnalyser();
        }
        return instance;
    }

    private SemanticAnalyser() {
        this.inheritance = new HashMap<>();
        this.validTypes = new HashSet<>(Arrays.asList("string", "float", "int", "bool"));
        this.classNames = new HashSet<>();
        this.errors = new ArrayList<>();
        this.postAnalysis = new HashSet<>();
        this.alreadyAnalyzed = new HashSet<>();
        this.globalConstants = new HashMap<>();
        this.scopeElements = new HashMap<>();
    }

    private void toAnalyse(SynthaticNode synthaticNode, HashSet<String> error, Lexeme lexeme) {
        if (this.postAnalysis.contains(synthaticNode)) {
            if (!this.alreadyAnalyzed.contains(synthaticNode)) {
                this.errors.add(new SemanticParseErrors(error, lexeme));
                System.out.println(new SemanticParseErrors(error, lexeme));
            } else {
                this.alreadyAnalyzed.add(synthaticNode);
            }
            this.postAnalysis.remove(synthaticNode);
        } else {
            this.postAnalysis.add(synthaticNode);
        }
    }

    private String getExpressionValue(SynthaticNode synthaticNode) {
        return synthaticNode.getProduction();
    }

    void analyse(SynthaticNode hasConsumed) {
        if ("<Program>".equals(hasConsumed.getProduction())) {
            String subProduction = hasConsumed.getNodeList().get(0).getProduction();
            if ("<Class>".equals(subProduction)) {
                String newType = hasConsumed.getNodeList().get(0).getNodeList().get(1).getToken().getLexeme().getValue();
                this.validTypes.add(newType);
                this.classNames.add(newType);
                subProduction = hasConsumed.getNodeList().get(0).getNodeList().get(2).getProduction();
                if ("<Extends>".equals(subProduction)) {
                    Token token = hasConsumed.getNodeList().get(0).getNodeList().get(2).getNodeList().get(0).getToken();
                    if (token != null) {
                        Lexeme lexeme = hasConsumed.getNodeList().get(0).getNodeList().get(2).getNodeList().get(1).getToken().getLexeme();
                        String className = lexeme.getValue();
                        if (this.classNames.contains(className)) {
                            this.inheritance.put(newType, className);
                        } else {
                            this.toAnalyse(hasConsumed, this.classNames, lexeme);
                        }
                    }
                }
            }
        } else if ("<Constant Assignment>".equals(hasConsumed.getProduction())) {
            String subProduction = hasConsumed.getNodeList().get(0).getProduction();
            if (subProduction != null) {
                Lexeme lexeme = hasConsumed.getNodeList().get(0).getNodeList().get(0).getNodeList().get(0).getToken().getLexeme();
                String type = lexeme.getValue();
                if (!this.validTypes.contains(type)) {
                    this.toAnalyse(hasConsumed, this.validTypes, lexeme);
                } else {
                    this.globalConstants.put(hasConsumed.getNodeList().get(0).getNodeList().get(1).getNodeList().get(0).getToken().getLexeme().getValue(), type);
                    if(!type.equals(this.getExpressionValue(hasConsumed.getNodeList().get(2)))){
                        this.errors.add(new SemanticParseErrors(this.validTypes, lexeme));
                        System.out.println(new SemanticParseErrors(this.validTypes, lexeme));
                    }
                }
            }
        } else if ("<Variable Assignment>".equals(hasConsumed.getProduction())) {
            String subProduction = hasConsumed.getNodeList().get(0).getProduction();
            if (subProduction != null) {
                Lexeme lexeme = hasConsumed.getNodeList().get(0).getNodeList().get(0).getNodeList().get(0).getToken().getLexeme();
                String type = lexeme.getValue();
                if (!this.validTypes.contains(type)) {
                    this.toAnalyse(hasConsumed, this.validTypes, lexeme);
                } else {
                    if (!this.scopeElements.containsKey(hasConsumed.getNodeList().get(0))) {
                        this.scopeElements.put(hasConsumed.getNodeList().get(0), new HashMap<>());
                    }
                    //System.out.println(this.scopeElements.get(hasConsumed.getNodeList().get(0)));
                }
            }
        }
    }

    void executePostAnalysis() {
        for (SynthaticNode synthaticNode : this.postAnalysis) {
            this.analyse(synthaticNode);
        }
    }
}
