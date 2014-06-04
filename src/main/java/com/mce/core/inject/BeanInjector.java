package com.mce.core.inject;

public abstract interface BeanInjector
{
  public abstract <T> T getBean(Class<T> paramClass);

  public abstract Object getBean(String paramString);

  public abstract String[] getBeanNamesForType(Class<?> paramClass);

  public abstract <T> T getBeanOnSafe(Class<T> paramClass);

  public abstract void inject(Object paramObject);
}

