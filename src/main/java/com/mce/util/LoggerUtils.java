 package com.mce.util;
 
 import org.apache.commons.logging.Log;
 
 public class LoggerUtils
 {
   public static void showLog(Throwable ex, Log logger)
   {
     logger.error("[" + ex.getMessage() + "]");
     StackTraceElement[] stc = ex.getStackTrace();
     if ((stc != null) && (stc.length > 0))
       for (StackTraceElement ste : stc)
         logger.error("Error[" + ste.getClassName() + "] method[" + ste.getMethodName() + "] line[" + ste.getLineNumber() + "]");
   }
 }




