package com.mce.core.resource;

public class FilterImpl
  implements Filter
{
  private transient String[] ignoredPackages = { "javax", "java", "sun", "com.sun", "javassist" };

  public final boolean accepts(String filename)
  {
    if (filename.endsWith(".class")) {
      if (filename.startsWith("/")) {
        filename = filename.substring(1);
      }
      if (!ignoreScan(filename.replace('/', '.'))) {
        return true;
      }
    }
    return false;
  }

  private boolean ignoreScan(String intf)
  {
    for (String ignored : this.ignoredPackages) {
      if (intf.startsWith(ignored + ".")) {
        return true;
      }
    }
    return false;
  }
}




