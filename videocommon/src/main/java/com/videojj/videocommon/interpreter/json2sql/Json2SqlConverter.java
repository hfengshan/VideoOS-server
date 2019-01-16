package com.videojj.videocommon.interpreter.json2sql;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.videojj.videocommon.exception.ServiceException;
import com.videojj.videocommon.util.SqlUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * JSON转SQL的转换器
 */
public class Json2SqlConverter {
    private static final String JSON_FIELD_ACTION = "action";
    private static final String JSON_FIELD_CONDITION = "condition";
    private static final String JSON_FIELD_KEY = "key";
    private static final String JSON_FIELD_VALUE = "value";
    private static final String JSON_FIELD_OPERATOR = "operator";
    static final Constant EXPRESSION_CONSTANT_TB_MOBILE_DATA_DETAIL = new Constant("tb_mobile_data_detail");

    //新增SQL动作扩展点
    private static final Map<String, Expression> actionMapExprConst = ImmutableMap.<String, Expression>builder()
            .put("count", new Constant("COUNT(*)"))
            .build();
    //新增SQL操作符扩展点
    private static final Map<String, Class<? extends Expression>> operatorMapExprClazz = ImmutableMap.<String, Class<? extends Expression>>builder()
            .put("equal", Equal.class)
            .build();

    /**
     * 对于tb_mobile_data_detail的JSON转SQL操作
     *
     * @param dataJsonString 一条包含action和condition的JSON字符串
     * @return 经过解释器转换后的SQL字符串
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static String convert4TbMobileDataDetail(String dataJsonString) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return convert(dataJsonString, EXPRESSION_CONSTANT_TB_MOBILE_DATA_DETAIL);
    }

    /**
     * Json转SQL核心方法
     *
     * @param dataJsonString 一条包含action和condition的JSON字符串
     * @param constTable     表名常量对象
     * @return 经过解释器转换后的SQL字符串
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private static String convert(String dataJsonString, Constant constTable) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //1.验证Json格式
        final JSONObject dataJSONObject = JSONObject.parseObject(dataJsonString);
        if (null == dataJSONObject.get(JSON_FIELD_ACTION)) {
            throwJsonFormatException(JSON_FIELD_ACTION);
        }
        final Expression exprAction = actionMapExprConst.get(dataJSONObject.get(JSON_FIELD_ACTION));
        if (null == exprAction) {
            throwJsonFormatException(JSON_FIELD_ACTION);
        }
        final JSONArray conditionJSONArray = dataJSONObject.getJSONArray(JSON_FIELD_CONDITION);
        //2.如果没有条件，则根据action查询全部内容
        if (null == conditionJSONArray || conditionJSONArray.size() == 0) {
            return new Sql(exprAction, constTable, null).interpret(new Context(constTable));
        } else {
            //3.根据条件执行SQL
            Context context = new Context(constTable);
            Expression condition = null;
            JSONObject conditionJsonObject;
            for (int i = 0; i < conditionJSONArray.size(); i++) {
                conditionJsonObject = conditionJSONArray.getJSONObject(i);
                if (i == 0) {
                    condition = createOperatorExpression(conditionJsonObject.getString(JSON_FIELD_OPERATOR), conditionJsonObject, context);
                    continue;
                }
                condition = new Or(condition,
                        createOperatorExpression(conditionJsonObject.getString(JSON_FIELD_OPERATOR), conditionJsonObject, context)
                );
            }
            return new Sql(exprAction, constTable, condition).interpret(context);
        }

    }

    /**
     * 创建操作符表达式对象
     *
     * @param operator      操作符字符串
     * @param varJSONObject 变量所在的JSONObject
     * @param context       解释器的上下文
     * @return 操作符表达式对象
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    private static Expression createOperatorExpression(String operator, JSONObject varJSONObject, Context context) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class operatorClazz = operatorMapExprClazz.get(operator);
        if (operatorClazz == null) {
            throwJsonFormatException(JSON_FIELD_OPERATOR);
        } else if (BinaryOperator.class.isAssignableFrom(operatorClazz)) {
            Variable varKey = new Variable();
            String stringKey = varJSONObject.getString(JSON_FIELD_KEY);
            if (null == stringKey) {
                throwJsonFormatException(JSON_FIELD_KEY);
            }
            context.assign(varKey, "'" + SqlUtil.transactSqlInjection(stringKey) + "'");
            Variable varValue = new Variable();
            String stringValue = varJSONObject.getString(JSON_FIELD_VALUE);
            if (null == stringValue) {
                throwJsonFormatException(JSON_FIELD_VALUE);
            }
            context.assign(varValue, "'" + SqlUtil.transactSqlInjection(stringValue) + "'");
            return (Expression) operatorClazz.getDeclaredConstructor(Expression.class, Expression.class).newInstance(varKey, varValue);
        }

        throwJsonFormatException(JSON_FIELD_OPERATOR);
        return null;
    }

    private static void throwJsonFormatException(String fieldName) {
        throw new ServiceException("后端校验失败，JSON数据格式不正确，请检查" + fieldName + "字段！");
    }

}
