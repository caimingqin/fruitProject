package com.mce.command;

import java.io.Serializable;

public abstract interface Command extends Serializable
{
  public static final String WRITE_TYPE = "WriteCommand";
  public static final String READY_TYPE = "ReadCommand";

  public abstract Object execute();
}
