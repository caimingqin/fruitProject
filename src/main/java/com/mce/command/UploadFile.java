package com.mce.command;

public abstract interface UploadFile
{
  public abstract String getName();

  public abstract byte[] getUploadByte();

  public abstract void delete();
}

