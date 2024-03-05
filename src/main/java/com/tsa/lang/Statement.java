package com.tsa.lang;

import com.tsa.lang.StatementType;

public interface Statement {
    public void execute();
    public StatementType getType();
}
