package com.mce.command.servlet.convertor;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mce.command.Command;
import com.mce.command.servlet.HttpCommandConverter;

public abstract class AbstractCommandConvertor
  implements HttpCommandConverter
{
  private Map<String, Class<? extends Command>> cmdMap = null;
  protected Log logger = LogFactory.getLog(getClass().getName());

  public AbstractCommandConvertor(Map<String, Class<? extends Command>> cmdMap) {
    this.cmdMap = cmdMap;
  }

  protected Class<? extends Command> findCommand(String name) {
    return (Class)this.cmdMap.get(name);
  }
}




