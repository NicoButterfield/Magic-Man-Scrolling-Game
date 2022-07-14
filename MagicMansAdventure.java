import java.awt.event.KeyEvent;
import java.awt.*;

public class MagicMansAdventure extends AbstractGame {
    
    private static final int INTRO = 0; 
    private static final int RULES = 1;
    private static final int RULESCONT = 2;
    
    //Backgrounds
    private String GRASSBACKGROUND_IMG = "MagicGrass.png";
    private String SPARKLEBACKGROUND_IMG = "sparkleBackground.png";
    private String TRAPBACKGROUND_IMG = "trapBackground.png";
    private String SNOWYBACKGROUND_IMG = "snowBackground.png";
    private String TIMEBACKGROUND_IMG = "timeBackground.png";
    private String PAUSED_IMG = "pausedBackground.png";
    private String SPLASH_IMG = "introScreen.png"; 
    private String WIN_IMG = "youWin.png"; 
    private String LOSE_IMG = "youLose.png"; 
    private String BREAKDOWN_IMG = "breakdown.png"; 
    private String CONTROLS_IMG = "controls.png"; 
    private static int screenShot;
    
    //Collectibles
    private String MAGICDUST_IMG = "magicDust.png";
    private String TRAP_IMG = "trap.png";
    private String SNOWFLAKE_IMG = "snowflake.png";
    private String CLOCK_IMG = "clock.png";
    
    //Characters
    private String SNOWMAN_IMG = "SnowMan.png"; 
    private String MAGICMAN_IMG = "MagicMan.png"; 
    private String PLAYER_IMG;
    
    //Misc.
    private String FROG_IMG = "frog.png";
    private String PERSON_IMG = "person1.png";
    private String PERSON1_IMG = "person1.png";
    private String PERSON2_IMG = "person2.png";
    private String PERSON3_IMG = "person3.png";
    private String SNOWBALL_IMG = "snowball.png";
    
    
    // default number of vertical/horizontal cells: height/width of grid
    private static final int DEFAULT_GRID_H = 5;
    private static final int DEFAULT_GRID_W = 10;
    
    private static final int DEFAULT_TIMER_DELAY = 100;
    
    // default location of user at start
    private static final int DEFAULT_PLAYER_ROW = 0;
    
    protected static final int STARTING_FACTOR = 2;      // you might change that this when working on timing
    
    protected int factor = STARTING_FACTOR;
    
    protected Location player;
    
    protected int screen = INTRO;
    
    protected GameGrid grid;
    
    protected int saves = 0;
    
    protected int time = 360;
    
    protected boolean pause = false;
    
    protected int magicDust = 0;
    
    public MagicMansAdventure() 
    {
        this(DEFAULT_GRID_H, DEFAULT_GRID_W);
    }
    
    public MagicMansAdventure(int grid_h, int grid_w)
    {
         this(grid_h, grid_w, DEFAULT_TIMER_DELAY);
    }

    
    
    public MagicMansAdventure(int hdim, int wdim, int init_delay_ms) 
    {
        super(init_delay_ms);
        //set up our "board" (i.e., game grid) 
        grid = new GameGrid(hdim, wdim);   
        
    }
    
    /******************** Methods **********************/
    
    protected void initGame()
    {
         // store and initialize user position
         PLAYER_IMG = MAGICMAN_IMG;
         player = new Location(DEFAULT_PLAYER_ROW, 0);
         grid.setCellImage(player, PLAYER_IMG);
         
         updateTitle();                           
    }
    
        
    // Display the intro screen: not too interesting at the moment
    // Notice the similarity with the while structure in play()
    // sleep is required to not consume all the CPU; going too fast freezes app 
    protected void displayIntro()
    {
     
       grid.setSplash(SPLASH_IMG);
       while (screen == INTRO) 
       {
          super.sleep(timerDelay);
          // Listen to keep press to break out of intro 
          // in particular here --> space bar necessary
          handleKeyPress();
          
       }
       grid.setSplash(null);
       
       updateIntro();
       
       grid.setGameBackground(GRASSBACKGROUND_IMG);
    }
    
    //Allows for two addition intro screens to explain the game
    protected void updateIntro() 
    {
    	while(screen == RULES || screen == RULESCONT)
    	{
			if(screen == RULES)
			{
				grid.setGameBackground(BREAKDOWN_IMG);
			}
			else if (screen == RULESCONT)
			{
				grid.setGameBackground(CONTROLS_IMG);
			}
			super.sleep(timerDelay);
			handleKeyPress();
    	}
    	
    }
  
    protected void updateGameLoop() 
    {
        	while(pause == true)
        	{
				super.sleep(timerDelay);
				handleKeyPress();
        	}
			handleKeyPress();        // update state based on user key press
			handleMouseClick();      // when the game is running: 
			// click & read the console output 
			
		
			if (turnsElapsed % factor == 0) {  // if it's the FACTOR timer tick
				// constant 3 initially
				scrollLeft();
				scrollRight();
				populateRightEdge();
			} 
			updateTitle();
			
    }
    
    // update game state to reflect adding in new cells in the right-most column
    private void populateRightEdge() 
    {
    	double n = rand.nextInt(51);
    	Location loc = new Location(rand.nextInt(grid.getNumRows()), (grid.getNumCols() - 1));
    	
    	if(n >= 0  && n < 1)
    	{
    		if(PLAYER_IMG != SNOWMAN_IMG)
    		{
    			grid.setCellImage(loc, SNOWFLAKE_IMG);
    		}
    		else
    		{
    			grid.setCellImage(loc, TRAP_IMG);
    		}
        }  
        if(n >= 1  && n <= 2)
    	{
    		grid.setCellImage(loc, CLOCK_IMG);
        }    
        if(n > 2 && n <= 6)
        {
        	grid.setCellImage(loc, FROG_IMG);
        	 
        }
        if(n > 6 && n <= 40)
        {
        	grid.setCellImage(loc, TRAP_IMG);
        }
        
        if(n > 40 && n <= 50)
        {
        	grid.setCellImage(loc, MAGICDUST_IMG);
        }
    }
  
    //Moves every object on the game board one to the left
    private void scrollLeft() 
    {
    	for(int y =  0 ; y < grid.getNumCols() - 1; y++)
    	{
    		for(int x =  0 ; x < grid.getNumRows(); x++)
    		{
				Location original = new Location(x, y);
				Location move = new Location(x, y + 1);
				
				if(grid.getCellImage(original) != PLAYER_IMG && grid.getCellImage(original) != SNOWBALL_IMG)
				{
					if(grid.getCellImage(move) == PLAYER_IMG && original.getCol() == 0)
					{
						grid.setCellImage(original, null);
					}
					
					if(grid.getCellImage(move) != PLAYER_IMG)
					{
						if(grid.getCellImage(move) != SNOWBALL_IMG)
						{
							grid.setCellImage(original, grid.getCellImage(move));
							grid.setCellImage(move, null);
						}
					}
				}
				else
				{
					if(grid.getCellImage(original) == SNOWBALL_IMG)  //Hnadles the collisions with Snowballs
					{
						if(original.getCol() == grid.getNumCols() - 2)
						{
							grid.setCellImage(original, null);
						}
						else
						{
							if(grid.getCellImage(move) != TRAP_IMG)
							{
								if(player.getCol() + 1 != original.getCol())
								{
									if(original.getCol() > 0)
									{
										grid.setCellImage(new Location(original.getRow(), original.getCol() - 1), grid.getCellImage(move));
										grid.setCellImage(move, null);
									}
								}
								else
								{
									if(original.getCol() > 1)
									{
										grid.setCellImage(new Location(original.getRow(), original.getCol() - 2), grid.getCellImage(move));
										grid.setCellImage(move, null);
									}
								}
							}
							else
							{
								grid.setCellImage(original, null);
								grid.setCellImage(move, null);
							}
						}
					}
					
					if(grid.getCellImage(original) == PLAYER_IMG)
					{
						handleCollision(move);
					}
				}
    		}
    	}
    	time--; //Counts down clock
    }
    
    //Controls the movement of the snowballs as they move right
    private void scrollRight() 
    {
			for(int i = grid.getNumCols() - 2 ; i > 0; i--)
			{
				for(int j =  grid.getNumRows() - 1; j >= 0; j--)
				{
					Location original = new Location(j, i);
					Location move = new Location(j, i + 1);
					
					if(grid.getCellImage(original) == SNOWBALL_IMG)
					{
						if(original.getCol() == grid.getNumCols() - 1)
						{
							grid.setCellImage(original, null);
						}
						
						if(grid.getCellImage(move) != TRAP_IMG)
						{
								grid.setCellImage(original, grid.getCellImage(move));
								grid.setCellImage(move, SNOWBALL_IMG);
						}
						else
						{
								grid.setCellImage(original, null);
								grid.setCellImage(move, null);
						}
					
					}
				}
			}
    }
    
    /* handleCollision()
     * handle a collision between the user and various objects in the game
     */    
    private void handleCollision(Location loc) 
    {
    	if(grid.getCellImage(loc) != null)
		{
			trapCollision(loc);
			
			frogCollision(loc);
			
			dustCollision(loc);
			
			snowflakeCollision(loc);
			
			clockCollision(loc);
			
			snowBallCollision(loc);
		}
        updateTitle();
    }
    
    
    private void trapCollision(Location loc) 
    {
    	if(grid.getCellImage(loc).equals(TRAP_IMG))
			{
				for(int x = 0; x < 10; x++)
				{
					grid.setGameBackground(TRAPBACKGROUND_IMG);
					super.sleep(timerDelay);
				}
				
				grid.setGameBackground(GRASSBACKGROUND_IMG);
				magicDust = 0;
				time -= 50;
				
				if(factor > 1)
				{
					factor--;
				}
				if(PLAYER_IMG == SNOWMAN_IMG)
				{
					PLAYER_IMG = MAGICMAN_IMG;
					
					if(loc.getCol() > 0)
					{
						grid.setCellImage(new Location(loc.getRow(), loc.getCol() - 1), null);
						grid.setCellImage(new Location(loc.getRow(), loc.getCol() - 1), MAGICMAN_IMG);
					}
				}
			}
    }
      private void clockCollision(Location loc) 
    {
    	if(grid.getCellImage(loc).equals(CLOCK_IMG))
			{
				for(int x = 0; x < 1; x++)
				{
					grid.setGameBackground(CLOCK_IMG);
					super.sleep(timerDelay);
				}
				grid.setGameBackground(GRASSBACKGROUND_IMG);
				time += 25;
			}
    }
    
       private void dustCollision(Location loc) 
    {
    	if(grid.getCellImage(loc).equals(MAGICDUST_IMG))
			{
				for(int x = 0; x < 1; x++)
				{
					grid.setGameBackground(SPARKLEBACKGROUND_IMG);
					super.sleep(timerDelay);
				}
				grid.setGameBackground(GRASSBACKGROUND_IMG);
				magicDust++;
			}
    }
    
    private void snowflakeCollision(Location loc) 
    {
    	if(grid.getCellImage(loc).equals(SNOWFLAKE_IMG))
			{
				grid.setCellImage(player,null);
				PLAYER_IMG = SNOWMAN_IMG;
				grid.setCellImage(player,PLAYER_IMG); 
				
				for(int x = 0; x < 1; x++)
				{
					grid.setGameBackground(SNOWYBACKGROUND_IMG);
					super.sleep(timerDelay);
				}
				grid.setGameBackground(GRASSBACKGROUND_IMG);
				factor++;
			}
    }
    
    private void frogCollision(Location loc) 
    {
    	if(grid.getCellImage(loc).equals(FROG_IMG))
			{
				double n = rand.nextInt(12);
				
				if(player.getCol() > 0 && magicDust >= 5)
				{
					if(n >= 0 && n < 4)
					{
						PERSON_IMG = PERSON1_IMG;
					}
					else if(n >= 4 && n < 8 )
					{
						PERSON_IMG = PERSON2_IMG;
					}
					else
					{
						PERSON_IMG = PERSON3_IMG;
					}
					grid.setCellImage(new Location(player.getRow(), player.getCol() - 1), PERSON_IMG);
					saves++;
					magicDust -= 5;
				}
				else if(magicDust >= 5)
				{
					saves++;
					magicDust -= 5;
				}
				else if (player.getCol() > 0)
				{
					grid.setCellImage(new Location(player.getRow(), player.getCol() - 1), FROG_IMG);
				}
			}
	}
	
	//So magic man can run into the transformed people without making them disappear
	private void personCollision(Location loc) 
    {
		if(grid.getCellImage(loc).equals(PERSON1_IMG))
		{
				grid.setCellImage(new Location(player.getRow(), player.getCol() - 1), PERSON1_IMG);
		}
		
		if(grid.getCellImage(loc).equals(PERSON2_IMG))
		{
				grid.setCellImage(new Location(player.getRow(), player.getCol() - 1), PERSON2_IMG);
		}
		
		if(grid.getCellImage(loc).equals(PERSON3_IMG))
		{
				grid.setCellImage(new Location(player.getRow(), player.getCol() - 1), PERSON3_IMG);
		}
	}
	
	//Same as above
	private void snowBallCollision(Location loc) 
    {
		if(grid.getCellImage(loc).equals(SNOWBALL_IMG))
		{
			grid.setCellImage(new Location(player.getRow(), player.getCol() + 1), SNOWBALL_IMG);
		}
	}
    //---------------------------------------------------//
    
    // handles actions upon mouse click in game
    private void handleMouseClick() {
        
        Location loc = grid.checkLastLocationClicked();
        
        if (loc != null) 
            System.out.println("You clicked on a square " + loc);
        
    }
    
    // handles actions upon key press in game
    protected void handleKeyPress() {
        
        int key = grid.checkLastKeyPressed();
        
        //use Java constant names for key presses
        //http://docs.oracle.com/javase/7/docs/api/constant-values.html#java.awt.event.KeyEvent.VK_DOWN
        // Q for quit
        if (key == KeyEvent.VK_Q)
        {
            System.exit(0);
        }
        else if (key == KeyEvent.VK_S)
        {
        	screenShot++;
            grid.save("screenshot" + screenShot + ".png");
    	}
    	else if (key == KeyEvent.VK_P)
        {
        	if(pause)
        	{
        		pause = false;
        		grid.setGameBackground(GRASSBACKGROUND_IMG);
        	}
        	else
        	{
        		pause = true; 
        		grid.setGameBackground(PAUSED_IMG);
        	}
    	}
        else if (key == KeyEvent.VK_SPACE)
        {
           screen += 1;
        }
        else if (key == KeyEvent.VK_D)
        {
           if(grid.getLineColor() == null)
           {
           	   grid.setLineColor(Color.RED);
           }
           else
           {
           	   grid.setLineColor(null);
           }
        }
        else if(key == KeyEvent.VK_UP)
        {
        	if(player.getRow() != 0)
        	{
        		grid.setCellImage(player, null);
        		player.decRow();
        		handleCollision(player);
        		grid.setCellImage(player, PLAYER_IMG);
        		
        	}
        }
        else if(key == KeyEvent.VK_DOWN)
        {
        	if(player.getRow() != grid.getNumRows() - 1)
        	{
        		grid.setCellImage(player, null);
        		player.incRow();
        		handleCollision(player);
        		grid.setCellImage(player, PLAYER_IMG);
        	}
        }
       else if(key == KeyEvent.VK_RIGHT)
       {
      		if(player.getCol() != grid.getNumCols() - 1)
        	{
        		grid.setCellImage(player, null);
        		player.incCol();
        		handleCollision(player);
        		grid.setCellImage(player, PLAYER_IMG);
        	}           
        }
        else if(key == KeyEvent.VK_LEFT)
        {
        	if(player.getCol() != 0)
        	{
        		grid.setCellImage(player, null);
        		player.decCol();
        		handleCollision(player);
        		grid.setCellImage(player, PLAYER_IMG);
        	}           
        }
        else if(key == KeyEvent.VK_CONTROL) //Shoots snowballs
        {
        	if(PLAYER_IMG == SNOWMAN_IMG && magicDust > 0)
        	{
        		grid.setCellImage(new Location (player.getRow(), player.getCol() + 1), SNOWBALL_IMG);
        		magicDust--;
        	}
        }
            

        /* To help you with step 9: 
         use the 'T' key to help you with implementing speed up/slow down/pause
         this prints out a debugging message */
        else if (key == KeyEvent.VK_T)  {
            boolean interval =  (turnsElapsed % factor == 0);
            System.out.println("timerDelay " + timerDelay + " msElapsed reset " + 
                               msElapsed + " interval " + interval);
        } 
    }
    
    // returns the number of people that have been transformed
    private int getSaves() 
    {
        return saves;    
    }
    
    private int getDust() 
    {
        return magicDust;    
    }
    
    private String getTime() 
    {
   
    	int minutes = time / 60; 
    	int seconds = time % 60;
        return minutes + " minutes. " + seconds + " seconds.";    
    }
    
    // update the title bar of the game window 
    private void updateTitle() 
    {
        grid.setTitle("Magic Man's Mad Dash- Frogs Transformed: " + getSaves() + " Dust collected: " + getDust() + " Time Left: " + getTime());
    }
    
    // return true if the game is finished, false otherwise
    //      used by play() to terminate the main game loop 
    protected boolean isGameOver() 
    {
    	if(time < 0)
    	{
    		return true;
        }
     
        return false;
    }
    
    protected void clearBoard() //Removes images from the end screen
    {
    	for(int x = 0; x < grid.getNumRows(); x++)
		{
			for(int y = 0 ; y < grid.getNumCols(); y++)
			{
				Location loc = new Location(x, y);
				grid.setCellImage(loc, null);
			}
		}
	}		
    
    // display the game over screen, blank for now
    protected void displayOutcome() 
    {
    		grid.setTitle("YOU SAVED " + getSaves() + " PEOPLE FROM LIFE AS A FROG!!");
    		
    		if(getSaves() >= 10)
    		{
    			grid.setGameBackground(WIN_IMG);
    		}
    		else
    		{
    			grid.setGameBackground(LOSE_IMG);
    		}
    		clearBoard();
    }
}
