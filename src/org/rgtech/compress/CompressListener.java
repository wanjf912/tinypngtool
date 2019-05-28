package org.rgtech.compress;

public abstract interface CompressListener
{
  public abstract void onProgress(int paramInt, String paramString1, String paramString2);
  
  public abstract void onSuccess(int paramInt, String paramString1, String paramString2);
  
  public abstract void onError(int paramInt, String paramString1, String paramString2, String paramString3);
}

