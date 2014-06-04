 package com.mce.core;
 
 import java.io.Serializable;
 import java.util.HashMap;
 import java.util.Map;
 
 public class UserSession
   implements Serializable
 {
   private static final long serialVersionUID = 1L;
   private final ThreadLocal<Object> threadMap = new ThreadLocal();
   private static final UserSession userSession = new UserSession();
 
   public static UserSession instance()
   {
     return userSession;
   }
 
   public void put(String name, Object data)
   {
     Map map = (Map)this.threadMap.get();
     if (map == null) {
       map = new HashMap();
       this.threadMap.set(map);
     }
     map.put(name, data);
   }
 
   public Map<String, Object> getAll()
   {
     Map map = (Map)this.threadMap.get();
     if (map != null) {
       return map;
     }
     return null;
   }
 
   public void remove(String data)
   {
     Map map = (Map)this.threadMap.get();
     if (map != null)
       map.remove(data);
   }
 
   public void removeAll()
   {
     Map map = (Map)this.threadMap.get();
     if (map != null)
       map.clear();
   }
 
   public Object get(String sessionParam)
   {
     Map map = (Map)this.threadMap.get();
     if (map != null) {
       return map.get(sessionParam);
     }
     return null;
   }
 }




