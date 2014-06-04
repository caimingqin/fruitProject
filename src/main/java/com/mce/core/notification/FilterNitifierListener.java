package com.mce.core.notification;

import org.apache.commons.lang.Validate;

class FilterNitifierListener
  implements NotificationListener
{
  private NotifyFilter nFilter;
  private NotificationListener nListener;

  FilterNitifierListener(NotificationListener nl, NotifyFilter nf)
  {
    Validate.noNullElements(new Object[] { nl, nf });
    this.nFilter = nf;
    this.nListener = nl;
  }

  public void handle(Notification nf)
  {
    if (this.nFilter.filter(nf))
      this.nListener.handle(nf);
  }
}




