package com.local.naruto.common.utils;

import com.local.naruto.common.constant.Constants;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.TimeZone;

public class DateUtils {

    /**
     * 字符串转换为日期
     *
     * @param dateString 需要转换的字符串
     * @param pattern    转换格式
     * @return 日期
     */
    public static Date parseToDateTime(String dateString, String pattern) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.parse(dateString);
    }

    /**
     * 获取UTC标准时间
     *
     * @return 日期的字符串
     */
    public static String getUtcTime() {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat(Constants.FULL_DATE_FORMAT);
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormatGmt.format(new Date());
    }

    /**
     * 时间比较
     *
     * @return 比较结果
     */
    public static boolean compareDate(String start, String end) throws ParseException {
        DateFormat format = new SimpleDateFormat(Constants.FULL_DATE_FORMAT);
        Date startDate = format.parse(start);
        Date endDate = format.parse(end);
        return startDate.before(endDate);
    }

    /**
     * 获取当前毫秒
     *
     * @return 毫秒
     */
    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 日期格式化
     *
     * @param date    需要格式化的日期
     * @param pattern 日期格式，自定义
     * @return 格式化后的日期字符串
     */
    public static String getFormatDateString(Date date, String pattern) {
        if (Objects.isNull(date)) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

    /**
     * 日期加年份、月份、天数
     *
     * @param currentDate 当前日期
     * @param years       需要加的年份，正数表示往后加，负数表示往前加
     * @param months      需要加的月份，正数表示往后加，负数表示往前加
     * @param days        需要加的天数，正数表示往后加，负数表示往前加
     * @return 加了年份、月份、天数后的日期
     */
    public static Date addYearMonthDay(Date currentDate, int years, int months, int days) {
        GregorianCalendar calender = new GregorianCalendar();
        calender.setTime(currentDate);
        calender.add(GregorianCalendar.YEAR, years);
        calender.add(GregorianCalendar.MONTH, months);
        calender.add(GregorianCalendar.DAY_OF_MONTH, days);
        return calender.getTime();
    }

}
