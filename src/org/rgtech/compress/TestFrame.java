package org.rgtech.compress;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class TestFrame
  extends JFrame
{
  private JPanel contentPane;
  private JTable table;
  
  public static void main(String[] args)
  {
    EventQueue.invokeLater(new Runnable()
    {
      public void run()
      {
        try
        {
          TestFrame frame = new TestFrame();
          frame.setVisible(true);
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
    });
  }
  
  public TestFrame()
  {
    setDefaultCloseOperation(3);
    setBounds(100, 100, 450, 300);
    this.contentPane = new JPanel();
    this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    this.contentPane.setLayout(new BorderLayout(0, 0));
    setContentPane(this.contentPane);
    
    this.table = new JTable();
    this.table.setModel(new DefaultTableModel(
      new Object[][] {
      { "11", "11" }, 
      { "22" }, 
      new Object[2], 
      new Object[2] }, 
      
      new String[] {
      "New column", "New column" }));
    

    this.contentPane.add(this.table, "Center");
  }
}
