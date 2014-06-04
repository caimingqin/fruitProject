 package com.mce.command.servlet.convertor;
 
 import com.mce.command.Command;
 import com.mce.command.servlet.HttpCommandConverter;
 import com.mce.core.inject.BeanInjector;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.Map;
 import javax.servlet.http.HttpServletRequest;
 
 public class DefaultHttpCommandConverterFactory
   implements HttpCommandConverter
 {
   private List<HttpCommandConverter> converters = new ArrayList<HttpCommandConverter>();
   private BeanInjector beanInjector = null;
 
   public DefaultHttpCommandConverterFactory(Map<String, Class<? extends Command>> cmdMaps, BeanInjector bi) {
     this.beanInjector = bi;
     this.converters.add(new JsonConverter(cmdMaps));
     this.converters.add(new ApachCommonsUploadConvertor(cmdMaps));
     this.converters.add(new FormCommandConverter(cmdMaps));
   }
 
   public Command createCommand(HttpServletRequest req)
     throws Exception
   {
     for (HttpCommandConverter hcc : this.converters) {
       if (hcc.isSupport(req)) {
         Command command = hcc.createCommand(req);
         if (this.beanInjector != null) {
           this.beanInjector.inject(command);
           return command;
         }
       }
     }
     throw new UnsupportedOperationException("not suport this Type[" + req.getContentType() + "]");
   }
 
   public boolean isSupport(HttpServletRequest req)
   {
     for (HttpCommandConverter hcc : this.converters) {
       if (hcc.isSupport(req)) {
         return true;
       }
     }
     return false;
   }
 }

