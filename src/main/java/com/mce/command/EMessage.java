 package com.mce.command;
 
 import java.io.Serializable;
 
 public class EMessage
   implements Serializable
 {
   private static final long serialVersionUID = 1L;
   private int errorCode;
   private String message;
 
   EMessage()
   {
   }
 
   public EMessage(int code, String message)
   {
     this.errorCode = code;
     this.message = message;
   }
 
   public int getErrorCode()
   {
     return this.errorCode;
   }
   public String getMessage() {
     return this.message;
   }
 }

