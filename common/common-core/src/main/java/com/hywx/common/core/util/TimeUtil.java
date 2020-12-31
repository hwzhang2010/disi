package com.hywx.common.core.util;

import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: common
 * @description: 时间处理共通类
 * @author: tangjing
 * @create: 2020-02-24 09:49
 */
public class TimeUtil {
    public static final String FORMAT_STR_SHORT = "yyyy-MM-dd";
    public static final String FORMAT_NUMBER_SHORT = "yyyyMMdd";
    public static final String FORMAT_STR_LONG = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_NUMBER_LONG = "yyyyMMddHHmmss";

    private static SimpleDateFormat df2 = new SimpleDateFormat(FORMAT_NUMBER_SHORT);

    public static SimpleDateFormat getDF2() {
        if (df2 == null) {
            df2 = new SimpleDateFormat(FORMAT_NUMBER_SHORT);
        }
        return df2;

    }

    /**
     * 格式化时间（[yyyyMMddHHmmss]转换成[yyyy-MM-dd HH:mm:ss]）
     *
     * @return String
     */
    public static String formatTime(long time) {
        String sTime = String.valueOf(time);
        Date date = stringToDate(sTime, TimeUtil.FORMAT_NUMBER_LONG);
        if (date != null) {
            return dateToString(date);
        }
        return null;
    }

    /**
     * 格式化时间（[yyyy-MM-dd HH:mm:ss]转换成[yyyyMMddHHmmss]）
     *
     * @return Long
     */
    public static Long formatTime(String time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(TimeUtil.FORMAT_STR_LONG);
            Date date = format.parse(time);
            String str = dateToString(date, TimeUtil.FORMAT_NUMBER_LONG);
            return Long.parseLong(str);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 格式化时间（[yyyy-MM-dd HH:mm:ss]转换成[yyyy-MM-dd]）
     *
     * @return Long
     */
    public static String formatStringTime(String time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(TimeUtil.FORMAT_STR_LONG);
            Date date = format.parse(time);
            String str = dateToString(date, TimeUtil.FORMAT_STR_SHORT);
            return str;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 格式化时间（[yyyy-MM-dd]转换成[yyyyMMdd]）
     *
     * @return Long
     */
    public static Integer formatDateTime(String time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(TimeUtil.FORMAT_STR_SHORT);
            Date date = format.parse(time);
            String str = dateToString(date, TimeUtil.FORMAT_NUMBER_SHORT);
            return Integer.parseInt(str);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 将Timestamp类型转换为String类型（默认格式）
     *
     * @return String
     */
    public static String dateToString(Date date) {
        return dateToString(date, TimeUtil.FORMAT_STR_LONG);
    }

    /**
     * 将Timestamp类型转换为String类型（指定格式）
     *
     * @return String
     */
    public static String dateToString(Date date, String format) {
        if (date != null) {
            SimpleDateFormat FORMAT = new SimpleDateFormat(format);
            return FORMAT.format(date);
        }
        return null;
    }

    /**
     * 将String类型（默认格式）转换为Timestamp类型
     *
     * @return Date
     */
    public static Date stringToDate(String str) {
        return stringToDate(str, TimeUtil.FORMAT_STR_LONG);
    }

    /**
     * 将String类型（指定格式）转换为Timestamp类型
     *
     * @return Date
     */
    public static Date stringToDate(String str, String format) {
        if (str != null && !"".equals(str)) {
            SimpleDateFormat FORMAT = new SimpleDateFormat(format);
            try {
                return FORMAT.parse(str);
            } catch (ParseException e) {
            }
        }
        return null;
    }

    /**
     * 字符串转换为UTC的秒数（1970-01-01 00:00:00以后秒数）
     * date 格式：yyyy-MM-dd HH:mm:ss
     *
     * @return long
     */
    public static long StringToUTC(String date) {
        long result = 0;
        try {
            Date cdate = stringToDate(date);
            if (cdate != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(cdate);
                result = cal.getTimeInMillis() / 1000;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将UTC的秒数（1970-01-01 00:00:00以后秒数）转换为字符串（yyyy-MM-dd HH:mm:ss）
     *
     * @return String
     */
    public static String UTCToString(long utcTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(utcTime * 1000);
        return dateToString(cal.getTime());
    }

    /**
     * 将UTC的秒数（1970-01-01 00:00:00以后秒数）转换为字符串（按日期格式）
     *
     * @return String
     */
    public static String UTCToString(long utcTime, String format) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(utcTime * 1000);
        return dateToString(cal.getTime(), format);
    }

    /**
     * 将秒数转化为01:01:01格式
     *
     * @return String
     */
    public static String formatSecond(int second) {
        if (second < 0) {
            return null;
        } else if (second == 0) {
            return "00:00:00";
        } else {
            int oneHour = 3600;
            int oneMinute = 60;
            String hours = second / oneHour < 10 ? "0" + second / oneHour
                    : second / oneHour + "";
            String minutes = second % oneHour / oneMinute < 10 ? "0" + second
                    % oneHour / oneMinute : second % oneHour / oneMinute + "";
            String seconds = second % oneHour % oneMinute < 10 ? "0" + second
                    % oneHour % oneMinute : second % oneHour % oneMinute + "";
            StringBuffer sb = new StringBuffer();
            sb.append(hours).append(":").append(minutes).append(":").append(seconds);
            return sb.toString();
        }
    }

    /**
     * 将秒数转化为 01小时01分钟01秒 格式
     *
     * @param second
     * @return
     */
    public static String formatSecondExt(int second) {
        StringBuffer sb = new StringBuffer();
        int totalSec = second < 0 ? 0 : second;

        int hour = totalSec / 3600;
        totalSec = totalSec % 3600;
        int min = totalSec / 60;
        totalSec = totalSec % 60;
        int sec = totalSec;

        if (hour > 0) sb.append(hour).append("小时");
        if (min > 0) sb.append(min).append("分钟");
        if (sec > 0) sb.append(sec).append("秒");
        return sb.toString();
    }

    /**
     * 获取startTime之后second秒的时间
     *
     * @param startTime
     * @param second
     * @return String
     */
    public static String getEndTime(String startTime, int second) {
        SimpleDateFormat FORMAT = new SimpleDateFormat(TimeUtil.FORMAT_STR_LONG);
        String newtime = null;
        try {
            Long time = FORMAT.parse(startTime).getTime() + second * 1000;
            newtime = FORMAT.format(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newtime;
    }

    //是否第一个日期小于第二个日期（是否过期日期小于当前日期：若小于则过期）
    public static boolean isSmallDate(Date date, Integer d2) {
        if (date != null) {
            Integer d1 = Integer.parseInt(getDF2().format(date));
            if (d1 < d2) return true;
        }
        return false;
    }

    //获取今天凌晨0点0分0秒的时间
    public static long getTodayTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return cal.getTimeInMillis();
    }

    /**
     * 时间格式转换为秒数
     *
     * @param date
     * @return
     */
    public static long dateToUTC(Date date) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.getTimeInMillis() / 1000;
        }
        return 0;
    }

    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days)) + 1;
    }

    private static ArrayList<String> filelist = new ArrayList<String>();

    /**
     * @param startTime 开始时间
     * @return void
     * @Description: 执行根据时间的分页查询时，当未指定初始时间时。初始化开始时间，将结束时间设置为当前时间
     * @Author: lj
     * @Date 2019/10/10 16:34
     * @throws:
     **/
    public static String initQueryStartTime(String startTime) {
        if (StringUtils.isEmpty(startTime)) {
            startTime = "1970-01-01 08:00:00";
        }
        return startTime;
    }

    /**
     * @param endTime 结束时间
     * @return void
     * @Description: 执行根据时间的分页查询时，当未指定初始时间时。初始化结束时间，将结束时间设置为当前时间
     * @Author: lj
     * @Date 2019/10/10 16:34
     * @throws:
     **/
    public static String initQueryEndTime(String endTime) {
        if (StringUtils.isEmpty(endTime)) {
            Date stopTime1 = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            endTime = sdf.format(stopTime1);
        }
        return endTime;
    }

    /**
     * @param endTime 结束时间
     * @return void
     * @Description: 执行根据时间的分页查询时，当未指定初始时间时。初始化结束时间，将结束时间设置为当前时间
     * @Author: lj
     * @Date 2019/10/10 16:34
     * @throws:
     **/
    public static String initQueryMaxEndTime(String endTime) {
        if (StringUtils.isEmpty(endTime)) {
            return "9999-12-31 00:00:00";
        }
        return endTime;
    }

    /**
     * @return java.lang.String
     * @Author: LXYuuuuu
     * @Description: 格式化时间
     * @Date 2019/12/5 16:49
     * @Param date
     * @throws:
     **/
    public static String fomartDate(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formatdate = df.format(date);
        return formatdate;
    }

    /**
     * @param
     * @return void
     * @Description: 驾驶证永久时间
     * @Author: lxy
     * @Date 2019/12/6 16:34
     * @throws:
     **/
    public static String initEndTime() {
        Date stopTime1 = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String endTime = sdf.format(stopTime1);
        return endTime;
    }

    /**
     * @return java.lang.String
     * @Author: LXYuuuuu
     * @Description: 判断星期几
     * @Date 2019/12/17 20:38
     * @Param pTime
     * @throws:
     **/
    public static String dayForWeek(String pTime) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date tmpDate = format.parse(pTime);
        Calendar cal = Calendar.getInstance();
        String[] weekDays = {"7", "1", "2", "3", "4", "5", "6"};
        try {
            cal.setTime(tmpDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    /** @Author: LXYuuuuu
     * @Description:  确定是不是节假日，是节假日返回true，工作日进行数据抓取返回false
     * 返回2 代表法定节假日休息，1 正常休息日，2 工作日
     * @Date 2019/12/17 20:39
     * @Param
     * @return boolean
     * @throws:
     **/
    public static boolean isHoliday(String date) {
        String httpUrl = "http://tool.bitefu.net/jiari/";
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?d=" +date;
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
            }
            reader.close();
            result = sbf.toString();
            // 0 上班  1周末 2节假日
            if ("0".equals(result)) {
                //return resultObject.getFailResult("上班");
                System.out.println("0上班");
                return false;
            }
            if ("1".equals(result)) {
                //return resultObject.getFailResult("1周末");
                System.out.println("1周末");
                return true;
            }
            if ("2".equals(result)) {
                //return resultObject.getFailResult("");
                System.out.println("2节假日");
                return true;
            }
            // 0 上班  1周末 2节假日
            /*if ("0".equals(jsonResult)) {
                //return resultObject.getFailResult("上班");
                System.out.println("0上班");
            }
            if ("1".equals(jsonResult)) {
                //return resultObject.getFailResult("1周末");
                System.out.println("1周末");
            }
            if ("2".equals(jsonResult)) {
                //return resultObject.getFailResult("");
                System.out.println("2节假日");
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * 格式化 CST类型的时间字符串
     *
     * @param date   CST类型的时间字符串
     * @param format 格式
     * @return 格式化后的字符串
     * @throws ParseException 异常
     */
    public static String formatCSTTime(String date, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        Date usDate = simpleDateFormat.parse(date);
        return dateToString(usDate,format);
    }

    /**
     * 获取前几天日期
     * @param d
     * @param day
     * @return
     */
    public static Date getDateBefore(Date d, int day) {
        Calendar no = Calendar.getInstance();
        no.setTime(d);
        no.set(Calendar.DATE, no.get(Calendar.DATE) - day);
        return no.getTime();
    }

    public static Date setDateStart(Date d, int day) {
        Calendar no = Calendar.getInstance();
        no.setTime(d);
        no.set(Calendar.DATE, no.get(Calendar.DATE) - day);
        return stringToDate(fomartDate(no.getTime())+" 00:00:00",FORMAT_STR_LONG);
}

    public static Date setDateEnd(Date d, int day) {
        Calendar no = Calendar.getInstance();
        no.setTime(d);
        no.set(Calendar.DATE, no.get(Calendar.DATE) - day);
        return stringToDate(fomartDate(no.getTime())+" 23:59:59",FORMAT_STR_LONG);
    }





    public static void main(String[] args) throws Throwable {
//        System.out.println(new Date());
        System.out.println(TimeUtil.stringToDate("2020-04-14 10:58:00","yyyy-MM-dd HH:mm:ss"));
    }
}
