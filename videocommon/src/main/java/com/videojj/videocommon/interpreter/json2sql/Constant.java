package com.videojj.videocommon.interpreter.json2sql;

/**
 * SQL中的语法常量
 */
public class Constant extends Expression {
    private String value;

    public Constant(String value) {
        this.value = value;
    }

    @Override
    public String interpret(Context context) {
        return this.value;
    }
}
