package com.mce.command.servlet;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mce.command.Command;
import com.mce.command.CommandClassVisitor;
import com.mce.command.CommandContext;
import com.mce.command.CommandHandleException;
import com.mce.command.CommandHandler;
import com.mce.command.CommandInterceptor;
import com.mce.command.CommandVisitor;
import com.mce.command.servlet.convertor.DefaultHttpCommandConverterFactory;
import com.mce.core.ResponsibleMessage;
import com.mce.core.inject.BeanInjector;
import com.mce.core.inject.annotation.asm.AsmAnnotationBeanReader;
import com.mce.core.inject.servlet.McsFrameworkServlet;
import com.mce.core.inject.servlet.ServletContextUtils;
import com.mce.core.notification.LogNotifyBox;
import com.mce.core.notification.NotifyBox;

public abstract class CommandHandlerServlet extends McsFrameworkServlet
{
  private static final long serialVersionUID = 1L;
  private BeanInjector bi;
  private HttpCommandConverter httpCommandConverter;
  private CommandHandler cHandler;
  private List<CommandInterceptor> cInterceptors = null;
  private NotifyBox nBox;

  private CommandClassVisitor getAnnoationVisitor()
  {
    CommandClassVisitor ccv = (CommandClassVisitor)getBeanInjector().getBeanOnSafe(CommandClassVisitor.class);
    return ccv == null ? new CommandVisitor() : ccv;
  }

  protected final void preInit(ServletConfig sc)
  {
	//初始化开始  TODO
    getLogger().info("Start initCommandProcessor now");

    URL rootUrl = ServletContextUtils.getRoot(sc.getServletContext());

    CommandClassVisitor cv = getAnnoationVisitor();
    AsmAnnotationBeanReader aabr = new AsmAnnotationBeanReader(rootUrl, cv);
    aabr.build();

    this.bi = ServletContextUtils.getBeanInjectorFor(sc.getServletContext());
    initInterceptors();
    this.nBox = creaetNotifyBox(this.bi);

    this.httpCommandConverter = creaeHttpCommandConverter(this.bi, cv.getCommandMap());

    initConfig(sc);

    this.cHandler = getCommandHandler(this.cInterceptors);

    getLogger().info("End prepared CommandProcessor now");
  }

  protected void initConfig(ServletConfig sc) {
  }

  protected final BeanInjector getComponentInjector() {
    return this.bi;
  }

  private NotifyBox creaetNotifyBox(BeanInjector bi) {
    if ((bi != null) && (NotifyBox.class.isAssignableFrom(bi.getClass()))) {
      getLogger().warn("Find Notify BeanInjectorContext now");
      return (NotifyBox)bi;
    }
    getLogger().warn("not found Notify box BeanInjectorContext so set it LogNotifyBox");
    return LogNotifyBox.get();
  }

  public NotifyBox getBox()
  {
    return this.nBox;
  }

  private void initInterceptors()
  {
    this.cInterceptors = new ArrayList<CommandInterceptor>();
    String[] cis = getBeanInjector().getBeanNamesForType(CommandInterceptor.class);
    if ((cis != null) && (cis.length > 0)) {
      getLogger().info("Find CommandInterceptor[" + cis.length + "]");
      for (String ci : cis) {
        CommandInterceptor bean = (CommandInterceptor)getBeanInjector().getBean(ci);
        getLogger().info("Interceptor[" + bean.getClass().getName() + "]");
        this.cInterceptors.add(bean);
      }
    }
  }

  private HttpCommandConverter creaeHttpCommandConverter(BeanInjector bp, Map<String, Class<? extends Command>> cmdMaps)
  {
    HttpCommandConverter hccf = (HttpCommandConverter)bp.getBeanOnSafe(HttpCommandConverter.class);
    if (hccf == null) {
      getLogger().warn("not found HttpCommandConverterFactory from BeanFactory use Default");
      hccf = new DefaultHttpCommandConverterFactory(cmdMaps, bp);
    }
    return hccf;
  }

  protected CommandContext createCommandContext(HttpServletRequest req, HttpServletResponse res) {
    CommandContext cc = new CommandContext();
    cc.put(HttpServletRequest.class.getName(), req);
    cc.put(HttpServletResponse.class.getName(), res);

    return cc;
  }

  protected final ResponsibleMessage handleRequest(HttpServletRequest req, HttpServletResponse res)
    throws Exception
  {
    getLogger().debug("Start Handle Command now.");
    CommandContext cc = createCommandContext(req, res);
    Command eCommand = null;
    try {
      eCommand = getCommand(req, res);
      Object result = this.cHandler.handle(cc, eCommand);
      return createSuccessResponsibleMessage(result);
    }
    catch (CommandHandleException eh) {
      getLogger().trace(eh);
      return createErrorResponsibleMessage(eh.getErrorCode(), eh.getMessage());
    }
    catch (RuntimeException eh) {
      getLogger().trace(eh);
      return createErrorResponsibleMessage(500, eh.getMessage());
    }
    catch (Exception eh)
    {
      final ResponsibleMessage localResponsibleMessage;
      getLogger().trace(eh);
      return createErrorResponsibleMessage(500, eh.getMessage());
    }
    finally {
      cc.clear();
    }
  }

  protected Command getCommand(HttpServletRequest req, HttpServletResponse res) throws Exception
  {
    if (!this.httpCommandConverter.isSupport(req)) {
      throw new CommandHandleException(415, "Not support the httpCommandConvert[" + req.getContentType() + "]");
    }

    Command eCommand = this.httpCommandConverter.createCommand(req);
    getLogger().debug("Handle commands now");
    return eCommand;
  }

  protected abstract CommandHandler getCommandHandler(List<CommandInterceptor> paramList);
}




