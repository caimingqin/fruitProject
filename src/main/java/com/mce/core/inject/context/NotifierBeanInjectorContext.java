package com.mce.core.inject.context;

import com.mce.core.inject.ConfigurableBeanInjector;
import com.mce.core.notification.Notification;
import com.mce.core.notification.NotificationListener;
import com.mce.core.notification.Notifier;
import com.mce.core.notification.NotifierConfig;
import com.mce.core.notification.NotifyBox;
import com.mce.core.notification.NotifyProducer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NotifierBeanInjectorContext
  implements BeanInjectorContext, NotifyBox
{
  private ConfigurableBeanInjector cBeanInjector;
  private Log logger = LogFactory.getLog(getClass().getName());
  private String sessinId;
  private AtomicBoolean start = new AtomicBoolean(false);
  private NotifyBox nBox;
  private Notifier notifer;

  public NotifierBeanInjectorContext(ConfigurableBeanInjector cbp)
  {
    this.cBeanInjector = cbp;
  }

  private List<NotificationListener> initListeners(ConfigurableBeanInjector cbi)
  {
    String[] names = cbi.getBeanNamesForType(NotificationListener.class);
    if ((names != null) && (names.length > 0)) {
      this.logger.info("find Bean NotificationListener[" + names.length + "]");
      List<NotificationListener> nll = new ArrayList<NotificationListener>();
      for (String name : names) {
        NotificationListener ns = (NotificationListener)cbi.getBean(name);
        nll.add(ns);
      }
      return nll;
    }
    return null;
  }

  public <T> T getBean(Class<T> type)
  {
    return this.cBeanInjector.getBean(type);
  }

  public Object getBean(String name)
  {
    return this.cBeanInjector.getBean(name);
  }

  public String[] getBeanNamesForType(Class<?> class1)
  {
    return this.cBeanInjector.getBeanNamesForType(class1);
  }

  public <T> T getBeanOnSafe(Class<T> type)
  {
    return this.cBeanInjector.getBeanOnSafe(type);
  }

  public void inject(Object bean)
  {
    this.cBeanInjector.inject(bean);
  }

  public void start(String sessionId)
  {
    if (this.start.compareAndSet(false, true)) {
      this.sessinId = sessionId;
      startNotifier(this.sessinId);
    } else {
      throw new IllegalStateException("The BeanInjectorContext is started");
    }
  }

  private void startNotifier(String sessinId) {
    this.notifer = getNotifer();
    List nll = initListeners(this.cBeanInjector);
    if (nll != null) {
      this.nBox = this.notifer.start(sessinId, nll);
      List<NotifyProducer> npl = initNotifyProducers(this.cBeanInjector);
      if (npl != null)
        for (NotifyProducer np : npl)
          np.startProducer(this.nBox);
    }
  }

  private List<NotifyProducer> initNotifyProducers(ConfigurableBeanInjector cbi)
  {
    String[] allProducers = cbi.getBeanNamesForType(NotifyProducer.class);
    if ((allProducers != null) && (allProducers.length > 0)) {
      List npl = new ArrayList();
      for (String apn : allProducers) {
        NotifyProducer np = (NotifyProducer)cbi.getBean(apn);
        npl.add(np);
      }
      return npl;
    }
    return null;
  }

  public void stop(String sessionId2)
  {
    if (this.start.get())
      if (this.sessinId.equalsIgnoreCase(sessionId2)) {
        this.notifer.shutdown(this.sessinId);
        this.cBeanInjector.destroyAll();
      }
      else {
        this.logger.warn("not Same SessionId not stop NotiferContext success");
      }
  }

  public boolean add(Notification n)
  {
    return this.nBox.add(n);
  }

  private Notifier getNotifer() {
    if (this.notifer != null) {
      return this.notifer;
    }
    NotifierConfig nc = (NotifierConfig)this.cBeanInjector.getBeanOnSafe(NotifierConfig.class);
    if (nc == null) {
      nc = new NotifierConfig("dnotifier");
    }
    return nc.build();
  }
}




