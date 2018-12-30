package controllers;

import models.value.Lexeme;
import models.value.Token;
import models.value.errors.ClassMethodError;
import models.value.errors.IncorrectTypeError;
import models.value.errors.SemanticParseErrors;
import util.SynthaticNode;

import java.util.*;

public class SemanticAnalyser {
    private static SemanticAnalyser instance;
    private HashSet<SynthaticNode> postAnalysis;
    private HashSet<SynthaticNode> alreadyAnalyzed;
    private HashSet<SynthaticNode> successAnalyzed;
    private List<SemanticParseErrors> errors;
    private HashMap<String, String> inheritance;
    private HashMap<String, HashSet<String>> classMethods;
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
        this.classMethods = new HashMap<>();
        this.validTypes = new HashSet<>(Arrays.asList("string", "float", "int", "bool"));
        this.classNames = new HashSet<>();
        this.errors = new ArrayList<>();
        this.postAnalysis = new HashSet<>();
        this.alreadyAnalyzed = new HashSet<>();
        this.successAnalyzed = new HashSet<>();
        this.globalConstants = new HashMap<>();
        this.scopeElements = new HashMap<>();
    }

    private void toAnalyse(SynthaticNode synthaticNode, HashSet<String> error, Lexeme lexeme) {
        if (this.postAnalysis.contains(synthaticNode)) {
            if (!this.alreadyAnalyzed.contains(synthaticNode)) {
                this.errors.add(new IncorrectTypeError(error, lexeme));
                System.out.println(new IncorrectTypeError(error, lexeme));
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

    private void analyseMethod(SynthaticNode synthaticNode, String className) {
        Lexeme lexeme = synthaticNode.getNodeList().get(1).getNodeList().get(1).getNodeList().get(0).getToken().getLexeme();
        String methodName = lexeme.getValue();
        if (this.classMethods.get(className).contains(methodName)) {
            this.errors.add(new ClassMethodError("Override", lexeme));
        }
    }

    private void analyseClassCode(SynthaticNode synthaticNode, String className) {
        if ("<Methods>".equals(synthaticNode.getNodeList().get(0).getProduction())) {
            this.analyseMethod(synthaticNode.getNodeList().get(0), className);
        }
    }

    private void analyseClass(SynthaticNode hasConsumed) {
        if (!this.successAnalyzed.contains(hasConsumed)) {
            String newType = hasConsumed.getNodeList().get(0).getNodeList().get(1).getToken().getLexeme().getValue();
            this.validTypes.add(newType);
            this.classNames.add(newType);
            this.classMethods.put(newType, new HashSet<>());
            this.successAnalyzed.add(hasConsumed);
            String subProduction = hasConsumed.getNodeList().get(0).getNodeList().get(2).getProduction();
            if ("<Extends>".equals(subProduction)) {
                Token token = hasConsumed.getNodeList().get(0).getNodeList().get(2).getNodeList().get(0).getToken();
                if (token != null) {
                    Lexeme lexeme = hasConsumed.getNodeList().get(0).getNodeList().get(2).getNodeList().get(1).getToken().getLexeme();
                    String className = lexeme.getValue();
                    if (this.classNames.contains(className)) {
                        this.successAnalyzed.add(hasConsumed.getNodeList().get(0).getNodeList().get(2));
                        this.inheritance.put(newType, className);
                    } else {
                        this.toAnalyse(hasConsumed, this.classNames, lexeme);
                    }
                }
            }
            this.analyseClassCode(hasConsumed.getNodeList().get(0).getNodeList().get(4), newType);
        }
    }

    private void analyseConstantAssignment(SynthaticNode hasConsumed) {
        if (!this.successAnalyzed.contains(hasConsumed)) {
            String subProduction = hasConsumed.getNodeList().get(0).getProduction();
            if (subProduction != null) {
                Lexeme lexeme = hasConsumed.getNodeList().get(0).getNodeList().get(0).getNodeList().get(0).getToken().getLexeme();
                String type = lexeme.getValue();
                if (!this.validTypes.contains(type)) {
                    this.toAnalyse(hasConsumed, this.validTypes, lexeme);
                } else {
                    this.globalConstants.put(hasConsumed.getNodeList().get(0).getNodeList().get(1).getNodeList().get(0).getToken().getLexeme().getValue(), type);
                    this.successAnalyzed.add(hasConsumed);
                    if (!type.equals(this.getExpressionValue(hasConsumed.getNodeList().get(2)))) {
                        this.errors.add(new IncorrectTypeError(this.validTypes, lexeme));
                        System.out.println(new IncorrectTypeError(this.validTypes, lexeme));
                    }
                    if (hasConsumed.getNodeList().size() > 4) {
                        this.analyseConstantAssignment(hasConsumed.getNodeList().get(4));
                    }
                }
            }
        }
    }

    private void analyseProgram(SynthaticNode synthaticNode) {
        for (SynthaticNode hasConsumed : synthaticNode.getNodeList()) {
            if ("<Class>".equals(hasConsumed.getProduction())) {
                this.analyseClass(synthaticNode);
            } else if ("<Constants>".equals(hasConsumed.getProduction())) {
                this.analyseConstantAssignment(hasConsumed.getNodeList().get(2));
            }
        }
    }

    void analyse(SynthaticNode hasConsumed) {

        if ("<Program>".equals(hasConsumed.getProduction())) {
            this.analyseProgram(hasConsumed);
        } else if ("<Constant Assignment>".equals(hasConsumed.getProduction())) {
            this.analyseConstantAssignment(hasConsumed);
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
