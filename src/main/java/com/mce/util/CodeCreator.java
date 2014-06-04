/*     */ package com.mce.util;
/*     */ 
/*     */ import java.text.DateFormat;
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.NumberFormat;
/*     */ import java.text.ParseException;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ 
/*     */ public final class CodeCreator
/*     */ {
/*  15 */   private NumberFormat formatter = new DecimalFormat("00000000");
/*  16 */   private static final Map<String, CodeCreator> ccMap = new HashMap();
/*     */   public static final String DEFAULT = "Default";
/*  18 */   private Type type = Type.NORMAL;
/*  19 */   private CodeValue codeValue = null;
/*     */   public static final String DEFAULT_ZERRO = "00000000";
/*     */ 
/*     */   public static CodeCreator get()
/*     */   {
/*  35 */     return (CodeCreator)ccMap.get("Default");
/*     */   }
/*     */ 
/*     */   public static boolean regist(String key, String zeros, Type type) {
/*  39 */     NumberFormat formatter = new DecimalFormat(zeros);
/*  40 */     CodeCreator cc = new CodeCreator();
/*  41 */     cc.formatter = formatter;
/*  42 */     cc.type = type;
/*  43 */     ccMap.put(key, cc);
/*  44 */     return ccMap.containsKey(key);
/*     */   }
/*     */ 
/*     */   public static boolean regist(String key, Type type) {
/*  48 */     NumberFormat formatter = new DecimalFormat("00000000");
/*  49 */     return regist(key, formatter, type);
/*     */   }
/*     */ 
/*     */   public static boolean regist(String key) {
/*  53 */     return regist(key, Type.NORMAL);
/*     */   }
/*     */ 
/*     */   public static boolean regist(String key, NumberFormat fd, Type type) {
/*  57 */     CodeCreator cc = new CodeCreator();
/*  58 */     cc.formatter = fd;
/*  59 */     cc.type = type;
/*  60 */     ccMap.put(key, cc);
/*  61 */     return ccMap.containsKey(key);
/*     */   }
/*     */ 
/*     */   public static CodeCreator get(String key) {
/*  65 */     return (CodeCreator)ccMap.get(key);
/*     */   }
/*     */ 
/*     */   public void add(Date lastDate, String lastCode, String startChar) {
/*  69 */     this.codeValue = createCodeValue(lastDate, lastCode, startChar);
/*  70 */     createCode();
/*     */   }
/*     */ 
/*     */   public void add(Date lastDate) {
/*  74 */     String lastCode = format(new Long(1L));
/*  75 */     add(lastDate, lastCode, null);
/*     */   }
/*     */ 
/*     */   public void init() {
/*  79 */     String lastCode = format(new Long(1L));
/*  80 */     add(new Date(), lastCode, null);
/*     */   }
/*     */ 
/*     */   public void add(Date lastDate, String lastCode)
/*     */   {
/*  85 */     add(lastDate, lastCode, null);
/*     */   }
/*     */ 
/*     */   public CodeValue createCodeValue(Date lastDate, String lastCode, String strChar) {
/*  89 */     Date date = new Date();
/*  90 */     if ((date.after(lastDate)) || (lastCode == null) || ("".equalsIgnoreCase(lastCode)) || (lastCode.length() < 1))
/*     */     {
/*  94 */       return new CodeValue(date, new Long(0L), strChar);
/*     */     }
/*  96 */     Long id = parseCode(lastCode);
/*  97 */     id = Long.valueOf(id.longValue() + 1L);
/*  98 */     return new CodeValue(lastDate, id, strChar);
/*     */   }
/*     */ 
/*     */   public String createCode() {
/* 102 */     Long lastId = Long.valueOf(this.codeValue.value.getAndAdd(1L));
/* 103 */     String sLastId = format(lastId);
/* 104 */     String s = DateUtils.yyyymmdd.format(this.codeValue.currentDate);
/* 105 */     StringBuffer sb = new StringBuffer();
/* 106 */     if (this.codeValue.startChar != null) {
/* 107 */       sb.append(this.codeValue.startChar);
/*     */     }
/* 109 */     if (Type.NORMAL.equals(this.type)) {
/* 110 */       sb.append(s);
/*     */     }
/* 112 */     sb.append(sLastId);
/* 113 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public String format(Long id)
/*     */   {
/* 119 */     return this.formatter.format(id);
/*     */   }
/*     */ 
/*     */   private Long parseCode(String code) {
/* 123 */     int statrIndex = code.length() - this.formatter.getMinimumIntegerDigits();
/* 124 */     String sCode = code.substring(statrIndex, code.length());
/*     */     try {
/* 126 */       Number res = this.formatter.parse(sCode);
/* 127 */       return Long.valueOf(res.longValue());
/*     */     } catch (ParseException e) {
/* 129 */       e.printStackTrace();
/*     */     }
/* 131 */     throw new IllegalArgumentException("convert code to id error");
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  24 */     ccMap.put("Default", new CodeCreator());
/*     */   }
/*     */ 
/*     */   public class CodeValue
/*     */   {
/*     */     private Date currentDate;
/* 138 */     private AtomicLong value = new AtomicLong(0L);
/*     */     private String startChar;
/*     */ 
/*     */     public CodeValue(Date lastDate, Long id, String startChar)
/*     */     {
/* 142 */       this.currentDate = lastDate;
/* 143 */       this.value.set(id.longValue());
/* 144 */       this.startChar = startChar;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum Type
/*     */   {
/*  28 */     NORMAL, NO_DATE;
/*     */   }
/*     */ }

/* Location:           E:\Zip2\mresouce\wmcore-0.6.9.jar
 * Qualified Name:     com.mce.util.CodeCreator
 * JD-Core Version:    0.6.2
 */