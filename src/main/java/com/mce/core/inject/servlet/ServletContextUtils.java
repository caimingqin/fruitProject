 package com.mce.core.inject.servlet;
 
 import java.io.File;
import java.net.URL;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mce.core.inject.BeanInjector;
import com.mce.core.inject.context.BeanInjectorContext;
 
 public final class ServletContextUtils
 {
   private static final Log logger = LogFactory.getLog(ServletContextUtils.class.getName());
 
   public static BeanInjectorContext getBeanInjectorFor(ServletContext scs) {
     return (BeanInjectorContext)scs.getAttribute(BeanInjector.class.getName());
   }
 
   public static URL getRoot(ServletContext sc) {
     String rootPath = sc.getRealPath("/");
     logger.info("==================>>"+rootPath);
     try {
       return new File(rootPath).toURI().toURL();
     }
     catch (Exception e) {
       logger.error("Error[" + e.getMessage() + "]", e);
       throw new RuntimeException(e);
     }
   }
 
   public static String getConfigPath(ServletContext sc, String resPath) {
     String rootPath = sc.getRealPath("/");
     StringBuffer sb = new StringBuffer(rootPath);
     sb.append(resPath);
     String resultPath = sb.toString();
     return resultPath;
   }
 
   public static URL getConfigURL(ServletContext sc, String resPath) {
     String resultPath = getConfigPath(sc, resPath);
     try {
       return new File(resultPath).toURI().toURL();
     }
     catch (Exception e) {
       logger.error("Load Config file[" + resultPath + "] error ");
       throw new RuntimeException(e);
     }
   }
 }




