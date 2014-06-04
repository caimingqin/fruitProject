/*    */ package com.mce.command.servlet.convertor;
/*    */ 
/*    */ import com.mce.command.Command;
/*    */ import com.mce.command.CommandHandleException;
/*    */ import com.mce.command.CommandTranslate;
/*    */ import com.mce.util.StringUtils;
/*    */ import com.mce.util.jackson.JacksonUtils;
/*    */ import java.util.Map;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.apache.commons.logging.Log;
/*    */ 
/*    */ public class FormCommandConverter extends JsonConverter
/*    */ {
/*    */   private static final String qParmas = "qcmd";
/*    */   private static final String formChars = "application/x-www-form-urlencoded;";
/*    */   private static final String formCharss = "application/x-www-form-urlencoded";
/*    */ 
/*    */   public FormCommandConverter(Map<String, Class<? extends Command>> cmdMap)
/*    */   {
/* 21 */     super(cmdMap);
/*    */   }
/*    */ 
/*    */   public boolean isSupport(HttpServletRequest req)
/*    */   {
/* 26 */     String cType = req.getContentType().toLowerCase();
/* 27 */     this.logger.debug("Contents Type[" + cType + "]");
/* 28 */     boolean res = ("application/x-www-form-urlencoded;".equalsIgnoreCase(req.getContentType())) || ("application/x-www-form-urlencoded".equalsIgnoreCase(req.getContentType())) || (cType.indexOf("application/x-www-form-urlencoded;") > 0) || (cType.startsWith("application/x-www-form-urlencoded;"));
/*    */ 
/* 32 */     return res;
/*    */   }
/*    */ 
/*    */   protected CommandTranslate createCommandTranslate(HttpServletRequest req) throws Exception
/*    */   {
/* 37 */     String cmd = req.getParameter("qcmd");
/* 38 */     if (StringUtils.isNull(cmd)) {
/* 39 */       throw new CommandHandleException(500, "not found Query Command Parameter");
/*    */     }
/* 41 */     return (CommandTranslate)JacksonUtils.readBean(cmd, CommandTranslate.class);
/*    */   }
/*    */ }

/* Location:           E:\Zip2\mresouce\wmcore-0.6.9.jar
 * Qualified Name:     com.mce.command.servlet.convertor.FormCommandConverter
 * JD-Core Version:    0.6.2
 */