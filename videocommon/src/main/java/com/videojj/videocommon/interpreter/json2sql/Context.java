package com.videojj.videocommon.interpreter.json2sql;

import java.util.HashMap;
import java.util.Map;

/**
 * 解释器的上下文
 */
public class Context {
    private Map<Variable, String> map = new HashMap<>();
    private Constant constTable;

    Context(Constant constTable){
        this.constTable = constTable;
    }

    public Constant getConstTable(){
        return this.constTable;
    }

    public void assign(Variable var, String value) {
        map.put(var, value);
    }

    public String lookup(Variable var) throws IllegalArgumentException {
        String value = map.get(var);
        if (value == null) {
            throw new IllegalArgumentException();
        }
        return value;
    }
}
