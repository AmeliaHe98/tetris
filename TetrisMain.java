import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

/******************************************************************
   This is the main file for main page that accesses the game
*******************************************************************/

public class TetrisMain implements ActionListener, MouseMotionListener 
{
           
   Drawing draw=new Drawing();
   ImageIcon mainPage1=new ImageIcon("Tetris main page.jpg"); 
   ImageIcon title=new ImageIcon("Title on main page.jpg"); 
   ImageIcon instructions=new ImageIcon("instructions page2.jpg"); 
   ImageIcon tetrisGrid=new ImageIcon("tetris grid.png"); 
   ImageIcon EndPage=new ImageIcon("EndPage.jpg"); 
   int screen;
   //1 is main page
   //2 is instructions page
   //3 is classic
   //4 is two players  
   public static JButton classic, twoPlayers, help;
   //in instructions page
   JButton back;    
   JFrame frame=new JFrame("Tetris Main Page"); 
   JPanel panel, panel2;  
   //coordinates of the mouse
   int x, y;   
   //in main page whether the back button is pressed
   boolean isBack; 
   int level, score, player;
       
   /*** paint ********************************************
   * Purpose: Draw the images including main page,       *
   *          instructions page                          *
   * Parameters: g                                       *
   * Returns: none                                       *
   ******************************************************/
   class Drawing extends JComponent
   {
      public void paint(Graphics g)
      {
         g.setColor(Color.white);
         if (screen==1)
         {
            g.drawImage(title.getImage(),280,15,this);
            g.drawImage(mainPage1.getImage(),280,130,this);
         }
         else if (screen==2)
         {
            g.drawImage(instructions.getImage(), 300,0, this);
            g.drawImage(title.getImage(),250,15,this);
         }
         else if (screen ==3)
            g.drawImage(tetrisGrid.getImage(),10,25,this);
         
         else if (screen==4)
         {
            g.drawImage(EndPage.getImage(), 250,0, this);
            g.drawString("You score is "+score,300,300);
         }
         else if (screen==5)
         {
            g.drawImage(EndPage.getImage(), 250,0, this);
            g.drawString("Player "+player+"won!",300,300);
         }
         else
         {
            g.setColor(Color.black);
         }
      }
   }

   public TetrisMain()
   {
      screen=1;
      isBack=false;
      level=1;
      score=0;
      player=1;
      frame.add(draw);
      draw.addMouseListener(new MouseListen());
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(1200, 750);
      frame.getContentPane().setBackground(Color.black);
      frame.setResizable(false);
      frame.setVisible(true);
      startScreen();
   }
     
   /*** mouseReleased ************************************
   * Purpose: When the return button on the instructions *
   *          page is clicked, the screen changes to main*
   *          page                                       *
   * Parameters: e                                       *
   * Returns: none                                       *
   ******************************************************/
   class MouseListen extends MouseAdapter
   {
      public void mouseReleased(MouseEvent e)
      {
         x = e.getX();
         y = e.getY();
         if (x>=325 && y>=530 && x<=550 && y<=615)
         {
            screen=1;
            isBack=true;
            startScreen();
         }
         draw.repaint();
      }
   }
    
   public void mouseMoved(MouseEvent e)
   {
   }
   
   public void mouseDragged(MouseEvent e)
   {
   }

   /*** actionPerformed **********************************
   * Purpose: When the buttons are clicked, the screen   *
   *          changes                                    *
   *          page                                       *
   * Parameters: e                                       *
   * Returns: none                                       *
   ******************************************************/   
   public void actionPerformed(ActionEvent e)
   {
      if (e.getSource() == help)            
      {
         instructionsPage();
         screen=2;
         draw.repaint();
      }  

     if (e.getSource()==twoPlayers)
     {
         TwoPlayers.main(null);
         frame.setVisible(false);
     }
     if (e.getSource()==classic)
     {
         OnePlayer.main(null);
         frame.setVisible(false);
     }
   }
  
   /*** startScreen **************************************
   * Purpose: This draws the main page                   *
   * Parameters: none                                    *
   * Returns: none                                       *
   ******************************************************/  
   public void startScreen()
   {
      if (isBack)
      {
         frame.remove(panel);
         frame.remove(panel2);
         frame.repaint();
         isBack=false;
      }
      classic = new JButton("Classic");
      twoPlayers = new JButton("Two Players");
      help = new JButton("Help");
      classic.addActionListener(this);
      twoPlayers.addActionListener(this);
      help.addActionListener(this);
      panel=new JPanel();
      panel.setLayout(new GridLayout(3, 1));
      panel2=new JPanel();
      panel2.add(classic);
      panel2.add(twoPlayers);
      panel2.add(help);
      frame.add(panel2,"South");
      frame.revalidate();
      frame.repaint();
      frame.setVisible(true);
      frame.setResizable(false);
   }
    
   /*** clssicScreen *************************************
   * Purpose: This removes the panels                    *
   * Parameters: none                                    *
   * Returns: none                                       *
   ******************************************************/
   public void classicScreen()
   {
      frame.remove(panel);
      frame.remove(panel2);
      frame.repaint();
   }

   /*** twoPlayers ***************************************
   * Purpose: This removes the panels                    *
   * Parameters: none                                    *
   * Returns: none                                       *
   ******************************************************/    
   public void twoPlayers()
   {
      frame.remove(panel);
      frame.remove(panel2);
      frame.repaint();
   }

   /*** instructionsPage *********************************
   * Purpose: This removes the panels                    *
   * Parameters: none                                    *
   * Returns: none                                       *
   ******************************************************/    
   public void instructionsPage()
   {
      frame.remove(panel);
      frame.remove(panel2);
      frame.repaint();
   }

   public static void main(String[] args) 
   {
      new TetrisMain();
   }        
}  
       
