package com.videojj.videocommon.interpreter.json2sql;

/**
 * 解释器的变量
 */
public class Variable extends Expression {
    @Override
    public String interpret(Context context) {
        return context.lookup(this);
    }
}
