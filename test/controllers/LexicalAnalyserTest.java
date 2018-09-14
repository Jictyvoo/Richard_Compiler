package controllers;

import models.value.Lexeme;
import models.value.Token;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LexicalAnalyserTest {

    private LexicalAnalyser lexicalAnalyser;

    LexicalAnalyserTest() {
        this.lexicalAnalyser = LexicalAnalyser.getInstance();
    }

    private Lexeme generateLexeme(String value) {
        return new Lexeme(value, "", 0, (short) 0, "");
    }

    @Test
    void analyseIdentifier() {
        Token receivedToken;
        /*Verify if identifier is ok*/
        receivedToken = lexicalAnalyser.analyse(this.generateLexeme("21_43gfv"));
        assertNull(receivedToken);
        receivedToken = lexicalAnalyser.analyse(this.generateLexeme("_43gf_AdsAdv"));
        assert (receivedToken != null);
        receivedToken = lexicalAnalyser.analyse(this.generateLexeme("gf_AdsAdere45df567v"));
        assert (receivedToken != null);
    }

    @Test
    void analyseNumber() {
        Token receivedToken;
        /*verify if number is ok*/
        receivedToken = lexicalAnalyser.analyse(this.generateLexeme("+54.0"));
        assertNull(receivedToken);
        receivedToken = lexicalAnalyser.analyse(this.generateLexeme("-65.99599"));
        assert (receivedToken != null);
        receivedToken = lexicalAnalyser.analyse(this.generateLexeme("+54.65...5.60"));
        assertNull(receivedToken);
        receivedToken = lexicalAnalyser.analyse(this.generateLexeme("0.56"));
        assert (receivedToken != null);
    }

    @Test
    void analyseString() {
        Token receivedToken;
        /*verify if string is ok*/
        receivedToken = lexicalAnalyser.analyse(this.generateLexeme("\"-65.99&%U&TET$"));
        assertNull(receivedToken);
        receivedToken = lexicalAnalyser.analyse(this.generateLexeme("\"-65.99&%U&TET$599\""));
        assert (receivedToken != null);
    }

    @Test
    void analyseComment() {
        Token receivedToken;
        /*verify if string is ok*/
        receivedToken = lexicalAnalyser.analyse(this.generateLexeme("/*sdsdsAbCd45353446534*"));
        assertNull(receivedToken);
        receivedToken = lexicalAnalyser.analyse(this.generateLexeme("//*-65.99&%U&TET$599\""));
        assert (receivedToken != null);
        receivedToken = lexicalAnalyser.analyse(this.generateLexeme("/*-65.99&%U&TET$599\"*/"));
        assert (receivedToken != null);
    }

}