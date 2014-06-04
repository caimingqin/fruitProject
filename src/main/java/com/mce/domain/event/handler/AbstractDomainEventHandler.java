package com.mce.domain.event.handler;

import com.mce.domain.event.DomainEvent;
import com.mce.domain.event.DomainEvent.HandleEventStatus;
import com.mce.domain.event.DomainEventHandler;
import com.mce.domain.event.DomainEventInterceptor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractDomainEventHandler implements DomainEventHandler {
	protected Log logger = LogFactory.getLog(getClass().getName());
	private String handleEventName;

	protected Log getLog() {
		return this.logger;
	}

	AbstractDomainEventHandler(String eventType) {
		this.handleEventName = eventType;
	}

	protected String getHandleEventName() {
		return this.handleEventName;
	}

	public DomainEventInterceptor[] getInterceptors() {
		throw new UnsupportedOperationException("not support this method");
	}

	public boolean isSupport(DomainEvent ae) {
		return this.handleEventName.equalsIgnoreCase(ae.getName());
	}

	public void handle(DomainEvent ae) {
		this.logger.debug("Start DomainEventHandler[" + this.handleEventName
				+ "]");
		try {
			if (DomainEvent.HandleEventStatus.STOP.equals(ae
					.getHandleEventStatus())) {
				this.logger
						.error("Is stop ActionEventStatus \tso not handle it["
								+ ae.getSessionId() + "][" + ae.getName() + "]");

				return;
			}
			processEvent(ae);
		} catch (RuntimeException e) {
			ae.stopHandle(e);
		} catch (Error e) {
			ae.stopHandle(e);
		} catch (Exception e) {
			ae.stopHandle(e);
		} catch (Throwable e) {
			ae.stopHandle(e);
		}
	}

	protected abstract void processEvent(DomainEvent paramDomainEvent);
}
