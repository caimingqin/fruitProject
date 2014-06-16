package com.mce.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public final class DateUtils
{
  public static final DateFormat MONT_YEAR = new SimpleDateFormat("yyMM");

  public static final SimpleDateFormat YearFormat = new SimpleDateFormat("yyyy");

  public static final DateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");

  public static final DateFormat YYYYMMDDHH = new SimpleDateFormat("yyyyMMddHH");

  public static final DateFormat yyyymm = new SimpleDateFormat("yyyyMM");

  public static final String[] ALL_MONTH_STRING = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };

  public static final String mappingYYMM(Date date) {
    return MONT_YEAR.format(date);
  }

  public static final Date getMonday() {
    Calendar c = Calendar.getInstance();
    c.setFirstDayOfWeek(2);
    int day = c.get(7);
    c.add(5, c.getFirstDayOfWeek() - day);
    return c.getTime();
  }

  public static Timestamp getSqlTimestamp(Date date) {
    Timestamp timestamp = new Timestamp(date.getTime());
    return timestamp;
  }

  public static final Date beforeYear(Date date) {
    Calendar calendar = GregorianCalendar.getInstance();
    calendar.setTime(date);
    String years = YearFormat.format(date);
    int intYear = Integer.parseInt(years);
    intYear -= 1;
    calendar.set(1, intYear);
    return calendar.getTime();
  }

  public static final String[] getAllMonthForYearString(String year) {
    String[] all = new String[12];
    for (int i = 0; i < all.length; i++) {
      all[i] = (year + ALL_MONTH_STRING[i]);
    }
    return all;
  }

  public static final boolean isTheSameDay(Date d1, Date d2) {
    Calendar c1 = Calendar.getInstance();
    Calendar c2 = Calendar.getInstance();
    c1.setTime(d1);
    c2.setTime(d2);
    return (c1.get(1) == c2.get(1)) && (c1.get(2) == c2.get(2)) && (c1.get(5) == c2.get(5));
  }

  public static final Date parseYYYYMMDD(String string, Date defaultDate)
  {
    Date returnDate = null;
    try {
      returnDate = yyyymmdd.parse(string);
    } catch (ParseException e) {
      returnDate = defaultDate;
    }
    return returnDate;
  }

  public static final Date parseYYYYMMDDHH(String date, Date defaultDate) {
    Date returnDate = null;
    try {
      returnDate = YYYYMMDDHH.parse(date);
    } catch (ParseException e) {
      returnDate = defaultDate;
    }
    return returnDate;
  }

  public static Timestamp getCurrentSQLDateTime() {
    Date date = new Date();
    Timestamp timestamp = new Timestamp(date.getTime());
    return timestamp;
  }

  public static Date getDate(Date date, int s) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.add(5, s);
    return c.getTime();
  }

  public static boolean between(Date sDate, Date eDate, Date target) {
    return sDate.compareTo(target) * target.compareTo(eDate) > 0;
  }
  
  
}

