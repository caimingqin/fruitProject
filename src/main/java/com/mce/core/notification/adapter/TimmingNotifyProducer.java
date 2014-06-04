/*    */ package com.mce.core.notification.adapter;
/*    */ 
/*    */ import com.mce.core.inject.StopBean;
/*    */ import com.mce.core.notification.NotifyBox;
/*    */ import com.mce.core.notification.NotifyProducer;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.concurrent.Executors;
/*    */ import java.util.concurrent.atomic.AtomicBoolean;
/*    */ import org.apache.commons.lang.Validate;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ 
/*    */ public class TimmingNotifyProducer
/*    */   implements NotifyProducer, StopBean
/*    */ {
/*    */   private HeartbeatNotifier hNotifier;
/* 19 */   private Long waitTime = null;
/* 20 */   private AtomicBoolean started = new AtomicBoolean(false);
/* 21 */   private Executor executors = Executors.newFixedThreadPool(1);
/* 22 */   private Log logger = LogFactory.getLog(getClass().getName());
/*    */ 
/*    */   public TimmingNotifyProducer() {
/* 25 */     this(HeartbeatNotifier.waitTime);
/*    */   }
/*    */ 
/*    */   public TimmingNotifyProducer(Long waitTime) {
/* 29 */     Validate.notNull(waitTime, "Not found waitTime");
/* 30 */     this.waitTime = waitTime;
/*    */   }
/*    */ 
/*    */   public void startProducer(NotifyBox nb)
/*    */   {
/* 36 */     if (this.started.compareAndSet(false, true)) {
/* 37 */       this.hNotifier = new HeartbeatNotifier(nb, this.waitTime.longValue());
/* 38 */       this.executors.execute(this.hNotifier);
/*    */     }
/*    */   }
/*    */ 
/*    */   public void stop()
/*    */   {
/* 45 */     this.logger.warn("Stop Heartbeat Timmer success");
/* 46 */     this.hNotifier.stopSend();
/*    */   }
/*    */ }

/* Location:           E:\Zip2\mresouce\wmcore-0.6.9.jar
 * Qualified Name:     com.mce.core.notification.adapter.TimmingNotifyProducer
 * JD-Core Version:    0.6.2
 */