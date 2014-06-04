package com.mce.core.resource;

public abstract interface Filter
{
  public abstract boolean accepts(String paramString);
}

