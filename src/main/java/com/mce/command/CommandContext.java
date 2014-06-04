/*    */ package com.mce.command;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public final class CommandContext
/*    */ {
/*  8 */   private Map<String, Object> contextParams = new HashMap();
/*    */ 
/*    */   public Map<String, Object> getParameters()
/*    */   {
/* 12 */     return this.contextParams;
/*    */   }
/*    */ 
/*    */   public Object getParameter(String key) {
/* 16 */     return this.contextParams.get(key);
/*    */   }
/*    */ 
/*    */   public void put(String name, Object value) {
/* 20 */     this.contextParams.put(name, value);
/*    */   }
/*    */ 
/*    */   public void clear() {
/* 24 */     this.contextParams.clear();
/*    */   }
/*    */ }

/* Location:           E:\Zip2\mresouce\wmcore-0.6.9.jar
 * Qualified Name:     com.mce.command.CommandContext
 * JD-Core Version:    0.6.2
 */