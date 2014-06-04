/*    */ package com.mce.command;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class CommandTranslate
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public static final String NAME = "commandTranslate";
/*    */   private String commandName;
/*    */   private String contents;
/*    */ 
/*    */   public String getCommandName()
/*    */   {
/* 13 */     return this.commandName;
/*    */   }
/*    */   public void setCommandName(String commandName) {
/* 16 */     this.commandName = commandName;
/*    */   }
/*    */   public String getContents() {
/* 19 */     return this.contents;
/*    */   }
/*    */   public void setContents(String contents) {
/* 22 */     this.contents = contents;
/*    */   }
/*    */ }

/* Location:           E:\Zip2\mresouce\wmcore-0.6.9.jar
 * Qualified Name:     com.mce.command.CommandTranslate
 * JD-Core Version:    0.6.2
 */