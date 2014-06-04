package com.mce.core.inject.annotation.asm;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.asm.ClassReader;

import com.mce.core.inject.BeanReader;
import com.mce.core.inject.ClassVisitor;
import com.mce.core.resource.ClassFileIterator;
import com.mce.core.resource.Filter;
import com.mce.core.resource.FilterImpl;
import com.mce.core.resource.JarFileIterator;
import com.mce.core.resource.ResourceIterator;

public class AsmAnnotationBeanReader
  implements BeanReader
{//TODO
  private URL rootUrl;
  private ClassVisitor cFilter;
  private static final Log logger = LogFactory.getLog(AsmAnnotationBeanReader.class.getName());
  public AsmAnnotationBeanReader(URL is, ClassVisitor af)
  {
    this.rootUrl = is;
    this.cFilter = af;
  }

  public void build()
  {
    try
    {
      ResourceIterator itr = getResourceIterator(this.rootUrl, new FilterImpl());
      if (itr != null) {
        InputStream is = null;
        while ((is = itr.next()) != null)
        {
          DataInputStream dstream = new DataInputStream(new BufferedInputStream(is));
          try {
            ClassMetadataReadingVisitor cmrv = new ClassMetadataReadingVisitor();//��Ҫ����
            ClassReader cf = new ClassReader(dstream);
            cf.accept(cmrv, true);
            this.cFilter.visit(cmrv);//TODO
          } finally {
            dstream.close();
            is.close();
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private ResourceIterator getResourceIterator(URL url, Filter filter) throws IOException
  {
    String urlString = url.toString();
    logger.info("urlString==================>>"+urlString);
    if (urlString.endsWith("!/")) {
      urlString = urlString.substring(4);
      urlString = urlString.substring(0, urlString.length() - 2);
      url = new URL(urlString);
    }

    if (!urlString.endsWith("/")) {
      return new JarFileIterator(url.openStream(), filter);
    }

    if (!url.getProtocol().equals("file")) {
      throw new IOException("Unable to understand protocol: " + url.getProtocol());
    }

    String filePath = URLDecoder.decode(url.getPath(), "UTF-8");
    File f = new File(filePath);
    if (!f.exists()) return null;

    if (f.isDirectory()) {
      return new ClassFileIterator(f, filter);
    }
    return new JarFileIterator(url.openStream(), filter);
  }
}


 
 
 