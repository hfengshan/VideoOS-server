package com.videojj.videocommon.interpreter.json2sql;

/**
 * SQL完整表达式
 */
public class Sql extends Expression {
    private Expression action, table, condition;

    public Sql(Expression action, Expression table, Expression condition) {
        this.action = action;
        this.table = table;
        this.condition = condition;
    }

    @Override
    public String interpret(Context context) {
        return "SELECT " + action.interpret(context) + " FROM " + table.interpret(context)
                + (condition != null ? " WHERE " + condition.interpret(context) : "");
    }
}
