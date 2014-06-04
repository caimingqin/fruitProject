package com.mce.core.notification;

public final class NotifierConfig
{
  public static final String DEFAULT = "dnotifier";
  public static final String SIMPLE = "snotifier";
  private String type;

  NotifierConfig()
  {
  }

  public NotifierConfig(String type)
  {
    this.type = type;
  }

  public Notifier build() {
    if ("dnotifier".equalsIgnoreCase(this.type)) {
      return new DefaultNotifier();
    }

    return new SimpleNotifier();
  }

}



