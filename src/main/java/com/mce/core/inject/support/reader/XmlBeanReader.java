package com.mce.core.inject.support.reader;

import com.mce.core.inject.BeanReader;
import com.mce.core.inject.ConfigurableBeanInjector;
import com.mce.core.inject.support.SpringBeanInjector;
import java.net.URL;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.Assert;

public class XmlBeanReader
  implements BeanReader
{
  private Resource resource;
  private XmlBeanDefinitionReader beanDefinitionReader;

  public XmlBeanReader(URL fullPath, ConfigurableBeanInjector cbp)
  {
    Assert.notNull(fullPath, "Reader path required");
    Assert.notNull(cbp, "ConfigurableBeanInjector required");
    this.resource = new UrlResource(fullPath);
    SpringBeanInjector sxbp = (SpringBeanInjector)cbp;
    this.beanDefinitionReader = new XmlBeanDefinitionReader(sxbp.getBeanFactory());
  }

  public void build()
  {
    this.beanDefinitionReader.loadBeanDefinitions(this.resource);
  }
}




