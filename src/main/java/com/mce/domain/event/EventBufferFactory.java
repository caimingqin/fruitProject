package com.mce.domain.event;

public abstract interface EventBufferFactory
{
  public abstract EventBuffer createEventBuffer();

  public abstract boolean clearBuffer(EventBuffer paramEventBuffer);
}

