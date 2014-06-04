 package com.mce.command;
 
 import java.util.Iterator;
import java.util.Map;

import com.mce.core.UserSession;
import com.mce.domain.event.DomainEvent;
 
 public abstract class AbstractEventCommand extends AbstractCommand
 {
   private static final long serialVersionUID = 1L;
   private DomainEvent dEvent;
 
   public final Object execute()
   {
     DomainEventGather deg = new DomainEventGather();
     Object obs = execute(deg);
     DomainEvent de = deg.getDomainEvent();
     if (de == null) {
       throw new CommandHandleException(500, "DomainEvent is null");
     }
     this.dEvent = de;
     addContextParameter(this.dEvent);
     return obs;
   }
 
   public abstract Object execute(DomainEventGather paramDomainEventGather);
 
   protected void preDestory()
   {
     this.dEvent = null;
   }
 
   private final void addContextParameter(DomainEvent de)
   {
     Map<?, ?> contextParams = UserSession.instance().getAll();
     if ((contextParams != null) && (contextParams.size() > 0))
     {
       Iterator<?> ki = contextParams.keySet().iterator();
       while (ki.hasNext()) {
         String key = (String)ki.next();
         Object param = contextParams.get(key);
         if (param != null)
           de.addContextProperty(key, param);
       }
     }
   }
 
   public DomainEvent getDomainEvent()
   {
     return this.dEvent;
   }
 }




