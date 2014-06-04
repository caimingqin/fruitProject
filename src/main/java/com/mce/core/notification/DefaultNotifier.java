package com.mce.core.notification;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DefaultNotifier implements Notifier {
	private NotificationStatus status = NotificationStatus.PREPARE;
	private String sessionId = null;
	private Log logger = LogFactory.getLog(getClass().getName());

	private Executor executor = Executors.newCachedThreadPool();

	private List<NotifyRunner> nrs = new LinkedList<NotifyRunner>();

	void addNotificationListener(NotificationListener nl) {
		Validate.notNull(nl, "not allowed null notificaitonListener to add["
				+ getClass().getName() + "]");
		NotifyFilter nf = DefaultNotifyFilter.get().getFilter(nl);
		NotifyRunner nr = new NotifyRunner(new FilterNitifierListener(nl, nf),
				this.executor);
		this.nrs.add(nr);
	}

	private void broadcast(Notification nf) {
		NotifyRunner.broadcast(this.nrs, nf);
	}

	private void addAllNotificationListeners(List<NotificationListener> nll) {
		for (NotificationListener nl : nll)
			addNotificationListener(nl);
	}

	public void shutdown(String sId) {
		this.logger.warn("Stop now");

		synchronized (this) {
			if (NotificationStatus.START.equals(this.status)) {
				if (!this.sessionId.equalsIgnoreCase(sId)) {
					throw new IllegalArgumentException(
							"Can not stop NotificationObserver[" + sId + "]");
				}
				this.status = NotificationStatus.STOP;
				this.logger.warn("Start Stopping NotificationListeners ");
				Notification stopNtf = new Notification("observerMessage",
						NotificationStatus.STOP);
				broadcast(stopNtf);
				waitStop();

				this.logger.warn("Stop notificationObserver successfully");
			}
		}
	}

	private void waitStop() {
		int count = 0;
		int nrSize = this.nrs.size();

		long timeOutValue = System.currentTimeMillis();
		timeOutValue += 60000L;

		boolean breakStatus = false;

		this.logger.info("Stop NotifyRunners");
		while (true) {
			for (NotifyRunner nr : this.nrs) {
				if (nr.isStop()) {
					count += 1;
				}
			}

			long cTimeOutValue = System.currentTimeMillis();
			if (timeOutValue > cTimeOutValue) {
				if (nrSize > count) {
					count = 0;
					try {
						wait(5000L);
					} catch (InterruptedException e) {
						this.logger.trace(e);
					}
				} else {
					breakStatus = true;
				}
			} else {
				breakStatus = true;
			}

			if (breakStatus)
				break;
		}
	}

	public NotifyBox start(String sessionId, List<NotificationListener> nll) {
		synchronized (this) {
			if (NotificationStatus.PREPARE.equals(this.status)) {
				this.sessionId = sessionId;
				this.status = NotificationStatus.START;

				addAllNotificationListeners(nll);
				startRunners();
				return new NotifyBox() {
					public boolean add(Notification n) {
						if (NotificationStatus.STOP
								.equals(DefaultNotifier.this.status)) {
							return false;
						}
						DefaultNotifier.this.broadcast(n);
						return true;
					}

				};
			}

			throw new IllegalStateException("Call start method once please");
		}
	}

	private void startRunners() {
		for (NotifyRunner nr : this.nrs)
			nr.start();
	}

	public boolean isRunning() {
		return NotificationStatus.START.equals(this.status);
	}
}
