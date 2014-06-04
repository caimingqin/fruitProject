package com.mce.core.resource;

import java.io.InputStream;

public abstract interface ResourceIterator
{
  public abstract InputStream next();

  public abstract void close();
}
