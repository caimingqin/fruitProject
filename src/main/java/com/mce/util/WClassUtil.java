 package com.mce.util;
 
 import java.io.ByteArrayInputStream;
 import java.io.ByteArrayOutputStream;
 import java.io.IOException;
 import java.io.ObjectInputStream;
 import java.io.ObjectOutputStream;
 import java.lang.reflect.Constructor;
 import java.lang.reflect.Modifier;
 import org.apache.commons.lang.Validate;
 import org.apache.commons.logging.Log;
 import org.apache.commons.logging.LogFactory;
 import org.springframework.util.ClassUtils;
 
 public final class WClassUtil
 {
   private static final Log logger = LogFactory.getLog(WClassUtil.class.getName());
   private static ClassLoader defaultClassLoader = null;
   public static final char INNER_CLASS_SEPARATOR_CHAR = '$';
 
   public static void setClassLoader(ClassLoader cl)
   {
     defaultClassLoader = cl;
   }
 
   public static Object getInstance(String s)
   {
     if (defaultClassLoader == null) {
       return getInstance(s, ClassUtils.getDefaultClassLoader());
     }
     return getInstance(s, defaultClassLoader);
   }
 
   public static boolean isInnerClass(Class<?> cls) {
     if (cls == null) {
       return false;
     }
     return cls.getName().indexOf('$') >= 0;
   }
 
   public static Object getInstance(String s, ClassLoader cl) {
     try {
       Class c = ClassUtils.forName(s, cl);
       if ((isInnerClass(c)) && 
         (!Modifier.isStatic(c.getModifiers()))) {
         return getInnerInstance(c, cl);
       }
 
       return c.newInstance();
     } catch (Exception e) {
       logger.error("not found class[" + e.getMessage() + "]", e);
       throw new IllegalArgumentException(e);
     }
   }
 
   public static Object getInnerInstance(Class<?> s, ClassLoader cl) {
     try {
       String name = s.getName();
       int index = name.indexOf('$');
       String pName = name.substring(0, index);
       Class pclss = Class.forName(pName);
       Constructor clazz = s.getDeclaredConstructor(new Class[] { pclss });
 
       ReflectionUtility.makeAccessible(clazz);
 
       Object outer = pclss.newInstance();
       return clazz.newInstance(new Object[] { outer });
     }
     catch (Exception e) {
       throw new RuntimeException(e);
     }
   }
 
   public static <T> T instantiateClass(Constructor<T> ctor, Object[] args) {
     Validate.notNull(ctor, "Constructor must not be null");
     try {
       ReflectionUtility.makeAccessible(ctor);
       return ctor.newInstance(args);
     }
     catch (Exception e) {
       throw new IllegalArgumentException(e);
     }
   }
 
   public static byte[] toByteArray(Object obj)
   {
     byte[] bytes = null;
     ByteArrayOutputStream bos = new ByteArrayOutputStream();
     try {
       ObjectOutputStream oos = new ObjectOutputStream(bos);
       oos.writeObject(obj);
       oos.flush();
       bytes = bos.toByteArray();
       oos.close();
       bos.close();
     } catch (IOException ex) {
       throw new RuntimeException(ex);
     }
     return bytes;
   }
 
   public static Object toObject(byte[] res)
   {
     try
     {
       ByteArrayInputStream bis = new ByteArrayInputStream(res);
       ObjectInputStream ois = new ObjectInputStream(bis);
       Object obj = ois.readObject();
       ois.close();
       bis.close();
       return obj;
     } catch (Exception e) {
       throw new RuntimeException(e);
     }
   }
 }




