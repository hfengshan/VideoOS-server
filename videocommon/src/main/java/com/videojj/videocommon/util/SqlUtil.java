package com.videojj.videocommon.util;

/**
 * SQL工具类
 *
 * @author zhangzhewen
 * @date 2018/11/26
 */
public class SqlUtil {

    /**
     * SQL注入的处理函数
     * @param str
     * @return
     */
    public static String transactSqlInjection(String str) {
        return str.replaceAll(".*([';]+|(--)+).*", "");
    }

}
