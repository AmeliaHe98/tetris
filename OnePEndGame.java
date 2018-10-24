import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
 
 /***********************************************
   This class displays end page for one player
 ************************************************/
public class OnePEndGame implements ActionListener
{            
           
    Drawing draw=new Drawing();
    ImageIcon EndPage=new ImageIcon("EndPage.jpg"); 
    public static JButton Return;
    JFrame frame=new JFrame("End Game");  
    JPanel endpanel;
    tetrisblock x;
       
    int p;
    
    public OnePEndGame(tetrisblock a)
    {
      
      x=a;
      frame.add(draw);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(1200, 750);
      frame.getContentPane().setBackground(Color.black);
      frame.setResizable(false);
      frame.setVisible(false);
      startScreen();
    }
 
   /*** paint *******************************************
   * Purpose: Draw which player wins and the score      *
   * Parameters: g                                      *
   * Returns: none                                      *
   *****************************************************/   
   class Drawing extends JComponent
   {
      public void paint(Graphics g)
      {
         g.setColor(Color.pink);
         g.setFont(new Font("TimesRoman",	Font.BOLD, 30));
         g.drawImage(EndPage.getImage(), 400,0, this);
         g.drawString("Game Over",450,300);
         g.drawString("your final score was: "+ x.score ,420,400);   
      }
    }
     
   /*** endGame ******************************************
   * Purpose: Set the frame visible                      *
   * Parameters: none                                    *
   * Returns: none                                       *
   ******************************************************/
    public void endGAME(int a)
    {
      frame.setVisible(true);
      draw.repaint();
    }

   /*** actionPerformed **********************************
   * Purpose: Return to the main page when the return    *
   *          button is clicked                          *
   * Parameters: e                                       *
   * Returns: none                                       *
   ******************************************************/
    public void actionPerformed(ActionEvent e)
    {
      if (e.getSource() == Return)            
      {
         TetrisMain.main(null);
         draw.repaint();
      }  
    }
    
    public void startScreen()
    {
      Return = new JButton("Return");
      Return.addActionListener(this);
      endpanel=new JPanel();
      endpanel.setLayout(new BorderLayout());
      endpanel.add(Return);
      frame.add(endpanel,"South");
      frame.revalidate();
      frame.repaint();
      frame.setVisible(false);
    }
 
    public static void main(String[] args) 
    {
    }
}
