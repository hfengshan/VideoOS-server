package com.videojj.videoservice.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/30 下午5:42.
 * @Description:
 */
public class CronUtil {

    private static final String CRON_DATE_FORMAT = "ss mm HH dd MM ? yyyy";

    public static void main(String args[]){

        Date testDate = new Date();

        System.out.println(testDate);

        String cronStr = getCron(testDate);

        System.out.println(cronStr);

    }

    public static String getCron(Date  date){


        String dateFormat="ss mm HH dd MM ? yyyy";
        return formatDateByPattern(date, dateFormat);
    }

    public static String formatDateByPattern(Date date, String dateFormat){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String formatTimeStr = null;
        if (date != null) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }

    /***
     *
     * @param cron Quartz cron的类型的日期
     * @return  Date日期
     */

    public static Date getDate(final String cron) {

        if(cron == null) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(CRON_DATE_FORMAT);
        Date date = null;
        try {
            date = sdf.parse(cron);
        } catch (Exception e) {
            return null;// 此处缺少异常处理,自己根据需要添加
        }
        return date;
    }


}
