package org.rgtech.compress;

import java.io.File;

public abstract interface WorkerFileListener
{
  public abstract void onFindFile(File paramFile);
  
  public abstract boolean isContinue();
  
  public abstract void onFindFinished();
}
