package com.mce.domain.event.config;

import com.mce.domain.event.handler.DomainEventHandlerConfigurable;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AutowiredDomainEventhandlerConfigurable
  implements DomainEventHandlerConfigurable
{
  private Log logger = LogFactory.getLog(getClass());
  private AtomicBoolean started = new AtomicBoolean(false);
  private URL rootURL = null;
  private List<Object> handles;

  public AutowiredDomainEventhandlerConfigurable()
  {
  }

  public AutowiredDomainEventhandlerConfigurable(URL url)
  {
    this.rootURL = url;
  }

  protected URL getRootURL() {
    return this.rootURL;
  }
  protected Log getLogger() {
    return this.logger;
  }

  protected abstract List<Object> getObjectHandlers();

  public Object[] build() {
    if (this.started.compareAndSet(false, true)) {
      this.handles = getObjectHandlers();
    }
    return this.handles.toArray(new Object[this.handles.size()]);
  }
}
