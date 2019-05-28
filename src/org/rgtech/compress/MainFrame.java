package org.rgtech.compress;

import com.tinify.AccountException;
import com.tinify.ClientException;
import com.tinify.ConnectionException;
import com.tinify.ServerException;
import com.tinify.Source;
import com.tinify.Tinify;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.rgtech.compress.CheckApiKeyResultListener;
import org.rgtech.compress.CompressListener;
import org.rgtech.compress.FileTableModel;
import org.rgtech.compress.FileWorker;
import org.rgtech.compress.ImageViewer;
import org.rgtech.compress.LocalNetUtil;
import org.rgtech.compress.WorkerFileListener;

public class MainFrame extends JFrame
{
  private static final long serialVersionUID = 1L;
  private JPanel contentPane;
  private JButton compressButton;
  private JButton cancelButton;
  private JButton apkKeyCheckbutton;
  private JButton srcScanButton;
  private JButton destScanButton;
  private JTextField srcDirTextField;
  private JTextField destDirTextField;
  private JTextField apiKyeTextField;
  private JProgressBar progressBar;
  private FileTableModel tableModel;
  private JTable fileTable;
  private JTable table;
  private JLabel statusLabel;
  private JPanel panel_1;
  private JLabel compressCountLeft;
  private ArrayList<Thread> threadList = new ArrayList();
  private boolean bCompressDoing = false;
  private int nCompressFinished = 0;
  private HashMap<String, Long> fileDatas = new HashMap();
  private static final int MAX_COMPRESS_PERTIME = 500;
  private static final String LOCAL_MAC_ADDRESS = "38-ad-8e-33-b2-5c";
  private static final String LOCAL_IP_ADDRESS = "192.168.10.1";
  private String localNetMac = LocalNetUtil.getMacAddress("192.168.10.1");
  
  public static void main(String[] args)
  {
    EventQueue.invokeLater(new Runnable()
    {
      public void run()
      {
        try
        {
          MainFrame frame = new MainFrame();
          frame.setVisible(true);
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
    });
  }
  
  public MainFrame()
  {
    setBackground(SystemColor.activeCaption);
    setDefaultCloseOperation(3);
    setTitle("Tinypng Client Tool");
    setResizable(false);
    setBounds(100, 100, 1212, 808);
    this.contentPane = new JPanel();
    this.contentPane.setBackground(SystemColor.window);
    this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(this.contentPane);
    this.contentPane.setLayout(null);
    
    JPanel panel = new JPanel();
    panel.setBorder(new TitledBorder(null, "", 4, 2, null, null));
    panel.setBackground(SystemColor.window);
    panel.setBounds(15, 15, 1161, 133);
    this.contentPane.add(panel);
    panel.setLayout(null);
    
    JLabel lblNewLabel = new JLabel("压缩目录");
    lblNewLabel.setFont(new Font("宋体", 0, 20));
    lblNewLabel.setBounds(15, 55, 81, 21);
    panel.add(lblNewLabel);
    
    JLabel label = new JLabel("保存目录");
    label.setFont(new Font("宋体", 0, 20));
    label.setBounds(15, 91, 81, 21);
    panel.add(label);
    

    this.srcDirTextField = new JTextField();
    this.srcDirTextField.setToolTipText("文件的压缩路径");
    this.srcDirTextField.setFont(new Font("宋体", 0, 20));
    this.srcDirTextField.setEditable(false);
    this.srcDirTextField.setBounds(109, 52, 901, 27);
    panel.add(this.srcDirTextField);
    this.srcDirTextField.setColumns(10);
    
    this.srcScanButton = new JButton("选择");
    this.srcScanButton.setToolTipText("点击设置压缩路径");
    this.srcScanButton.setFont(new Font("宋体", 0, 20));
    this.srcScanButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        MainFrame.this.panel_1.setVisible(false);
        MainFrame.this.onScanBtnCliced(MainFrame.this.srcScanButton, e);
      }
    });
    this.srcScanButton.setBounds(1025, 51, 123, 29);
    panel.add(this.srcScanButton);
    

    this.destDirTextField = new JTextField();
    this.destDirTextField.setToolTipText("文件压缩后的保存路径");
    this.destDirTextField.setFont(new Font("宋体", 0, 20));
    this.destDirTextField.setEditable(false);
    this.destDirTextField.setColumns(10);
    this.destDirTextField.setBounds(109, 88, 901, 27);
    panel.add(this.destDirTextField);
    
    this.destScanButton = new JButton("选择");
    this.destScanButton.setToolTipText("点击设置保存路径");
    this.destScanButton.setFont(new Font("宋体", 0, 20));
    this.destScanButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        MainFrame.this.onScanBtnCliced(MainFrame.this.destScanButton, e);
      }
    });
    this.destScanButton.setBounds(1025, 87, 123, 29);
    panel.add(this.destScanButton);
    

    JLabel lblApiKey = new JLabel("API KEY");
    lblApiKey.setFont(new Font("宋体", 0, 20));
    lblApiKey.setBounds(15, 15, 81, 21);
    panel.add(lblApiKey);
    
    this.apiKyeTextField = new JTextField();
    this.apiKyeTextField.setToolTipText("每个API KEY每月免费压缩500张图片");
    this.apiKyeTextField.setFont(new Font("宋体", 0, 20));
    this.apiKyeTextField.setColumns(10);
    this.apiKyeTextField.setBounds(109, 12, 708, 27);
    panel.add(this.apiKyeTextField);
    

    this.apkKeyCheckbutton = new JButton("检测");
    this.apkKeyCheckbutton.setToolTipText("点击检测API KEY的有效性");
    this.apkKeyCheckbutton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        MainFrame.this.onApiKeyButtonClicked();
      }
    });
    this.apkKeyCheckbutton.setFont(new Font("宋体", 0, 20));
    this.apkKeyCheckbutton.setBounds(1025, 11, 123, 29);
    panel.add(this.apkKeyCheckbutton);
    
    this.compressCountLeft = new JLabel("");
    this.compressCountLeft.setFont(new Font("宋体", 0, 20));
    this.compressCountLeft.setBounds(832, 15, 178, 21);
    panel.add(this.compressCountLeft);
    

    JLabel progres = new JLabel("压缩进度");
    progres.setFont(new Font("宋体", 0, 20));
    progres.setBounds(25, 163, 81, 21);
    this.contentPane.add(progres);
    
    this.progressBar = new JProgressBar();
    this.progressBar.setForeground(Color.BLUE);
    this.progressBar.setBounds(126, 163, 1050, 21);
    this.contentPane.add(this.progressBar);
    

    this.compressButton = new JButton("压缩");
    this.compressButton.setToolTipText("点击开始压缩");
    this.compressButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        MainFrame.this.onStartCompressClicked();
      }
    });
    this.compressButton.setFont(new Font("宋体", 0, 20));
    this.compressButton.setEnabled(false);
    this.compressButton.setBounds(897, 681, 123, 29);
    this.contentPane.add(this.compressButton);
    

    this.cancelButton = new JButton("取消");
    this.cancelButton.setToolTipText("点击取消压缩");
    this.cancelButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        MainFrame.this.onCancelCompressClicked();
      }
    });
    this.cancelButton.setFont(new Font("宋体", 0, 20));
    this.cancelButton.setBounds(1053, 681, 123, 29);
    this.cancelButton.setEnabled(false);
    this.contentPane.add(this.cancelButton);
    
    JPanel tablePanel = new JPanel();
    tablePanel.setBackground(SystemColor.menu);
    tablePanel.setBounds(15, 199, 1161, 440);
    this.contentPane.add(tablePanel);
    tablePanel.setLayout(new GridLayout(1, 0, 0, 0));
    
    addFileTable(tablePanel);
    
    this.panel_1 = new JPanel();
    this.panel_1.setBorder(new BevelBorder(1, null, null, null, null));
    this.panel_1.setBackground(SystemColor.text);
    this.panel_1.setBounds(-12, 717, 1228, 52);
    this.contentPane.add(this.panel_1);
    this.panel_1.setLayout(null);
    
    this.statusLabel = new JLabel("文件：");
    this.statusLabel.setForeground(Color.RED);
    this.statusLabel.setFont(new Font("宋体", 0, 18));
    this.statusLabel.setBounds(15, 11, 1048, 41);
    this.panel_1.add(this.statusLabel);
    this.panel_1.setVisible(false);
  }
  
  private void onScanBtnCliced(JButton btn, ActionEvent e)
  {
    JTextField txtField = null;
    int openType = 1;
    if (btn == this.destScanButton)
    {
      txtField = this.destDirTextField;
      openType = 2;
    }
    else
    {
      txtField = this.srcDirTextField;
      openType = 1;
    }
    final File file = selectFilesAndDir(openType);
    if ((file != null) && (!file.getAbsolutePath().equals("")))
    {
      txtField.setText(file.getAbsolutePath());
      if (btn == this.destScanButton) {
        return;
      }
      this.panel_1.setVisible(true);
      this.srcScanButton.setEnabled(false);
      clearFileTable();
      
      new Thread(new Runnable()
      {
        int count = 0;
        
        public void run()
        {
          LinkedList<File> fileList = FileWorker.getAllFiles(file.getAbsolutePath(), new WorkerFileListener()
          {
            public void onFindFile(final File file)
            {
              try
              {
                Thread.sleep(10L);
              }
              catch (InterruptedException e)
              {
                e.printStackTrace();
              }
              
              count += 1;
              
              EventQueue.invokeLater(new Runnable()
              {
                public void run()
                {
                  if (count > 500)
                  {
                    JOptionPane.showMessageDialog(MainFrame.this, "一次最多只可以压缩500张图片！", "提示", 1);
                    return;
                  }
                  MainFrame.this.statusLabel.setText("文件：" + file.getAbsolutePath());
                  FileTableModel tableModel = (FileTableModel)MainFrame.this.fileTable.getModel();
                  int cnt = MainFrame.this.fileTable.getRowCount();
                  
                  tableModel.setValueAt(Integer.valueOf(cnt + 1), cnt, 0);
                  tableModel.setValueAt(file.getAbsolutePath(), cnt, 1);
                  tableModel.setValueAt(String.format("%d", (long)FileWorker.getFileLength(file)), cnt, 2);
                  tableModel.setValueAt("--", cnt, 3);
                  tableModel.setValueAt("--", cnt, 4);
                  tableModel.setValueAt("未处理", cnt, 5);
                  MainFrame.this.fileTable.revalidate();
                  
                  MainFrame.this.fileDatas.put(file.getAbsolutePath(), Long.valueOf(FileWorker.getFileLength(file.getAbsolutePath())));
                }
              });
            }
            
            public void onFindFinished()
            {
              EventQueue.invokeLater(new Runnable()
              {
                public void run()
                {
                  MainFrame.this.statusLabel.setText("文件导入完成！共计：" + count + "张");
                  MainFrame.this.compressButton.setEnabled(true);
                  MainFrame.this.srcScanButton.setEnabled(true);
                }
              });
            }
            
            public boolean isContinue()
            {
              return count < 500;
            }
          });
        }
      }).start();
    }
  }
  
  public File selectFilesAndDir(int type)
  {
    JFileChooser jfc = new JFileChooser();
    
    FileSystemView fsv = FileSystemView.getFileSystemView();
    jfc.setCurrentDirectory(fsv.getHomeDirectory());
    
    jfc.setFileSelectionMode(1);
    
    int ret = jfc.showDialog(new JLabel(), type == 1 ? "选择压缩目录" : "选择保存目录");
    if (ret == 0)
    {
      File file = jfc.getSelectedFile();
      return file;
    }
    return null;
  }
  
  public void onApiKeyButtonClicked()
  {
    String keyString = this.apiKyeTextField.getText();
    if (keyString.isEmpty())
    {
      JOptionPane.showMessageDialog(this, "请输入正确的API KEY", "提示", 2);
      return;
    }
    this.compressCountLeft.setText("验证中...");
    checkApiKeyValid(keyString, null);
  }
  
  public void addFileTable(JPanel panel)
  {
    if (panel != null)
    {
      this.tableModel = new FileTableModel();
      this.fileTable = new JTable(this.tableModel)
      {
        private static final long serialVersionUID = 1L;
        
        public boolean isCellEditable(int row, int column)
        {
          return false;
        }
      };
      this.fileTable.addMouseListener(new MouseAdapter()
      {
        JPopupMenu menu = null;
        
        public void mouseClicked(MouseEvent arg0)
        {
          int row = ((JTable)arg0.getSource()).rowAtPoint(arg0.getPoint());
          MainFrame.this.fileTable.setRowSelectionInterval(row, row);
          if (this.menu != null)
          {
            MainFrame.this.fileTable.remove(this.menu);
            this.menu = null;
          }
          if (arg0.getButton() == 3)
          {
            this.menu = MainFrame.this.createPopupMenu(row);
            this.menu.show(MainFrame.this.fileTable, arg0.getX(), arg0.getY());
          }
        }
      });
      this.fileTable.setRowHeight(28);
      this.fileTable.setFont(new Font("Table.font", 0, 20));
      this.fileTable.getTableHeader().setFont(new Font("Dialog", 0, 20));
      this.fileTable.setRowSelectionAllowed(true);
      
      TableColumnModel cm = this.fileTable.getColumnModel();
      
      TableColumn column = cm.getColumn(0);
      column.setPreferredWidth(60);
      column.setMaxWidth(60);
      
      column = cm.getColumn(2);
      column.setPreferredWidth(120);
      column.setMaxWidth(120);
      
      column = cm.getColumn(3);
      column.setPreferredWidth(120);
      column.setMaxWidth(120);
      
      column = cm.getColumn(4);
      column.setPreferredWidth(80);
      column.setMaxWidth(80);
      
      column = cm.getColumn(5);
      column.setPreferredWidth(80);
      column.setMaxWidth(80);
      

      DefaultTableCellRenderer fontColor = new DefaultTableCellRenderer()
      {
        public void setValue(Object value)
        {
          double a = (value instanceof Double) ? ((Double)value).doubleValue() : 0.0D;
          setForeground(((String)value).equals("完成") ? Color.green : Color.black);
          setText(value.toString());
        }
      };
      column.setCellRenderer(fontColor);
      
      JScrollPane jsPanel = new JScrollPane(this.fileTable);
      panel.add(jsPanel);
    }
  }
  
  public void onStartCompressClicked()
  {
//    if (!this.localNetMac.equals("38-ad-8e-33-b2-5c"))
//    {
//      JOptionPane.showMessageDialog(this, "该工具只允许在特定局域网内使用！", "提示", 2);
//      return;
//    }
    final String keyString = this.apiKyeTextField.getText();
    if (keyString.isEmpty())
    {
      JOptionPane.showMessageDialog(this, "请输入正确的API KEY", "提示", 2);
      return;
    }
    String savePath = this.destDirTextField.getText();
    if (savePath.isEmpty())
    {
      JOptionPane.showMessageDialog(this, "请设置保存目录！", "提示", 2);
      return;
    }
    this.progressBar.setValue(0);
    this.srcScanButton.setEnabled(false);
    this.destScanButton.setEnabled(false);
    this.compressButton.setEnabled(false);
    this.statusLabel.setText("API KEY检测中......");
    
    //重置状态
    FileTableModel tableModel = (FileTableModel)this.fileTable.getModel();
    int cnt = MainFrame.this.fileTable.getRowCount();
    for(int n = 0; n < cnt; n++){
    	tableModel.setValueAt("--", n, 3);
        tableModel.setValueAt("--", n, 4);
        tableModel.setValueAt("未处理", n, 5);
    }
    this.fileTable.revalidate();
    
    this.checkApiKeyValid(keyString, new CheckApiKeyResultListener()
    {
      public void onResult(boolean bSuccess)
      {
        if (bSuccess)
        {
          MainFrame.this.nCompressFinished = 0;
          MainFrame.this.stopCompress();
          MainFrame.this.cancelButton.setEnabled(true);
          
          Tinify.setKey(keyString);
          
          MainFrame.this.startCompress(new CompressListener()
          {
            long allOldSize = 0L;
            long allNewSize = 0L;
            int cnt = 0;
            
            public void onProgress(int index, String org, String dest)
            {
              FileTableModel tableModel = (FileTableModel)MainFrame.this.fileTable.getModel();
              tableModel.setValueAt("压缩中", index, 5);
              if (MainFrame.this.statusLabel.getText().equals("API KEY检测中......")) {
                MainFrame.this.statusLabel.setText("开始...");
              }
            }
            
            public void onError(int index, String org, String dest, String err)
            {
              MainFrame.this.srcScanButton.setEnabled(true);
              MainFrame.this.destScanButton.setEnabled(true);
              MainFrame.this.compressButton.setEnabled(true);
              MainFrame.this.statusLabel.setText("压缩失败！");
              JOptionPane.showMessageDialog(MainFrame.this, err, "提示", 2);
            }
            
            public void onSuccess(int index, String org, String dest)
            {
              double newSize = FileWorker.getFileLength(dest);
              double oldSize = ((Long)MainFrame.this.fileDatas.get(org)).longValue();
              int rate = 100 - (int)(100.0D * newSize / oldSize);
              int rows = MainFrame.this.fileTable.getRowCount();
              
              FileTableModel tableModel = (FileTableModel)MainFrame.this.fileTable.getModel();
              tableModel.setValueAt(String.format("%d", (long)newSize), index, 3);
              tableModel.setValueAt(rate + "%", index, 4);
              tableModel.setValueAt("完成", index, 5);
              
              allOldSize = (long) ((this.allOldSize + oldSize));
              allNewSize = (long) ((this.allNewSize + newSize));
              
              MainFrame.this.fileTable.invalidate();
              
              MainFrame.this.progressBar.setValue(MainFrame.this.nCompressFinished * 100 / rows);
              MainFrame.this.progressBar.invalidate();
              if (MainFrame.this.bCompressDoing) {
                MainFrame.this.statusLabel.setText(String.format("完成：%d/%d ", ++cnt, MainFrame.this.fileTable.getRowCount()));
              }
              if (MainFrame.this.progressBar.getValue() >= 100)
              {
                MainFrame.this.srcScanButton.setEnabled(true);
                MainFrame.this.destScanButton.setEnabled(true);
                MainFrame.this.compressButton.setEnabled(true);
                MainFrame.this.cancelButton.setEnabled(false);
                MainFrame.this.statusLabel.setText(String.format("完成！共计文件：%d个 原始大小：%d(字节) 压缩后大小：%d(字节) 总压缩比：%d%%", rows, allOldSize,allNewSize, 100 - (int)(100L * this.allNewSize / this.allOldSize)));
                JOptionPane.showMessageDialog(MainFrame.this, "压缩完成！", "提示", 1);
              }
            }
          });
        }
        else
        {
          MainFrame.this.srcScanButton.setEnabled(true);
          MainFrame.this.destScanButton.setEnabled(true);
          MainFrame.this.compressButton.setEnabled(true);
          MainFrame.this.statusLabel.setText("API KEY检测失败！");
        }
      }
    });
  }
  
  public void onCancelCompressClicked()
  {
    stopCompress();
    this.cancelButton.setEnabled(false);
    this.destScanButton.setEnabled(true);
    this.srcScanButton.setEnabled(true);
    this.compressButton.setEnabled(true);
    this.statusLabel.setText("已取消！");
  }
  
  public void checkApiKeyValid(String apiKey, final CheckApiKeyResultListener listener)
  {
    Tinify.setKey(apiKey);
    new Thread(new Runnable()
    {
      public void run()
      {
        try
        {
          Tinify.validate();
        }
        catch (final Exception e)
        {
          EventQueue.invokeLater(new Runnable()
          {
            public void run()
            {
              MainFrame.this.compressCountLeft.setText("");
              JOptionPane.showMessageDialog(MainFrame.this, e.toString(), "提示", 2);
              if (listener != null) {
                 listener.onResult(false);
              }
            }
          });
          return;
        }
        EventQueue.invokeLater(new Runnable()
        {
          int compressionsThisMonth = Tinify.compressionCount();
          
          public void run()
          {
            MainFrame.this.compressCountLeft.setText("当月压缩：" + this.compressionsThisMonth);
            if (listener != null) {
              listener.onResult(true);
            }
          }
        });
      }
    }).start();
  }
  
  private void startCompress(final CompressListener listener)
  {
    FileTableModel tableModel = (FileTableModel)this.fileTable.getModel();
    ArrayList<ArrayList<Object>> rows = tableModel.getAllRowObjects();
    final Iterator<ArrayList<Object>> rowIterator = rows.iterator();
    final String baseSavePath = this.destDirTextField.getText();
    final String baseSrcPath = this.srcDirTextField.getText();
    
    this.bCompressDoing = true;
    
    Thread outThread = new Thread(new Runnable()
    {
      int rowIndex = 0;
      
      public void run()
      {
        while (rowIterator.hasNext())
        {
          final ArrayList<Object> row = (ArrayList)rowIterator.next();
          
          Thread innerThread = new Thread(new Runnable(){
     
            String _savaFullPath;
            int _rowIndex = rowIndex;
      	    ArrayList<Object> _row = row;
      	    
            public void run()
            {
              EventQueue.invokeLater(new Runnable() {
			      public void run(){
				      if (listener != null) {
				    	  System.out.print("compressing...." + _rowIndex + "\n");
				    	  listener.onProgress(_rowIndex, (String)_row.get(1), _savaFullPath);
				      }
				 }
              });
              try{
                Thread.sleep(100L);
              }
              catch (InterruptedException e1){
                e1.printStackTrace();
                return;
              }
              if (!MainFrame.this.bCompressDoing) {
                return;
              }
              Exception exp = null;
              try
              {
                FileTableModel model = (FileTableModel)MainFrame.this.fileTable.getModel();
                model.setUserData(_rowIndex, Integer.valueOf(1));
                
                Source source = Tinify.fromFile((String)_row.get(1));
                _savaFullPath = FileWorker.getSubPathFromFullPath((String)_row.get(1), baseSrcPath);
                _savaFullPath = (baseSavePath + _savaFullPath);
                FileWorker.makeDirs(_savaFullPath);

                source.toFile(_savaFullPath);
                
                MainFrame.this.nCompressFinished += 1;
                
                if (listener != null) {
                	System.out.print("compress success...." + _rowIndex + "\n");
                	listener.onSuccess(_rowIndex, (String)_row.get(1), _savaFullPath);
                }
                
//                EventQueue.invokeLater(new Runnable() {
//                  int _rowIndex = rowIndex;
//            	  ArrayList<Object> _row = row;
//                  public void run()
//                  {
//                    if (listener != null) {
//                    	System.out.print("compress success...." + _rowIndex + "\n");
//                    	listener.onSuccess(_rowIndex, (String)_row.get(1), _savaFullPath);
//                    }
//                  }
//	             });
              }
              catch (AccountException e)
              {
                exp = e;
                e.printStackTrace();
              }
              catch (ClientException e)
              {
                exp = e;
                e.printStackTrace();
              }
              catch (ServerException e)
              {
                exp = e;
                e.printStackTrace();
              }
              catch (ConnectionException e)
              {
                exp = e;
                e.printStackTrace();
              }
              catch (Exception e)
              {
                exp = e;
                e.printStackTrace();
              }
              if (exp != null)
              {
                MainFrame.this.stopCompress();
                final Exception exception = exp;
                EventQueue.invokeLater(new Runnable(){
                    public void run(){
                    if ( listener != null) {
                       listener.onError(_rowIndex, (String)_row.get(1),  _savaFullPath, exception.toString());
                    }
                  }
	             });
              }
            }
          });
          try
          {
            Thread.sleep(100L);
          }
          catch (InterruptedException e)
          {
            e.printStackTrace();
            return;
          }
          innerThread.start();
          MainFrame.this.threadList.add(innerThread);
          rowIndex += 1;
        }
      }
    });
    outThread.start();
    threadList.add(outThread);
  }
  
  private void stopCompress()
  {
    if (this.bCompressDoing)
    {
      this.bCompressDoing = false;
      Iterator<Thread> iterator = threadList.iterator();
      while (iterator.hasNext())
      {
        Thread thread = (Thread)iterator.next();
        if (thread.isAlive()) {
          thread.interrupt();
        }
      }
      threadList.clear();
      
      FileTableModel model = (FileTableModel)this.fileTable.getModel();
      ArrayList<ArrayList<Object>> rowlist = model.getAllRowObjects();
      Iterator<ArrayList<Object>> iterable = rowlist.iterator();
      while (iterable.hasNext())
      {
        ArrayList<Object> row = (ArrayList)iterable.next();
        String status = (String)row.get(5);
        int num = ((Integer)row.get(0)).intValue();
        
        Object userdata = model.getUserData(num - 1);
        if ((status.equals("压缩中")) && (userdata == null)) {
          model.setValueAt("已取消", num - 1, 5);
        }
      }
      model.clearAllUserData();
    }
  }
  
  private void clearFileTable()
  {
    int cnt = this.fileTable.getRowCount();
    if (cnt > 0)
    {
      FileTableModel model = (FileTableModel)fileTable.getModel();
      model.removeAllRow();
      fileTable.invalidate();
    }
    fileDatas.clear();
    progressBar.setValue(0);
    compressButton.setEnabled(false);
  }
  
  private JPopupMenu createPopupMenu(final int row)
  {
    final FileTableModel model = (FileTableModel)this.fileTable.getModel();
    String status = (String)model.getValueAt(row, 5);
    
    JPopupMenu popupMenu = new JPopupMenu();
    
    JMenuItem delMenItem = new JMenuItem();
    delMenItem.setText("打开源文件");
    delMenItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        String path = (String)model.getValueAt(row, 1);
        ImageViewer viewer = new ImageViewer(path);
        viewer.appendTitle(path);
        viewer.display();
      }
    });
    popupMenu.add(delMenItem);
    if (status.equals("完成"))
    {
      delMenItem = new JMenuItem();
      delMenItem.setText("打开压缩后的文件");
      delMenItem.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent evt)
        {
          String orgFilePath = (String)model.getValueAt(row, 1);
          String baseSrcPath = MainFrame.this.srcDirTextField.getText();
          String baseSavePath = MainFrame.this.destDirTextField.getText();
          String savaFullPath = FileWorker.getSubPathFromFullPath(orgFilePath, baseSrcPath);
          savaFullPath = baseSavePath + savaFullPath;
          ImageViewer viewer = new ImageViewer(savaFullPath);
          viewer.appendTitle(savaFullPath);
          viewer.display();
        }
      });
      popupMenu.add(delMenItem);
    }
    delMenItem = new JMenuItem();
    delMenItem.setText("打开所在目录");
    delMenItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        try
        {
          String orgFilePath = (String)model.getValueAt(row, 1);
          String rootPath = FileWorker.getRootPathFromFullPath(orgFilePath);
          Desktop.getDesktop().open(new File(rootPath));
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
    });
    popupMenu.add(delMenItem);
    if (status.equals("完成"))
    {
      delMenItem = new JMenuItem();
      delMenItem.setText("打开保存目录");
      delMenItem.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent evt)
        {
          try
          {
            String orgFilePath = (String)model.getValueAt(row, 1);
            String baseSrcPath = MainFrame.this.srcDirTextField.getText();
            String baseSavePath = MainFrame.this.destDirTextField.getText();
            String savaFullPath = FileWorker.getSubPathFromFullPath(orgFilePath, baseSrcPath);
            savaFullPath = baseSavePath + savaFullPath;
            String rootPath = FileWorker.getRootPathFromFullPath(savaFullPath);
            Desktop.getDesktop().open(new File(rootPath));
          }
          catch (IOException e)
          {
            e.printStackTrace();
          }
        }
      });
      popupMenu.add(delMenItem);
    }
    return popupMenu;
  }
}
