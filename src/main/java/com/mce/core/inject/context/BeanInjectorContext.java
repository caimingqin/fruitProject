package com.mce.core.inject.context;

import com.mce.core.inject.BeanInjector;

public abstract interface BeanInjectorContext extends BeanInjector
{
  public abstract void start(String paramString);

  public abstract void stop(String paramString);
}

