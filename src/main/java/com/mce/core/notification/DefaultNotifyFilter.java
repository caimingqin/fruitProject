package com.mce.core.notification;

class DefaultNotifyFilter
{
  private LifecycleNotifyFilter lnf = new LifecycleNotifyFilter();
  private AllPassNotifyFilter apnf = new AllPassNotifyFilter();

  private static final DefaultNotifyFilter instance = new DefaultNotifyFilter();

  public static DefaultNotifyFilter get()
  {
    return instance;
  }

  public NotifyFilter lifecycleFilter() {
    return this.lnf;
  }

  public NotifyFilter allPassFilter() {
    return this.apnf;
  }

  public NotifyFilter getFilter(NotificationListener nl) {
    if (NotifyProducer.class.isAssignableFrom(nl.getClass())) {
      return lifecycleFilter();
    }
    return allPassFilter();
  }

  private class AllPassNotifyFilter
    implements NotifyFilter
  {
    private AllPassNotifyFilter()
    {
    }

    public boolean filter(Notification nf)
    {
      return true;
    }
  }

  private class LifecycleNotifyFilter
    implements NotifyFilter
  {
    private LifecycleNotifyFilter()
    {
    }

    public boolean filter(Notification nf)
    {
      return (nf != null) && ((Notification.isPreparedNotify(nf)) || (Notification.isStartupNotify(nf)) || (Notification.isStopNotify(nf)));
    }
  }
}

