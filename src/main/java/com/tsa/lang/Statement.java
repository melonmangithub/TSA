package com.tsa.lang;

import com.tsa.lang.StatementType;

public interface Statement {
    public StatementType getType();
    public void print();
}
