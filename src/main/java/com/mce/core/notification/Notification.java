 package com.mce.core.notification;
 
 import java.io.Serializable;
 
 public class Notification
   implements Serializable
 {
   private static final long serialVersionUID = 1L;
   public static final String HEARTBEAT_TNF = "heartbeatNotify";
   private String name;
   private Object body;
   private String sessionId;
 
   public static boolean isHeartbeatNotify(Notification fn)
   {
     if ((fn != null) && ("heartbeatNotify".equalsIgnoreCase(fn.getName()))) {
       return true;
     }
     return false;
   }
   Notification() {
   }
   public Notification(String nName) {
     this(nName, null);
   }
 
   public Notification(String name, Object body) {
     this.name = name;
     this.body = body;
   }
 
   public String getName() {
     return this.name;
   }
 
   public Object getBody() {
     return this.body;
   }
   public String getSessionId() {
     return this.sessionId;
   }
 
   public static boolean isStartupNotify(Notification nf)
   {
     return ("observerMessage".equalsIgnoreCase(nf.getName())) && (NotificationStatus.START.equals(nf.getBody()));
   }
 
   public static boolean isStopNotify(Notification nf)
   {
     return ("observerMessage".equalsIgnoreCase(nf.getName())) && (NotificationStatus.STOP.equals(nf.getBody()));
   }
 
   public static boolean isPreparedNotify(Notification nf)
   {
     return ("observerMessage".equalsIgnoreCase(nf.getName())) && (NotificationStatus.PREPARE.equals(nf.getBody()));
   }
 }

