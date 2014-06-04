package com.mce.command;

public class UploadFileInterceptor
  implements CommandInterceptor
{
  public void before(CommandContext cc, Command cmd)
    throws Exception
  {
  }

  public void after(CommandContext cc, Command cmd)
    throws Exception
  {
    if (AbstractCommand.class.isAssignableFrom(cmd.getClass())) {
      AbstractCommand mc = (AbstractCommand)cmd;
      mc.clear();
    }
  }
}




