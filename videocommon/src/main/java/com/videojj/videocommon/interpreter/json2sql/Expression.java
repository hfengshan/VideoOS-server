package com.videojj.videocommon.interpreter.json2sql;

/**
 * 解释器的表达式
 */
public abstract class Expression {
    /**
     * 解释器的核心方法，通过此方法递归地翻译SQL语句。
     *
     * @param context
     * @return
     */
    abstract public String interpret(Context context);
}
