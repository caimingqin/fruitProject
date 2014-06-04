/*    */ package com.mce.command;
/*    */ 
/*    */ public class CommandHandleException extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public static final int ERROR = 500;
/*    */   private int errorCode;
/*    */ 
/*    */   public CommandHandleException(int errorCode)
/*    */   {
/* 13 */     this(errorCode, "");
/*    */   }
/*    */ 
/*    */   public CommandHandleException(int errorCode, String message) {
/* 17 */     super(message);
/* 18 */     this.errorCode = errorCode;
/*    */   }
/*    */ 
/*    */   public CommandHandleException(int errorCode, Throwable t) {
/* 22 */     super(t);
/* 23 */     this.errorCode = errorCode;
/*    */   }
/*    */ 
/*    */   public int getErrorCode() {
/* 27 */     return this.errorCode;
/*    */   }
/*    */ 
/*    */   public void setErrorCode(int errorCode) {
/* 31 */     this.errorCode = errorCode;
/*    */   }
/*    */ }

/* Location:           E:\Zip2\mresouce\wmcore-0.6.9.jar
 * Qualified Name:     com.mce.command.CommandHandleException
 * JD-Core Version:    0.6.2
 */