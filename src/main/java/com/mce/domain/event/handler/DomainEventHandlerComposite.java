 package com.mce.domain.event.handler;
 
 import com.mce.domain.event.DomainEvent;
 import com.mce.domain.event.DomainEventHandler;
 import com.mce.domain.event.DomainEventInterceptor;
 import java.util.ArrayList;
 import java.util.List;
 
 public class DomainEventHandlerComposite extends AbstractDomainEventHandler
 {
   private List<DomainEventHandler> ael = new ArrayList();
   private DomainEventHandler targetHandler;
   private DomainEventInterceptor[] interceptors;
 
   public DomainEventHandlerComposite(String name, DomainEventHandler ah, DomainEventInterceptor[] interceptors)
   {
     super(name);
     this.interceptors = interceptors;
     this.targetHandler = ah;
   }
 
   protected void processEvent(DomainEvent ae)
   {
     this.targetHandler.handle(ae);
     for (DomainEventHandler handler : this.ael)
       handler.handle(ae);
   }
 
   public int getChildrenSize()
   {
     return this.ael.size();
   }
 
   public List<DomainEventHandler> getChildren() {
     return this.ael;
   }
 
   public void addChild(DomainEventHandler ch) {
     this.ael.add(ch);
   }
 
   public void addChildren(List<DomainEventHandler> deh) {
     for (DomainEventHandler de : deh)
       addChild(de);
   }
 
   public String toString()
   {
     StringBuffer sb = new StringBuffer(super.toString());
     sb.append("&");
     if (this.ael.size() > 0) {
       sb.append("\n[Child[");
       for (DomainEventHandler aeh : this.ael) {
         sb.append("{" + aeh.toString() + "}");
       }
       sb.append("]");
     }
     return sb.toString();
   }
 
   public DomainEventInterceptor[] getInterceptors()
   {
     return this.interceptors;
   }
 }




