package venn;
import java.awt.Color;

import javax.swing.JFrame;
 
 
public class MainApp extends JFrame{
     
    static JFrame window = new JFrame(); //STATIC: We can only have one window.
     
    public static void main(String[] args) {
         
        createWindow();
        Ball redBall = new Ball();
        redBall.drawBall(window);
     
        redBall.setBallBorderColor(Color.red);
        redBall.setBallWidth(100);
        redBall.setBallHeight(100);
        redBall.setBallX(0);
        redBall.setBallY(100);
        redBall.drawBall(window);
         
        Ball blueBall = new Ball(0,0,100,100,Color.blue,window);
        blueBall.drawBall(window);
         
         
         
    }
     
    public static void createWindow()
    {
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(400,400);
        window.setVisible(true);
    }
}
