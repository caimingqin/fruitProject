package com.mce.command;

import com.mce.core.inject.ClassVisitor;
import java.util.Map;

public abstract interface CommandClassVisitor extends ClassVisitor
{
  public abstract Map<String, Class<? extends Command>> getCommandMap();
}

