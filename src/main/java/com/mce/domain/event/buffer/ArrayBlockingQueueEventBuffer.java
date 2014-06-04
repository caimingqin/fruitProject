package com.mce.domain.event.buffer;

import com.mce.domain.event.DomainEvent;
import com.mce.domain.event.EventBuffer;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class ArrayBlockingQueueEventBuffer implements EventBuffer {
	private Queue<DomainEvent> deQueue = null;

	public ArrayBlockingQueueEventBuffer(int i) {
		this.deQueue = new ArrayBlockingQueue<DomainEvent>(i);
	}

	public DomainEvent get() {
		return (DomainEvent) this.deQueue.poll();
	}

	public boolean push(DomainEvent de) {
		try {
			return this.deQueue.add(de);
		} catch (RuntimeException re) {
			return false;
		} catch (Exception e) {
			return false;
		} catch (Throwable t) {
		}
		return false;
	}

	public int size() {
		return this.deQueue.size();
	}
}
