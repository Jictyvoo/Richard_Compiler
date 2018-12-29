package controllers;

import java.util.*;

import models.value.Lexeme;
import models.value.SemanticParseErrors;
import models.value.Token;
import util.SynthaticNode;

public class SemanticAnalyser {
    private static SemanticAnalyser instance;
    private List<SemanticParseErrors> errors;
    private HashMap<String, String> inheritance;
    private HashSet<String> validTypes;
    private HashSet<String> classNames;

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
                            this.errors.add(new SemanticParseErrors(this.classNames, null));
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
                    this.errors.add(new SemanticParseErrors(this.validTypes, lexeme));
                }
            }
        }
    }
}
