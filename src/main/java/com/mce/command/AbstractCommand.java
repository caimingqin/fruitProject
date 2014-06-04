 package com.mce.command;
 
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import org.apache.commons.logging.Log;
 import org.apache.commons.logging.LogFactory;
 
 public abstract class AbstractCommand
   implements Command
 {
   private static final long serialVersionUID = 5444677509816224889L;
   private boolean inited = false;
   private List<UploadFile> uploadFiles;
   private Map<String, String> contextParams = new HashMap();
   private Log logger = LogFactory.getLog(getClass().getName());
 
   public void init(List<UploadFile> uFiles)
   {
     synchronized (this) {
       if (this.inited) {
         throw new IllegalArgumentException("was inited");
       }
       this.inited = true;
       this.uploadFiles = uFiles;
     }
   }
 
   public List<UploadFile> getUploadFiles() {
     return this.uploadFiles;
   }
 
   public void addContextParams(String text, String value) {
     this.contextParams.put(text, value);
   }
 
   public String getContextParam(String key) {
     return (String)this.contextParams.get(key);
   }
   protected void preDestory() {
   }
 
   void clear() {
     this.inited = false;
     this.contextParams.clear();
     try
     {
       preDestory();
     }
     finally {
       clearUploadFiles();
     }
   }
 
   private void clearUploadFiles()
   {
     if ((this.uploadFiles != null) && (this.uploadFiles.size() > 0))
       for (UploadFile uf : this.uploadFiles)
         try {
           uf.delete();
         }
         catch (RuntimeException re) {
           this.logger.trace(re);
         }
         catch (Exception e) {
           this.logger.trace(e);
         }
         catch (Throwable t) {
           this.logger.error(t.getMessage(), t);
           if (t.getCause() != null)
             this.logger.error(t.getCause().getMessage());
         }
   }
 }

