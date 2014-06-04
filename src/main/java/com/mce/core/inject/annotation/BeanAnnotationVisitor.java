package com.mce.core.inject.annotation;

import com.mce.core.inject.Bean;
import com.mce.core.inject.ClassMetadata;
import com.mce.core.inject.ClassVisitor;
import com.mce.core.inject.ConfigurableBeanInjector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ClassUtils;

public class BeanAnnotationVisitor
  implements ClassVisitor
{
  private ConfigurableBeanInjector cbi = null;
  private Log logger = LogFactory.getLog(getClass().getName());

  public BeanAnnotationVisitor(ConfigurableBeanInjector bi) { this.cbi = bi; }


  public void visit(ClassMetadata cmrv)
  {
    String[] anames = cmrv.getAnnotations();
    if (anames.length > 0)
      for (String name : anames)
        if (Bean.class.getName().equalsIgnoreCase(name))
          try
          {
            Class clazz = ClassUtils.forName(cmrv.getClassName(), null);
            Bean an = (Bean)clazz.getAnnotation(Bean.class);
            this.cbi.registClass(an.name(), clazz);
            this.logger.info("Regist Bean[" + an.name() + "] class[" + clazz + "]");
          }
          catch (ClassNotFoundException e) {
            e.printStackTrace();
          } catch (LinkageError e) {
            e.printStackTrace();
          }
  }
}




