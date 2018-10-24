import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
 
 /*********************************************** 
   This class prints the end page for two player
 ***********************************************/
public class TwoPEndGame implements ActionListener 
{
           
    Drawing draw=new Drawing();
    ImageIcon EndPage=new ImageIcon("EndPage.jpg"); 
    public static JButton Return;
    JFrame frame=new JFrame("End Game"); 
    JPanel endpanel;
    tetrisblock x,y;
       
    public TwoPEndGame(tetrisblock a, tetrisblock b)
    {     
      x=a;
      y=b;
      frame.add(draw);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(1200, 750);
      frame.getContentPane().setBackground(Color.black);
      frame.setResizable(false);
      frame.setVisible(false);
      startScreen();
    }
 
   /*** paint *******************************************
   * Purpose: Draw which player wins and the score       *
   * Parameters: g                                       *
   * Returns: none                                       *
   ******************************************************/
   class Drawing extends JComponent
   {
      public void paint(Graphics g)
      {
         g.setColor(Color.pink);
         g.setFont(new Font("TimesRoman",	Font.BOLD, 30));
         g.drawImage(EndPage.getImage(), 400,0, this);
         if(x.score<y.score)
            g.drawString("Player 1 Won with a score of "+ y.score ,400,450);
         else if(x.score>y.score)
            g.drawString("Player 2 Won with a score of "+ x.score ,400,450);
         else if(x.score==y.score)
            g.drawString("Tie with a score of "+ x.score ,400,450);
      }
    }

   /*** endGame ******************************************
   * Purpose: Set the frame visible                      *
   * Parameters: none                                    *
   * Returns: none                                       *
   ******************************************************/  
    public void endGAME()
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

    /*** startScreen *************************************
   * Purpose: Draw the button on the end game page       *
   * Parameters: none                                    *
   * Returns: none                                       *
   ******************************************************/    
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
