 package com.mce.core;
 
 import java.io.Serializable;
 
 public class ResponsibleMessage
   implements Serializable
 {
   private static final long serialVersionUID = 1L;
   private int statusCode;
   private String message;
   private Object contents;
   private String sessionId;
   private ContentsType contentsType = ContentsType.JSON;
 
   public int getStatusCode() {
     return this.statusCode;
   }
 
   public void setStatusCode(int statusCode) {
     this.statusCode = statusCode;
   }
 
   public String getMessage() {
     return this.message;
   }
 
   public void setMessage(String message) {
     this.message = message;
   }
 
   public Object getContents() {
     return this.contents;
   }
 
   public void setContents(Object contents) {
     this.contents = contents;
   }
 
   public String getSessionId() {
     return this.sessionId;
   }
 
   public void setSessionId(String sessionId) {
     this.sessionId = sessionId;
   }
 
   public ContentsType getContentsType() {
     return this.contentsType;
   }
 
   public void setContentsType(ContentsType contentsType) {
     this.contentsType = contentsType;
   }
 }

