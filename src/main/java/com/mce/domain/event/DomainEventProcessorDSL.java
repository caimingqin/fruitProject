package com.mce.domain.event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class DomainEventProcessorDSL
{
  private int processorSize;
  private final AtomicBoolean started = new AtomicBoolean(false);

  private List<DomainEventProcessor> proArray = new ArrayList<DomainEventProcessor>();
  private EventBuffer eBuffer = null;
  private Log logger = LogFactory.getLog(getClass().getName());
  public static final int DEFAULT_PROCESSOR_SIZE = 2;
  private EventBufferFactory ebFactory;

  public DomainEventProcessorDSL(int processorSize2, EventBufferFactory eBuffer)
  {
    Validate.notNull(eBuffer, "EventBuffer is null");
    this.processorSize = processorSize2;
    this.ebFactory = eBuffer;
  }

  public DomainEventProcessorDSL(EventBufferFactory eBuffer2)
  {
    this(2, eBuffer2);
  }

  public DomainEventProcessorConfiger createDomainEventProcessor(DomainEventHandler[] handles)
  {
    return createDomainEventProcessor(handles, 0L, new LoggerExceptionHandler());
  }

  public DomainEventProcessorConfiger createDomainEventProcessor(DomainEventHandler[] handles, long waitTime)
  {
    return createDomainEventProcessor(handles, waitTime, new LoggerExceptionHandler());
  }

  public DomainEventProcessorConfiger createDomainEventProcessor(DomainEventHandler[] handles, ExceptionHandler eh)
  {
    return createDomainEventProcessor(handles, 0L, eh);
  }

  public DomainEventProcessorConfiger createDomainEventProcessor(DomainEventHandler[] handles, long waitTime, ExceptionHandler eh)
  {
    return new DomainEventProcessorConfiger(this.processorSize, handles, eh, waitTime);
  }

  public EventBuffer start(DomainEventProcessorConfiger depc)
  {
    checkOnlyStartedOnce();
    this.eBuffer = this.ebFactory.createEventBuffer();
    DomainEventProcessor[] processors = depc.getDomainEventProcessor(this.eBuffer);
    for (DomainEventProcessor dep : processors) {
      dep.start();
      this.proArray.add(dep);
    }
    return this.eBuffer;
  }

  public void stop() {
    if (!this.started.compareAndSet(true, false)) {
      throw new IllegalStateException("DomainEventProcessorDSL.stop() must only be called once.");
    }
    waitForQueueClear();
    this.logger.warn("Clear Queue ok");
    for (DomainEventProcessor dep : this.proArray) {
      dep.halt();
    }
    this.logger.warn("Stop ProcessDSL successfull");
  }

  private void waitForQueueClear() {
    synchronized (this) {
      while (!this.ebFactory.clearBuffer(this.eBuffer))
        try {
          this.logger.warn("Waitting now");
          wait(1000L);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
    }
  }

  private void checkOnlyStartedOnce()
  {
    if (!this.started.compareAndSet(false, true))
      throw new IllegalStateException("DomainEventProcessorDSL.start() must only be called once.");
  }
}




