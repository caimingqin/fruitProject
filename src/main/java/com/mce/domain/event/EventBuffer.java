package com.mce.domain.event;

public abstract interface EventBuffer
{
  public static final int DEFAULT_BUFFER_SIZE = 100000;

  public abstract DomainEvent get();

  public abstract boolean push(DomainEvent paramDomainEvent);

  public abstract int size();
}

