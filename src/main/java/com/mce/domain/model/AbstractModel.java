 package com.mce.domain.model;
 
 import com.mce.util.IdUtils;

import java.io.Serializable;
import java.util.Date;
 
 public class AbstractModel
   implements Serializable
 {
   private static final long serialVersionUID = 1L;
   private String obsId;
   private Date created;
   private ModelStatus obsStatus;
 
   public AbstractModel()
   {
     this.obsId = IdUtils.getUUID();
     this.created = new Date();
     this.obsStatus = ModelStatus.active;
   }
 
   public String getObsId()
   {
     return this.obsId;
   }
 
   public Date getCreated() {
     return this.created;
   }
 
   public ModelStatus getObsStatus() {
     return this.obsStatus;
   }
 
   public static enum ModelStatus
   {
     active(1), locked(2), destroy(0);
 
     private int code;
 
     private ModelStatus(int code) { this.code = code; }
 
     public int getCode()
     {
       return this.code;
     }
 
     public String toString() {
       return this.code + "";
     }
 
     public static ModelStatus get(int code) {
       ModelStatus[] sArray = values();
       for (ModelStatus s : sArray) {
         if (s.getCode() == code) {
           return s;
         }
       }
       return null;
     }
   }
 }




