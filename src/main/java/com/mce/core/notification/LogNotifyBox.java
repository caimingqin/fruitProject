package com.mce.core.notification;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LogNotifyBox
  implements NotifyBox
{
  private Log logger = LogFactory.getLog(getClass().getName());

  private static final LogNotifyBox logNotifyBox = new LogNotifyBox();

  public static LogNotifyBox get()
  {
    return logNotifyBox;
  }

  public boolean add(Notification n)
  {
    this.logger.warn("Notification[" + n.toString() + "]");
    return true;
  }
}




