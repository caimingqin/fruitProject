 package com.mce.command;
 
 import com.mce.core.inject.ClassMetadata;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ClassUtils;
 
 public class CommandVisitor
   implements CommandClassVisitor
 {
   private Map<String, Class<? extends Command>> cmdMaps = new HashMap<String, Class<? extends Command>>();
   private Log log=LogFactory.getLog(this.getClass().getName());
   public void visit(ClassMetadata cmrv)
   {
     for (String acmd : cmrv.getAnnotations())
       if (AutoCommand.class.getName().equalsIgnoreCase(acmd))
         try {
        	 
           Class c = ClassUtils.forName(cmrv.getClassName(), null);
           if (!Command.class.isAssignableFrom(c)) {
             throw new IllegalArgumentException("The class[" + cmrv.getClassName() + "] not implements Command interface");
           }
           
           AutoCommand ac = (AutoCommand)c.getAnnotation(AutoCommand.class);
           this.cmdMaps.put(ac.name(), c);
           log.info("[AutoCommand:"+ac.name()+":"+c+"]");
         } catch (ClassNotFoundException e) {
           e.printStackTrace();
         } catch (LinkageError e) {
           e.printStackTrace();
         }
   }
 
   public Map<String, Class<? extends Command>> getCommandMap()
   {
     return this.cmdMaps;
   }
 }




