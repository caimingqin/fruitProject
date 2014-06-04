package com.mce.domain.event.handler;

import com.mce.domain.event.DomainEvent;
import com.mce.util.ReflectionUtility;
import java.lang.reflect.Method;

public final class MethodAction extends AbstractDomainEventHandler
{
  private Method method;
  private Object target;
  private String eventType;

  public MethodAction(String listenerType, Method method, Object target)
  {
    super(listenerType);
    this.eventType = listenerType;
    this.method = method;
    this.target = target;
  }

  public String getEventType() {
    return this.eventType;
  }
  public Method getMethod() { return this.method; } 
  public Object getTarget() { return this.target; }

  protected void processEvent(DomainEvent ae)
  {
    ReflectionUtility.invokeMethod(this.method, this.target, new Object[] { ae });//TODO
  }

  public String toString()
  {
    StringBuffer sb = new StringBuffer(super.toString());
    sb.append("&");
    sb.append(this.target.getClass().getName());
    sb.append("@Method(" + getEventType() + ")");
    return sb.toString();
  }
}




