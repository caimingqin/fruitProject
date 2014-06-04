package com.mce.domain.event;

abstract interface ExceptionHandler
{
  public abstract void handleException(DomainEvent paramDomainEvent);
}

