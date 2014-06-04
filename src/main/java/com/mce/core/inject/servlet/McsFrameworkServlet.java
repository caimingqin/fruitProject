 package com.mce.core.inject.servlet;
 
 import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;

import com.mce.command.EMessage;
import com.mce.core.ContentsType;
import com.mce.core.ResponsibleMessage;
import com.mce.core.inject.BeanReader;
import com.mce.core.inject.ConfigurableBeanInjector;
import com.mce.core.inject.support.SpringBeanInjector;
import com.mce.core.inject.support.reader.XmlBeanReader;
import com.mce.util.LoggerUtils;
import com.mce.util.StringUtils;
 
 public abstract class McsFrameworkServlet extends HttpServlet
 {
   private static final long serialVersionUID = 1L;
   private ConfigurableBeanInjector injector;
   private Log logger = LogFactory.getLog(getClass().getName());
   private static final String CONFIG_LOCATION = "configLocation";
   private ObjectMapper om = new ObjectMapper();
 
   public void init(ServletConfig sc) throws ServletException
   {
     super.init(sc);
     this.injector = createInjector(sc);
     String cl = getFilePath(sc);
     if (!StringUtils.isNull(cl)) {
       URL fullPath = ServletContextUtils.getConfigURL(sc.getServletContext(), cl);
       //将配置文件的Bean加入Ioc容器管理
       BeanReader br = new XmlBeanReader(fullPath, this.injector);
       br.build();
     }
     preInit(sc);
 
     initConfig(sc);
   }
   protected void preInit(ServletConfig sc) {
   }
 
   protected void initConfig(ServletConfig sc) {
   }
 
   protected ConfigurableBeanInjector createInjector(ServletConfig sc) {
     return new SpringBeanInjector();
   }
 
   protected String getFilePath(ServletConfig sc) {
     return sc.getInitParameter("configLocation");
   }
   protected final Log getLogger() {
     return this.logger;
   }
   protected final ConfigurableBeanInjector getBeanInjector() { return this.injector; }
 
 
   protected final void doGet(HttpServletRequest req, HttpServletResponse resp)
     throws ServletException, IOException
   {
     doPost(req, resp);
   }
 
   protected final void doPost(HttpServletRequest req, HttpServletResponse resp)
     throws ServletException, IOException
   {
     service(req, resp);
   }
 
   protected final void service(HttpServletRequest req, HttpServletResponse resp)
     throws ServletException, IOException
   {
     resp.setCharacterEncoding("utf-8");
     try {
       ResponsibleMessage resm = handleRequest(req, resp);
       if (resm != null)
         writeMessage(resp, resm);
     }
     catch (RuntimeException e) {
       breakError(resp, e); } catch (Error e) {
       breakError(resp, e); } catch (Exception e) {
       breakError(resp, e);
     } catch (Throwable e) {
       breakError(resp, e);
     }
   }
 
   protected ResponsibleMessage handleRequest(HttpServletRequest req, HttpServletResponse resp)
     throws Exception
   {
     throw new UnsupportedOperationException("not support");
   }
 
   public final void destroy()
   {
     this.logger.warn("Destroying BeanProviders >>>>>>>>>>>>");
     try {
       super.destroy();
       preDestory();
     }
     finally {
       this.injector.destroyAll();
     }
   }
 
   protected void preDestory() {
   }
 
   public ResponsibleMessage createErrorResponsibleMessage(int errorCode, String msg) {
     ResponsibleMessage rm = new ResponsibleMessage();
     EMessage em = new EMessage(errorCode, msg);
     rm.setContents(em);
     rm.setStatusCode(200);
     return rm;
   }
 
   public ResponsibleMessage createSuccessResponsibleMessage(Object contents) {
     ResponsibleMessage rm = new ResponsibleMessage();
     rm.setStatusCode(200);
     rm.setContents(contents);
     rm.setContentsType(ContentsType.JSON);
     return rm;
   }
 
   protected void breakError(HttpServletResponse res, Throwable e) {
     LoggerUtils.showLog(e, this.logger);
     ResponsibleMessage rm = createErrorResponsibleMessage(503, e.getMessage());
 
     writeMessage(res, rm);
   }
 
   private void writeMessage(HttpServletResponse res, ResponsibleMessage rMessage) {
     try {
       String msg = this.om.writeValueAsString(rMessage);
       res.getWriter().write(msg);
     }
     catch (IOException e) {
       LoggerUtils.showLog(e, this.logger);
     }
   }
 }




