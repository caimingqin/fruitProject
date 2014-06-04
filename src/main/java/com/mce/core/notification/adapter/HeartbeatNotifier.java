/*    */ package com.mce.core.notification.adapter;
/*    */ 
/*    */ import com.mce.core.notification.Notification;
/*    */ import com.mce.core.notification.NotifyBox;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ 
/*    */ class HeartbeatNotifier
/*    */   implements Runnable
/*    */ {
/* 11 */   public static final Long MIN_TIME = new Long(60000L);
/* 12 */   public static final Long waitTime = new Long(120000L);
/*    */ 
/* 15 */   private boolean start = true;
/* 16 */   private Log logger = LogFactory.getLog(getClass());
/*    */   private NotifyBox nBox;
/* 18 */   private long cWatingTime = waitTime.longValue();
/*    */ 
/*    */   HeartbeatNotifier(NotifyBox nf)
/*    */   {
/* 22 */     this(nf, waitTime.longValue());
/*    */   }
/*    */ 
/*    */   HeartbeatNotifier(NotifyBox nf, long waitTime) {
/* 26 */     this.nBox = nf;
/* 27 */     this.cWatingTime = waitTime;
/*    */   }
/*    */ 
/*    */   public void run()
/*    */   {
/* 32 */     synchronized (this) {
/* 33 */       while (this.start)
/*    */         try {
/* 35 */           wait(this.cWatingTime);
/* 36 */           Notification nf = new Notification("heartbeatNotify");
/* 37 */           this.nBox.add(nf);
/*    */         } catch (InterruptedException e) {
/* 39 */           this.logger.trace(e);
/*    */         }
/*    */     }
/*    */   }
/*    */ 
/*    */   void stopSend()
/*    */   {
/* 47 */     this.start = false;
/*    */   }
/*    */ }

/* Location:           E:\Zip2\mresouce\wmcore-0.6.9.jar
 * Qualified Name:     com.mce.core.notification.adapter.HeartbeatNotifier
 * JD-Core Version:    0.6.2
 */