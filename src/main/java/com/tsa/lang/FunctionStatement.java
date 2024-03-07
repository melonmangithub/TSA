package com.tsa.lang;

import com.tsa.lang.Statement;
import com.tsa.lang.StatementType;

public class FunctionStatement implements Statement {
    private String functionName;

    public FunctionStatement(String functionName) {
        this.functionName = functionName;
    }

    public void print() {
        System.out.print("Call to: " + this.functionName);
    }

    public StatementType getType() {
        return StatementType.STATEMENT_FUNCTION;
    }
}