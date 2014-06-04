package com.mce.core.notification;

import java.util.List;

public abstract interface Notifier
{
  public static final String OBSERVER_MESSAGE = "observerMessage";

  public abstract void shutdown(String paramString);

  public abstract NotifyBox start(String paramString, List<NotificationListener> paramList);

  public abstract boolean isRunning();
}

