package com.tsa.lang;

import com.tsa.lang.Statement;
import com.tsa.lang.StatementType;
import java.util.*;

public class RepeatStatement implements Statement {
    public int repetitions;
    public ArrayList<Statement> subStatements;
    private int debugDepth;

    public RepeatStatement(int repetitions) {
        this.repetitions = repetitions;
        this.subStatements = new ArrayList<Statement>();
        this.debugDepth = 0;
    }

    public RepeatStatement(int repetitions, Statement body) {
        this.repetitions = repetitions;
        this.subStatements = new ArrayList<Statement>();
        this.subStatements.add(body);
        this.debugDepth = 0;
    }

    public void print() {
        System.out.println("Repeat Statement! " + String.format("%d Times", this.repetitions));

        for (Statement statement : this.subStatements) {
            statement.print();
        }
    }

    public RepeatStatement addSubStatement(Statement statement) {
        if (this.subStatements == null) {
            this.subStatements = new ArrayList<Statement>();
        }

        this.subStatements.add(statement);
        return this;
    }

    public void setDebugDepth(int debugDepth) {
        this.debugDepth = debugDepth;
    }

    public StatementType getType() {
        return StatementType.STATEMENT_REPEAT;
    }
}
