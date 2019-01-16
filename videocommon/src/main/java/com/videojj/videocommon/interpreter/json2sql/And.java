package com.videojj.videocommon.interpreter.json2sql;

/**
 * AND操作符
 */
public class And extends Expression {
    private Expression left, right;

    public And(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String interpret(Context context) {
        return "(" + this.left.interpret(context) + " AND " + this.right.interpret(context) + ")";
    }
}
