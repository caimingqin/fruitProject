 package com.mce.core.notification;
 
 import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
 
 public class SimpleNotifier
   implements Notifier
 {
   private List<NotificationListener> listeners;
   private Log logger;
   private String sessionId;
   private NotificationStatus status;
   private SimpleNotifyBox snotifyBox;
 
   public SimpleNotifier()
   {
     this.listeners = new ArrayList<NotificationListener>();
     this.logger = LogFactory.getLog(getClass().getName());
 
     this.status = NotificationStatus.PREPARE;
     this.snotifyBox = new SimpleNotifyBox();
   }
 
   public void shutdown(String sId)
   {
     this.logger.warn("Stop now");
 
     synchronized (this) {
       if (NotificationStatus.START.equals(this.status)) {
         if (!this.sessionId.equalsIgnoreCase(sId)) {
           throw new IllegalArgumentException("Can not stop NotificationObserver[" + sId + "]");
         }
         this.snotifyBox.started = false;
 
         this.status = NotificationStatus.STOP;
         this.logger.warn("Stop notificationObserver successfully");
       }
     }
   }
 
   private void addListeners(List<NotificationListener> nll) {
     for (NotificationListener nl : nll) {
       NotifyFilter nf = DefaultNotifyFilter.get().getFilter(nl);
       this.listeners.add(new FilterNitifierListener(nl, nf));
     }
   }
 
   public NotifyBox start(String ntfSessionId, List<NotificationListener> nll)
   {
     synchronized (this) {
       if (NotificationStatus.PREPARE.equals(this.status)) {
         this.sessionId = ntfSessionId;
         this.status = NotificationStatus.START;
 
         addListeners(nll);
 
         Executors.newFixedThreadPool(1).execute(this.snotifyBox);
 
         return this.snotifyBox;
       }
 
       throw new IllegalStateException("Call start method once please");
     }
   }
 
   private void handle(Notification ntf)
   {
     for (NotificationListener nt : this.listeners)
       nt.handle(ntf);
   }
 
   public boolean isRunning()
   {
     return NotificationStatus.START.equals(this.status);
   }
 
   private class SimpleNotifyBox extends AbstractNotifyBox
     implements Runnable
   {
     private boolean started = true;
 
     private SimpleNotifyBox() {
     }
     protected boolean isStarted() { return this.started; }
 
 
     public void run()
     {
       while (this.started) {
         Notification ntf = get();
         if (ntf != null)
           SimpleNotifier.this.handle(ntf);
       }
     }
   }
 }

