package org.rgtech.compress;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocalNetUtil
{
  public static String getMacAddress(String ip)
  {
    String macAddress = "";
    macAddress = getMacInWindows(ip).trim();
    if ((macAddress == null) || ("".equals(macAddress))) {
      macAddress = getMacInLinux(ip).trim();
    }
    return macAddress;
  }
  
  public static String getMacInLinux(String ip)
  {
    String result = "";
    String[] cmd = { "/bin/sh", "-c", "ping " + ip + " -c 2 && arp -a" };
    String cmdResult = callCmd(cmd);
    result = filterMacAddress(ip, cmdResult, ":");
    
    return result;
  }
  
  public static String callCmd(String[] cmd)
  {
    String result = "";
    
    String line = "";
    try
    {
      Process proc = Runtime.getRuntime().exec(cmd);
      
      InputStreamReader is = new InputStreamReader(proc.getInputStream());
      
      BufferedReader br = new BufferedReader(is);
      while ((line = br.readLine()) != null) {
        result = result + line;
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return result;
  }
  
  public static String callCmd(String[] cmd, String[] another)
  {
    String result = "";
    
    String line = "";
    try
    {
      Runtime rt = Runtime.getRuntime();
      
      Process proc = rt.exec(cmd);
      
      proc.waitFor();
      
      proc = rt.exec(another);
      
      InputStreamReader is = new InputStreamReader(proc.getInputStream());
      
      BufferedReader br = new BufferedReader(is);
      while ((line = br.readLine()) != null) {
        result = result + line;
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return result;
  }
  
  public static String filterMacAddress(String ip, String sourceString, String macSeparator)
  {
    String result = "";
    
    String regExp = "((([0-9,A-F,a-f]{1,2}" + macSeparator + 
      "){1,5})[0-9,A-F,a-f]{1,2})";
    
    Pattern pattern = Pattern.compile(regExp);
    
    Matcher matcher = pattern.matcher(sourceString);
    while (matcher.find())
    {
      result = matcher.group(1);
      if (sourceString.indexOf(ip) <= sourceString.lastIndexOf(matcher
        .group(1))) {
        break;
      }
    }
    return result;
  }
  
  public static String getMacInWindows(String ip)
  {
    String result = "";
    
    String[] cmd = {
    
      "cmd", 
      
      "/c", 
      
      "ping " + ip };
    


    String[] another = {
    
      "cmd", 
      
      "/c", 
      
      "arp -a" };
    


    String cmdResult = callCmd(cmd, another);
    


    result = filterMacAddress(ip, cmdResult, "-");
    
    return result;
  }
}
