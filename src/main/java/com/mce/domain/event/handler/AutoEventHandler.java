package com.mce.domain.event.handler;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.TYPE})
@Inherited
@Documented
public @interface AutoEventHandler
{
  public abstract String name() ;

//  public abstract String[] interceptors() ;
//
//  public abstract boolean isGrouping();
  
  public abstract String[] interceptors() default "";
  
  public abstract boolean isGrouping() default false ;
}

