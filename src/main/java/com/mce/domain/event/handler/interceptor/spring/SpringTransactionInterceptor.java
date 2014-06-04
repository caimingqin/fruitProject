 package com.mce.domain.event.handler.interceptor.spring;
 
 import com.mce.domain.event.DomainEvent;
 import com.mce.domain.event.DomainEventContext;
 import com.mce.domain.event.DomainEventInterceptor;
 import com.mce.util.LoggerUtils;
 import org.apache.commons.logging.Log;
 import org.apache.commons.logging.LogFactory;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.transaction.PlatformTransactionManager;
 import org.springframework.transaction.TransactionException;
 import org.springframework.transaction.TransactionStatus;
 import org.springframework.transaction.TransactionSystemException;
 import org.springframework.transaction.support.DefaultTransactionDefinition;
 
 public class SpringTransactionInterceptor
   implements DomainEventInterceptor
 {
   private Log logger = LogFactory.getLog(getClass().getName());
   private PlatformTransactionManager transactionManager;
   private int transactionTimeout = 30;
 
   public SpringTransactionInterceptor() {
   }
   public SpringTransactionInterceptor(PlatformTransactionManager transactionManager) {
     this.transactionManager = transactionManager;
   }
 
   @Autowired
   public void setTransactionManager(PlatformTransactionManager ptm) {
     this.transactionManager = ptm;
   }
 
   public PlatformTransactionManager getTransactionManager() {
     return this.transactionManager;
   }
 
   public void afterHandle(DomainEventContext dec, DomainEvent de) {
     this.logger.info("After Transaction action start==================================>>");
     TransactionStatus status = (TransactionStatus)dec.get(TransactionStatus.class.getName());
     if (status == null) {
       this.logger.error("not set transaction manager");
       throw new IllegalArgumentException("not set transaction now");
     }
     Throwable tErrors = de.getError();
     if (tErrors != null) {
       this.logger.error("find error so roll back it");
       rollbackOnException(status, tErrors);
       LoggerUtils.showLog(tErrors, this.logger);
       return;
     }
     this.transactionManager.commit(status);
     this.logger.info("Commit transaction complited");
   }
 
   public void beforeHandle(DomainEventContext dec, DomainEvent de) {
     this.logger.info("Start transaction now");
     DefaultTransactionDefinition dtd = new DefaultTransactionDefinition();
     dtd.setTimeout(this.transactionTimeout);
     dtd.setPropagationBehavior(0);
     TransactionStatus status = this.transactionManager.getTransaction(dtd);
     dec.addProperty(TransactionStatus.class.getName(), status);
   }
 
   private void rollbackOnException(TransactionStatus status, Throwable ex) throws TransactionException {
     this.logger.debug("Initiating transaction rollback on application exception", ex);
     try {
       this.transactionManager.rollback(status);
     }
     catch (TransactionSystemException ex2) {
       this.logger.error("Application exception overridden by rollback exception", ex);
       ex2.initApplicationException(ex);
       throw ex2;
     }
     catch (RuntimeException ex2) {
       this.logger.error("Application exception overridden by rollback exception", ex);
       throw ex2;
     }
     catch (Error err) {
       this.logger.error("Application exception overridden by rollback error", ex);
       throw err;
     }
   }
 }




