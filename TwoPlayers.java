import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;

/*********************************************************
   This is the main file for two player game
*********************************************************/

public class TwoPlayers extends JFrame 
{
   TwoPEndGame end;
   //If there appears to be an error at this location, retype tetrisblock
   tetrisblock a = new tetrisblock(1);
   tetrisblock b = new tetrisblock(2);
   boolean amy=false;
   int endscore=110;
  
   public TwoPlayers() 
   {
      JPanel container = new JPanel();
      container.setLayout(new BoxLayout(container,BoxLayout.X_AXIS));
      end= new TwoPEndGame(a,b);
      a.finalEnd(end);
      b.finalEnd(end);
      addKeyListener(a);
      addKeyListener(b);
      addMouseListener(a);
      addMouseListener(b);
      container.add(b);
      container.add(a);
      add(container);     
   }
      
   public static void main(String[] args) 
   {
      TwoPlayers frame = new TwoPlayers();
      frame.setLocationRelativeTo(null);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(1200, 750);
      frame.setTitle("Two Players");
      frame.setVisible(true);
      frame.setResizable(false);   
   }

}

class tetrisblock extends JPanel implements KeyListener, MouseListener
{
   TwoPEndGame end;
   private int blockType;
   public int score;
   public int level;
   public boolean amy=false;
   public boolean mod=false;
   private int turnState;
   public int x;
   public int y;
   private int i = 0;
   private int whichBlock=0;
   private int howManyLines=0;
   private int newTime= 1000;
   private int nco=0;
   private boolean ll= false;
   private boolean lr= false;
   private boolean lu= false;
   private boolean ld= false;
   private boolean rr= false;
   private boolean ru= false;
   private boolean rd= false;
   private boolean rl= false;   
   Timer timer;
   Timer blocktimer;
   int j = 0;
   int flag = 0;
   int[][] map = new int[17][37];
   int[][] colourMap = new int[17][37];
   BufferedImage img;  
   //nextBlock
   int nextBlockType;
   int nextTurnState;  
   int nextBlockShapes[][]=new int [4][4];

   private final int shapes[][][] = new int[][][]
   { 
       // i
       { { 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
         { 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0 },
         { 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
         { 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0 } },
          // s
       { { 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
         { 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
         { 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
         { 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 } },
          // z
       { { 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
         { 0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
         { 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
         { 0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 } },
          // j
       { { 0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0 },
         { 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
         { 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
         { 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 } },
          // o
       { { 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
         { 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
         { 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
         { 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } },
          // l
       { { 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0 },
         { 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
         { 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
         { 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 } },
          // t
       { { 0, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
         { 0, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
         { 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
         { 0, 1, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0 } } 
      };

   Color[] PIECE_COLORS = 
   {
   	new Color(0xFF00FF), // fucia
   	new Color(0xDC143C), // crimson
   	new Color(0x00CED1), // dark turquoise
   	new Color(0xFFD700), // gold
   	new Color(0x32CD32), // lime green
   	new Color(0x008080), // teal
   	new Color(0xFFA500), // orange
  };  

   public void finalEnd(TwoPEndGame endl)
   { 
      end=endl;
   }
  
   /*** nextBlock ****************************************
   * Purpose: Creates the next block that will be falling*
   * Parameters: none                                    *
   * Returns: none                                       *
   ******************************************************/
   public void nextBlock()
   {
      nextBlockType =(int)	(Math.random()	* 1000) % 7;
      nextTurnState =(int)	(Math.random()	* 1000) % 4;
      for (int i=0; i<4; i++)
         nextBlockShapes[0][i]=shapes[nextBlockType][nextTurnState][i];
      for (int i=0; i<4; i++)
         nextBlockShapes[1][i]=shapes[nextBlockType][nextTurnState][4+i];
      for (int i=0; i<4; i++)
         nextBlockShapes[2][i]=shapes[nextBlockType][nextTurnState][8+i];
      for (int i=0; i<4; i++)
         nextBlockShapes[3][i]=shapes[nextBlockType][nextTurnState][12+i];
      for (int i=0; i<4; i++)
      {
         for (int j=0; j<4; j++)
      System.out.println();
      }
      System.out.println();
   }

   /*** newblock *****************************************
   * Purpose: Creates the new block                      *
   * Parameters: none                                    *
   * Returns: none                                       *
   ******************************************************/
   public void newblock() 
   {
      if( whichBlock==0)
      {
         blockType = nextBlockType;
         turnState= nextTurnState;
      }
      else 
      {
         blockType = nextBlockType;
         turnState= nextTurnState;
      }   
      whichBlock++;
      x = 4;
      y = 0;
      nextBlock();
      if (isGameOver()==true) 
      {
         newmap();
         drawwall();         
         newTime=1000;
         amy=true;
         repaint();
         end.endGAME();
      }
   }
   
   /*** isGameOver ***************************************
   * Purpose: Checks whether the game is over            *
   * Parameters: none                                    *
   * Returns: whether the game is over                   *
   ******************************************************/
   public boolean isGameOver()
   {
      if (gameover(x, y) == 1) 
         return true;
      else if (mod==true)
         return true;
      else
         return false;
   }
   
   /*** drawwall *****************************************
   * Purpose: Draws the boundary around the blocks       *
   * Parameters: none                                    *
   * Returns: none                                       *
   ******************************************************/
   public void drawwall() 
   {
      for (i = 0; i < 16; i++) 
      {
         map[i][35] = 2;
      }
      for (j = 0; j < 36; j++) 
      {
         map[15][j] = 2;
         map[0][j] = 2;
      }
   }
   
   /*** newmap *******************************************
   * Purpose: Initialize the map by setting the values   *
   *          in the grid to 0                           *
   * Parameters: none                                    *
   * Returns: none                                       *
   ******************************************************/
   public void newmap() 
   {
      for (i = 0; i < 16; i++) 
      {
         for (j = 0; j < 36; j++) 
         {
            map[i][j] = 0;
         }
      }
   }
   
   /*** endmap *******************************************
   * Purpose: Set the values of all grids to 1           *
   * Parameters: none                                    *
   * Returns: none                                       *
   ******************************************************/
   public void endmap() 
   {
      for (i = 0; i < 16; i++) 
      {
         for (j = 0; j < 36; j++) 
         {
            map[i][j] = 1;
         }
      }
   }
   
   /*** newColourMap *************************************
   * Purpose: Initialize the color map that stores all   *
   *          the colors of the blocks                   *
   * Parameters: none                                    *
   * Returns: none                                       *
   ******************************************************/
   public void newColourMap() 
   {
      for (i = 0; i < 16; i++) 
      {
         for (j = 0; j < 36; j++) 
         {
            colourMap[i][j] = -1;
         }
      }
   }
   
   int player;
   tetrisblock(int player) 
   {
      this.player = player;
      if(player==1)
         nco=400;
      else if(player==2)
         nco=900;
      
      nextBlock();
      newblock();
      newmap();
      newColourMap();
      drawwall();
      score=0;
      level=1;
      timer = new Timer(newTime, new TimerListener());
      blocktimer = new Timer(90, new BlockTimerListener());
      timer.start();
      blocktimer.start();
      try {
         img = ImageIO.read(new File("tetris grid.png"));
      } 
      catch (IOException e) 
      {
         e.printStackTrace();
      }
   }

   /*** turn *********************************************
   * Purpose: Changes the orientation of the block       *
   * Parameters: none                                    *
   * Returns: none                                       *
   ******************************************************/
   public void turn() 
   {
      int tempturnState = turnState;
      turnState = (turnState + 1) % 4;
      if (fall(x, y, blockType, turnState) == 1) 
      {
      }
      if (fall(x, y, blockType, turnState) == 0) 
      {
         turnState = tempturnState;
      }
      repaint();
   }

   /*** left *********************************************
   * Purpose: Shifts the block left by one               *
   * Parameters: none                                    *
   * Returns: none                                       *
   ******************************************************/ 
   public void left() 
   {
      if (fall(x - 1, y, blockType, turnState) == 1) 
      {
         x = x - 1;
      }
      ;
      repaint();
   }

   /*** right ********************************************
   * Purpose: Shifts the block right by one              *
   * Parameters: none                                    *
   * Returns: none                                       *
   ******************************************************/   
   public void right() 
   {
      if (fall(x + 1, y, blockType, turnState) == 1) 
      {
         x = x + 1;
      }
      ;
      repaint();
   }
  
   /*** down *********************************************
   * Purpose: Shifts the block down by one               *
   * Parameters: none                                    *
   * Returns: none                                       *
   ******************************************************/   
   public void down() 
   {
      if (fall(x, y + 1, blockType, turnState) == 1) 
      {
         y = y + 1;
         delline();
      }
      ;
      if (fall(x, y + 1, blockType, turnState) == 0) 
      {
         add(x, y, blockType, turnState);
         newblock();
         delline();
      }
      ;
      repaint();
   }

   /*** fall *********************************************
   * Purpose: Makes the blocks fall faster               *
   * Parameters: none                                    *
   * Returns: whether the block is at the bottom         *
   ******************************************************/
   public int fall(int x, int y, int blockType, int turnState) 
   {
      if(mod==false)
      for (int a = 0; a < 4; a++) 
      {
         for (int b = 0; b < 4; b++) 
         {
            if (((shapes[blockType][turnState][a * 4 + b] == 1) && (map[x
                   + b +1][y + a] == 1))
                   || ((shapes[blockType][turnState][a * 4 + b] == 1) && (map[x
                           + b + 1][y + a] == 2))) 
            {
               return 0;
            }
         }
      }
      return 1;
   }
   
   /*** delline ******************************************
   * Purpose: Deletes the line at the bottom             *
   * Parameters: none                                    *
   * Returns: none                                       *
   ******************************************************/
   public void delline() 
   {
      int c = 0;
      for (int b = 0; b < 36; b++) 
      {
         for (int a = 0; a < 16; a++) 
         {
            if (map[a][b] == 1) 
            {
               c = c + 1;
               if (c == 14) 
               {
                  score += 100;
                  howManyLines++;
                  speed();
                  for (int d = b; d > 0; d--) 
                  {
                     for (int e = 0; e < 16; e++)
                     {
                        map[e][d] = map[e][d - 1];
                        colourMap[e][d] = colourMap[e][d - 1];
                     }
                  }
               }
            }
         }
         c = 0;
      }
   }
     
   /*** giveScore ****************************************
   * Purpose: Gives the current score                    *
   * Parameters: none                                    *
   * Returns: the current score                          *
   ******************************************************/
   public int giveScore()
   {
      return this.score;
   }
   
   /*** giveLevel ****************************************
   * Purpose: Gives the current level                    *
   * Parameters: none                                    *
   * Returns: the current level                          *
   ******************************************************/
   public int giveLevel()
   {
      return this.level;
   }
   
   /*** speed ********************************************
   * Purpose: Speed up the falling of blocks when every  *
   *          five lines are cleared                     *
   * Parameters: none                                    *
   * Returns: none                                       *
   ******************************************************/
   public void speed()
   {
      if(howManyLines%5==0)
      {
         newTime= newTime-300;
         level++;
      }
        timer.setDelay(newTime); 
   }
 
   /*** gameover *****************************************
   * Purpose: Checks whether the game is over            *
   * Parameters: none                                    *
   * Returns: whether the game is over                   *
   ******************************************************/
   public int gameover(int x, int y) 
   {
      if (fall(x, y, blockType, turnState) == 0) 
      {
         return 1;
      }
      return 0;
   }
   
   /*** add **********************************************
   * Purpose: Add current map                            *
   * Parameters: none                                    *
   * Returns: none                                       *
   ******************************************************/
   public void add(int x, int y, int blockType, int turnState) 
   {
      int j = 0;
      for (int a = 0; a < 4; a++) 
      {
         for (int b = 0; b < 4; b++) 
         {
            if (map[x + b + 1][y + a] == 0) 
            {
               map[x + b + 1][y + a] = shapes[blockType][turnState][j];
               if(shapes[blockType][turnState][j]==1)
                  colourMap[x + b + 1][y + a]= blockType;
            }
            ;
            j++;
         }
      }
   }

   /*** paint ********************************************
   * Purpose: Draw all the blocks and the next block that*
   *          will be falling                            *
   * Parameters: g                                       *
   * Returns: none                                       *
   ******************************************************/
   public void paintComponent(Graphics g) 
   {
      super.paintComponent(g);
      g.drawImage(img, 0, 0, 683, 765, this);
      for (j = 0; j < 16; j++) 
      {
         if (shapes[blockType][turnState][j] == 1) 
         {
            switch (blockType)//goes through the array and draws a block if the 
                                      //# in the block is not zero. it also assigns color acording to the #
            {
               case 0 : g.setColor(PIECE_COLORS[0]);
                  g.fillRect((j % 4 + x + 1) * 20, (j / 4 + y) * 20, 20, 20);
                  break;
               case 1 : g.setColor(PIECE_COLORS[1]);
                  g.fillRect((j % 4 + x + 1) * 20, (j / 4 + y) * 20, 20, 20);
                  break;
               case 2 : g.setColor(PIECE_COLORS[2]);
                  g.fillRect((j % 4 + x + 1) * 20, (j / 4 + y) * 20, 20, 20);
                  break;
               case 3 : g.setColor(PIECE_COLORS[3]);
                  g.fillRect((j % 4 + x + 1) * 20, (j / 4 + y) * 20, 20, 20);
                  break;
               case 4 : g.setColor(PIECE_COLORS[4]);
                  g.fillRect((j % 4 + x + 1) * 20, (j / 4 + y) * 20, 20, 20);
                  break;
               case 5 : g.setColor(PIECE_COLORS[5]);
                  g.fillRect((j % 4 + x + 1) * 20, (j / 4 + y) * 20, 20, 20);                        
                  break;
               case 6 : g.setColor(PIECE_COLORS[6]);
                  g.fillRect((j % 4 + x + 1) * 20, (j / 4 + y) * 20, 20, 20);
                  break; 
            }
         }
      }
      
      for (int i=0; i<4; i++)
      {
         for (int j=0; j<4; j++)
         {
            if (nextBlockShapes[i][j]!=0)
            {
               switch (nextBlockType)
               {
                  case 0 :	g.setColor(PIECE_COLORS[0]);
                     g.fillRect(400+(j+1)*15, 50+(i+1)*15, 15, 15);
                     break;
                  case 1 :	g.setColor(PIECE_COLORS[1]);
                     g.fillRect(400+(j+1)*15, 50+(i+1)*15, 15, 15);
                     break;
                  case 2 :	g.setColor(PIECE_COLORS[2]);
                     g.fillRect(400+(j+1)*15, 50+(i+1)*15, 15, 15);
                     break;
                  case 3 :	g.setColor(PIECE_COLORS[3]);
                     g.fillRect(400+(j+1)*15, 50+(i+1)*15, 15, 15);
                     break;
                  case 4 :	g.setColor(PIECE_COLORS[4]);
                     g.fillRect(400+(j+1)*15, 50+(i+1)*15, 15, 15);
                     break;
                  case 5 :	g.setColor(PIECE_COLORS[5]);
                     g.fillRect(400+(j+1)*15, 50+(i+1)*15, 15, 15);								  
                     break;
                  case 6 :	g.setColor(PIECE_COLORS[6]);
                     g.fillRect(400+(j+1)*15, 50+(i+1)*15, 15, 15);
                     break; 
               }
            }
         }
      }
      
      // draw already existing blocks
      int col;
      for (j = 0; j < 36; j++) 
      {
         for (i = 0; i < 16; i++) 
         {
            if (map[i][j] == 1) 
            {
               col= giveCol(i,j);
               g.setColor(PIECE_COLORS[col]);
               g.fillRect(i * 20, j * 20, 20, 20);
            }
            if (map[i][j] == 2) 
            {
               Color myColour = new Color(255, 255, 255, 0);
               g.setColor(myColour);
            }
         }
      }
      g.setColor(Color.black);
      g.setFont(new Font("TimesRoman",	Font.BOLD, 30)); 
      g.drawString(""+ score, 450, 290);
      g.drawString(""+ level, 450, 200);
      g.setFont(new Font("TimesRoman",	Font.BOLD, 30)); 
   }
   
   /*** givCol *******************************************
   * Purpose: Return the color of one grid on the color  *
   *          map                                        *
   * Parameters: none                                    *
   * Returns: the color of one grid on the color map     *
   ******************************************************/
   public int giveCol(int a,int b)
   {
      int squareColour;
      squareColour= colourMap[a][b];
      return squareColour;
   }
   
   /*** mouseClicked *************************************
   * Purpose: Return to the main page when the return    *
   *          button is clicked                          *
   * Parameters: e                                       *
   * Returns: none                                       *
   ******************************************************/
   public void mouseClicked(MouseEvent e)      
   {
      x = e.getX();
      y = e.getY();
      if ((x>=350 && y>=600 && x<=510 && y<=730)||(x>930 && y>=600 && x<=1120 && y<=730))
      {
         end.endGAME();
         mod=true;

      }
   }

   public void mouseEntered(MouseEvent e)
   {
   }
   
   public void mouseExited(MouseEvent e)
   {
   }
   
   public void mousePressed(MouseEvent e)
   {
   }
   
   public void mouseReleased(MouseEvent e)
   {
   }

   
   /*** keyReleased **************************************
   * Purpose: Respond to up, down, left and right, "a",  *
   *          "d", "w","s" keys                          *
   * Parameters: e                                       *
   * Returns: none                                       *
   ******************************************************/
   public void keyReleased(KeyEvent e) 
   {
      switch (e.getKeyCode()) {
            case KeyEvent.VK_DOWN:
               rd=false;
               break;
            case KeyEvent.VK_UP:
               ru=false;
               break;
            case KeyEvent.VK_RIGHT:
               rr=false;
               break;
            case KeyEvent.VK_LEFT:
               rl=false;
               break;
            case KeyEvent.VK_S:
               ld=false;
               break;
            case KeyEvent.VK_W:
               lu=false;
               break;
            case KeyEvent.VK_D:
               lr=false;
               break;
            case KeyEvent.VK_A:
               ll=false;
               break;        
      }
   }
   
   /*** keyPressed ***************************************
   * Purpose: Respond to up, down, left and right, "a",  *
   *          "d", "w","s" keys                          *
   * Parameters: e                                       *
   * Returns: none                                       *
   ******************************************************/
   public void keyPressed(KeyEvent e) 
   {
      switch (e.getKeyCode()) {
            case KeyEvent.VK_DOWN:
               rd=true;
               break;
            case KeyEvent.VK_UP:
               ru=true;
               break;
            case KeyEvent.VK_RIGHT:
               rr=true;
               break;
            case KeyEvent.VK_LEFT:
               rl=true;
               break;
            case KeyEvent.VK_S:
               ld=true;
               break;
            case KeyEvent.VK_W:
               lu=true;
               break;
            case KeyEvent.VK_D:
               lr=true;
               break;
            case KeyEvent.VK_A:
               ll=true;
               break;        
      }

   }

   public void keyTyped(KeyEvent e) 
   {
   }
   
   class TimerListener implements ActionListener 
   {
      
      /*** actionPerformed **********************************
      * Purpose: Makes the block fall when no key is pressed*
      * Parameters: e                                       *
      * Returns: none                                       *
      ******************************************************/
      public void actionPerformed(ActionEvent e) 
      {
         repaint();
         if (fall(x, y + 1, blockType, turnState) == 1) 
         {
            y = y + 1;
            delline();
         }
         ;
         if (fall(x, y + 1, blockType, turnState) == 0) 
         {
            if (flag == 1) 
            {
               add(x, y, blockType, turnState);
               delline();
               newblock();
               flag = 0;
            }
            flag = 1;
         }
         ;
      }
   }
   
   class BlockTimerListener implements ActionListener 
   {
   
      /*** actionPerformed **********************************
      * Purpose: Checks whether any keys is pressed         *
      * Parameters: e                                       *
      * Returns: none                                       *
      ******************************************************/
      public void actionPerformed(ActionEvent e) 
      {
         repaint();
          if(player==1)
            {
               if(rr==true)
                  right();
               else if (rl==true)
                  left();
               else if(rd==true)
                  down();
              else if(ru==true)
                  turn();
            }     
    
          if(player==2)
          {
             if(lr==true)
                right();
             else if (ll==true)
                left();
             else if(ld==true)
                down();
            else if(lu==true)
                turn();
          }
 
      }

   }
 
}