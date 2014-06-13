package com.mce.util;

import java.security.MessageDigest;

import sun.misc.BASE64Encoder;

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
	public static String md5(String source) {
		try {
			MessageDigest md = MessageDigest.getInstance("md5");
			BASE64Encoder encoder = new BASE64Encoder();
			return encoder.encode(md.digest(source.getBytes("utf-8")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}


 
 
 