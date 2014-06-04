package com.mce.core.inject;

import java.util.Map;

public abstract interface ConfigurableBeanInjector extends BeanInjector
{
  public abstract void registBean(String paramString, Object paramObject);

  public abstract void destroyAll();

  public abstract void registClass(String paramString, Class<?> paramClass);

  public abstract void registClass(String paramString, Class<?> paramClass, Map<String, Object> paramMap);

  public abstract String[] getBeanNames();
}

