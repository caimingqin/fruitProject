package com.mce.domain.event;

import com.mce.util.LoggerUtils;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

final class DomainEventProcessor implements Runnable {
	private final AtomicBoolean running = new AtomicBoolean(false);
	private long waitTime;
	private Executor executor = Executors.newFixedThreadPool(1);
	private ExceptionHandler exceptionHandler;
	private EventBuffer eBuffer;
	private DomainEventHandler[] handlers;
	public static final int DEFAULT_PROCESSOR_SIZE = 2;
	private static Log logger = LogFactory.getLog(DomainEventProcessor.class
			.getName());

	public DomainEventProcessor(DomainEventHandler[] handlers, EventBuffer des,
			ExceptionHandler eHandler, long waitTime) {
		this.handlers = handlers;
		this.exceptionHandler = eHandler;
		this.waitTime = waitTime;
		this.eBuffer = des;
	}

	void halt() {
		if (!this.running.compareAndSet(true, false))
			throw new IllegalStateException("Thread is already running");
	}

	public void run() {
		if (this.waitTime > 0L) {
			synchronized (this) {
				try {
					wait(this.waitTime);
				} catch (InterruptedException e) {
					throw new IllegalStateException("can not run");
				}
			}
		}
		while (this.running.get()) {
			DomainEvent de = this.eBuffer.get();
			if (de != null) {
				handle(de, this.handlers);
				if (!de.isSuccess())
					this.exceptionHandler.handleException(de);
			}
		}
	}

	void start() {
		if (!this.running.compareAndSet(false, true)) {
			throw new IllegalStateException("Thread is already running");
		}
		this.executor.execute(this);
		logger.info("Start DomainEventProcessor already");
	}

	public static void handle(DomainEvent de, DomainEventHandler[] handlers) {
		for (DomainEventHandler deh : handlers)
			if (deh.isSupport(de)) {
				handle(deh, de);
				break;
			}
	}

	private static void handle(DomainEventHandler ah, DomainEvent ae) {
		Throwable ex = null;
		DomainEventContext dec = new DomainEventContext();
		try {
			beforeInterceptor(dec, ah.getInterceptors(), ae);
			ah.handle(ae);
		} catch (RuntimeException e) {
			ex = e;
		} catch (Error e) {
			ex = e;
		} catch (Exception e) {
			ex = e;
		} catch (Throwable t) {
			ex = t;
		} finally {
			if (ex != null) {
				LoggerUtils.showLog(ex, logger);
			}
			afterInterceptor(dec, ah.getInterceptors(), ae);
		}
	}

	private static void beforeInterceptor(DomainEventContext dec,
			DomainEventInterceptor[] interceptors, DomainEvent ae) {
		if ((interceptors != null) && (interceptors.length > 0))
			for (DomainEventInterceptor i : interceptors)
				i.beforeHandle(dec, ae);
	}

	private static void afterInterceptor(DomainEventContext dec,
			DomainEventInterceptor[] interceptors, DomainEvent ae) {
		if ((interceptors != null) && (interceptors.length > 0))
			for (DomainEventInterceptor i : interceptors)
				try {
					i.afterHandle(dec, ae);
				} catch (RuntimeException ex2) {
					LoggerUtils.showLog(ex2, logger);
				} catch (Error err) {
					LoggerUtils.showLog(err, logger);
				} catch (Exception e) {
					LoggerUtils.showLog(e, logger);
				} catch (Throwable ex2) {
					LoggerUtils.showLog(ex2, logger);
				}
	}
}
