/*     */ package com.mce.util;
/*     */ 
/*     */ import java.sql.Timestamp;
/*     */ import java.text.DateFormat;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
/*     */ 
/*     */ public final class DateUtils
/*     */ {
/*  20 */   public static final DateFormat MONT_YEAR = new SimpleDateFormat("yyMM");
/*     */ 
/*  22 */   public static final SimpleDateFormat YearFormat = new SimpleDateFormat("yyyy");
/*     */ 
/*  24 */   public static final DateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
/*     */ 
/*  26 */   public static final DateFormat YYYYMMDDHH = new SimpleDateFormat("yyyyMMddHH");
/*     */ 
/*  28 */   public static final DateFormat yyyymm = new SimpleDateFormat("yyyyMM");
/*     */ 
/*  30 */   public static final String[] ALL_MONTH_STRING = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };
/*     */ 
/*     */   public static final String mappingYYMM(Date date) {
/*  33 */     return MONT_YEAR.format(date);
/*     */   }
/*     */ 
/*     */   public static final Date getMonday() {
/*  37 */     Calendar c = Calendar.getInstance();
/*  38 */     c.setFirstDayOfWeek(2);
/*  39 */     int day = c.get(7);
/*  40 */     c.add(5, c.getFirstDayOfWeek() - day);
/*  41 */     return c.getTime();
/*     */   }
/*     */ 
/*     */   public static Timestamp getSqlTimestamp(Date date) {
/*  45 */     Timestamp timestamp = new Timestamp(date.getTime());
/*  46 */     return timestamp;
/*     */   }
/*     */ 
/*     */   public static final Date beforeYear(Date date) {
/*  50 */     Calendar calendar = GregorianCalendar.getInstance();
/*  51 */     calendar.setTime(date);
/*  52 */     String years = YearFormat.format(date);
/*  53 */     int intYear = Integer.parseInt(years);
/*  54 */     intYear -= 1;
/*  55 */     calendar.set(1, intYear);
/*  56 */     return calendar.getTime();
/*     */   }
/*     */ 
/*     */   public static final String[] getAllMonthForYearString(String year) {
/*  60 */     String[] all = new String[12];
/*  61 */     for (int i = 0; i < all.length; i++) {
/*  62 */       all[i] = (year + ALL_MONTH_STRING[i]);
/*     */     }
/*  64 */     return all;
/*     */   }
/*     */ 
/*     */   public static final boolean isTheSameDay(Date d1, Date d2) {
/*  68 */     Calendar c1 = Calendar.getInstance();
/*  69 */     Calendar c2 = Calendar.getInstance();
/*  70 */     c1.setTime(d1);
/*  71 */     c2.setTime(d2);
/*  72 */     return (c1.get(1) == c2.get(1)) && (c1.get(2) == c2.get(2)) && (c1.get(5) == c2.get(5));
/*     */   }
/*     */ 
/*     */   public static final Date parseYYYYMMDD(String string, Date defaultDate)
/*     */   {
/*  78 */     Date returnDate = null;
/*     */     try {
/*  80 */       returnDate = yyyymmdd.parse(string);
/*     */     } catch (ParseException e) {
/*  82 */       returnDate = defaultDate;
/*     */     }
/*  84 */     return returnDate;
/*     */   }
/*     */ 
/*     */   public static final Date parseYYYYMMDDHH(String date, Date defaultDate) {
/*  88 */     Date returnDate = null;
/*     */     try {
/*  90 */       returnDate = YYYYMMDDHH.parse(date);
/*     */     } catch (ParseException e) {
/*  92 */       returnDate = defaultDate;
/*     */     }
/*  94 */     return returnDate;
/*     */   }
/*     */ 
/*     */   public static Timestamp getCurrentSQLDateTime() {
/*  98 */     Date date = new Date();
/*  99 */     Timestamp timestamp = new Timestamp(date.getTime());
/* 100 */     return timestamp;
/*     */   }
/*     */ 
/*     */   public static Date getDate(Date date, int s) {
/* 104 */     Calendar c = Calendar.getInstance();
/* 105 */     c.setTime(date);
/* 106 */     c.add(5, s);
/* 107 */     return c.getTime();
/*     */   }
/*     */ 
/*     */   public static boolean between(Date sDate, Date eDate, Date target) {
/* 111 */     return sDate.compareTo(target) * target.compareTo(eDate) > 0;
/*     */   }
/*     */ }

/* Location:           E:\Zip2\mresouce\wmcore-0.6.9.jar
 * Qualified Name:     com.mce.util.DateUtils
 * JD-Core Version:    0.6.2
 */