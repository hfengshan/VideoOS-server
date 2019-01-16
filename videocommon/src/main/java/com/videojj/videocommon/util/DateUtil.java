package com.videojj.videocommon.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 时间工具类
 * 
 * @author
 * @version $Id: DateUtils.java, v 0.1 2016年6月1日 下午8:17:36 wangkun Exp $
 */
@Slf4j
public class DateUtil {

    //============================0.获取当前时间====================================

    public static final String SHORT_DATE_FORMAT            = "yyyy-MM-dd";
    public static final String SHORT_DATE_GBK_FORMAT        = "yyyy年MM月dd日";
    public static final String DATE_FORMAT                  = "yyyy-MM-dd HH:mm";
    public static final String DATE_GBK_FORMAT              = "yyyy年MM月dd日 HH时mm分";
    public static final String LONG_DATE_FORMAT             = "yyyy-MM-dd HH:mm:ss";
    public static final String LONG_DATE_GBK_FORMAT         = "yyyy年MM月dd日 HH时mm分ss秒";
    /**UTC标准时间格式化字符串*/
    public static final String LOG_DATE_FORMAT 				= "yyyy-MM-dd'T'HH:mm:ss.sss'+08:00'";

    //============================1.Date2String=====================================
    public static final String MAIL_DATE_FORMAT             = "yyyyMMddHHmmss";
    public static final String MAIL_DATE_HHMM_FORMAT        = "HH:mm";
    public static final String FULL_DATE_FORMAT             = "yyyy-MM-dd HH:mm:ss:SSS";
    public static final String STD_DATE_FORMAT              = "yyyy-MM-dd HH:mm:ss";
    public static final String FULL_DATE_GBK_FORMAT         = "yyyy年MM月dd日 HH时mm分ss秒SSS毫秒";

    /**
     * 
     */
    public static final String FULL_DATE_COMPACT_FORMAT     = "yyyyMMddHHmmssSSS";

    /**
     * 
     */
    public static final String LDAP_DATE_FORMAT             = "yyyyMMddHHmm'Z'";
    public static final String US_LOCALE_DATE_FORMAT        = "EEE MMM dd HH:mm:ss zzz yyyy";
    public static final String MAIL_DATE_DT_PART_FORMAT     = "yyyyMMdd";
    public static final String MAIL_TIME_TM_PART_FORMAT     = "HHmmss";
    public static final String LONG_DATE_TM_PART_FORMAT     = "HH:mm:ss";
    public static final String Long_DATE_TM_PART_GBK_FORMAT = "HH时mm分ss秒";
    public static final String MAIL_DATA_DTM_PART_FORMAT    = "MM月dd日HH:mm";
    public static final String POINT_DATA_DTM_PART_FORMAT   = "yyyy.MM.dd";

    public static final String US_DATE_FORMAT               = "EEE MMM dd yyyy HH:mm:ss 'GMT'Z";
    public static final String DEFAULT_DATE_FORMAT          = US_LOCALE_DATE_FORMAT;
    public static long         NANO_ONE_SECOND              = 1000;
    public static long         NANO_ONE_MINUTE              = 60 * NANO_ONE_SECOND;

    //============================2.String2Date=====================================    
    public static long         NANO_ONE_HOUR                = 60 * NANO_ONE_MINUTE;
    public static long         NANO_ONE_DAY                 = 24 * NANO_ONE_HOUR;

    /**
     *
     */
    private DateUtil() {
    }

    /**
     * 获取当前日期类型时间
     */
    public static Date getNow() {
        return new Date();
    }

    /**
     * 获取当前时间戳
     */
    public static long getNowTimestamp() {
        return getNow().getTime();
    }

    /**
     * 获取当前日期 yyyyMMdd
     */
    public static String getCurrentDate() {
        return toMailDateDtPartString(getNow());
    }

    /**
     * 获取当期时间HHmmss
     * 
     * @return
     */
    public static String getCurrentTime() {
        return toMailTimeTmPartString(getNow());
    }

    /**
     * 获取当期时间MM月dd日HH:mm
     * 
     * @return
     */
    public static String getCurrentMmDdHmTime() {
        return toMailDtmPart(getNow());
    }

    /**
     * 获取当前日期和时间yyyyMMddHHmmss
     * 
     * @return
     */
    public static String getCurrentDateTime() {
        return toMailDateString(getNow());
    }

    /**
     * 将一个日期型转换为指定格式字串
     * 
     * @param aDate
     * @param formatStr
     * @return
     */
    public static final String toFormatDateString(Date aDate, String formatStr) {
        if (aDate == null)
            return StringUtils.EMPTY;
        Assert.hasText(formatStr);
        return new SimpleDateFormat(formatStr).format(aDate);

    }

    //============================3.String2String===================================== 

    /**
     * 将一个日期型转换为'yyyy-MM-dd'格式字串
     * 
     * @param aDate
     * @return
     */
    public static final String toShortDateString(Date aDate) {
        return toFormatDateString(aDate, SHORT_DATE_FORMAT);
    }

    /**
     * 将一个日期型转换为'yyyyMMdd'格式字串
     * 
     * @param aDate
     * @return
     */
    public static final String toMailDateDtPartString(Date aDate) {
        return toFormatDateString(aDate, MAIL_DATE_DT_PART_FORMAT);
    }

    //============================4.时间加减=====================================    

    /**
     * 将一个日期型转换为'HHmmss'格式字串
     * 
     * @param aDate
     * @return
     */
    public static final String toMailTimeTmPartString(Date aDate) {
        return toFormatDateString(aDate, MAIL_TIME_TM_PART_FORMAT);
    }

    /**
     * 将一个日期型转换为'yyyyMMddHHmmss'格式字串
     * 
     * @param aDate
     * @return
     */
    public static final String toMailDateString(Date aDate) {
        return toFormatDateString(aDate, MAIL_DATE_FORMAT);
    }

    /**
     * 将一个日期型转换为MM月dd日HH:mm格式字串
     * 
     * @param aDate
     * @return
     */
    public static final String toMailDtmPart(Date aDate) {
        return toFormatDateString(aDate, MAIL_DATA_DTM_PART_FORMAT);
    }

    /**
     * 将一个日期型转换为yyyy.MM.dd格式字串
     * 
     * @param aDate
     * @return
     */
    public static final String toPointDtmPart(Date aDate) {
        return toFormatDateString(aDate, POINT_DATA_DTM_PART_FORMAT);
    }

    /**
     * 将一个日期型转换为'yyyy-MM-dd HH:mm:ss'格式字串
     * 
     * @param aDate
     * @return
     */
    public static final String toLongDateString(Date aDate) {
        return toFormatDateString(aDate, LONG_DATE_FORMAT);
    }

    /**
     * 将一个日期型转换为'HH:mm:ss'格式字串
     * 
     * @param aDate
     * @return
     */
    public static final String toLongDateTmPartString(Date aDate) {
        return toFormatDateString(aDate, LONG_DATE_TM_PART_FORMAT);
    }

    /**
     * 将一个日期型转换为'yyyy年MM月dd日'格式字串
     * 
     * @param aDate
     * @return
     */
    public static final String toShortDateGBKString(Date aDate) {
        return toFormatDateString(aDate, SHORT_DATE_GBK_FORMAT);
    }

    /**
     * 将一个日期型转换为'yyyy年MM月dd日 HH时mm分'格式字串
     * 
     * @param aDate
     * @return
     */
    public static final String toDateGBKString(Date aDate) {
        return toFormatDateString(aDate, DATE_GBK_FORMAT);
    }

    //======================================5.时间国际化=================================

    /**
     * 将一个日期型转换为'yyyy年MM月dd日 HH时mm分ss秒'格式字串
     * 
     * @param aDate
     * @return
     */
    public static final String toLongDateGBKString(Date aDate) {
        return toFormatDateString(aDate, LONG_DATE_GBK_FORMAT);
    }

    /**
     * 将一个日期型转换为'HH时mm分ss秒'格式字串
     * 
     * @param aDate
     * @return
     */
    public static final String toLongDateTmPartGBKString(Date aDate) {
        return toFormatDateString(aDate, Long_DATE_TM_PART_GBK_FORMAT);
    }

    /**
     * 将一个日期型转换为'yyyy-MM-dd HH:mm:ss:SSS'格式字串
     * 
     * @param aDate
     * @return
     */
    public static final String toFullDateString(Date aDate) {
        return toFormatDateString(aDate, FULL_DATE_FORMAT);
    }

    /**
     * 将一个日期型转换为'yyyy年MM月dd日 HH时mm分ss秒SSS毫秒'格式字串
     * 
     * @param aDate
     * @return
     */
    public static final String toFullDateGBKString(Date aDate) {
        return toFormatDateString(aDate, FULL_DATE_GBK_FORMAT);
    }

    //==================================6. miscellaneous==========================

    /**
     * 将一个日期型转换为'yyyyMMddHHmmssSSS'格式字串
     * 
     * @param aDate
     * @return
     */
    public static final String toFullDateCompactString(Date aDate) {
        return toFormatDateString(aDate, FULL_DATE_COMPACT_FORMAT);
    }

    /**
     * 将一个日期型转换为LDAP格式字串
     * 
     * @param aDate
     * @return
     */
    public static final String toLDAPDateString(Date aDate) {
        return toFormatDateString(aDate, LDAP_DATE_FORMAT);
    }

    /**
     * 将一个符合指定格式的字串解析成日期型
     * 
     * @param aDateStr
     * @param formatter
     * @return
     * @throws ParseException
     */
    public static final Date parser(String aDateStr, String formatter) throws ParseException {
        if (StringUtils.isBlank(aDateStr))
            return null;
        Assert.hasText(formatter);
        SimpleDateFormat sdf = new SimpleDateFormat(formatter);
        return sdf.parse(aDateStr);

    }

    /* ------- start ------------ */

    /**
     * 将一个符合'yyyy-MM-dd HH:mm:ss'格式的字串解析成日期型
     * 
     * @param aDateStr
     * @return
     */
    public static final Date parseLongDateString(String aDateStr) throws ParseException {
        return parser(aDateStr, LONG_DATE_FORMAT);

    }

    /**
     * added by //
     *
     * @param aDateStr
     * @return
     */
    public static final Date parseDefaultDateString(String aDateStr) throws ParseException {
        return parser(aDateStr, US_DATE_FORMAT);

    }

    /**
     * 将一个符合'yyyy-MM-dd HH:mm:ss'格式的字串解析成日期型
     * 
     * @param aDateStr
     * @return
     */
    public static final Date parseLongDateDtPartString(String aDateStr) throws ParseException {
        return parser(aDateStr, LONG_DATE_FORMAT);

    }

    /**
     * 将一个符合'yyyy-MM-dd HH:mm:ss'格式的字串解析成日期型
     * 
     * @param aDateStr
     * @return
     */
    public static final Date parseLongDateTmPartString(String aDateStr) throws ParseException {
        return parser(aDateStr, LONG_DATE_FORMAT);

    }

    /**
     * 将一个符合'yyyy-MM-dd'格式的字串解析成日期型
     * 
     * @param aDateStr
     * @return
     */
    public static final Date parseShortDateString(String aDateStr) throws ParseException {
        return parser(aDateStr, SHORT_DATE_FORMAT);

    }

    /**
     * 将一个符合'yyyyMMddHHmmss'格式的字串解析成日期型
     * 
     * @param aDateStr
     * @return
     */
    public static final Date parseMailDateString(String aDateStr) throws ParseException {
        return parser(aDateStr, MAIL_DATE_FORMAT);

    }

    /* ------- end ------------ */

    /**
     * @param aDateStr
     * @return
     */
    public static final boolean chkStdDateFmt(String aDateStr) {
        try {
            parser(aDateStr, STD_DATE_FORMAT);
        } catch (ParseException e) {
            log.info("chkStdDateFmt error:", e);
            return false;
        }

        return true;
    }

    /**
     * 将一个符合'yyyyMMdd'格式的字串解析成日期型
     * 
     * @param aDateStr
     * @return
     */
    public static final Date parseMailDateDtPartString(String aDateStr) throws ParseException {
        return parser(aDateStr, MAIL_DATE_DT_PART_FORMAT);
    }

    /**
     * 将一个符合'HHmmss'格式的字串解析成日期型
     * 
     * @param aDateStr
     * @return
     */
    public static final Date parseMailDateTmPartString(String aDateStr) throws ParseException {
        return parser(aDateStr, MAIL_TIME_TM_PART_FORMAT);
    }

    /**
     * 将一个符合'yyyy-MM-dd HH:mm:ss:SSS'格式的字串解析成日期型
     * 
     * @param aDateStr
     * @return
     */
    public static final Date parseFullDateString(String aDateStr) throws ParseException {
        return parser(aDateStr, FULL_DATE_FORMAT);

    }

    /**
     * 将一个符合'yyyy-MM-dd HH:mm:ss'格式的字串解析成日期型
     *
     * @param aDateStr
     * @return
     */
    public static final Date parseStdDateString(String aDateStr) throws ParseException {
        return parser(aDateStr, STD_DATE_FORMAT);

    }

    /**
     * 转换日期格式 yyyyMMdd => yyyy年MM月dd日
     * 
     * @param dt yyyyMMdd
     * @return yyyy年MM月dd日
     */
    public static String transfer2LongDateGbkDtPart(String dt) {
        if (dt == null || dt.length() != 8) {
            return dt;
        }
        Assert.notNull(dt);
        Assert.isTrue(dt.length() == 8);
        return dt.substring(0, 4).concat("年").concat(dt.substring(4, 6)).concat("月").concat(dt.substring(6))
                .concat("日");
    }

    /**
     * 转换日期格式HHmmss => HH时mm分ss秒
     * 
     * @param tm HHmmss
     * @return HH时mm分ss秒
     */
    public static String transfer2LongDateGbkTmPart(String tm) {
        if (tm == null || tm.length() != 6) {
            return tm;
        }
        Assert.notNull(tm);
        Assert.isTrue(tm.length() == 6);
        return tm.substring(0, 2).concat("时").concat(tm.substring(2, 4)).concat("分").concat(tm.substring(4))
                .concat("秒");
    }

    /**
     * 为一个日期加上指定年数
     * 
     * @param aDate
     * @param amount 年数
     * @return
     */
    public static final Date addYears(Date aDate, int amount) {
        return addTime(aDate, Calendar.YEAR, amount);
    }

    /**
     * 为一个日期加上指定月数
     * 
     * @param aDate
     * @param amount 月数
     * @return
     */
    public static final Date addMonths(Date aDate, int amount) {
        return addTime(aDate, Calendar.MONTH, amount);
    }

    /**
     * 为一个日期加上指定天数
     * 
     * @param aDate
     * @param amount 天数
     * @return
     */
    public static final Date addDays(Date aDate, int amount) {
        return addTime(aDate, Calendar.DAY_OF_YEAR, amount);
    }

    /**
     * 为一个日期加上指定天数
     * 
     * @param aDate yyyyMMdd格式字串
     * @param amount 天数
     * @return
     */
    public static final String addDays(String aDate, int amount) {
        try {
            return DateUtil.toMailDateDtPartString(
                    addTime(DateUtil.parseMailDateDtPartString(aDate), Calendar.DAY_OF_YEAR, amount));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 为一个日期加上指定小时数
     * 
     * @param aDate
     * @param amount 小时数
     * @return
     */
    public static final Date addHours(Date aDate, int amount) {
        return addTime(aDate, Calendar.HOUR, amount);

    }

    /**
     * 为一个日期加上指定分钟数
     * 
     * @param aDate
     * @param amount 分钟数
     * @return
     */
    public static final Date addMinutes(Date aDate, int amount) {
        return addTime(aDate, Calendar.MINUTE, amount);
    }

    /**
     * 为一个日期加上指定秒数
     * 
     * @param aDate
     * @param amount 秒数
     * @return
     */
    public static final Date addSeconds(Date aDate, int amount) {
        return addTime(aDate, Calendar.SECOND, amount);

    }

    /**
     * @param aDate
     * @param timeType
     * @param amount
     * @return
     */
    public static final Date addTime(Date aDate, int timeType, int amount) {
        if (aDate == null) {
            return null;
        }
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(aDate);
        cal.add(timeType, amount);
        return cal.getTime();
    }

    /**
     * 得到当前时间的UTC时间
     * 
     * @return
     */
    public static final String getUTCTime() {
        return getSpecifiedZoneTime(Calendar.getInstance().getTime(), TimeZone.getTimeZone("GMT+0"));
    }

    /**
     * 得到指定时间的UTC时间
     * 
     * @param aDate 时间戳
     * @return yyyy-MM-dd HH:mm:ss 格式
     */
    public static final String getUTCTime(Date aDate) {
        return getSpecifiedZoneTime(aDate, TimeZone.getTimeZone("GMT+0"));
    }

    /**
     * 得到当前时间的指定时区的时间
     * 
     * @param tz
     * @return
     */
    public static final String getSpecifiedZoneTime(TimeZone tz) {
        return getSpecifiedZoneTime(Calendar.getInstance().getTime(), tz);

    }

    /**
     * 得到指定时间的指定时区的时间
     * 
     * @param aDate 时间戳,Date是一个瞬间的long型距离历年的位移偏量，
     *            在不同的指定的Locale/TimeZone的jvm中，它toString成不同的显示值，
     *            所以没有必要为它再指定一个TimeZone变量表示获取它时的jvm的TimeZone
     * @param tz 要转换成timezone
     * @return yyyy-MM-dd HH:mm:ss 格式
     */
    public static final String getSpecifiedZoneTime(Date aDate, TimeZone tz) {
        if (aDate == null)
            return StringUtils.EMPTY;
        Assert.notNull(tz);
        SimpleDateFormat sdf = new SimpleDateFormat(LONG_DATE_FORMAT);
        sdf.setTimeZone(tz);
        return sdf.format(aDate);
    }

    /**
     * 计算两个日期之间相差的月数
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    public static final int getDifferenceMonths(Date startDate, Date endDate) {
        Assert.notNull(startDate);
        Assert.notNull(endDate);
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        return Math.abs((startCal.get(Calendar.YEAR) - endCal.get(Calendar.YEAR)) * 12
                + (startCal.get(Calendar.MONTH) - endCal.get(Calendar.MONTH)));
    }

    /**
     * 计算两个日期之间相差的天数
     * 
     * @param startDateStr yyyy-mm-dd
     * @param endDateStr yyyy-mm-dd
     * @return
     */
    public static final int getDifferenceDays(String startDateStr, String endDateStr) {
        return new Long(getDifferenceMillis(startDateStr, endDateStr) / (NANO_ONE_DAY)).intValue();
    }

    /**
     * 计算两个日期之间相差的天数
     * 
     * @param startDateStr yyyymmdd
     * @param endDateStr yyyymmdd
     * @return
     */
    public static final int getDifferenceDays2(String startDateStr, String endDateStr) {
        return new Long(getDifferenceMillis(startDateStr, endDateStr, MAIL_DATE_DT_PART_FORMAT) / (NANO_ONE_DAY/NANO_ONE_SECOND))
                .intValue();
    }

    /**
     * 两个日期之间相减（存在负数）
     * 
     * @param startDateStr yyyy-mm-dd
     * @param endDateStr yyyy-mm-dd
     * @return
     */
    public static final int getDaysSubtract(String startDateStr, String endDateStr) {
        return new Long(getDaysSubtractMillis(startDateStr, endDateStr) / (NANO_ONE_DAY)).intValue();
    }

    /**
     * 两个日期之间相减（存在负数）
     * 
     * @param startDateStr yyyymmdd
     * @param endDateStr yyyymmdd
     * @return
     */
    public static final int getDaysSubtract2(String startDateStr, String endDateStr) {
        return new Long(getDaysSubtractMillis(startDateStr, endDateStr, MAIL_DATE_DT_PART_FORMAT) / (NANO_ONE_DAY))
                .intValue();
    }

    /**
     * 两个日期之间相减（存在负数）
     * 
     * @param startDateStr yyyy-mm-dd
     * @param endDateStr yyyy-mm-dd
     * @return
     * @throws ParseException
     */
    public static final long getDaysSubtractMillis(String startDateStr, String endDateStr) {
        return getDaysSubtractMillis(startDateStr, endDateStr, SHORT_DATE_FORMAT);
    }

    /**
     * 计算两个日期之间相差的的毫秒数（存在负数）
     * 
     * @param startDateStr
     * @param endDateStr
     * @param dateFormat
     * @return
     */
    public static final long getDaysSubtractMillis(String startDateStr, String endDateStr, String dateFormat) {
        try {
            return getDaysSubtractMillis(parser(startDateStr, dateFormat), parser(endDateStr, dateFormat));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 计算两个日期之间相差的的毫秒数（存在负数）
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    public static final long getDaysSubtractMillis(Date startDate, Date endDate) {
        Assert.notNull(startDate);
        Assert.notNull(endDate);
        return endDate.getTime() - startDate.getTime();
    }

    /**
     * 计算两个日期之间相差的天数
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    public static final int getDifferenceDays(Date startDate, Date endDate) {
        return new Long(getDifferenceMillis(startDate, endDate) / (NANO_ONE_DAY / NANO_ONE_SECOND)).intValue();

    }

    /**
     * 计算两个日期之间相差的小时数
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    public static final int getDifferenceHours(Date startDate, Date endDate) {
        return new Long(getDifferenceMillis(startDate, endDate) / (NANO_ONE_HOUR / NANO_ONE_SECOND)).intValue();
    }
    /**
     * 计算两个日期之间相差的的毫秒数
     * 
     * @param startDateStr yyyy-mm-dd
     * @param endDateStr yyyy-mm-dd
     * @return
     * @throws ParseException
     */
    public static final long getDifferenceMillis(String startDateStr, String endDateStr) {
        return getDifferenceMillis(startDateStr, endDateStr, SHORT_DATE_FORMAT);
    }

    /**
     * 计算两个日期之间相差的的毫秒数
     * 
     * @param startDateStr yyyyMMddHHmmss
     * @param endDateStr yyyyMMddHHmmss
     * @return
     * @throws ParseException
     */
    public static final long getDifferenceMillis2(String startDateStr, String endDateStr) {
        return getDifferenceMillis(startDateStr, endDateStr, MAIL_DATE_FORMAT);
    }

    /**
     * 计算两个日期之间相差的的毫秒数
     * 
     * @param startDateStr
     * @param endDateStr
     * @param dateFormat
     * @return
     */
    public static final long getDifferenceMillis(String startDateStr, String endDateStr, String dateFormat) {
        try {
            return getDifferenceMillis(parser(startDateStr, dateFormat), parser(endDateStr, dateFormat));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 计算两个日期之间相差的的毫秒数
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    public static final long getDifferenceMillis(Date startDate, Date endDate) {
        Assert.notNull(startDate);
        Assert.notNull(endDate);
        return Math.abs(endDate.getTime() / NANO_ONE_SECOND - startDate.getTime() / NANO_ONE_SECOND);
    }

    /**
     * 检验 日期是否在指定区间内，如果格式错误，返回false
     * 如果maxDateStr或minDateStr为空则比较时变为正负无穷大，如果都为空，则返回false
     * 
     * @param aDate
     * @param minDateStr 必须是yyyy-MM-dd格式，时分秒为00:00:00
     * @param maxDateStr 必须是yyyy-MM-dd格式，时分秒为00:00:00
     * @return
     */
    public static final boolean isDateBetween(Date aDate, String minDateStr, String maxDateStr) {
        Assert.notNull(aDate);
        boolean ret = false;
        try {
            Date dMaxDate = null;
            Date dMinDate = null;
            dMaxDate = DateUtil.parseShortDateString(maxDateStr);
            dMinDate = DateUtil.parseShortDateString(minDateStr);
            switch ((dMaxDate != null ? 5 : 3) + (dMinDate != null ? 1 : 0)) {
                case 6:
                    ret = aDate.before(dMaxDate) && aDate.after(dMinDate);
                    break;
                case 5:
                    ret = aDate.before(dMaxDate);
                    break;
                case 4:
                    ret = aDate.after(dMinDate);
                    break;
                default:
                    break;
            }
        } catch (ParseException e) {
            log.info("isDateBetween parse error:", e);
        }
        return ret;
    }

    /**
     * isDate
     * 
     * @param dateStr
     * @param formatStr
     * @return
     */
    public static boolean isDate(String dateStr, String formatStr) {
        if (dateStr == null) {
            return false;
        }
        if (formatStr == null) {
            return false;
        }
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        try {
            format.parse(dateStr);
        } catch (Exception e) {
            log.warn("isDate judge error:", e);
            return false;
        }
        return true;
    }

    /**
     * 计算某日期所在月份的天数
     * 
     * @param aDate
     * @return
     */
    public static final int getDaysInMonth(Date aDate) {
        Assert.notNull(aDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(aDate);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * getHttpDate
     * 
     * @param dateStr
     * @param dateFormat
     * @return
     */
    public static Date getHttpDate(String dateStr, String dateFormat) {
        try {
            return parser(dateStr, dateFormat);
        } catch (ParseException e) {
            throw new RuntimeException("将字符串" + dateStr + "按照" + dateFormat + "格式进行转换日期时发生异常:", e);
        }
    }

    /**
     * formatDateDefault
     * 
     * @param date
     * @return
     */
    public static String formatDateDefault(Date date) {
        return toFormatDateString(date, STD_DATE_FORMAT);
    }

    /**
     * 获得指定月的第一天
     * 
     * @param nowTime
     * @return
     */
    public static Date getMonthFirstDay(Date nowTime) {
        if (nowTime == null) {
            nowTime = new Date();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(nowTime);
        cal.add(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 方法说明: 描述：（创建于 2014年3月31日 下午12:06:04）.拼装时间 避免在SQL中使用函数 拼装当天开始时间 2014-03-31
     * 00：00：00
     * 
     * @param strDate
     * @return
     * @returnType Date
     */
    public static Date getStartDate(Date strDate) {
        if (strDate == null) {
            return null;
        }
        Calendar start = new GregorianCalendar();
        start.setTime(strDate);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        return start.getTime();

    }

    /**
     * 方法说明: 描述：（创建于 2014年3月31日 下午12:06:38）.拼装时间 避免在SQL中使用函数 拼装当天结束时间 2014-03-31
     * 23：59：59
     * 
     * @param strDate
     * @return
     * @returnType Date
     */
    public static Date getEndDate(Date strDate) {
        if (strDate == null) {
            return null;
        }
        Calendar end = new GregorianCalendar();
        end.setTime(strDate);
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);
        end.set(Calendar.MILLISECOND, 0);
        return end.getTime();
    }

    /**
     * 获取当前日期在当前月的第几天
     */
    public static final int getDaysInCurMonth(Date date) {
        return getDifferenceDays(getMonthFirstDay(date), getStartDate(date)) + 1;

    }

    /**
     * @Description:比较两个时间点 如果secondDate表示的时间等于此 firstDate 表示的时间，则返回 0 值； 如果此
     *                      firstDate 的时间在参数<secondDate>表示的时间之前，则返回小于 0 的值； 如果此
     *                      firstDate 的时间在参数<secondDate>表示的时间之后，则返回大于 0 的值
     * @param firstDate
     * @param secondDate
     * @ReturnType int
     * @author:
     * @Created 2012 2012-9-20上午08:34:33
     */
    public static int compare(Date firstDate, Date secondDate) {

        Calendar firstCalendar = null;
        /** 使用给定的 Date 设置此 Calendar 的时间。 **/
        if (firstDate != null) {
            firstCalendar = Calendar.getInstance();
            firstCalendar.setTime(firstDate);
        }

        Calendar secondCalendar = null;
        /** 使用给定的 Date 设置此 Calendar 的时间。 **/
        if (firstDate != null) {
            secondCalendar = Calendar.getInstance();
            secondCalendar.setTime(secondDate);
        }

        try {
            /**
             * 比较两个 Calendar 对象表示的时间值（从历元至现在的毫秒偏移量）。 如果参数表示的时间等于此 Calendar
             * 表示的时间，则返回 0 值； 如果此 Calendar 的时间在参数表示的时间之前，则返回小于 0 的值； 如果此
             * Calendar 的时间在参数表示的时间之后，则返回大于 0 的值
             **/
            return firstCalendar.compareTo(secondCalendar);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 获得指定月的第几天
     * 
     * @param nowTime
     * @return
     */
    public static Date getMonthInsureDay(Date nowTime, int day) {
        if (nowTime == null) {
            nowTime = new Date();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(nowTime);
        cal.add(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 获得当前时间的月份数
     * 
     * @param nowTime
     * @return
     */
    public static int getCurMonth(Date nowTime) {
        if (nowTime == null) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(nowTime);
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * 获得当前时间的年份数
     * 
     * @param nowTime
     * @return
     */
    public static int getCurYear(Date nowTime) {
        if (nowTime == null) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(nowTime);
        return cal.get(Calendar.YEAR);
    }
    
    /**
     * 获得当前时间的日期
     * 
     * @param nowTime
     * @return
     */
    public static int getCurDay(Date nowTime) {
        if (nowTime == null) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(nowTime);
        return cal.get(Calendar.DATE);
    }

    /**
     * 时间转换成日期
     */
    public static Date getDate(Date date) throws  Exception{

        return new SimpleDateFormat("yyyyMMdd").parse(new SimpleDateFormat("yyyyMMdd").format(date));
    }
    /**
     * 返回YYYYMM
     * @param actualLoanDate
     * @return
     */
	public static String getNextMonth(Date actualLoanDate) {
		
		Integer month = getCurMonth(actualLoanDate);
		
		Integer year = getCurYear(actualLoanDate);
		
		String strMonth = null;
		
		if(month==12){
			
			year = year + 1 ;
			
			strMonth = "01";
		}else{
			
			month = month + 1 ;
			
			if(month<10){
				
				strMonth = "0".concat(month.toString());
			}
		}
		
		return year.toString().concat(strMonth);
	}
    /**
     * 时间比较（HH:mm:ss）
     * 当前时间是否大于12点
     * date :  yyyyMMdd
     */
//    public static boolean  compareTime(Date date){
//        try{
//
//            String now = new SimpleDateFormat(MAIL_DATE_DT_PART_FORMAT).format(new Date());
//
//            String  req =  new SimpleDateFormat(MAIL_DATE_DT_PART_FORMAT).format(date);
//
//           if(DateUtil.getDaysSubtract2(req,now) ==1){
//               return new SimpleDateFormat(LONG_DATE_TM_PART_FORMAT).parse(
//                       new SimpleDateFormat(LONG_DATE_TM_PART_FORMAT).format(new Date())).getTime()
//                       > new SimpleDateFormat(LONG_DATE_TM_PART_FORMAT).parse(CommonConstants.AGREE_CLAIM_DATE_TIME).getTime();
//           }
//           return false;
//        }catch(Exception e){
//            log.info("dateFormat error",e);
//            return false;
//        }
//    }
    /**
     * 看清楚再用 added by //
     * @param startDate
     * @param endDate
     * @return
     */
    public static final long getDifferenceDay(Date startDate, Date endDate) {
    	
    	Assert.notNull(startDate);
        Assert.notNull(endDate);
        try {
	    	String startDateStr = DateUtil.toShortDateString(startDate);
	    	String endDateStr = DateUtil.toShortDateString(endDate);
	    	Date transStartDate = parser(startDateStr, SHORT_DATE_FORMAT);
			Date transEndDate = parser(endDateStr, SHORT_DATE_FORMAT);
	        return Math.abs(transEndDate.getTime() / NANO_ONE_DAY - transStartDate.getTime() / NANO_ONE_DAY);
        } catch (ParseException e) {
        	
        	throw new RuntimeException("格式进行转换日期时发生异常:", e);
		}
    }

	/**
     * 看清楚再用 added by //
     * @param startDate 有正有负数
     * @param endDate
     * @return
     */
    public static final long getDifferenceDayNotAbs(Date startDate, Date endDate) {
    	
    	Assert.notNull(startDate);
        Assert.notNull(endDate);
        try {
	    	String startDateStr = DateUtil.toShortDateString(startDate);
	    	String endDateStr = DateUtil.toShortDateString(endDate);
	    	Date transStartDate = parser(startDateStr, SHORT_DATE_FORMAT);
			Date transEndDate = parser(endDateStr, SHORT_DATE_FORMAT);
	        return transEndDate.getTime() / NANO_ONE_DAY - transStartDate.getTime() / NANO_ONE_DAY;
        } catch (ParseException e) {
        	
        	throw new RuntimeException("格式进行转换日期时发生异常:", e);
		}
    }

    public static final List<String> getYyyymmddSpan(String startDate, String endDate){
        Assert.notNull(startDate, "startDate不能为null");
        Assert.notNull(endDate, "endDate不能为null");
        startDate = startDate.trim();
        endDate = endDate.trim();

        List<String> resultList = new ArrayList<>();
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        if(start.isAfter(end)){
            throw new IllegalArgumentException("startDate不能大于endDate！");
        }
        while(!start.isAfter(end)){
            resultList.add(start.format(formatters));
            start = start.plusDays(1L);
        }
        return resultList;
    }

    public static void main(String args[]){

        try {
//            Date testDate =  parseDefaultDateString("Thu Aug 30 2018 00:00:00 GMT+0800");

//            System.out.println(testDate);


//            String dateString = "Thu Aug 30 2018 00:00:00 GMT+0800";
//            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z", Locale.US);
//            Date dd = sdf.parse(dateString); //将字符串改为date的格式
//            String resDate= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dd);
//            System.out.println(resDate);


            List<String> result = getYyyymmddSpan("2019-01-01","2019-02-01");
            Date date = new Date("Thu Aug 30 2018 00:00:00 GMT+0800");
            System.out.println(date);
            SimpleDateFormat dateformat1=new SimpleDateFormat("yyyy-MM-dd");

            String a1=dateformat1.format(new Date());
            System.out.println(a1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
