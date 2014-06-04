 package com.mce.core.inject.support;
 
 import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.QualifierAnnotationAutowireCandidateResolver;
import org.springframework.beans.factory.annotation.RequiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

import com.mce.core.inject.ConfigurableBeanInjector;
 
 public class SpringBeanInjector
   implements ConfigurableBeanInjector
 {
   private DefaultListableBeanFactory cBeanFactory;
   private AutowiredAnnotationBeanPostProcessor autowireBeanPoster = null;
   private final Log logger = LogFactory.getLog(getClass().getName());
   private final AtomicBoolean closed = new AtomicBoolean(false);
   public static final String REF = "ref";
   public static final String CONSTRUCTOR = "constructor";
 
   public SpringBeanInjector()
   {
     initPostProcessor();
   }
 
   private void initPostProcessor()
   {
     this.cBeanFactory = new DefaultListableBeanFactory();
     this.autowireBeanPoster = new AutowiredAnnotationBeanPostProcessor();
     this.autowireBeanPoster.setBeanFactory(this.cBeanFactory);
     this.cBeanFactory.addBeanPostProcessor(this.autowireBeanPoster);
     this.cBeanFactory.setAutowireCandidateResolver(new QualifierAnnotationAutowireCandidateResolver());
     this.cBeanFactory.addBeanPostProcessor(new RequiredAnnotationBeanPostProcessor());
 
     String[] pps = this.cBeanFactory.getBeanNamesForType(PropertyPlaceholderConfigurer.class);
     if ((pps != null) && (pps.length > 0)) {
       PropertyPlaceholderConfigurer ppc = (PropertyPlaceholderConfigurer)this.cBeanFactory.getBean(pps[0]);
       ppc.postProcessBeanFactory(this.cBeanFactory);
     }
   }
 
   public <T> T getBean(Class<T> type)
   {
     return this.cBeanFactory.getBean(type);
   }
 
   public Object getBean(String name)
   {
     return this.cBeanFactory.getBean(name);
   }
 
   public void registBean(String name, Object bean)
   {
     this.logger.info("Regist bean[" + name + "]");
     this.cBeanFactory.registerSingleton(name, bean);
   }
 
   public void registClass(String key, Class<?> obs)
   {
     RootBeanDefinition rbd = new RootBeanDefinition(obs);
     rbd.setScope("singleton");
     this.cBeanFactory.registerBeanDefinition(key, rbd);
   }
 
   public void registClass(String name, Class<?> clazz, Map<String, Object> omap)
   {
     MutablePropertyValues mpv = new MutablePropertyValues();
     Map<String, Object> consParams = new HashMap<String, Object>();
     Set<String> keys = omap.keySet();
     for (String key : keys) {
       String value = (String)omap.get(key);
       Object tValue = getValue(value);
       if (key.startsWith("constructor")) {
         consParams.put(key, tValue);
       }
       else {
         mpv.add(key, tValue);
       }
     }
     RootBeanDefinition rbd = new RootBeanDefinition(clazz);
     rbd.setScope("singleton");
     rbd.setPropertyValues(mpv);
     if (!consParams.isEmpty()) {
       Set<String> cKeys = consParams.keySet();
       for (String ck : cKeys) {
         Object tValue = consParams.get(ck);
         int prix = ck.indexOf("-");
         if (prix < 1) {
           prix = ck.indexOf("_");
         }
         if (prix > 0) {
           int conIndex = Integer.parseInt(ck.substring(prix + 1));
           rbd.getConstructorArgumentValues().addIndexedArgumentValue(conIndex, tValue);
         }
         else {
           rbd.getConstructorArgumentValues().addGenericArgumentValue(tValue);
         }
       }
     }
     this.cBeanFactory.registerBeanDefinition(name, rbd);
   }
 
   private Object getValue(String value)
   {
     if (value.startsWith("ref")) {
       int sIndex = value.indexOf("-") + 1;
       String realValue = value.substring(sIndex);
       return new RuntimeBeanReference(realValue);
     }
     return value;
   }
 
   public void destroyAll() {
     if (!this.closed.compareAndSet(false, true)) {
       this.logger.warn("Destroy Starting>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
       this.cBeanFactory.destroySingletons();
     }
   }
 
   public void inject(Object pt)
   {
     Validate.notNull(pt, "Can not Inject null object");
     this.autowireBeanPoster.processInjection(pt);
   }
 
   public String[] getBeanNamesForType(Class<?> class1)
   {
     return this.cBeanFactory.getBeanNamesForType(class1);
   }
 
   public BeanDefinitionRegistry getBeanFactory() {
     return this.cBeanFactory;
   }
 
   public <T> T getBeanOnSafe(Class<T> type)
   {
     String[] names = this.cBeanFactory.getBeanNamesForType(type);
     if ((names == null) || (names.length < 1)) {
       return null;
     }
 
     if (names.length > 1) {
       int count = names.length;
       throw new IllegalArgumentException("No unique bean of type [com.mce.core.bean.TestABean] is defined: expected single bean but found " + count);
     }
 
     return this.cBeanFactory.getBean(type);
   }
 
   public String[] getBeanNames()
   {
     return this.cBeanFactory.getBeanDefinitionNames();
   }
 }




