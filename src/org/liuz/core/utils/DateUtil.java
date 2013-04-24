 package org.liuz.core.utils;

 import java.math.BigDecimal;
 import java.text.DateFormat;
 import java.text.ParseException;
 import java.text.SimpleDateFormat;
 import java.util.Calendar;
 import java.util.Date;
 import java.util.TimeZone;

 public class DateUtil
 {
   private static final String DATAFORMAT_STR = "yyyy-MM-dd";
   private static final String YYYY_MM_DATAFORMAT_STR = "yyyy-MM";
   private static final String DATATIMEF_STR = "yyyy-MM-dd HH:mm:ss";
   private static final String ZHCN_DATAFORMAT_STR = "yyyy年MM月dd日";
   private static final String ZHCN_DATATIMEF_STR = "yyyy年MM月dd日HH时mm分ss秒";
   private static final String ZHCN_DATATIMEF_STR_4yMMddHHmm = "yyyy年MM月dd日HH时mm分";
   private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
   private static DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   private static DateFormat zhcnDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
   private static DateFormat zhcnDateTimeFormat = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");

   private static DateFormat getDateFormat(String formatStr)
   {
     if (formatStr.equalsIgnoreCase("yyyy-MM-dd"))
       return dateFormat;
     if (formatStr.equalsIgnoreCase("yyyy-MM-dd HH:mm:ss"))
       return dateTimeFormat;
     if (formatStr.equalsIgnoreCase("yyyy年MM月dd日"))
       return zhcnDateFormat;
     if (formatStr.equalsIgnoreCase("yyyy年MM月dd日HH时mm分ss秒")) {
       return zhcnDateTimeFormat;
     }
     return new SimpleDateFormat(formatStr);
   }

   public static Date getDate(String dateTimeStr)
   {
     return getDate(dateTimeStr, "yyyy-MM-dd HH:mm:ss");
   }

   public static Date getDate(String dateTimeStr, String formatStr)
   {
     try
     {
       if ((dateTimeStr == null) || (dateTimeStr.equals(""))) {
         return null;
       }
       DateFormat sdf = getDateFormat(formatStr);
       return sdf.parse(dateTimeStr);
     }
     catch (ParseException e) {
       throw new RuntimeException(e);
     }
   }

   public static Date transferDate(String date)
     throws Exception
   {
     if ((date == null) || (date.length() < 1)) {
       return null;
     }
     if (date.length() != 8)
       throw new Exception("日期格式错误");
     String con = "-";

     String yyyy = date.substring(0, 4);
     String mm = date.substring(4, 6);
     String dd = date.substring(6, 8);

     int month = Integer.parseInt(mm);
     int day = Integer.parseInt(dd);
     if ((month < 1) || (month > 12) || (day < 1) || (day > 31)) {
       throw new Exception("日期格式错误");
     }
     String str = yyyy + con + mm + con + dd;
     return getDate(str, "yyyy-MM-dd");
   }

   public static String dateToDateString(Date date)
   {
     return dateToDateString(date, "yyyy-MM-dd HH:mm:ss");
   }

   public static String dateToDateShortString(Date date)
   {
     return dateToDateString(date, "yyyy-MM-dd");
   }

   public static String dateToZhDateString(Date date)
   {
     return dateToDateString(date, "yyyy年MM月dd日");
   }

   public static String dateToZhTimeString(Date date)
   {
     return dateToDateString(date, "yyyy年MM月dd日HH时mm分ss秒");
   }

   public static String dateToDateString(Date date, String formatStr)
   {
     DateFormat df = getDateFormat(formatStr);
     return df.format(date);
   }

   public static String getTimeString(String dateTime)
   {
     return getTimeString(dateTime, "yyyy-MM-dd HH:mm:ss");
   }

   public static String getTimeString(String dateTime, String formatStr)
   {
     Date d = getDate(dateTime, formatStr);
     String s = dateToDateString(d);
     return s.substring("yyyy-MM-dd HH:mm:ss".indexOf('H'));
   }

   public static String getCurDate()
   {
     return dateToDateString(Calendar.getInstance().getTime(),
       "yyyy-MM-dd");
   }

   public static String getCurZhCNDate()
   {
     return dateToDateString(new Date(), "yyyy年MM月dd日");
   }

   public static String getCurDateTime()
   {
     return dateToDateString(new Date(), "yyyy-MM-dd HH:mm:ss");
   }

   public static String getCurZhCNDateTime()
   {
     return dateToDateString(new Date(), "yyyy年MM月dd日HH时mm分ss秒");
   }

   public static Date getInternalDateByDay(Date d, int days)
   {
     Calendar now = Calendar.getInstance(TimeZone.getDefault());
     now.setTime(d);
     now.add(5, days);
     return now.getTime();
   }

   public static Date getInternalDateByMon(Date d, int months) {
     Calendar now = Calendar.getInstance(TimeZone.getDefault());
     now.setTime(d);
     now.add(2, months);
     return now.getTime();
   }

   public static Date getInternalDateByYear(Date d, int years) {
     Calendar now = Calendar.getInstance(TimeZone.getDefault());
     now.setTime(d);
     now.add(1, years);
     return now.getTime();
   }

   public static Date getInternalDateBySec(Date d, int sec) {
     Calendar now = Calendar.getInstance(TimeZone.getDefault());
     now.setTime(d);
     now.add(13, sec);
     return now.getTime();
   }

   public static Date getInternalDateByMin(Date d, int min) {
     Calendar now = Calendar.getInstance(TimeZone.getDefault());
     now.setTime(d);
     now.add(12, min);
     return now.getTime();
   }

   public static Date getInternalDateByHour(Date d, int hours) {
     Calendar now = Calendar.getInstance(TimeZone.getDefault());
     now.setTime(d);
     now.add(11, hours);
     return now.getTime();
   }

   public static Date getInternalDateByHour(Date d, int hours, int min, int sec) {
     Calendar now = Calendar.getInstance(TimeZone.getDefault());
     now.setTime(d);
     now.add(11, hours);
     now.add(12, min);
     now.add(13, sec);
     return now.getTime();
   }

   public static String getZhCNDateTime(String dateStr)
   {
     Date d = getDate(dateStr);
     return dateToDateString(d, "yyyy年MM月dd日HH时mm分ss秒");
   }

   public static String getZhCNDate(String dateStr)
   {
     Date d = getDate(dateStr, "yyyy-MM-dd");
     return dateToDateString(d, "yyyy年MM月dd日");
   }

   public static String getDateStr(String dateStr, String fmtFrom, String fmtTo)
   {
     Date d = getDate(dateStr, fmtFrom);
     return dateToDateString(d, fmtTo);
   }

   public static long compareDateStr(String time1, String time2)
   {
     Date d1 = getDate(time1);
     Date d2 = getDate(time2);
     return d2.getTime() - d1.getTime();
   }

   public static long getMicroSec(BigDecimal hours)
   {
     BigDecimal bd = hours.multiply(new BigDecimal(3600000));
     return bd.longValue();
   }

   public static int getMin(Date d)
   {
     Calendar now = Calendar.getInstance(TimeZone.getDefault());
     now.setTime(d);
     return now.get(12);
   }

   public static int getHour(Date d)
   {
     Calendar now = Calendar.getInstance(TimeZone.getDefault());
     now.setTime(d);
     return now.get(11);
   }

   public static int getSecond(Date d)
   {
     Calendar now = Calendar.getInstance(TimeZone.getDefault());
     now.setTime(d);
     return now.get(13);
   }

   public static int getDay(Date d)
   {
     Calendar now = Calendar.getInstance(TimeZone.getDefault());
     now.setTime(d);
     return now.get(5);
   }

   public static int getMonth(Date d)
   {
     Calendar now = Calendar.getInstance(TimeZone.getDefault());
     now.setTime(d);
     return now.get(2) + 1;
   }

   public static int getYear(Date d)
   {
     Calendar now = Calendar.getInstance(TimeZone.getDefault());
     now.setTime(d);
     return now.get(1);
   }

   public static String getYearMonthOfLastMon(Date d)
   {
     Date newdate = getInternalDateByMon(d, -1);
     String year = String.valueOf(getYear(newdate));
     String month = String.valueOf(getMonth(newdate));
     return year + month;
   }

   public static String getCurYearMonth()
   {
     Calendar now = Calendar.getInstance(TimeZone.getDefault());
     String DATE_FORMAT = "yyyyMM";
     SimpleDateFormat sdf = new SimpleDateFormat(
       DATE_FORMAT);
     sdf.setTimeZone(TimeZone.getDefault());
     return sdf.format(now.getTime());
   }

   public static Date getNextMonth(String year, String month) {
     String datestr = year + "-" + month + "-01";
     Date date = getDate(datestr, "yyyy-MM-dd");
     return getInternalDateByMon(date, 1);
   }

   public static Date getLastMonth(String year, String month) {
     String datestr = year + "-" + month + "-01";
     Date date = getDate(datestr, "yyyy-MM-dd");
     return getInternalDateByMon(date, -1);
   }

   public static String getSingleNumDate(Date d)
   {
     return dateToDateString(d, "yyyy-MM-dd");
   }

   public static String getHalfYearBeforeStr(Date d)
   {
     return dateToDateString(getInternalDateByMon(d, -6), "yyyy-MM-dd");
   }

   public static String getInternalDateByLastDay(Date d, int days)
   {
     return dateToDateString(getInternalDateByDay(d, days), "yyyy-MM-dd");
   }

   public static String addDate(int field, int amount)
   {
     int temp = 0;
     if (field == 1) {
       temp = 1;
     }
     if (field == 2) {
       temp = 2;
     }
     if (field == 3) {
       temp = 5;
     }

     String Time = "";
     try {
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
       Calendar cal = Calendar.getInstance(TimeZone.getDefault());
       cal.add(temp, amount);
       return sdf.format(cal.getTime());
     }
     catch (Exception e) {
       e.printStackTrace();
     }return null;
   }

   public static int getCurentMonthDay()
   {
     Date date = Calendar.getInstance().getTime();
     return getMonthDay(date);
   }

   public static int getMonthDay(Date date)
   {
     Calendar c = Calendar.getInstance();
     c.setTime(date);
     return c.getActualMaximum(5);
   }

   public static int getMonthDay(String date)
   {
     Date strDate = getDate(date, "yyyy-MM-dd");
     return getMonthDay(strDate);
   }

   public static String getStringDate(Calendar cal)
   {
     SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
     return format.format(cal.getTime());
   }
 }
