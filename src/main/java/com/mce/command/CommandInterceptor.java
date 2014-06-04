package com.mce.command;

public abstract interface CommandInterceptor
{
  public abstract void before(CommandContext paramCommandContext, Command paramCommand)
    throws Exception;

  public abstract void after(CommandContext paramCommandContext, Command paramCommand)
    throws Exception;
}

