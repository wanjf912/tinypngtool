package org.rgtech.compress;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;

public class FileWorker
{
  public static LinkedList<File> getAllFiles(String dir, WorkerFileListener workerFileListener)
  {
    int fmtLength = 4;
    LinkedList<File> list = getDirectory(dir);
    
    Iterator<File> iter = list.iterator();
    while (iter.hasNext())
    {
      File file = (File)iter.next();
      String filename = file.getName();
      if ((filename.substring(filename.length() - fmtLength, filename.length()).equals(".png")) || 
        (filename.substring(filename.length() - fmtLength, filename.length()).equals(".jpg")))
      {
        if (workerFileListener != null) {
          if (workerFileListener.isContinue())
          {
            workerFileListener.onFindFile(file);
          }
          else
          {
            workerFileListener.onFindFinished();
            return list;
          }
        }
      }
      else {
        iter.remove();
      }
    }
    if (workerFileListener != null) {
      workerFileListener.onFindFinished();
    }
    return list;
  }
  
  public static double getFileLength(File file)
  {
    if ((file != null) && (file.exists()) && (file.isFile())) {
      return file.length();
    }
    return -1.0D;
  }
  
  public static long getFileLength(String name)
  {
    File file = new File(name);
    if ((file != null) && (file.exists()) && (file.isFile())) {
      return file.length();
    }
    return -1L;
  }
  
  public static LinkedList<File> getDirectory(String path)
  {
    File file = new File(path);
    
    LinkedList<File> Dirlist = new LinkedList();
    LinkedList<File> fileList = new LinkedList();
    GetOneDir(file, Dirlist, fileList);
    while (!Dirlist.isEmpty())
    {
      File tmp = (File)Dirlist.removeFirst();
      
      GetOneDir(tmp, Dirlist, fileList);
    }
    return fileList;
  }
  
  private static void GetOneDir(File file, LinkedList<File> Dirlist, LinkedList<File> fileList)
  {
    File[] files = file.listFiles();
    if ((files == null) || (files.length == 0)) {
      return;
    }
    for (File f : files) {
      if (f.isDirectory()) {
        Dirlist.add(f);
      } else {
        fileList.add(f);
      }
    }
  }
  
  public static String getRootPathFromFullPath(String path)
  {
    int lastPath = path.lastIndexOf('\\');
    if ((lastPath == path.length() - 1) || (lastPath == -1)) {
      return null;
    }
    return path.substring(0, lastPath);
  }
  
  public static String getFileNameFromFullPath(String path)
  {
    int lastPath = path.lastIndexOf('\\');
    if ((lastPath == path.length() - 1) || (lastPath == -1)) {
      return null;
    }
    return path.substring(lastPath + 1, path.length());
  }
  
  public static String getSubPathFromFullPath(String fullpath, String basepath)
  {
    int lastPath = fullpath.indexOf(basepath);
    if (lastPath == -1) {
      return null;
    }
    return fullpath.substring(lastPath + basepath.length(), fullpath.length());
  }
  
  public static void makeDirs(String path)
  {
    int pathPos = path.lastIndexOf('\\');
    String rootDirString = path.substring(0, pathPos);
    File file = new File(rootDirString);
    if (!file.exists()) {
      file.mkdirs();
    }
  }
}
