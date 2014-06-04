package com.mce.command;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface AutoCommand
{
  public abstract String name();

//  public abstract String[] acceptRoles() ;
}

