 package com.mce.command;
 
 import com.mce.domain.event.DomainEvent;
 
 public final class DomainEventGather
 {
   private DomainEvent domainEvent;
 
   public DomainEvent getDomainEvent()
   {
     return this.domainEvent;
   }
   public void setDomainEvent(DomainEvent domainEvent) {
     this.domainEvent = domainEvent;
   }
 }




