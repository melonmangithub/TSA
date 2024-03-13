package com.tsa.lang;

import com.tsa.lang.Statement;
import com.tsa.lang.StatementType;

public class FunctionStatement implements Statement {
    public String functionName;

    public FunctionStatement(String functionName) {
        this.functionName = functionName.toLowerCase();
    }

    public void print() {
        System.out.print("Call to: " + this.functionName);
    }

    public StatementType getType() {
        return StatementType.STATEMENT_FUNCTION;
    }
}