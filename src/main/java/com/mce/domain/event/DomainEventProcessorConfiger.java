package com.mce.domain.event;

public class DomainEventProcessorConfiger
{
  private int coreSize;
  private DomainEventHandler[] handlers;
  private ExceptionHandler eHandler;
  private long waitTime;

  DomainEventProcessorConfiger(int size, DomainEventHandler[] handles, ExceptionHandler eh, long waitTime)
  {
    this.coreSize = size;
    this.handlers = handles;
    this.eHandler = eh;
    this.waitTime = waitTime;
  }

  DomainEventProcessor[] getDomainEventProcessor(EventBuffer des) {
    DomainEventProcessor[] dep = new DomainEventProcessor[this.coreSize];
    for (int i = 0; i < this.coreSize; i++) {
      dep[i] = new DomainEventProcessor(this.handlers, des, this.eHandler, this.waitTime);
    }
    return dep;
  }
}

