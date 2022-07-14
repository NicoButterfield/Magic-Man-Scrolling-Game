import java.awt.event.KeyEvent;
import java.awt.*;

public class BaseGame extends AbstractGame {
    
    private static final int INTRO = 0; 

    private String PLAYER_IMG = "user.gif";    // specify user image file
    private String SPLASH_IMG = "ink.png"; 
    private String BACKGROUND_IMG = "MagicGrass.png";
    private String AVOID_IMG = "avoid.gif";  // ADD others for Avoid/Get items 
    private String GET_IMG = "get.gif"; 
   
    
    
    // default number of vertical/horizontal cells: height/width of grid
    private static final int DEFAULT_GRID_H = 5;
    private static final int DEFAULT_GRID_W = 10;
    
    private static final int DEFAULT_TIMER_DELAY = 100;
    
    // default location of user at start
    private static final int DEFAULT_PLAYER_ROW = 0;
    
    protected static final int STARTING_FACTOR = 3;      // you might change that this when working on timing
    
    protected int factor = STARTING_FACTOR;
    
    protected Location player;
    
    protected int screen = INTRO;
    
    protected GameGrid grid;
    
    protected int gameScore = 0;
    
    protected int lives = 3;
    
    protected boolean pause = false;
    
    protected int screenShot;
    
    public BaseGame() 
    {
        this(DEFAULT_GRID_H, DEFAULT_GRID_W);
    }
    
    public BaseGame(int grid_h, int grid_w)
    {
         this(grid_h, grid_w, DEFAULT_TIMER_DELAY);
    }

    
    
    public BaseGame(int hdim, int wdim, int init_delay_ms) 
    {
        super(init_delay_ms);
        //set up our "board" (i.e., game grid) 
        grid = new GameGrid(hdim, wdim);   
        
    }
    
    /******************** Methods **********************/
    
    protected void initGame()
    {
         // store and initialize user position
         player = new Location(DEFAULT_PLAYER_ROW, 0);
         grid.setCellImage(player, PLAYER_IMG);
         
         updateTitle();                           
    }
    
        
    // Display the intro screen: not too interesting at the moment
    // Notice the similarity with the while structure in play()
    // sleep is required to not consume all the CPU; going too fast freezes app 
    protected void displayIntro(){
     
        grid.setSplash(SPLASH_IMG);
       while (screen == INTRO) {
          super.sleep(timerDelay);
          // Listen to keep press to break out of intro 
          // in particular here --> space bar necessary
          handleKeyPress();
       }
       
       grid.setSplash(null);
       
       grid.setGameBackground(BACKGROUND_IMG);
    }
  
    protected void updateGameLoop() {
        
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
            populateRightEdge();
        }     
        updateTitle();
    }
    
    // update game state to reflect adding in new cells in the right-most column
    private void populateRightEdge() 
    {
    	int n = rand.nextInt(3);
    	Location loc = new Location(rand.nextInt(grid.getNumRows()), (grid.getNumCols() - 1));
    
    	if(n == 0)
    	{
    		grid.setCellImage(loc, AVOID_IMG);
        }
        if(n == 1)
        {
        	grid.setCellImage(loc, GET_IMG);
        	 
        }
        if(n == 2)
        {
        	grid.setCellImage(loc, null);
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
				if(grid.getCellImage(original) != PLAYER_IMG)
				{
					if(grid.getCellImage(move) != PLAYER_IMG)
					{
						grid.setCellImage(original, grid.getCellImage(move));
						grid.setCellImage(move, null);
					}
				}
				else
				{
						handleCollision(move);
				}
    		}
    		
    	}
    }
    
    /* handleCollision()
     * handle a collision between the user and an object in the game
     */    
    private void handleCollision(Location loc) 
    {
    	if(grid.getCellImage(loc) != null)
		{
			if(grid.getCellImage(loc).equals(AVOID_IMG))
			{
				lives--;
			}
			
			if(grid.getCellImage(loc).equals(GET_IMG))
			{
				gameScore += 10;
			}
		}
        updateTitle();
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
        	if(!pause)
        	{
        		pause = true;
        	}
        	else
        	{
        		pause = false;
        	}
    	}
        else if (key == KeyEvent.VK_SPACE)
        {
           screen += 1;
        }
        else if (key == KeyEvent.VK_COMMA)
        {
        	if(factor != 1)
        	{
        		factor--;
        	}
    	}
    	else if (key == KeyEvent.VK_PERIOD)
        {
        	factor++;
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
            

        /* To help you with step 9: 
         use the 'T' key to help you with implementing speed up/slow down/pause
         this prints out a debugging message */
        else if (key == KeyEvent.VK_T)  {
            boolean interval =  (turnsElapsed % factor == 0);
            System.out.println("timerDelay " + timerDelay + " msElapsed reset " + 
                               msElapsed + " interval " + interval);
        } 
    }
    
    // return the "score" of the game 
    private int getScore() 
    {
        return gameScore;    //dummy for now
    }
    
    private int getLives() 
    {
        return lives;    //dummy for now
    }
    
    // update the title bar of the game window 
    private void updateTitle() 
    {
        grid.setTitle("Scrolling Game-  Score: " + getScore() + " Lives: " + getLives());
    }
    
    // return true if the game is finished, false otherwise
    //      used by play() to terminate the main game loop 
    protected boolean isGameOver() 
    {
    	if(getScore() == 100)
    	{
    		return true;
    	}
    	if(getLives() == 0)
    	{
    		return true;
        }
     
        return false;
    }
    
    // display the game over screen, blank for now
    protected void displayOutcome() 
    {
    	if(getLives() == 0)
    	{
    		//screen = 2;
    		System.out.println("You lose!");
    	}
    	if(getScore() == 100 && getLives() > 0)
    	{
    		//screen = 3;
    		System.out.println("You win!");
    	}
    }
}
