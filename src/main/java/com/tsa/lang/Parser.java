package com.tsa.lang;

import java.util.*;
import com.tsa.lang.*;

/*
 * Statements:
 *  - functions: no parenthesis, semicolon at end
 *    ex: "RobotForward"
 *  - loops: in the form of "repeat"
 *    ex: "repeat 10: {RobotForward}"
 */

public class Parser {
    private ArrayList<Statement> program;
    private String code;

    private String identifierBuffer;
    private int numberBuffer;
    private TokenType currentToken;

    private int p;

    public Parser(String code) {
        this.program = new ArrayList<Statement>();
        this.code = code;
        this.p = 0;

        this.constructProgram();
    }

    private void constructProgram() {
        this.currentToken = this.nextToken();

        while ((this.currentToken != TokenType.END) && (this.currentToken != TokenType.ERROR)) {
            System.out.println(this.currentToken.toString());

            this.currentToken = this.nextToken();
        }
    }

    private TokenType nextToken() {
        if (this.p >= this.code.length()) {
            return TokenType.END;
        }

        while (Character.isWhitespace(this.code.charAt(p))) {
            this.p++;
        }

        if (Character.isAlphabetic(this.code.charAt(p))) {
            this.identifierBuffer = "";

            while (Character.isAlphabetic(this.code.charAt(this.p))) {
                this.identifierBuffer += this.code.charAt(this.p++);
            }

            return TokenType.IDENTIFIER;
        }

        if (Character.isDigit(this.code.charAt(p))) {
            this.identifierBuffer = "";

            while (Character.isDigit(this.code.charAt(p))) {
                this.identifierBuffer += this.code.charAt(this.p++);
            }

            this.numberBuffer = Integer.parseInt(this.identifierBuffer);
            return TokenType.NUMBER;
        }

        switch (this.code.charAt(this.p)) {
            case '{':
                this.p++;
                return TokenType.OPEN_BRACE;
            case '}':
                this.p++;
                return TokenType.CLOSE_BRACE;
        }

        return TokenType.ERROR;
    }

    public static void main(String[] args) {
        Parser parser = new Parser("Repeat 10 {RobotForward}");
    }
}