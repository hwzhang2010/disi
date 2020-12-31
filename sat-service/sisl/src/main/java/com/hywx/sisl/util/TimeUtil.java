package com.hywx.sisl.util;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.GregorianCalendar;

/**
 * 时间处理工具类
 * @author zhang.huawei
 *
 */
public class TimeUtil {
	private static final int ZONE_OFFSET = 8;
	private static final int SECOND_SKIP = 18;
	
	/**
	 * 根据积日计算日期
	 * @param jd
	 * @return
	 */
	public static LocalDateTime calFromJD(int jd) {
		LocalDateTime start = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
		LocalDateTime end = start.plusDays(jd - 1);
		
		return end;
	}
	
	/**
	 * 根据积日积秒计算时间
	 * @param jd
	 * @param js
	 * @return
	 */
	public static LocalDateTime calFromJDJS(int jd, double js) {
		LocalDateTime start = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
		LocalDateTime end = start.plusDays(jd - 1).plusSeconds((long)js / 10000);
		
		return end;
	}
	
	/**
	 * 计算积日：相对于2000年1月1日0时0分0秒
	 * @param to
	 * @return
	 */
	public static long calJD(LocalDateTime to) {
		//起点时间：2000年1月1日零点
		LocalDateTime from = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
		
		Duration duration = Duration.between(from, to);
		//计算时间和起点时间相差的天数
		return duration.toDays();
	}
	
	/**
	 * 计算积秒：当日0时0分0秒，量化单位：0.1ms
	 * @param to
	 * @return
	 */
	public static long calJS(LocalDateTime to) {
		//起点时间：当日零点
		LocalDateTime from = LocalDateTime.of(to.getYear(), to.getMonth(), to.getDayOfMonth(), 0, 0, 0);
		
		Duration duration = Duration.between(from, to);
		//计算时间和起点时间相差的纳秒数，并按0.1ms为量化单位
		return (long) (duration.toNanos() / 100000.0);
	}
	
	/**
	 * 计算当日已经过去的秒数
	 * @param to
	 * @return
	 */
	public static long calToadyPassedSeconds(LocalDateTime to) {
		//起点时间：当日零点
		LocalDateTime from = LocalDateTime.of(to.getYear(), to.getMonth(), to.getDayOfMonth(), 0, 0, 0);
		
		Duration duration = Duration.between(from, to);
		//计算时间和起点时间相差的秒数
		return (long) (duration.toMillis() / 1000.0);
	}
	
	/**
	 * 计算GPS周：时间起点1980年1月6日0点
	 * @param to
	 * @return
	 */
	public static int calWeekOfGps(LocalDateTime to) {
		//UTC时间
		to = to.minusHours(8);
		
		//初始化未360天，即1980年1月6日 - 1981年1月1日的天数
		int totalday = 360;
		
		//从1981年1月1日到当前年的上一年经过的天数
		int year = to.getYear();
		for (int y = 1981; y < year; y++)
        {
			//闰年
            if ( ((GregorianCalendar) GregorianCalendar.getInstance()).isLeapYear(y) )
                totalday += 366;
            else
                totalday += 365;
        }
		
		int[] daysOfMonth = new int[] { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		int month = to.getMonthValue();
		//从当前年到当前月的上一个月经过的天数
		for (int m = 0; m < month - 1; m++)
        {
            totalday += daysOfMonth[m];
            if (m == 1)
            {
                if ( (year % 4 == 0 && year % 100 != 0) || year % 400 == 0 )
                    totalday += 1;
            }
        }
		
		//当前月经过的天数
		totalday += to.getDayOfMonth();
		
		//经过的GPS周
		return totalday / 7;
	}
	
	/**
	 * 计算GPS周：时间起点1980年1月6日0点
	 * @param to
	 * @return
	 */
	public static int calGpsWeek(LocalDateTime to) {
		//UTC时间
		to = to.minusHours(ZONE_OFFSET);
		
		//计算日期差
		Duration duration = Duration.between(LocalDateTime.of(1980, 1, 6, 0, 0, 0), to);
		
		return (int) (duration.toDays() / 7);
	}
	
	
	/**
	 * 计算周内GPS秒
	 * @param to
	 * @return
	 */
	public static double calSecondsOfGps(LocalDateTime to) {
		//UTC时间
		to = to.minusHours(ZONE_OFFSET);
				
		//初始化未360天，即1980年1月6日 - 1981年1月1日的天数
		int totalday = 360;
		
		//从1981年1月1日到当前年的上一年经过的天数
		int year = to.getYear();
		for (int y = 1981; y < year; y++)
        {
			//闰年
            if ( ((GregorianCalendar) GregorianCalendar.getInstance()).isLeapYear(y) )
                totalday += 366;
            else
                totalday += 365;
        }
		
		int[] daysOfMonth = new int[] { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		int month = to.getMonthValue();
		//从当前年到当前月的上一个月经过的天数
		for (int m = 0; m < month - 1; m++)
        {
            totalday += daysOfMonth[m];
            if (m == 1)
            {
                if ( (year % 4 == 0 && year % 100 != 0) || year % 400 == 0 )
                    totalday += 1;
            }
        }
		//当前月经过的天数
		totalday += to.getDayOfMonth();
		
		//周内GPS秒，18表示跳秒
		return (totalday % 7) * 86400 + to.getHour() * 3600 + to.getMinute() * 60 + to.getSecond() + SECOND_SKIP;
	}
	
	/**
	 * 计算周内GPS秒
	 * @param to
	 * @return
	 */
	public static double calGpsSecond(LocalDateTime to) {
		//UTC时间
		to = to.minusHours(8);
		
		//计算日期差
		Period period = Period.between(LocalDate.of(1980, 1, 6), LocalDate.of(to.getYear(), to.getMonth(), to.getDayOfMonth()));
		LocalDateTime from = to.minusDays(period.getDays() % 7)
				               .withHour(0)
				               .withMinute(0)
				               .withSecond(0)
				               .withNano(0);
		
		//计算时间差
		Duration duration = Duration.between(from, to);
	
		//18表示跳秒
		return duration.toMillis() / 1000.0 + SECOND_SKIP;
	}
	
}
