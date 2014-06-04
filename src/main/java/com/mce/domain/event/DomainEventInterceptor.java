package com.mce.domain.event;

public abstract interface DomainEventInterceptor
{
  public abstract void beforeHandle(DomainEventContext paramDomainEventContext, DomainEvent paramDomainEvent);

  public abstract void afterHandle(DomainEventContext paramDomainEventContext, DomainEvent paramDomainEvent);
}

