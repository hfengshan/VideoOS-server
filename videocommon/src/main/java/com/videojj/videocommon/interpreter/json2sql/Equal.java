package com.videojj.videocommon.interpreter.json2sql;

/**
 * =操作符
 */
public class Equal extends Expression implements BinaryOperator {
    private Expression left, right;

    public Equal(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String interpret(Context context) {
        if (context.getConstTable() == Json2SqlConverter.EXPRESSION_CONSTANT_TB_MOBILE_DATA_DETAIL) {
            return "(data_key = " + this.left.interpret(context) + " AND data_value = " + this.right.interpret(context) + ")";
        }
        return "(" + this.left.interpret(context) + " = " + this.right.interpret(context) + ")";
    }
}
