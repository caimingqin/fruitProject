package com.mce.domain.event.buffer;

import com.mce.domain.event.DomainEvent;
import com.mce.domain.event.EventBuffer;
import jsr166y.LinkedTransferQueue;

public class LinkedTransferQueueEventBuffer implements EventBuffer {
	private LinkedTransferQueue<DomainEvent> deQueue = new LinkedTransferQueue<DomainEvent>();

	public DomainEvent get() {
		try {
			return (DomainEvent) this.deQueue.take();
		} catch (InterruptedException e) {
		}
		return null;
	}

	public boolean push(DomainEvent de) {
		try {
			this.deQueue.transfer(de);
			return true;
		} catch (InterruptedException e) {
		}
		return false;
	}

	public int size() {
		return this.deQueue.size();
	}
}
