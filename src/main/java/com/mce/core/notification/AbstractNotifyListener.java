 package com.mce.core.notification;
 
 import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractNotifyListener
  implements NotificationListener
{
  private AtomicBoolean started = new AtomicBoolean(false);
  private AtomicBoolean prepared = new AtomicBoolean(false);
  private NotificationStatus nStatus;

  public final void handle(Notification nf)
  {
    if (Notification.isPreparedNotify(nf)) {
      if (this.prepared.compareAndSet(false, true)) {
        this.nStatus = NotificationStatus.PREPARE;
        prepared();
      }
    }
    else if (Notification.isStartupNotify(nf)) {
      if ((this.nStatus == null) || (!NotificationStatus.PREPARE.equals(this.nStatus))) {
        throw new IllegalStateException("The NotificationListener is not Prepared");
      }
      if (this.started.compareAndSet(false, true)) {
        this.nStatus = NotificationStatus.START;
        start();
      }
    }
    else if (Notification.isStopNotify(nf)) {
      if (this.started.get()) {
        this.nStatus = NotificationStatus.STOP;
        stop();
      }
    }
    else {
      if (!NotificationStatus.START.equals(this.nStatus)) {
        throw new IllegalStateException("Please class start method first[" + this.nStatus + "]");
      }
      notify(nf);
    }
  }

  protected abstract void notify(Notification paramNotification);

  protected void prepared()
  {
  }

  protected void start()
  {
  }

  protected void stop()
  {
  }
}
