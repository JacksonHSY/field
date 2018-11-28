package com.yuminsoft.ams.system.util;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期时间工具
 * 
 * @author: fuhongxing
 */
public class DateUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);
	
	/**yyyy-MM-dd HH:mm:ss*/
	public static final String FORMAT_DATA_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	/**yyyy-MM-dd HH:mm*/
	public static final String FORMAT_DATE_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	/**yyyy-MM-dd*/
	public static final String FORMAT_DATE_YYYY_MM_DD = "yyyy-MM-dd";
	/**yyyyMMdd*/
	public static final String FORMAT_DATE_YYYYMMDD = "yyyyMMdd";
	/**yyyy年MM月dd日*/
	public static final String FORMAT_DATE_ZHONGWEN = "yyyy年MM月dd日";


	/** 默认的日期格式 yyyyMMddHHmmss*/
	public static final String DEFAULT_DATE_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	/** 默认的日期格式 yyyy-MM-dd */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

	/** 默认的日期格式 yyyyMMdd */
	public static final String DEFAULTDATEFORMAT = "yyyyMMdd";

    /** 日期格式 yyyy/MM/dd */
    public static final String UGLY_DATE_FORMAT = "yyyy/MM/dd";

    /** 日期格式2 yyyyMMdd */
    public static final String UGLY_DATE_FORMAT2 = "yyyyMMdd";

    /** 日期格式3 yyyy年MM月dd日 */
    public static final String UGLY_DATE_FORMAT3 = "yyyy'年'MM'月'dd'日'";
    
    /** 日期格式 M/d/yyyy */
    public static final String MOTHERFUCKER_DATE_FORMAT = "M/d/yyyy";

    /** 默认的日期+时间格式 yyyy-MM-dd HH:mm:ss */
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /** 日期+时间格式 yyyy/MM/dd HH:mm:ss */
    public static final String UGLY_DATETIME_FORMAT = "yyyy/MM/dd HH:mm:ss";

    /** 日期+时间格式 yyyy/MM/dd HH:mm:ss */
    public static final String UGLY_DATETIME_FORMAT2 = "yyyyMMdd HH:mm:ss";

    /** 默认的时间格式 HH:mm:ss */
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    /** 默认的日期格式对象 yyyy-MM-dd */
    public static final DateFormat DEFAULT_DATE_FORMATTER = new SimpleDateFormat(DEFAULT_DATE_FORMAT);

    /** 日期格式对象 yyyy/MM/dd */
    public static final DateFormat UGLY_DATE_FORMATTER = new SimpleDateFormat(UGLY_DATE_FORMAT);

    /** 日期格式对象2 yyyyMMdd */
    public static final DateFormat UGLY_DATE_FORMATTER2 = new SimpleDateFormat(UGLY_DATE_FORMAT2);

    /** 日期格式对象3 yyyy年MM月dd日 */
    public static final DateFormat UGLY_DATE_FORMATTER3 = new SimpleDateFormat(UGLY_DATE_FORMAT3);
    
    /** 日期格式对象 M/d/yyyy */
    public static final DateFormat MOTHERFUCKER_DATE_FORMATTER = new SimpleDateFormat(MOTHERFUCKER_DATE_FORMAT);

    /** 日期+时间格式对象 yyyy-MM-dd HH:mm:ss */
    public static final DateFormat DEFAULT_DATETIME_FORMATTER = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);

    /** 日期+时间格式对象 yyyy/MM/dd HH:mm:ss */
    public static final DateFormat UGLY_DATETIME_FORMATTER = new SimpleDateFormat(UGLY_DATETIME_FORMAT);

    /** 日期+时间格式对象2 yyyyMMdd HH:mm:ss */
    public static final DateFormat UGLY_DATETIME_FORMATTER2 = new SimpleDateFormat(UGLY_DATETIME_FORMAT2);

    /** 默认的时间格式对象 HH:mm:ss */
    public static final DateFormat DEFAULT_TIME_FORMATTER = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
    
    
	
	private DateUtils(){
		
	}
	
	/**
	 * 
	 * 字符串转换为某一格式的日期
	 * @param dateStr
	 * @param formatStr
	 * @return
	 */
	public static Date stringToDate(String dateStr, String formatStr) {
		DateFormat dd = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
			date = dd.parse(dateStr);
		} catch (ParseException e) {
			LOGGER.error("转换异常",e);
		}

		return date;
	}

	/**
	 * 时间转字符串
	 * @param time
	 * @param pattern
	 * @return
	 */
	public static String dateToString(Date time, String pattern) {
		SimpleDateFormat format = null;
		if(pattern == null || "".equals(pattern)){
			format = new SimpleDateFormat(FORMAT_DATA_YYYY_MM_DD_HH_MM_SS);
		}else{
			format = new SimpleDateFormat(pattern);
		}
		return format.format(time);
	}
	
	/**
	 * 两个时间相隔天数（不计算时分秒）
	 * 
	 * @param fDate
	 * @param oDate
	 * @return
	 */
	public static int daysOfTwo(Date fDate, Date oDate) {
		return (int) ((getDate(fDate).getTime() - getDate(oDate).getTime()) / 86400000);
	}
	
	/**
	 * 天增加或减少计算(当前日期+count)
	 * @param date 当前时间
	 * @param count 天数
	 * @return 
	 */
	public static Date addDate(Date date, int count) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.DAY_OF_YEAR, count);

		return calendar.getTime();
	}
	
	/**
	 * 分钟添加递减计算
	 * @param date  当前时间
	 * @param count 分钟 
	 * @return
	 */
	public static Date addMinute(Date date, int count) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, count);
		return calendar.getTime();
	}

	/**
	 * 
	 * @description: 月份增减计算
	 * @param date
	 * @param count
	 * @return
	 */
	public static Date addMonth(Date date, int count) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.MONTH, count);

		return calendar.getTime();
	}

	/**
	 * 
	 * 月份增减计算，默认为当天
	 * @param count
	 * @return
	 */
	public static Date addMonth(int count) {
		Date date = new Date();
		return addMonth(date, count);
	}
	

	/**
	 * 将日期的时分秒都改为最大值
	 * @param date
	 * @return
	 */
	public static Date getDateByLast(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MINUTE, 23);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.HOUR_OF_DAY, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}
	
	/**
	 * 周 增加或减少计算
	 * @param date
	 * @param count
	 * @return
	 */
	public static Date addWeek(Date date, int count) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 7 * count);

		return calendar.getTime();
	}

	/**
	 * 
	 * @description: 增减周计算
	 * @author: wujinsong
	 * @param count
	 * @return
	 */
	public static Date addWeek(int count) {
		Date date = new Date();

		return addWeek(date, count);
	}

	/**
	 * 返回当前季度
	 * @return
	 */
	public static Integer currentQuarter() {
		Calendar calendar = Calendar.getInstance();
		return (int) Math.ceil((calendar.get(Calendar.MONTH) + 1) / Double.valueOf(3));
	}
	
	/**
	 * 返回当前日期 时分秒
	 * @return
	 */
	public static Date getCurrenDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	/**
	 * 返回当前日期的下一天 时分秒
	 * @return
	 */
	public static Date getNextDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.add(Calendar.DATE, 1);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	/**
	 * 返回当前日期的前一天 时分秒
	 * @return
	 */
	public static Date getYesterDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.add(Calendar.DATE, -1);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	/**
	 * 得到本季度第一天的日期
	 * 
	 * @Methods Name getFirstDayOfQuarter
	 * @return Date
	 */
	public static Date getFirstDayOfQuarter() {
		Calendar cDay = Calendar.getInstance();
		cDay.setTime(new Date());
		int curMonth = cDay.get(Calendar.MONTH);
		if (curMonth >= Calendar.JANUARY && curMonth <= Calendar.MARCH) {
			cDay.set(Calendar.MONTH, Calendar.JANUARY);
		}
		if (curMonth >= Calendar.APRIL && curMonth <= Calendar.JUNE) {
			cDay.set(Calendar.MONTH, Calendar.APRIL);
		}
		if (curMonth >= Calendar.JULY && curMonth <= Calendar.AUGUST) {
			cDay.set(Calendar.MONTH, Calendar.JULY);
		}
		if (curMonth >= Calendar.OCTOBER && curMonth <= Calendar.DECEMBER) {
			cDay.set(Calendar.MONTH, Calendar.OCTOBER);
		}
		cDay.set(Calendar.DAY_OF_MONTH, cDay.getActualMinimum(Calendar.DAY_OF_MONTH));
		cDay.set(Calendar.MINUTE, 0);
		cDay.set(Calendar.SECOND, 0);
		cDay.set(Calendar.HOUR_OF_DAY, 0);
		cDay.set(Calendar.MILLISECOND, 0);
		return cDay.getTime();
	}

	/**
	 * 获取当前时间 只有年月日
	 * 
	 * @return
	 */
	public static Calendar getCurrenCalendar() {
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}
	
	/**
	 * 获取本周星期一
	 * 
	 * @return
	 */
	public static Date getCurrentMonday() {
		Calendar calendar = getCurrenCalendar();
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return calendar.getTime();
	}

	/**
	 * 获取本周星期天
	 * 
	 * @return
	 */
	public static Date getCurrentSunday() {
		Calendar calendar = getCurrenCalendar();
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return calendar.getTime();
	}

	/**
	 * 获取下周星期一
	 * 
	 * @return
	 */
	public static Date getNextMonday() {
		Calendar calendar = getCurrenCalendar();
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		calendar.add(Calendar.WEEK_OF_YEAR, 1);
		return calendar.getTime();
	}

	/**
	 * 获取下周星期一
	 * 
	 * @return
	 */
	public static Date getNextMonday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		calendar.add(Calendar.WEEK_OF_YEAR, 1);
		return calendar.getTime();
	}

	/**
	 * 
	 * @description: 获取本周周一
	 * @return
	 */
	public static String getMondayOfThisWeek() {
		Calendar c = Calendar.getInstance();
		int dayofweek = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (dayofweek == 0){
			dayofweek = 7;
		}
		c.add(Calendar.DATE, -dayofweek + 1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		return sdf.format(c.getTime());
	}

	
	/**
	 * 
	 * @description: 获取日期是周几
	 * @return
	 */
	public static String getWeekDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayofweek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		return convert(dayofweek);
	}

	static String convert(int val) {
		String retStr = "";
		switch (val) {
		case 0:
			return "星期日";
		case 1:
			return "星期一";
		case 2:
			return "星期二";
		case 3:
			return "星期三";
		case 4:
			return "星期四";
		case 5:
			return "星期五";
		case 6:
			return "星期六";
		default:
			break;
		}
		return retStr;
	}

	/**
	 * 
	 * @description: 获取本周周末
	 * @return
	 */
	public static String getSundayOfThisWeek() {
		Calendar c = Calendar.getInstance();
		int dayofweek = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (dayofweek == 0){
			dayofweek = 7;
		}
		c.add(Calendar.DATE, -dayofweek + 7);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		return sdf.format(c.getTime());
	}


	/**
	 * 获得当年某月的第一天
	 * 
	 * @param count 月份 01 02 11 12
	 * @return
	 */
	public static Date getFirstDayOfMonth(int count) {
		Calendar calendar = Calendar.getInstance();
		int thisYear = calendar.get(Calendar.YEAR);
		calendar.clear();
		calendar.set(Calendar.YEAR, thisYear);
		calendar.add(Calendar.MONTH, count - 1);
		return calendar.getTime();
	}

	/**
	 * 获得当年某月的最后一天
	 * 
	 * @param count
	 *            month 月份 01 02 11 12
	 * @return
	 */
	public static Date getLastDayOfMonth(int count) {
		Calendar calendar = Calendar.getInstance();
		int thisYear = calendar.get(Calendar.YEAR);
		calendar.clear();
		calendar.set(Calendar.YEAR, thisYear);
		calendar.add(Calendar.MONTH, count);
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		return calendar.getTime();
	}

	/**
	 * 
	 * @description: 获取n小时前的时间
	 * @param n
	 * @return
	 */
	public static Date getNHourTime(int n) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - n);

		return calendar.getTime();
	}

	/**
	 * 
	 * @description: 获取当天结束时间(24小时制)
	 * @return
	 */
	public static Date getEndTime() {
		Calendar todayEnd = Calendar.getInstance();
		todayEnd.set(Calendar.HOUR_OF_DAY, 23);
		todayEnd.set(Calendar.MINUTE, 59);
		todayEnd.set(Calendar.SECOND, 59);
		todayEnd.set(Calendar.MILLISECOND, 999);
		return todayEnd.getTime();
	}

	/**
	 * 
	 * @description: 获取当天开始时间(24小时制)
	 * @return
	 */
	public static Date getStartTime() {
		Calendar todayStart = Calendar.getInstance();
		todayStart.set(Calendar.HOUR_OF_DAY, 0);
		todayStart.set(Calendar.MINUTE, 0);
		todayStart.set(Calendar.SECOND, 0);
		todayStart.set(Calendar.MILLISECOND, 0);
		return todayStart.getTime();
	}


	/**
	 * 去掉时分秒
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 
	 * @description: 转换日期为yyyy-MM-dd格式
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date format(Date date) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATE_YYYY_MM_DD);
		String sDate = format.format(date);
		return format.parse(sDate);
	}

	/**
	 * 获取当年的最后一天 by gab
	 */
	public static Date getCurrYearLast() {
		Calendar currCal = Calendar.getInstance();
		int currentYear = currCal.get(Calendar.YEAR);
		return getYearLast(currentYear);
	}

	public static Date getYearLast(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		Date currYearLast = calendar.getTime();
		return currYearLast;
	}

	/**
	 * 
	 * @description: 判断时间间隔，分钟
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static boolean judgeMinuteDiffByTime(Date time1, Date time2, int min) {
		long diffValue = time1.getTime() - time2.getTime();
		return Math.abs(diffValue) > min * 1000 * 60;
	}

	/**
	 * 
	 * @description: 判断时间间隔
	 * @author: wujinsong
	 * @param time1
	 * @param min
	 * @return
	 */
	public static boolean judgeMinuteDiffByTime(Date time1, int min) {
		return judgeMinuteDiffByTime(time1, new Date(), min);
	}

	/**
	 * isToday
	 * 
	 * @param date 判断时间是不是当天
	 * @return boolean
	 * 
	 */
	public static boolean isToday(Date date) {
		return DateUtils.getEndTime().getTime() >= date.getTime() && DateUtils.getStartTime().getTime() <= date.getTime();
	}

	/**
	 * 获取当前时间是否在指定时间之前。
	 * 
	 * @return
	 */
	public static boolean beforeForHour(Date date, int hour) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateUtils.getDate(date));
		cal.set(Calendar.HOUR_OF_DAY, hour);
		if (System.currentTimeMillis() < cal.getTimeInMillis()) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @description: 获取当天开始时间(24小时制)
	 * @author: gab
	 * @return
	 */
	public static Date getInitTime(Date date) {
		Calendar start = Calendar.getInstance();
		start.setTime(date);
		start.set(Calendar.HOUR_OF_DAY, 0);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
		start.set(Calendar.MILLISECOND, 0);
		return start.getTime();
	}

	/**
	 * 获取日期间隔 by gab
	 * */
	public static int getDaysBetweenDate(Date date1, Date date2) {
		long time1 = getInitTime(date1).getTime();
		long time2 = getInitTime(date2).getTime();
		long betweenDays = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(betweenDays));
	}

	/**
	 * 将日期的时分秒都改为最大值。
	 */
	public static Date getDateLast(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}
	
	/**
     * 判断第三个参数的日期是否在第一个参数和第二个参数区间之中
     * 
     * @param a 日期 A
     * @param b 日期 B
     * @param date 要判断的日期对象
     * @return 是否在 A 和 B 区间内。<i>如果等于 B，也返回 true</i>
     */
	public static final boolean isBetween(Date a, Date b, Date date) {
		if (a == null || b == null || date == null) {
			return false;
		}

		Interval interval = new Interval(toJodaDateTime(a), toJodaDateTime(b));
		return interval.contains(date.getTime()) || date.equals(b);
	}
	
	/**
     * 判断第三个参数的日期是否在第一个参数和第二个参数区间之中（字符串版）
     * 
     * @param a 日期 A
     * @param b 日期 B
     * @param date 要判断的日期字符串
     * @return 是否在 A 和 B 区间内。<i>如果等于 B，也返回 true</i>
     * @throws ParseException 如果传入的日期字符串不符合格式，抛出该异常
     */
	public static final boolean isBetween(String a, String b, String date) throws ParseException {
		if (a == null || b == null || date == null) {
			return false;
		}

		Interval interval = new Interval(toJodaDateTime(a), toJodaDateTime(b));
		return interval.contains(toJodaDateTime(date)) || date.equals(b);
	}
    
    /**
     * 取得 Joda 日期时间对象
     * 
     * @param date 要转换的日期字符串（yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss）
     * @return Joda 对象
     * @throws ParseException 如果传入的日期字符串不符合格式，抛出该异常
     */
	public static final DateTime toJodaDateTime(String date) throws ParseException {
		return toJodaDateTime(parseDate(date));
	}
    
    /**
     * 取得 Joda 日期时间对象
     * 
     * @param date 要转换的对象
     * @return Joda 对象
     */
	public static final DateTime toJodaDateTime(Date date) {
		return new DateTime(date);
	}

	private static Date parseDate(String date) {
		DateFormat formatters[] = { DEFAULT_DATETIME_FORMATTER, UGLY_DATETIME_FORMATTER, UGLY_DATETIME_FORMATTER2, DEFAULT_TIME_FORMATTER, DEFAULT_DATE_FORMATTER, UGLY_DATE_FORMATTER, UGLY_DATE_FORMATTER2, UGLY_DATE_FORMATTER3, MOTHERFUCKER_DATE_FORMATTER };

		for (DateFormat formatter : formatters) {
			try {
				return formatter.parse(date);
			} catch (ParseException e) {
				continue;
			}
		}
		return null;
	}
	
	/**
     * 获取 a 到 b 两个日期间隔多少分钟
     * 
     * @param a 第一个时间字符串（yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss）
     * @param b 第二个时间字符串（yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss）
     * @return 分钟数
     * @throws ParseException 如果传入的日期字符串不符合格式，抛出该异常
     */
	public static final int getMinutesBetween(String a, String b) throws ParseException {
		return Minutes.minutesBetween(toJodaDateTime(a), toJodaDateTime(b)).getMinutes();
	}
	
	/**
     * 获取 a 到 b 两个日期间隔多少秒
     * 
     * @param a 第一个时间字符串（yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss）
     * @param b 第二个时间字符串（yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss）
     * @return 秒数
     * @throws ParseException 如果传入的日期字符串不符合格式，抛出该异常
     */
    public static final int getSecondsBetween(String a, String b) throws ParseException
    {
        return Seconds.secondsBetween(toJodaDateTime(a), toJodaDateTime(b)).getSeconds();
    }

	/**
	 * 两个时间相隔月数只精确到月(currentDate-date)
	 * @param date 第一个时间和第二个时间的月差 -第二个减去第一个时间
	 * @param currentDate 为空时取当前时间
	 * @return 返回null获取间隔月数失败
	 */
	public static Integer monthsOfTwo(Date date, Date currentDate) {
		Integer result = 0;
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			//如果比较时间为空，默认取当前时间
			if(null == currentDate){
				currentDate = new Date();
			}
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(currentDate);

			int year = calendar2.get(Calendar.YEAR) - calendar.get(Calendar.YEAR);
			int month = calendar2.get(Calendar.MONTH) - calendar.get(Calendar.MONTH);
			year = year * 12;// 获取年之间的差多少个月;
			result = year + month;
		} catch (Exception e) {
			LOGGER.error("两个时间相隔月数获取异常:", e);
			result = null;
		}
		return result;
	}

}
