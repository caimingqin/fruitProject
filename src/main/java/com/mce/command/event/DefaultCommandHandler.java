package com.mce.command.event;

import com.mce.command.AbstractEventCommand;
import com.mce.command.Command;
import com.mce.command.CommandHandleException;
import com.mce.command.CommandHandler;
import com.mce.command.CommandInterceptor;
import com.mce.core.notification.Notification;
import com.mce.core.notification.NotifyBox;
import com.mce.domain.event.DomainEvent;
import com.mce.domain.event.EventBuffer;
import java.util.List;
import org.apache.commons.lang.Validate;

public class DefaultCommandHandler extends CommandHandler
{
  private EventBuffer eBuffer = null;
  private NotifyBox nBox;

  public DefaultCommandHandler(EventBuffer eb, NotifyBox nBox)
  {
    this(eb, nBox, null);
  }

  public DefaultCommandHandler(EventBuffer eBuffer, NotifyBox nBox, List<CommandInterceptor> cInterceptors)
  {
    super(cInterceptors);

    Validate.notNull(eBuffer, "EventBuffer required");
    Validate.notNull(nBox, "NotifyBox required");
    this.nBox = nBox;
    this.eBuffer = eBuffer;
  }

  protected Object handleCommand(Command command)
  {
    Object returnObs = command.execute();
    if (AbstractEventCommand.class.isAssignableFrom(command.getClass())) {
      AbstractEventCommand nbb = (AbstractEventCommand)command;
      DomainEvent de = nbb.getDomainEvent();
      boolean eStatus = this.eBuffer.push(de);
      if (!eStatus) {
        throw new CommandHandleException(500, "failed Push DomainEvent to EventBuffer");
      }
      Notification ntf = new Notification("eventHandledNotification", de);
      this.nBox.add(ntf);
    }
    return returnObs;
  }
}


 
 
 