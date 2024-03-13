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
        this.code = code + " ";
        this.p = 0;
    }

    public ArrayList<Statement> getProgram() {
        this.program = new ArrayList<Statement>();
        Statement statement = this.parseStatement();

        while (statement != null) {
            this.program.add(statement);
            statement = this.parseStatement();
        }

        System.out.println(this.program);

        return this.program;
    }

    private Statement parseStatement() {
        if (this.currentToken == null) {
            this.currentToken = nextToken();
        }

        switch (this.currentToken) {
            case IDENTIFIER: {
                return parseFunctionStatement();
            }

            case KEYW_REPEAT: {
                return parseRepeatStatement();
            }

            case END:
            default:
                return null;
        }
    }
    
    // repeat <n> {}
    private RepeatStatement parseRepeatStatement() {
        this.currentToken = this.nextToken();

        if (this.currentToken != TokenType.NUMBER) {
            return null;
        }

        RepeatStatement statement = new RepeatStatement(this.numberBuffer);

        this.currentToken = this.nextToken();

        if (this.currentToken != TokenType.OPEN_BRACE) {
            return null;
        }

        this.currentToken = this.nextToken();

        while (this.currentToken != TokenType.CLOSE_BRACE) {
            if ((this.currentToken == TokenType.END) || (this.currentToken == TokenType.ERROR)) {
                return null;
            }

            statement.addSubStatement(this.parseStatement());
        }

        this.currentToken = this.nextToken();

        return statement;
    }

    private FunctionStatement parseFunctionStatement() {
        FunctionStatement function = new FunctionStatement(this.identifierBuffer);
        this.currentToken = this.nextToken();

        if (this.currentToken != TokenType.SEMICOLON) {
            return null;
        }

        this.currentToken = this.nextToken();

        return function;
    }

    private boolean inRange() {
        return this.p < this.code.length();
    }

    private TokenType nextToken() {
        if (!this.inRange()) {
            return TokenType.END;
        }

        while (this.inRange() && Character.isWhitespace(this.code.charAt(this.p))) {
            this.p++;
        }

        if (this.inRange() && Character.isAlphabetic(this.code.charAt(this.p))) {
            this.identifierBuffer = "";

            while (this.inRange() && Character.isAlphabetic(this.code.charAt(this.p))) {
                this.identifierBuffer += this.code.charAt(this.p);
                this.p++;
            }

            if (this.identifierBuffer.toLowerCase().equals("repeat")) {
                return TokenType.KEYW_REPEAT;
            }

            return TokenType.IDENTIFIER;
        }

        if (this.inRange() && Character.isDigit(this.code.charAt(this.p))) {
            this.identifierBuffer = "";

            while (this.inRange() && Character.isDigit(this.code.charAt(this.p))) {
                this.identifierBuffer += this.code.charAt(this.p);
                this.p++;
            }

            this.numberBuffer = Integer.parseInt(this.identifierBuffer);
            return TokenType.NUMBER;
        }

        if (this.inRange()) {
            switch (this.code.charAt(this.p)) {
                case '{':
                    this.p++;
                    return TokenType.OPEN_BRACE;
                case '}':
                    this.p++;
                    return TokenType.CLOSE_BRACE;
                case ';':
                    this.p++;
                    return TokenType.SEMICOLON;
            }
        }

        return TokenType.ERROR;
    }
}