package com.mce.util;

import java.io.InputStream;

public final class ConfigHelper
{
  public static InputStream getResourceAsStream(String resource)
  {
    String stripped = resource.startsWith("/") ? resource.substring(1) : resource;

    InputStream stream = null;
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    if (classLoader != null) {
      stream = classLoader.getResourceAsStream(stripped);
    }
    if (stream == null) {
      stream = ConfigHelper.class.getResourceAsStream(resource);
    }
    if (stream == null) {
      stream = ConfigHelper.class.getClassLoader().getResourceAsStream(stripped);
    }
    if (stream == null) {
      throw new RuntimeException(resource + " not found");
    }
    return stream;
  }
}




