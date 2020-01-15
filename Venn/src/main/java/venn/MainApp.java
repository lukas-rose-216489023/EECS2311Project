package venn;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.*;
 
 
public class MainApp extends JFrame{
     
    static JFrame window = new JFrame();//STATIC: We can only have one window.
    static JMenuBar menuBar = new JMenuBar();
    static JMenu menu, submenu;
    static JMenuItem menuItem;
    
     
    public static void main(String[] args) {
         
        createWindow();
        createMenu();
        
         
         
         
    }
     
    public static void createWindow()
    {
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit tk = Toolkit.getDefaultToolkit();
        int xSize = ((int) tk.getScreenSize().getWidth());
        int ySize = ((int) tk.getScreenSize().getHeight());
        window.setSize(xSize,ySize);
        window.setVisible(true); 
    }
    	
    public static void createMenu() 
    {
    	menu = new JMenu("Menu");    	
    	menu.setMnemonic(KeyEvent.VK_A);
    	menu.getAccessibleContext().setAccessibleDescription("Menu for Venn Diagram Maker");
    	
    	menuItem = new JMenuItem("Circle");
    	menuItem.addActionListener((event) -> createCircle());;
    	
    	menuBar.add(menu);
    	window.setJMenuBar(menuBar);
    	menu.add(menuItem);
    }
    
    public static void createCircle() {
    	Ball redBall = new Ball();
     
        redBall.setBallBorderColor(Color.red);
        redBall.setBallWidth(100);
        redBall.setBallHeight(100);
        redBall.setBallX(100);
        redBall.setBallY(100);
        redBall.drawBall(window);
         
    }
    
    
}
