package org.rgtech.compress;

import java.awt.Color;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

public class ImageViewer
  extends JFrame
{
  private JLabel showpicture;
  
  public ImageViewer(String imgPath)
  {
    setForeground(Color.DARK_GRAY);
    setBackground(Color.DARK_GRAY);
    getContentPane().setBackground(Color.DARK_GRAY);
    
    this.showpicture = new JLabel();
    
    this.showpicture.setBackground(Color.BLACK);
    this.showpicture.setHorizontalAlignment(0);
    this.showpicture.setVerticalAlignment(0);
    this.showpicture.setIcon(new ImageIcon(imgPath));
    
    JScrollPane scrollPane = new JScrollPane(this.showpicture);
    scrollPane.setOpaque(false);
    scrollPane.getViewport().setOpaque(false);
    getContentPane().add(scrollPane, "Center");
    
    setDefaultCloseOperation(1);
    setTitle("Tinypng Client Tool - Image Viewer");
    

    setBounds(100, 100, 1212, 808);
    
    MouseDragListener listener = new MouseDragListener(this.showpicture);
    this.showpicture.addMouseListener(listener);
    this.showpicture.addMouseMotionListener(listener);
  }
  
  public void appendTitle(String title)
  {
    String org = getTitle();
    setTitle(org + " [" + title + "]");
    invalidate();
  }
  
  public void display()
  {
    setVisible(true);
  }
  
  private class MouseDragListener
    implements MouseInputListener
  {
    JLabel target;
    Point point = new Point(0, 0);
    
    public MouseDragListener(JLabel l)
    {
      this.target = l;
    }
    
    public void mouseClicked(MouseEvent e) {}
    
    public void mousePressed(MouseEvent e)
    {
      this.point = SwingUtilities.convertPoint(this.target, e.getPoint(), this.target.getParent());
    }
    
    public void mouseReleased(MouseEvent e) {}
    
    public void mouseEntered(MouseEvent e) {}
    
    public void mouseExited(MouseEvent e) {}
    
    public void mouseDragged(MouseEvent e)
    {
      Point newPoint = SwingUtilities.convertPoint(this.target, e.getPoint(), this.target.getParent());
      ImageViewer.this.showpicture.setLocation(ImageViewer.this.showpicture.getX() + (newPoint.x - this.point.x), this.target.getY() + (newPoint.y - this.point.y));
      

      this.point = newPoint;
    }
    
    public void mouseMoved(MouseEvent e) {}
  }
}
