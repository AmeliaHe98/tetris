import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;

/*********************************************************
   This is the main file for one player game
*********************************************************/

public class OnePlayer extends JFrame 
{
   OnePEndGame end2;
   //if an error occures, rewrite tetrisblock(just the word)!!
   tetrisblock a	= new tetrisblock();

   public OnePlayer() 
   {
      end2= new OnePEndGame(a);
      a.finalEnd(end2);
      addKeyListener(a);
      add(a);
      addMouseListener(a);
      add(a);
   }
   
   public static void main(String[]	args)	
   {
      OnePlayer frame =	new OnePlayer();
      frame.setLocationRelativeTo(null);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(1200, 750);
      frame.setTitle("One Player");
      frame.setVisible(true);
   }
}
class	tetrisblock extends JPanel implements	KeyListener,MouseListener
{
   OnePEndGame end2;
   private int	blockType;
   public static int score = 0;
   public static int level = 1;
   public static boolean mod = false;
   private int	turnState;
   private int	x;
   private int	y;
   private int	i = 0;
   int screen;
   int j	= 0;
   int flag	= 0;
   int[][] map	= new	int[17][21];
   int[][] colourMap	= new	int[17][21];
   int howManyLines;
   int newTime	= 1000;
   BufferedImage img;
   Timer timer;   
   //nextBlock
   int nextBlockType;
   int nextTurnState;   
   int nextBlockShapes[][]=new int [4][4];
   
   private final int	shapes[][][] =	new int[][][] 
   {
      //L
      {	{ 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
   		{ 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0 },
   		{ 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
   		{ 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0 } },
   	// S
   	{	{ 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
   		{ 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
   		{ 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
   		{ 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 }	},
      // Z
   	{	{ 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
   		{ 0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
   		{ 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
   		{ 0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 }	},
   	// J
   	{	{ 0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0 },
   	   { 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
   		{ 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
   		{ 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 }	},
   	// O
   	{	{ 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
   		{ 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
      	{ 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
   		{ 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }	},
   	// I
   	{	{ 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0 },
   		{ 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
         { 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
   		{ 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 }	},
       // t
      {	{ 0, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
      	{ 0, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
   		{ 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
   		{ 0, 1, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0 }	} 
   };
	
   Color[] PIECE_COLORS	= 
   {
   		new Color(0xFF00FF),	//	fucia
   		new Color(0xDC143C),	//	crimson
   		new Color(0x00CED1),	//	dark turquoise
   		new Color(0xFFD700),	//	gold
   		new Color(0x32CD32),	//	lime green
   		new Color(0x008080),	//	teal
   		new Color(0xFFA500),	//	orange
   };
   	 
   public void finalEnd(OnePEndGame endl)
  { 
   end2=endl;
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
   public void	newblock() 
   {
      blockType =	nextBlockType;
      turnState =	nextTurnState;
      x = 4;
      y = 0;
      nextBlock();
      if	(isGameOver()==true||mod==true) 
      {
         newmap();
         drawwall();
         newTime=1000;
         repaint();
         end2.endGAME(score);
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
   public void	drawwall() 
   {
      for (i =	0;	i < 16; i++) 
      {
         map[i][19] = 2;
      }
      for (j =	0;	j < 20; j++) 
      {
         map[15][j] = 2;
         map[0][j] =	2;
      }
   }
	
   /*** newmap *******************************************
   * Purpose: Initialize the map by setting the values   *
   *          in the grid to 0                           *
   * Parameters: none                                    *
   * Returns: none                                       *
   ******************************************************/
   public void	newmap()	
   {
      for (i =	0;	i < 16; i++) 
      {
         for (j =	0;	j < 20; j++) 
         {
            map[i][j] =	0;
         }
      }
   }
	
   /*** newColourMap *************************************
   * Purpose: Initialize the color map that stores all   *
   *          the colors of the blocks                   *
   * Parameters: none                                    *
   * Returns: none                                       *
   ******************************************************/
   public void	newColourMap()	
   {
      for (i =	0;	i < 16; i++) 
      {
         for (j =	0;	j < 20; j++) 
         {
            colourMap[i][j] =	-1;
         }
      }
   }
	
   tetrisblock()	
   {
      score=0;
      level=1;
      nextBlock();
      newblock();
      newmap();
      newColourMap();
      drawwall();
      timer	= new	Timer(newTime,	new TimerListener());
      timer.start();
      try 
      {
         img =	ImageIO.read(new File("tetris grid.png"));
      } 
      catch	(IOException e) 
      {
         e.printStackTrace();
      }   }
    
   /*** mouseClicked *************************************
   * Purpose: When the return button is clicked, the game*
   *          goes back to main page                     *
   *          page                                       *
   * Parameters: e                                       *
   * Returns: none                                       *
   ******************************************************/
    public void mouseClicked(MouseEvent e)      
    {
      x = e.getX();
      y = e.getY();
      if (x>=620 && y>=600 && x<=1000 && y<=730)
      {
         mod=true;
         end2.endGAME(score);

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

   /*** turn *********************************************
   * Purpose: Changes the orientation of the block       *
   * Parameters: none                                    *
   * Returns: none                                       *
   ******************************************************/
   public void	turn() 
   {
      int tempturnState	= turnState;
      turnState =	(turnState + 1) %	4;
      if	(fall(x,	y,	blockType, turnState) == 1) 
      {
      }
      if	(fall(x,	y,	blockType, turnState) == 0) 
      {
         turnState =	tempturnState;
      }
      repaint();
   }
 
   /*** left *********************************************
   * Purpose: Shifts the block left by one               *
   * Parameters: none                                    *
   * Returns: none                                       *
   ******************************************************/ 
   public void	left() 
   {
      if	(fall(x - 1, y, blockType,	turnState) == 1) 
      {
         x = x	- 1;
      }
   	
      repaint();
   }

   /*** right ********************************************
   * Purpose: Shifts the block right by one              *
   * Parameters: none                                    *
   * Returns: none                                       *
   ******************************************************/	
   public void	right() 
   {
      if	(fall(x + 1, y, blockType,	turnState) == 1) 
      {
         x = x	+ 1;
      } 	
      repaint();
   }
 
   /*** down *********************************************
   * Purpose: Shifts the block down by one               *
   * Parameters: none                                    *
   * Returns: none                                       *
   ******************************************************/ 
   public void	down() 
   {
      if	(fall(x,	y + 1, blockType,	turnState) == 1) 
      {
         y = y	+ 1;
         delline();
      }
   	
      if	(fall(x,	y + 1, blockType,	turnState) == 0) 
      {
         add(x, y, blockType,	turnState);
         newblock();
         delline();
      }
      repaint();
   }

   /*** fall *********************************************
   * Purpose: Makes the blocks fall faster               *
   * Parameters: none                                    *
   * Returns: whether the block is at the bottom         *
   ******************************************************/  
   public int fall(int x, int	y,	int blockType,	int turnState)	
   {
      if(mod==false)
      for (int	a = 0; a	< 4; a++) 
      {
         for (int	b = 0; b	< 4; b++) 
         {
            if	(((shapes[blockType][turnState][a *	4 + b] == 1) && (map[x
            		 +	b + 1][y	+ a] == 1))
            		 || ((shapes[blockType][turnState][a *	4 + b] == 1) && (map[x
            					+ b +	1][y + a] == 2)))	
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
   public void	delline() 
   {
      int c	= 0;
      for (int	b = 0; b	< 20;	b++) 
      {
         for (int	a = 0; a	< 16;	a++) 
         {
            if	(map[a][b] == 1) 
            {
               c = c	+ 1;
               if	(c	==	14) 
               {
                  score	+=	100;
                  howManyLines++;
                  speed();
                  
                  for (int	d = b; d	> 0; d--) 
                  {
                     for (int	e = 0; e	< 16;	e++)
                     {
                        map[e][d] =	map[e][d	- 1];
                        colourMap[e][d] =	colourMap[e][d	- 1];
                     }
                  }
               }
            }
         }
         c = 0;
      }
      
   }

   /*** speed ********************************************
   * Purpose: Speed up the falling of blocks when every  *
   *          five lines are cleared                     *
   * Parameters: none                                    *
   * Returns: none                                       *
   ******************************************************/   
   public void speed()
   {
      if (howManyLines%5==0)
      {
         newTime=newTime-300;
         level++;
      } 
      timer.setDelay(newTime);
   }

   /*** gameover *****************************************
   * Purpose: Checks whether the game is over            *
   * Parameters: none                                    *
   * Returns: whether the game is over                   *
   ******************************************************/	
   public int gameover(int	x,	int y) 
   {
      if	(fall(x,	y,	blockType, turnState) == 0) 
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
   public void	add(int x, int	y,	int blockType,	int turnState)	
   {
      int j	= 0;
      for (int	a = 0; a	< 4; a++) 
      {
         for (int	b = 0; b	< 4; b++) 
         {
            if	(map[x +	b + 1][y	+ a] == 0) 
            {
               map[x	+ b +	1][y + a] =	shapes[blockType][turnState][j];
               if(shapes[blockType][turnState][j]==1)
                  colourMap[x	+ b +	1][y + a]= blockType;
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
   public void	paintComponent(Graphics	g)	
   {
      super.paintComponent(g);
      g.drawImage(img, 0, 0, getWidth(), getHeight(),	this);
   
      for (j =	0;	j < 16; j++) {
         if	(shapes[blockType][turnState][j]	==	1)	
         {
            switch (blockType)//goes through	the array and draws a block if the 
            								  //#	in	the block is not zero. it also assigns	color	acording	to	the #
            {
               case 0 :	g.setColor(PIECE_COLORS[0]);
                  g.fillRect((j % 4	+ x +	1)	* 35,	(j	/ 4 +	y)	* 35,	35, 35);
                  break;
               case 1 :	g.setColor(PIECE_COLORS[1]);
                  g.fillRect((j % 4	+ x +	1)	* 35,	(j	/ 4 +	y)	* 35,	35, 35);
                  break;
               case 2 :	g.setColor(PIECE_COLORS[2]);
                  g.fillRect((j % 4	+ x +	1)	* 35,	(j	/ 4 +	y)	* 35,	35, 35);
                  break;
               case 3 :	g.setColor(PIECE_COLORS[3]);
                  g.fillRect((j % 4	+ x +	1)	* 35,	(j	/ 4 +	y)	* 35,	35, 35);
                  break;
               case 4 :	g.setColor(PIECE_COLORS[4]);
                  g.fillRect((j % 4	+ x +	1)	* 35,	(j	/ 4 +	y)	* 35,	35, 35);
                  break;
               case 5 :	g.setColor(PIECE_COLORS[5]);
                  g.fillRect((j % 4	+ x +	1)	* 35,	(j	/ 4 +	y)	* 35,	35, 35);								  
                  break;
               case 6 :	g.setColor(PIECE_COLORS[6]);
                  g.fillRect((j % 4	+ x +	1)	* 35,	(j	/ 4 +	y)	* 35,	35, 35);
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
                     g.fillRect(700+(j+1)*20, 3+(i+1)*20, 20, 20);
                     break;
                  case 1 :	g.setColor(PIECE_COLORS[1]);
                     g.fillRect(700+(j+1)*20, 3+(i+1)*20, 20, 20);
                     break;
                  case 2 :	g.setColor(PIECE_COLORS[2]);
                     g.fillRect(700+(j+1)*20, 3+(i+1)*20, 20, 20);
                     break;
                  case 3 :	g.setColor(PIECE_COLORS[3]);
                     g.fillRect(700+(j+1)*20, 3+(i+1)*20, 20, 20);
                     break;
                  case 4 :	g.setColor(PIECE_COLORS[4]);
                     g.fillRect(700+(j+1)*20, 3+(i+1)*20, 20, 20);
                     break;
                  case 5 :	g.setColor(PIECE_COLORS[5]);
                     g.fillRect(700+(j+1)*20, 3+(i+1)*20, 20, 20);								  
                     break;
                  case 6 :	g.setColor(PIECE_COLORS[6]);
                     g.fillRect(700+(j+1)*20, 3+(i+1)*20, 20, 20);
                     break; 
               }
            }
         }
      }

   	//	draw already existing blocks
      int col;
      for (j =	0;	j < 20; j++) 
      {
         for (i =	0;	i < 16; i++) 
         {
            if	(map[i][j] == 1) 
            {
               col= giveCol(i,j);
               g.setColor(PIECE_COLORS[col]);
               g.fillRect(i *	35, j	* 35,	35, 35);
            }
            if	(map[i][j] == 2) 
            {
               Color myColour = new Color(255, 255, 255, 0);
               g.setColor(myColour);
               g.drawRect(i *	35, j	* 35,	35, 35);
            }
         }
      }
      g.setColor(Color.black);
      g.setFont(new Font("TimesRoman",	Font.BOLD, 40)); 
      g.drawString(""+score, 760, 273);
      g.drawString(""+level, 760, 192);
      //draws string
      if	(score>=0 && score<=500)
         level=1;
      else if (score>500 && score<=1000)
         level=2;
      else if (score>1000 && score<=1500)
         level=3;
      else if (score>1500 && score<=2000)
         level=4;
      else if (score>2000 && score<=2500)
         level=5;
      else if (score>2500 && score<=3000)
         level=6;
      else if (score>3000 && score<=3500)
         level=7;
      else if (score>3500 && score<=4000)
         level=8;
      else if (score>4000)
         level=9;
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
	
   /*** keyPressed ***************************************
   * Purpose: Respond to up, down, left and right keys   *
   * Parameters: e                                       *
   * Returns: none                                       *
   ******************************************************/
   public void	keyPressed(KeyEvent e) 
   {
      switch (e.getKeyCode())	
      {
         case KeyEvent.VK_DOWN:
            down();
            break;
         case KeyEvent.VK_UP:
            turn();
            break;
         case KeyEvent.VK_RIGHT:
            right();
            break;
         case KeyEvent.VK_LEFT:
            left();
            break;
      }
   }

   public void	keyReleased(KeyEvent	e)	
   {
   }
   
   public void	keyTyped(KeyEvent	e)	
   {
   }
	
   class	TimerListener implements ActionListener 
   {
   
      /*** actionPerformed **********************************
      * Purpose: Makes the block fall when no key is pressed*
      * Parameters: e                                       *
      * Returns: none                                       *
      ******************************************************/
      public void	actionPerformed(ActionEvent e) 
      {
         repaint();
         if	(fall(x,	y + 1, blockType,	turnState) == 1) 
         {
            y = y	+ 1;
            delline();
         }
         if	(fall(x,	y + 1, blockType,	turnState) == 0) 
         {
            if	(flag	==	1)	
            {
               add(x, y, blockType,	turnState);
               delline();
               newblock();
               flag = 0;
            }
            flag = 1;
         }
      }
   }
}
