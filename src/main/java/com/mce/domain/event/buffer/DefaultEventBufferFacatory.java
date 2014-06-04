package com.mce.domain.event.buffer;

import com.mce.domain.event.EventBuffer;
import com.mce.domain.event.EventBufferFactory;

public class DefaultEventBufferFacatory implements EventBufferFactory {
	public DefaultEventBufferFacatory() {
	}

	public DefaultEventBufferFacatory(int size) {
	}

	public EventBuffer createEventBuffer() {
		return new LinkedTransferQueueEventBuffer();
	}

	public boolean clearBuffer(EventBuffer eBuffer) {
		return eBuffer.size() < 1;
	}
}
