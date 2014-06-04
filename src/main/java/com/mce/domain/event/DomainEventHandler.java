package com.mce.domain.event;

public abstract interface DomainEventHandler
{
  public abstract void handle(DomainEvent paramDomainEvent);

  public abstract boolean isSupport(DomainEvent paramDomainEvent);

  public abstract DomainEventInterceptor[] getInterceptors();
}

