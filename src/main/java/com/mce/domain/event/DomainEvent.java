package com.mce.domain.event;

import com.mce.util.IdUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;

public class DomainEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String EVENT_HANDLED_NOTIFICATION = "eventHandledNotification";
	private String name;
	private Object target;
	private Map<String, Object> contextMap = new HashMap<String, Object>();
	private String sessionId = IdUtils.getUUID();
	private HandleEventStatus handleEventStatus = HandleEventStatus.NORMAL;
	private Throwable error;

	public static boolean isDomainEventNotification(String nName) {
		return "eventHandledNotification".equalsIgnoreCase(nName);
	}

	DomainEvent() {
	}

	public DomainEvent(String type) {
		this(type, null);
	}

	public DomainEvent(String name, Object target) {
		Validate.notNull(name, "Event name is null");
		this.name = name;
		this.target = target;
	}

	public String getName() {
		return this.name;
	}

	public Object getTarget() {
		return this.target;
	}

	public void addContextProperty(String key, Object value) {
		this.contextMap.put(key, value);
	}

	public Object getContextProperty(String key) {
		return this.contextMap.get(key);
	}

	public Map<String, Object> getContextMap() {
		return this.contextMap;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public HandleEventStatus getHandleEventStatus() {
		return this.handleEventStatus;
	}

	public void stopHandle(Throwable t) {
		this.error = t;
		this.handleEventStatus = HandleEventStatus.STOP;
	}

	public boolean isStopHandle() {
		return HandleEventStatus.STOP.equals(this.handleEventStatus);
	}

	public boolean isSuccess() {
		return this.error == null;
	}

	public Throwable getError() {
		return this.error;
	}

	public static enum HandleEventStatus {
		NORMAL, STOP, COMPLETED;
	}
}
