package com.mce.domain.event;

import java.util.HashMap;
import java.util.Map;

public final class DomainEventContext
{
  private final Map<String, Object> contextMap = new HashMap<String, Object>();

  public void addProperty(String key, Object value) { this.contextMap.put(key, value); }

  public void removeProperty(String key)
  {
    this.contextMap.remove(key);
  }

  public Object get(String key) {
    return this.contextMap.get(key);
  }
}




