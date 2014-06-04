package com.mce.command.servlet;

import com.mce.command.Command;
import javax.servlet.http.HttpServletRequest;

public abstract interface HttpCommandConverter
{
  public abstract Command createCommand(HttpServletRequest paramHttpServletRequest)
    throws Exception;

  public abstract boolean isSupport(HttpServletRequest paramHttpServletRequest);
}

