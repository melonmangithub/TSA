package com.tsa.lang;

import com.tsa.lang.Statement;
import com.tsa.lang.StatementType;
import java.util.*;

public class RepeatStatement implements Statement {
    private int repetitions;
    private ArrayList<Statement> subStatements;
    private int debugDepth;

    public RepeatStatement(int repetitions) {
        this.repetitions = repetitions;
        this.subStatements = new ArrayList<Statement>();
        this.debugDepth = 0;
    }

    public void print() {
        System.out.println("Repeat Statement! " + String.format("%d Times", this.repetitions));

        for (Statement statement : this.subStatements) {
            statement.print();
        }
    }

    public void addSubStatement(Statement statement) {
        if (this.subStatements == null) {
            this.subStatements = new ArrayList<Statement>();
        }

        this.subStatements.add(statement);
    }

    public void setDebugDepth(int debugDepth) {
        this.debugDepth = debugDepth;
    }

    public StatementType getType() {
        return StatementType.STATEMENT_REPEAT;
    }
}
