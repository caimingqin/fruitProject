package com.mce.core.inject;

public abstract interface ClassMetadata
{
  public abstract String getClassName();

  public abstract boolean isInterface();

  public abstract boolean isAbstract();

  public abstract boolean isConcrete();

  public abstract boolean isFinal();

  public abstract boolean isIndependent();

  public abstract boolean hasEnclosingClass();

  public abstract String getEnclosingClassName();

  public abstract boolean hasSuperClass();

  public abstract String getSuperClassName();

  public abstract String[] getInterfaceNames();

  public abstract String[] getMemberClassNames();

  public abstract String[] getAnnotations();
}

