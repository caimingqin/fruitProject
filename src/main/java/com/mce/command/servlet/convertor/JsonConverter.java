package com.mce.command.servlet.convertor;

import com.mce.command.Command;
import com.mce.command.CommandTranslate;
import java.io.InputStream;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonConverter extends AbstractCommandConvertor
{
  protected final Log logger = LogFactory.getLog(getClass());

  private final ObjectMapper oMapper = new ObjectMapper();

  public JsonConverter(Map<String, Class<? extends Command>> cmdMap)
  {
    super(cmdMap);
  }

  public Command convertJson(InputStream cmdIns, Class<? extends Command> cmdType)
    throws Exception
  {
    CommandTranslate ct = (CommandTranslate)this.oMapper.readValue(cmdIns, CommandTranslate.class);
    Command eCommand = (Command)this.oMapper.readValue(ct.getContents(), cmdType);
    return eCommand;
  }

  public boolean isSupport(HttpServletRequest req)
  {
    this.logger.debug("Request contents type[" + req.getContentType() + "]");
    String reqContents = req.getContentType().toLowerCase();
    boolean res = reqContents.startsWith("application/json");
    return res;
  }

  public Command createCommand(HttpServletRequest req) throws Exception
  {
    CommandTranslate ct = createCommandTranslate(req);
    Class<? extends Command> cmdType = findCommand(ct.getCommandName());
    if (cmdType == null) {
      throw new NullPointerException("not found CommandType[" + ct.getCommandName() + "]");
    }
    return (Command)this.oMapper.readValue(ct.getContents(), cmdType);
  }

  protected CommandTranslate createCommandTranslate(HttpServletRequest req)
    throws Exception
  {
    return (CommandTranslate)this.oMapper.readValue(req.getInputStream(), CommandTranslate.class);
  }
}




