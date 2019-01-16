package com.videojj.videocommon.interpreter.json2sql;

/**
 * OR操作符
 */
public class Or extends Expression {
    private Expression left, right;

    public Or(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String interpret(Context context) {
        return "(" + this.left.interpret(context) + " OR " + this.right.interpret(context) + ")";
    }
}
