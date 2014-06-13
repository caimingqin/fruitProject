package com.mce.command;

import java.io.Serializable;

public abstract interface Command extends Serializable
{
  public static final String WRITE_TYPE = "WriteCommand";
  public static final String READY_TYPE = "ReadCommand";
  public static final String SUCCESS="200";
  public static final String ERROR="500";
  

  public abstract Object execute();
}
