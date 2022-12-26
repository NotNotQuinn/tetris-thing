import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class MyWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TetrisWorld extends World
{
    public TetrisBoard player1 = new TetrisBoard();
    public Mario player2 = new Mario();
    public Counter timerCounter = new Counter("Time remaining: ");
    private SimpleTimer timer;
    private ScoreBoard scoreBoard;
    private ScoreBoard rules = new ScoreBoard("Rules:\n\n    Player 1 is the tetris player.\n    Player 2 is Mario.\n\n    1.  Player 1 wins if player 2 gets squashed.\n    2.  Player 1 loses if they lose at tetris.\n    3.  Player 2 wins if they reach the top.\n    4.  Player 2 wins if one minute passes.\n\n\nControls:\n\n    Player 1 uses the left and right arrows\n    to move and up to rotate the block.\n\n    Player 2 uses \"a\" and \"d\" keys to move,\n    and the space bar to jump.", 12f);
    public static int timeLimit = 60;

    /**
     * Creates a world
     */
    public TetrisWorld()
    {
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(700, 700, 1);
        Greenfoot.setSpeed(50);
        timerCounter.setValue(timeLimit);

        // Create tetris board
        int tetrisBoardX = 0
                // the length of the margin from the top of the board to the top of the world
            + (getHeight() - (TetrisBoard.MAX_Y*TetrisBoard.BLOCK_PIXEL_SIZE)+1)/2
                // The distance from the centre of the board to the left side of the board
            + (TetrisBoard.BLOCK_PIXEL_SIZE*TetrisBoard.MAX_X)/2
                // left 3 blocks
            - TetrisBoard.BLOCK_PIXEL_SIZE*3
                // offset to make it look better
            - 1;

        int tetrisBoardY = getHeight()/2 /* offset */ - 1;
        addObject(player1, tetrisBoardX, tetrisBoardY);
        addObject(player2, tetrisBoardX, tetrisBoardY);
        addObject(rules, 495, 395);
        addObject(timerCounter, tetrisBoardX, (int)((getHeight() - (TetrisBoard.MAX_Y*TetrisBoard.BLOCK_PIXEL_SIZE)+1)*1.75) + (TetrisBoard.BLOCK_PIXEL_SIZE*TetrisBoard.MAX_X));
        // player1.debug();
        setPaintOrder(new Class[]{ Mario.class, TetrisSquare.class });
    }

    public void started()
    {
        if (timer == null) {
            timer = new SimpleTimer();
            timer.mark();
        }
    }

    public void act()
    {
        int elapsed = timer.millisElapsed();
        timerCounter.setValue(timeLimit - (elapsed / 1000));

        if (timerCounter.getValue() <= 0) {
            gameOver("Player 2 wins!\nTime ran out.");
        }
    }

    public void gameOver(String message)
    {
        if (scoreBoard == null) {
            removeObject(rules);
            scoreBoard = new ScoreBoard(message);
            addObject(scoreBoard, 495, 395);
        }

        Greenfoot.stop();
    }
}
