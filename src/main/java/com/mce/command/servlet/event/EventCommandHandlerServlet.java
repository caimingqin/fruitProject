package com.mce.command.servlet.event;

import com.mce.command.CommandHandler;
import com.mce.command.CommandInterceptor;
import com.mce.command.event.DefaultCommandHandler;
import com.mce.command.servlet.CommandHandlerServlet;
import com.mce.domain.event.EventBuffer;
import com.mce.domain.event.bootstrap.DomainEventProcessorBootstrap;
import com.mce.util.StringUtils;
import java.util.List;
import javax.servlet.ServletConfig;

public class EventCommandHandlerServlet extends CommandHandlerServlet
{
  private static final long serialVersionUID = 1L;
  private DomainEventProcessorBootstrap epBootstrap;
  private static final String EVENT_PROCESSOR_CORE = "depCore";
  private EventBuffer eBuffer = null;

  protected void initConfig(ServletConfig sc)
  {
    this.epBootstrap = createEventProcessorBoostrap(sc);
    this.eBuffer = this.epBootstrap.start();
  }

  private DomainEventProcessorBootstrap createEventProcessorBoostrap(ServletConfig sc)
  {
    String scoreSzie = sc.getInitParameter(EVENT_PROCESSOR_CORE);
    return StringUtils.isNull(scoreSzie) ? new DomainEventProcessorBootstrap(getBeanInjector()) : new DomainEventProcessorBootstrap(Integer.parseInt(scoreSzie));
  }

  protected void preDestory()
  {
    this.epBootstrap.stop();
  }

  protected CommandHandler getCommandHandler(List<CommandInterceptor> inters)
  {
    return new DefaultCommandHandler(this.eBuffer, super.getBox(), inters);
  }
}




