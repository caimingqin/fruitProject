 package com.mce.domain.event.config;
 
 import com.mce.core.inject.BeanReader;
 import com.mce.core.inject.ClassMetadata;
 import com.mce.core.inject.ClassVisitor;
 import com.mce.core.inject.annotation.asm.AsmAnnotationBeanReader;
 import com.mce.domain.event.handler.AutoEventHandler;
 import com.mce.util.WClassUtil;
 import java.net.URL;
 import java.util.ArrayList;
 import java.util.List;
import org.apache.commons.logging.Log;
 
 public class AnnotationDomainEventHandlerConfigurable extends AutowiredDomainEventhandlerConfigurable
 {
   public AnnotationDomainEventHandlerConfigurable()
   {
     this(Thread.currentThread().getContextClassLoader().getResource("/"));
   }
 
   public AnnotationDomainEventHandlerConfigurable(URL url) {
     super(url);
   }
 
   protected List<Object> getObjectHandlers()
   {
     getLogger().info("Current Path[" + getRootURL().toString() + "]");
     AutoEventHandlerBeanAnnotationVisitor a = new AutoEventHandlerBeanAnnotationVisitor();
     BeanReader ai = new AsmAnnotationBeanReader(getRootURL(), a);
     ai.build();
     return a.handlers;
   }
 
   public class AutoEventHandlerBeanAnnotationVisitor implements ClassVisitor
   {
     public List<Object> handlers = new ArrayList<Object>();
 
     public AutoEventHandlerBeanAnnotationVisitor() {
     }
 
     public void visit(ClassMetadata cmrv) {
       String[] anames = cmrv.getAnnotations();
       if (anames.length > 0)
         for (String name : anames)
           if (AutoEventHandler.class.getName().equalsIgnoreCase(name)) {
             Object obs = WClassUtil.getInstance(cmrv.getClassName());
             this.handlers.add(obs);
             break;
           }
     }
   }
 }


 
 
 