 package com.mce.util;
 
 import java.lang.reflect.Constructor;
 import java.lang.reflect.Field;
 import java.lang.reflect.InvocationTargetException;
 import java.lang.reflect.Method;
 import java.lang.reflect.Modifier;
 import java.sql.SQLException;
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.List;
 import org.apache.commons.lang.Validate;
 
 public abstract class ReflectionUtility
 {
   public static FieldFilter COPYABLE_FIELDS = new FieldFilter() {
     public boolean matches(Field field) {
       return (!Modifier.isStatic(field.getModifiers())) && (!Modifier.isFinal(field.getModifiers()));
     }
   };
 
   public static Field findField(Class<?> clazz, String name)
   {
     return findField(clazz, name, null);
   }
 
   public static Field findField(Class<?> clazz, String name, Class<?> type) {
     Validate.notNull(clazz, "Class must not be null");
     Validate.isTrue((name != null) || (type != null), "Either name or type of the field must be specified");
     Class searchType = clazz;
     while ((!Object.class.equals(searchType)) && (searchType != null)) {
       Field[] fields = searchType.getDeclaredFields();
       for (int i = 0; i < fields.length; i++) {
         Field field = fields[i];
         if (((name == null) || (name.equals(field.getName()))) && ((type == null) || (type.equals(field.getType()))))
         {
           return field;
         }
       }
       searchType = searchType.getSuperclass();
     }
     return null;
   }
 
   public static void setField(Field field, Object target, Object value) {
     try {
       field.set(target, value);
     }
     catch (IllegalAccessException ex) {
       handleReflectionException(ex);
       throw new IllegalStateException("Unexpected reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
     }
   }
 
   public static Object getField(Field field, Object target)
   {
     try
     {
       return field.get(target);
     }
     catch (IllegalAccessException ex) {
       handleReflectionException(ex);
       throw new IllegalStateException("Unexpected reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
     }
   }
 
   public static Method findMethod(Class<?> clazz, String name)
   {
     return findMethod(clazz, name, new Class[0]);
   }
 
   public static Method findMethod(Class<?> clazz, String name, Class<?>[] paramTypes)
   {
     Validate.notNull(clazz, "Class must not be null");
     Validate.notNull(name, "Method name must not be null");
     Class searchType = clazz;
     while ((!Object.class.equals(searchType)) && (searchType != null)) {
       Method[] methods = searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods();
       for (int i = 0; i < methods.length; i++) {
         Method method = methods[i];
         if ((name.equals(method.getName())) && ((paramTypes == null) || (Arrays.equals(paramTypes, method.getParameterTypes()))))
         {
           return method;
         }
       }
       searchType = searchType.getSuperclass();
     }
     return null;
   }
 
   public static Object invokeMethod(Method method, Object target)
   {
     return invokeMethod(method, target, null);
   }
 
   public static Object invokeMethod(Method method, Object target, Object[] args)
   {
     try
     {
       return method.invoke(target, args);
     }
     catch (Exception ex) {
       handleReflectionException(ex);
     }
     throw new IllegalStateException("Should never get here");
   }
 
   public static Object invokeJdbcMethod(Method method, Object target)
     throws SQLException
   {
     return invokeJdbcMethod(method, target, null);
   }
 
   public static Object invokeJdbcMethod(Method method, Object target, Object[] args)
     throws SQLException
   {
     try
     {
       return method.invoke(target, args);
     }
     catch (IllegalAccessException ex) {
       handleReflectionException(ex);
     }
     catch (InvocationTargetException ex) {
       if ((ex.getTargetException() instanceof SQLException)) {
         throw ((SQLException)ex.getTargetException());
       }
       handleInvocationTargetException(ex);
     }
     throw new IllegalStateException("Should never get here");
   }
 
   public static void handleReflectionException(Exception ex)
   {
     if ((ex instanceof NoSuchMethodException)) {
       throw new IllegalStateException("Method not found: " + ex.getMessage());
     }
     if ((ex instanceof IllegalAccessException)) {
       throw new IllegalStateException("Could not access method: " + ex.getMessage());
     }
     if ((ex instanceof InvocationTargetException)) {
       handleInvocationTargetException((InvocationTargetException)ex);
     }
     if ((ex instanceof RuntimeException)) {
       throw ((RuntimeException)ex);
     }
     handleUnexpectedException(ex);
   }
 
   public static void handleInvocationTargetException(InvocationTargetException ex)
   {
     rethrowRuntimeException(ex.getTargetException());
   }
 
   public static void rethrowRuntimeException(Throwable ex)
   {
     if ((ex instanceof RuntimeException)) {
       throw ((RuntimeException)ex);
     }
     if ((ex instanceof Error)) {
       throw ((Error)ex);
     }
     handleUnexpectedException(ex);
   }
 
   public static void rethrowException(Throwable ex)
     throws Exception
   {
     if ((ex instanceof Exception)) {
       throw ((Exception)ex);
     }
     if ((ex instanceof Error)) {
       throw ((Error)ex);
     }
     handleUnexpectedException(ex);
   }
 
   private static void handleUnexpectedException(Throwable ex)
   {
     IllegalStateException isex = new IllegalStateException("Unexpected exception thrown");
     isex.initCause(ex);
     throw isex;
   }
 
   public static boolean declaresException(Method method, Class<?> exceptionType)
   {
     Validate.notNull(method, "Method must not be null");
     Class[] declaredExceptions = method.getExceptionTypes();
     for (int i = 0; i < declaredExceptions.length; i++) {
       Class declaredException = declaredExceptions[i];
       if (declaredException.isAssignableFrom(exceptionType)) {
         return true;
       }
     }
     return false;
   }
 
   public static boolean isPublicStaticFinal(Field field)
   {
     int modifiers = field.getModifiers();
     return (Modifier.isPublic(modifiers)) && (Modifier.isStatic(modifiers)) && (Modifier.isFinal(modifiers));
   }
 
   public static boolean isEqualsMethod(Method method)
   {
     if ((method == null) || (!method.getName().equals("equals"))) {
       return false;
     }
     Class[] paramTypes = method.getParameterTypes();
     return (paramTypes.length == 1) && (paramTypes[0] == Object.class);
   }
 
   public static boolean isHashCodeMethod(Method method)
   {
     return (method != null) && (method.getName().equals("hashCode")) && (method.getParameterTypes().length == 0);
   }
 
   public static boolean isToStringMethod(Method method)
   {
     return (method != null) && (method.getName().equals("toString")) && (method.getParameterTypes().length == 0);
   }
 
   public static void makeAccessible(Field field)
   {
     if ((!Modifier.isPublic(field.getModifiers())) || (!Modifier.isPublic(field.getDeclaringClass().getModifiers())))
     {
       field.setAccessible(true);
     }
   }
 
   public static void makeAccessible(Method method)
   {
     if ((!Modifier.isPublic(method.getModifiers())) || (!Modifier.isPublic(method.getDeclaringClass().getModifiers())))
     {
       method.setAccessible(true);
     }
   }
 
   public static void makeAccessible(Constructor<?> ctor)
   {
     if ((!Modifier.isPublic(ctor.getModifiers())) || (!Modifier.isPublic(ctor.getDeclaringClass().getModifiers())))
     {
       ctor.setAccessible(true);
     }
   }
 
   public static void doWithMethods(Class<?> targetClass, MethodCallback mc)
     throws IllegalArgumentException
   {
     doWithMethods(targetClass, mc, null);
   }
 
   public static void doWithMethods(Class<?> targetClass, MethodCallback mc, MethodFilter mf)
     throws IllegalArgumentException
   {
     do
     {
       Method[] methods = targetClass.getDeclaredMethods();
       for (int i = 0; i < methods.length; i++) {
         if ((mf == null) || (mf.matches(methods[i])))
         {
           try
           {
             mc.doWith(methods[i]);
           }
           catch (IllegalAccessException ex) {
             throw new IllegalStateException("Shouldn't be illegal to access method '" + methods[i].getName() + "': " + ex);
           }
         }
       }
       targetClass = targetClass.getSuperclass();
     }
     while (targetClass != null);
   }
 
   public static Method[] getAllDeclaredMethods(Class<?> leafClass)
     throws IllegalArgumentException
   {
     List<Method> list = new ArrayList(32);
     doWithMethods(leafClass, new MethodCallback() {
       public void doWith(Method method) {
//         this.val$list.add(method);
       }
     });
     return (Method[])list.toArray(new Method[list.size()]);
   }
 
   public static void doWithFields(Class<?> targetClass, FieldCallback fc)
     throws IllegalArgumentException
   {
     doWithFields(targetClass, fc, null);
   }
 
   public static void doWithFields(Class<?> targetClass, FieldCallback fc, FieldFilter ff)
     throws IllegalArgumentException
   {
     do
     {
       Field[] fields = targetClass.getDeclaredFields();
       for (int i = 0; i < fields.length; i++)
       {
         if ((ff == null) || (ff.matches(fields[i])))
         {
           try
           {
             fc.doWith(fields[i]);
           }
           catch (IllegalAccessException ex) {
             throw new IllegalStateException("Shouldn't be illegal to access field '" + fields[i].getName() + "': " + ex);
           }
         }
       }
       targetClass = targetClass.getSuperclass();
     }
     while ((targetClass != null) && (targetClass != Object.class));
   }
 
   public static void shallowCopyFieldState(Object src, final Object dest)
     throws IllegalArgumentException
   {
     if (src == null) {
       throw new IllegalArgumentException("Source for field copy cannot be null");
     }
     if (dest == null) {
       throw new IllegalArgumentException("Destination for field copy cannot be null");
     }
     if (!src.getClass().isAssignableFrom(dest.getClass())) {
       throw new IllegalArgumentException("Destination class [" + dest.getClass().getName() + "] must be same or subclass as source class [" + src.getClass().getName() + "]");
     }
 
     doWithFields(src.getClass(), new FieldCallback() {
       public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
         ReflectionUtility.makeAccessible(field);
//         Object srcValue = field.get(this.val$src);
//         field.set(dest, srcValue);
       }
     }
     , COPYABLE_FIELDS);
   }
 
   public static abstract interface FieldFilter
   {
     public abstract boolean matches(Field paramField);
   }
 
   public static abstract interface FieldCallback
   {
     public abstract void doWith(Field paramField)
       throws IllegalArgumentException, IllegalAccessException;
   }
 
   public static abstract interface MethodFilter
   {
     public abstract boolean matches(Method paramMethod);
   }
 
   public static abstract interface MethodCallback
   {
     public abstract void doWith(Method paramMethod)
       throws IllegalArgumentException, IllegalAccessException;
   }
 }




