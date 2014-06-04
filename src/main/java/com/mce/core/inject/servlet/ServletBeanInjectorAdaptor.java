 package com.mce.core.inject.servlet;
 
 import com.mce.core.inject.BeanInjector;
 import javax.servlet.ServletContext;
 import org.apache.commons.logging.Log;
 import org.apache.commons.logging.LogFactory;
 import org.springframework.beans.factory.annotation.Autowired;
 
 public class ServletBeanInjectorAdaptor
   implements BeanInjector
 {
   private BeanInjector bp;
   private String name = null;
   private Log logger = LogFactory.getLog(getClass().getName());
 
   public ServletBeanInjectorAdaptor() {
     this(null);
   }
 
   @Autowired(required=false)
   public ServletBeanInjectorAdaptor(String name) {
     this.name = name;
   }
 
   @Autowired
   public void setServletContext(ServletContext sc) {
     BeanInjector sbp = null;
     if (this.name != null) {
       this.logger.info("Set [" + this.name + "]BeanProvider now");
       sbp = (BeanInjector)sc.getAttribute(this.name);
       if (sbp == null) {
         throw new IllegalArgumentException("can not get BeanProvider[" + this.name + "]");
       }
       this.bp = sbp;
     }
     else {
       this.logger.info("Set BeanProvider now");
       this.bp = ((BeanInjector)sc.getAttribute(BeanInjector.class.getName()));
     }
   }
 
   public <T> T getBean(Class<T> type)
   {
     return this.bp.getBean(type);
   }
 
   public Object getBean(String name)
   {
     return this.bp.getBean(name);
   }
 
   public String[] getBeanNamesForType(Class<?> class1)
   {
     return this.bp.getBeanNamesForType(class1);
   }
 
   public <T> T getBeanOnSafe(Class<T> type)
   {
     return this.bp.getBeanOnSafe(type);
   }
 
   public void inject(Object bean)
   {
     throw new UnsupportedOperationException("not support this method");
   }
 }




