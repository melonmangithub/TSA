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
        this.subStatements = null;
        this.debugDepth = 0;
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

    public void execute() {
        System.out.println("Repeat " + this.repetitions + " Times");

        for (Statement statement : this.subStatements) {
            System.out.print(" ".repeat(debugDepth));
            statement.execute();
        }
    }

    public StatementType getType() {
        return StatementType.STATEMENT_REPEAT;
    }
}
