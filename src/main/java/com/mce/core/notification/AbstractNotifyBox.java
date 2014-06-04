 package com.mce.core.notification;
 
 import java.util.Queue;
 import java.util.concurrent.ArrayBlockingQueue;
 import java.util.concurrent.BlockingQueue;

 public abstract class AbstractNotifyBox
   implements NotifyBox
 {
   private Queue<Notification> notfs = null;
 
   public AbstractNotifyBox()
   {
     this(100000);
   }
   public AbstractNotifyBox(int bufferSize) {
     this.notfs = new ArrayBlockingQueue<Notification>(bufferSize);
   }
 
   public AbstractNotifyBox(BlockingQueue<Notification> nQueue) {
     this.notfs = nQueue;
   }
 
   public final boolean add(Notification nf) {
     if (isStarted()) {
       return this.notfs.add(nf);
     }
     return false;
   }
 
   protected abstract boolean isStarted();
 
   public int size()
   {
     return this.notfs.size();
   }
 
   Notification get() {
     return (Notification)this.notfs.poll();
   }
 }




