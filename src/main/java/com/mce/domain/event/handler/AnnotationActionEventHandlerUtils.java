 package com.mce.domain.event.handler;
 
 import com.mce.domain.event.DomainEventHandler;
 import com.mce.domain.event.DomainEventInterceptor;
 import com.mce.util.WClassUtil;
 import java.lang.reflect.Method;
 import java.util.ArrayList;
 import java.util.Collection;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;
 import java.util.Set;
 import org.apache.commons.logging.Log;
 import org.apache.commons.logging.LogFactory;
 
 public abstract class AnnotationActionEventHandlerUtils
 {
   private static final Log logger = LogFactory.getLog(AnnotationActionEventHandlerUtils.class);
 
   public static DomainEventHandler[] convert(List<Object> handlers, List<DomainEventInterceptor> interceptors)
   {
     Map rootEventHandlerMap = new HashMap();
     Map childrenCache = new HashMap();
     for (Iterator i$ = handlers.iterator(); i$.hasNext(); ) { Object bean = i$.next();
       Method[] ms = bean.getClass().getMethods();
       for (Method m : ms) {
         if (m.isAnnotationPresent(AutoEventHandler.class)) {
           AutoEventHandler aHandler = (AutoEventHandler)m.getAnnotation(AutoEventHandler.class);
           DomainEventHandler ah = convert(aHandler.name(), m, interceptors, bean);
           if (aHandler.isGrouping()) {
             List childList = (List)childrenCache.get(aHandler.name());
             if (childList == null) {
               childList = new ArrayList();
               childrenCache.put(aHandler.name(), childList);
             }
             childList.add(ah);
           }
           else {
             String[] ints = aHandler.interceptors();
             DomainEventInterceptor[] interceptorObs = null;
             if ((ints != null) && (ints.length > 0) && (!"".equalsIgnoreCase(ints[0])))
             {
               if ((interceptors == null) || (interceptors.size() < 1)) {
                 throw new IllegalArgumentException("found interceptor names from DomainEventHandler but Interceptor list is null");
               }
               interceptorObs = getInterceptors(aHandler, ints, interceptors);
             }
             DomainEventHandlerComposite aehc = new DomainEventHandlerComposite(aHandler.name(), ah, interceptorObs);
             rootEventHandlerMap.put(aHandler.name(), aehc);
           }
         }
       }
     }
     Set<String> keys = rootEventHandlerMap.keySet();
     for (String key : keys) {
       DomainEventHandlerComposite dehc = (DomainEventHandlerComposite)rootEventHandlerMap.get(key);
       List deh = (List)childrenCache.get(key);
       if (deh != null) {
         dehc.addChildren(deh);
         childrenCache.remove(key);
       }
     }
     if (!childrenCache.isEmpty()) {
       Set<String> childKey = childrenCache.keySet();
       for (String key : childKey) {
         logger.warn("RootKey[" + key + "] not found");
         List<DomainEventHandler> del = (List)childrenCache.get(key);
         for (DomainEventHandler dd : del) {
           logger.error("DomainEventHandller[" + dd.toString() + "]");
         }
       }
 
       throw new IllegalArgumentException("The DomainEventHandler is not mached");
     }
     Collection del = rootEventHandlerMap.values();
     printResultDomainEventHandler(del);
     DomainEventHandler[] handleArray = new DomainEventHandler[del.size()];
     return (DomainEventHandler[])del.toArray(handleArray);
   }
 
   private static void printResultDomainEventHandler(Collection<DomainEventHandlerComposite> del)
   {
     StringBuilder sb = new StringBuilder();
     boolean isFirst = true;
     for (DomainEventHandler deh : del) {
       if (!isFirst) {
         sb.append("\n");
       }
       if (isFirst) {
         isFirst = false;
       }
       sb.append(deh.toString());
     }
 
     logger.info("All of DomainEventHandler[" + sb.toString() + "]");
   }
 
   public static DomainEventHandler[] convertSingle(Object bean, List<DomainEventInterceptor> interceptors) {
     List handles = new ArrayList();
     handles.add(bean);
     return convert(handles, interceptors);
   }
 
   public static DomainEventHandler[] convertSaS(Object bean, DomainEventInterceptor interceptor)
   {
     List handles = new ArrayList();
     handles.add(bean);
 
     List cis = new ArrayList();
     cis.add(interceptor);
 
     return convert(handles, cis);
   }
 
   public static DomainEventHandler[] convertSingle(Object bean)
   {
     return convertSingle(bean, null);
   }
 
   public static DomainEventHandler convert(String name, Method m, List<DomainEventInterceptor> interceptors, Object bean)
   {
     return new MethodAction(name, m, bean);
   }
 
   public static DomainEventInterceptor[] getInterceptors(AutoEventHandler aHandler, String[] ins, List<DomainEventInterceptor> interceptors)
   {
     List ii = new ArrayList();
     for (String name : ins) {
       DomainEventInterceptor in = findActionEventInterceptor(name, interceptors);
       if (in == null) {
         logger.error("not found[" + name + "] interceptor for Creat DomainEvent[" + aHandler.name() + "]");
         throw new IllegalArgumentException("not found [" + name + "] interceptors for Create DomainEvent[" + aHandler.name() + "]");
       }
       ii.add(in);
     }
     DomainEventInterceptor[] ss = new DomainEventInterceptor[ii.size()];
     return (DomainEventInterceptor[])ii.toArray(ss);
   }
 
   private static DomainEventInterceptor findActionEventInterceptor(String name, List<DomainEventInterceptor> interceptors)
   {
     for (DomainEventInterceptor iis : interceptors) {
       if (iis.getClass().getName().equalsIgnoreCase(name)) {
         return iis;
       }
     }
     return null;
   }
 
   public static List<DomainEventInterceptor> convertInterceptors(List<Object> handlers) {
     List del = new ArrayList();
     Map iMap = new HashMap();
     for (Iterator i$ = handlers.iterator(); i$.hasNext(); ) { Object bean = i$.next();
       Method[] ms = bean.getClass().getMethods();
       for (Method m : ms) {
         AutoEventHandler aHandler = (AutoEventHandler)m.getAnnotation(AutoEventHandler.class);
         if ((aHandler != null) && (!aHandler.isGrouping())) {
           String[] ints = aHandler.interceptors();
           if ((ints != null) && (ints.length > 0)) {
             for (String insname : ints) {
               DomainEventInterceptor dei = (DomainEventInterceptor)iMap.get(insname);
               if (dei == null) {
                 dei = (DomainEventInterceptor)WClassUtil.getInstance(insname);
               }
             }
           }
         }
       }
     }
     if (iMap.size() > 0) {
       Collection cdel = iMap.values();
       del.addAll(cdel);
     }
     return del;
   }
 }

