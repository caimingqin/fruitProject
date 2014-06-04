package com.mce.core.notification;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import jsr166y.LinkedTransferQueue;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class NotifyRunner implements Runnable {
	private LinkedTransferQueue<Notification> pool = new LinkedTransferQueue<Notification>();

	private boolean start = true;
	private Set<NotificationListener> listeners = new CopyOnWriteArraySet<NotificationListener>();
	private Log logger = LogFactory.getLog(getClass().getName());
	private AtomicBoolean started = new AtomicBoolean(false);
	private Executor executor;

	NotifyRunner() {
	}

	NotifyRunner(NotificationListener nl, Executor executor) {
		this(new NotificationListener[] { nl }, executor);
	}

	NotifyRunner(NotificationListener[] nl, Executor executor) {
		Validate.notEmpty(nl);
		Validate.notNull(executor);
		this.executor = executor;
		for (NotificationListener n : nl)
			this.listeners.add(n);
	}

	public void run() {
		while (this.start) {
			Notification ntf = null;
			try {
				ntf = (Notification) this.pool.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (ntf != null) {
				proceess(ntf);
			}
		}
		if (this.pool.size() > 0)
			for (Notification n : this.pool)
				proceess(n);
	}

	private void proceess(Notification n) {
		for (NotificationListener nl : this.listeners)
			handeNotify(nl, n, this.logger);
	}

	private void pushNotification(Notification n) {
		try {
			this.pool.transfer(n);
		} catch (InterruptedException e) {
			this.logger.error(e.getMessage());
		}

		if (Notification.isStopNotify(n))
			stop();
	}

	private void stop() {
		this.start = false;
	}

	boolean isStop() {
		return (!this.start) && (this.pool.isEmpty());
	}

	static void handeNotify(NotificationListener nl, Notification n, Log logger) {
		try {
			nl.handle(n);
		} catch (RuntimeException re) {
			logger.error(re);
		} catch (Exception e) {
			logger.error(e);
		} catch (Throwable t) {
			logger.error(t);
		}
	}

	static void broadcast(List<NotifyRunner> nrs, Notification nf) {
		Validate.notNull(nrs);
		Validate.notNull(nf);
		for (NotifyRunner nr : nrs)
			nr.pushNotification(nf);
	}

	void start() {
		if (this.started.compareAndSet(false, true)) {
			this.executor.execute(this);

			Notification nf = new Notification("observerMessage",
					NotificationStatus.PREPARE);
			pushNotification(nf);

			Notification snf = new Notification("observerMessage",
					NotificationStatus.START);
			pushNotification(snf);
		} else {
			throw new IllegalStateException("The NotifyRunner is Running now");
		}
	}
}
