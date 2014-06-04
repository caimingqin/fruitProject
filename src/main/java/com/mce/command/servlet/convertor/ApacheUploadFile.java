 package com.mce.command.servlet.convertor;
 
 import com.mce.command.UploadFile;
 import org.apache.commons.fileupload.FileItem;
 
 public class ApacheUploadFile
   implements UploadFile
 {
   private boolean deleted;
   private FileItem fItem;
 
   public ApacheUploadFile(FileItem fi)
   {
     this.fItem = fi;
   }
 
   public String getName()
   {
     return this.fItem.getName();
   }
 
   public byte[] getUploadByte()
   {
     return this.fItem.get();
   }
 
   public void delete()
   {
     if (this.deleted)
       this.fItem.delete();
   }
 }

