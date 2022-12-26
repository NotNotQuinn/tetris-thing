import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class Mario here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Mario extends Actor
{
    private final int gravity = 1;
    private int velocity = 0;

    public void act()
    {
        fall();
        if( Greenfoot.isKeyDown ("space") && isOnGround())
        {
            jump();
        }
        checkDeath();
        checkKeys();
    }

    /**
     * Act - do whatever the Mario wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void fall()
    {
        if (velocity < 0 && !canMoveUp()) {
            velocity = 0;
        }
        setLocation(getX(),getY()+ velocity);
        if(isOnGround()) {
            velocity = 0;
        }
        else {
            velocity += gravity;
        }
    }

    public void jump()
    {
        velocity = -15;
    }

    /**
     * Returns true if Mario is on the ground.
     */
    private boolean isOnGround()
    {
        return !canMoveDown();
    }

    public boolean canMoveDown() {
        TetrisWorld world = (TetrisWorld)getWorld();
        int boardY = world.player1.getBoardY(getY());
        int boardX = world.player1.getBoardX(getX());

        // outside world boundries
        if (world.player1.getY() - TetrisBoard.BLOCK_PIXEL_SIZE/2 + ((TetrisBoard.BLOCK_PIXEL_SIZE*TetrisBoard.MAX_Y)/2) <= getY())
        {
            setLocation(getX(), world.player1.getY() - TetrisBoard.BLOCK_PIXEL_SIZE/2 + ((TetrisBoard.BLOCK_PIXEL_SIZE*TetrisBoard.MAX_Y)/2));
            return false;
        }

        for (TetrisSquare sq: world.player1.board)
        {
            if (sq.boardY == boardY-1 && sq.boardX == boardX && sq.getY()-TetrisBoard.BLOCK_PIXEL_SIZE <= getY()) {
                setLocation(getX(), sq.getY()-TetrisBoard.BLOCK_PIXEL_SIZE+1);
                return false;
            }
        }

        return true;
    }

    public boolean canMoveUp() {
        TetrisWorld world = (TetrisWorld)getWorld();
        int boardY = world.player1.getBoardY(getY());
        int boardX = world.player1.getBoardX(getX());

        // outside world boundries
        if (world.player1.getY() + TetrisBoard.BLOCK_PIXEL_SIZE/2 - ((TetrisBoard.BLOCK_PIXEL_SIZE*TetrisBoard.MAX_Y)/2) >= getY())
        {
            // This means the player 2 has reached the top!
            world.gameOver("Player 2 wins!\n\nThey reached\nthe top.");
            setLocation(getX(), world.player1.getY() + TetrisBoard.BLOCK_PIXEL_SIZE/2 - ((TetrisBoard.BLOCK_PIXEL_SIZE*TetrisBoard.MAX_Y)/2));
            return false;
        }

        for (TetrisSquare sq: world.player1.board)
        {
            if (sq.boardY == boardY+1 && sq.boardX == boardX && sq.getY()+TetrisBoard.BLOCK_PIXEL_SIZE <= getY()) {
                setLocation(getX(), sq.getY()+TetrisBoard.BLOCK_PIXEL_SIZE-1);
                return false;
            }
        }

        return true;
    }

    public boolean canMoveRight() {
        TetrisWorld world = (TetrisWorld)getWorld();
        int boardY = world.player1.getBoardY(getY());
        int boardX = world.player1.getBoardX(getX());

        // outside world boundries
        if (world.player1.getX() - TetrisBoard.BLOCK_PIXEL_SIZE/2 + ((TetrisBoard.BLOCK_PIXEL_SIZE*TetrisBoard.MAX_X)/2) <= getX())
        {
            setLocation(world.player1.getX() - TetrisBoard.BLOCK_PIXEL_SIZE/2 + ((TetrisBoard.BLOCK_PIXEL_SIZE*TetrisBoard.MAX_X)/2), getY());
            return false;
        }

        for (TetrisSquare sq: world.player1.board)
        {
            if (sq.boardY == boardY && sq.boardX == boardX+1 && sq.getX()-TetrisBoard.BLOCK_PIXEL_SIZE <= getX()) {
                setLocation(sq.getX()-TetrisBoard.BLOCK_PIXEL_SIZE+1, getY());
                return false;
            }
        }

        return true;
    }

    public boolean canMoveLeft() {
        TetrisWorld world = (TetrisWorld)getWorld();
        int boardY = world.player1.getBoardY(getY());
        int boardX = world.player1.getBoardX(getX());

        // outside world boundries
        if (world.player1.getX() + TetrisBoard.BLOCK_PIXEL_SIZE/2 - ((TetrisBoard.BLOCK_PIXEL_SIZE*TetrisBoard.MAX_X)/2) >= getX())
        {
            setLocation(world.player1.getX() + TetrisBoard.BLOCK_PIXEL_SIZE/2 - ((TetrisBoard.BLOCK_PIXEL_SIZE*TetrisBoard.MAX_X)/2), getY());
            return false;
        }

        for (TetrisSquare sq: world.player1.board)
        {
            if (sq.boardY == boardY && sq.boardX == boardX-1 && sq.getX()+TetrisBoard.BLOCK_PIXEL_SIZE >= getX()) {
                setLocation(sq.getX()+TetrisBoard.BLOCK_PIXEL_SIZE-1, getY());
                return false;
            }
        }

        return true;
    }

    public void checkKeys()
    {
        if(Greenfoot.isKeyDown("d"))
        {
            // Checks if the playter is not to the right of the wall of the tetris board
            if (canMoveRight())
            {
                move(5);
                setImage("first step.png");
                setImage("run.png");
            }
        }
        if(Greenfoot.isKeyDown("a"))
        {
            // Checks if the playter is not to the left of the wall of the tetris board
            if (canMoveLeft())
            {
                move(-5);
                setImage("first step.png");
                setImage("run.png");
            }
        }
    }

    private void checkDeath()
    {
        if (!isOnGround()) return;

        TetrisWorld world = (TetrisWorld)getWorld();
        int boardY = world.player1.getBoardY(getY());
        int boardX = world.player1.getBoardX(getX());

        for (TetrisSquare sq: world.player1.board)
        {
            if (sq.boardX == boardX && sq.boardY == boardY)
            {
                world.gameOver("Player 1 wins!\n\nPlayer 2 was\nsquashed.");
            }
        }
    }
}

