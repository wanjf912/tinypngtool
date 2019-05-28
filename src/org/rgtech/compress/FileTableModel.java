package org.rgtech.compress;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class FileTableModel
  extends AbstractTableModel
{
  ArrayList<ArrayList<Object>> p = new ArrayList();
  ArrayList<Object> usedata = new ArrayList();
  String[] n = { "±àºÅ", "ÎÄ¼þ", "Ñ¹ËõÇ°(×Ö½Ú)", "Ñ¹Ëõºó(×Ö½Ú)", "Ñ¹Ëõ±È", "×´Ì¬" };
  
  public int getRowCount()
  {
    return this.p.size();
  }
  
  public int getColumnCount()
  {
    return this.n.length;
  }
  
  public Object getValueAt(int row, int col)
  {
    return ((ArrayList)this.p.get(row)).get(col);
  }
  
  public String getColumnName(int column)
  {
    return this.n[column];
  }
  
  public boolean isCellEditable(int rowIndex, int columnIndex)
  {
    return true;
  }
  
  public void setValueAt(Object value, int rowIndex, int columnIndex)
  {
    if (this.p.size() == 0) {
      this.p.add(new ArrayList());
    }
    if (this.p.size() < rowIndex + 1)
    {
      int n = this.p.size();
      for (int i = n - 1; i < rowIndex; i++) {
        this.p.add(new ArrayList());
      }
    }
    ArrayList<Object> pp = (ArrayList)this.p.get(rowIndex);
    if (pp.size() == 0) {
      pp.add(new Object());
    }
    if (pp.size() < columnIndex + 1)
    {
      int n = pp.size();
      for (int i = n - 1; i < columnIndex; i++) {
        pp.add(new Object());
      }
    }
    pp.set(columnIndex, value);
    
    fireTableCellUpdated(rowIndex, columnIndex);
  }
  
  public void removeRowAt(int rowIndex)
  {
    if (this.p.size() > rowIndex)
    {
      this.p.remove(rowIndex);
      for (int i = rowIndex; i < this.p.size(); i++)
      {
        ArrayList<Object> row = (ArrayList)this.p.get(i);
        int num = ((Integer)row.get(0)).intValue();
        row.set(0, Integer.valueOf(num - 1));
      }
      fireTableRowsDeleted(rowIndex, rowIndex);
    }
  }
  
  public void removeAllRow()
  {
    int cnt = this.p.size();
    this.p.clear();
    clearAllUserData();
    fireTableRowsDeleted(0, cnt - 1);
  }
  
  public ArrayList<ArrayList<Object>> getAllRowObjects()
  {
    return this.p;
  }
  
  public void setUserData(int row, Object data)
  {
    if (this.usedata.size() == 0) {
      this.usedata.add("");
    }
    if (this.usedata.size() < row + 1)
    {
      int n = this.usedata.size();
      for (int i = n - 1; i < row; i++) {
        this.usedata.add("");
      }
    }
    this.usedata.set(row, data);
  }
  
  public Object getUserData(int row)
  {
    if (row + 1 > this.usedata.size()) {
      return null;
    }
    return this.usedata.get(row);
  }
  
  public void clearAllUserData()
  {
    this.usedata.clear();
  }
}
