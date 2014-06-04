package com.mce.util;

public final class StringUtils
{
  public static boolean isNull(String pa)
  {
    if (pa != null) {
      String pas = pa.trim();
      if (("".equalsIgnoreCase(pas)) || (pas.length() < 1))
      {
        return true;
      }
      return false;
    }
    return true;
  }

  public static String trim(String pas) {
    if (isNull(pas)) {
      return "";
    }

    return pas.trim();
  }
}


 
 
 