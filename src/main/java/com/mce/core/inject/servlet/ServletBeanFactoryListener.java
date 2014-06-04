package com.mce.core.inject.servlet;

import com.mce.core.inject.BeanInjector;
import com.mce.core.inject.ConfigurableBeanInjector;
import com.mce.core.inject.annotation.BeanAnnotationVisitor;
import com.mce.core.inject.annotation.asm.AsmAnnotationBeanReader;
import com.mce.core.inject.context.BeanInjectorContext;
import com.mce.core.inject.context.NotifierBeanInjectorContext;
import com.mce.core.inject.support.SpringBeanInjector;
import com.mce.core.inject.support.reader.XmlBeanReader;
import com.mce.util.IdUtils;
import com.mce.util.StringUtils;

import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServletBeanFactoryListener
  implements ServletContextListener
{
  private String BEAN_CONFIG_LOCATION = "configLocation";
  private Log logger = LogFactory.getLog(getClass().getName());
  private String BeanInjectorContext_SessionId = IdUtils.getUUID();

  public void contextInitialized(ServletContextEvent arg0)
  {//TODO
    ServletContext sc = arg0.getServletContext();
    ConfigurableBeanInjector cbp = createInjector(sc);
    String filePath = arg0.getServletContext().getInitParameter(this.BEAN_CONFIG_LOCATION);
    this.logger.info("Start Create BeanInjector ");
    if (StringUtils.isNull(filePath)) {
      this.logger.warn("Can not be load Config file because not found ConfigLocation parameter");
    }
    else {
      buildXmlInjectComponent(filePath, sc, cbp);
    }
    buildAndInjectComponent(sc, cbp);
    NotifierBeanInjectorContext defbic = new NotifierBeanInjectorContext(cbp);
    defbic.start(this.BeanInjectorContext_SessionId);

    arg0.getServletContext().setAttribute(BeanInjector.class.getName(), defbic);
    this.logger.info("Creat BeanInjector success");
  }

  private void buildXmlInjectComponent(String filePath, ServletContext sc, ConfigurableBeanInjector cbp)
  {
    URL fullPath = ServletContextUtils.getConfigURL(sc, filePath);
    XmlBeanReader xbr = new XmlBeanReader(fullPath, cbp);
    xbr.build();
  }

  private ConfigurableBeanInjector createInjector(ServletContext sc) {
    return new SpringBeanInjector();
  }

  public void contextDestroyed(ServletContextEvent arg0)
  {
    BeanInjectorContext cbp = (BeanInjectorContext)arg0.getServletContext().getAttribute(BeanInjector.class.getName());

    if (cbp != null) {
      this.logger.warn("Destroy Parent BeanProvider");
      cbp.stop(this.BeanInjectorContext_SessionId);
    }
  }

  private void buildAndInjectComponent(ServletContext sc, ConfigurableBeanInjector cbp) {
    URL url = ServletContextUtils.getRoot(sc);//TODO
    AsmAnnotationBeanReader asb = new AsmAnnotationBeanReader(url, new BeanAnnotationVisitor(cbp));
    asb.build();
  }
}




