import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
import java.lang.*;

/**
 * TetrisBoard is where all the gameplay lives.
 *
 * @author Quinn
 * @version April 21
 */
public class TetrisBoard extends Actor
{
    // List of blocks that exist on this world
    public List<TetrisSquare> board = new ArrayList<TetrisSquare>();
    // The number of blocks wide the tetris board will be
    public static final int MAX_X = 10;
    // The number of blocks high the tetris board will be
    public static final int MAX_Y = 19;
    // The size of each tetris square in pixels (images must be this size +1, so the borders overlap)
    public static final int BLOCK_PIXEL_SIZE = 25;
    // The current piece that is falling and being controlled by the player 1
    private TetrisBlock activePiece;
    // How many times the act method has been called. Used for timing.
    public int ticks = 0;
    // How often in ticks the active piece falls for player 1
    private int blockFallSpeed = 40;
    // How long in ticks for a key hold to count again
    private int keyDelay = 5;
    // Tracks the last key press for each key in ticks
    private HashMap<String, Integer> lastKeyPress = new HashMap<String, Integer>();

    /**
     * Constructor for objects of class TetrisBoard.
     */
    public TetrisBoard()
    {
        updateImage();
    }

    /**
     * Adds a block to the world. Does not reposition squares.
     */
    public void addBlock(TetrisBlock block)
    {
        TetrisSquare[] sqs = block.getSquares();
        for (TetrisSquare sq: sqs)
            if (sq != null)
                getWorld().addObject(sq, 0, 0);
        board.addAll(Arrays.asList(sqs));
    }

    /**
     * Debug method.
     */
    public void debug() {
        addBlock(new TetrisBlock("O", 0, 0));
        addBlock(new TetrisBlock("O", 2, 0));
        addBlock(new TetrisBlock("O", 4, 0));
        addBlock(new TetrisBlock("O", 6, 0));
        addBlock(new TetrisBlock("O", 8, 0));
        addBlock(new TetrisBlock("O", 0, 2));
        addBlock(new TetrisBlock("O", 8, 2));
        addBlock(new TetrisBlock("O", 8, 2));
        addBlock(new TetrisBlock("O", 8, 4));
        addBlock(new TetrisBlock("O", 8, 6));
        addBlock(new TetrisBlock("O", 8, 8));
        // addBlock(new TetrisBlock("O", 8, 10));

        repositionSquares(board);
    }

    /**
     * Update the image with the board.
     */
    public void updateImage()
    {
        GreenfootImage image = new GreenfootImage(
                MAX_X * BLOCK_PIXEL_SIZE+1,
                MAX_Y * BLOCK_PIXEL_SIZE+1
            );

        for(int i = 0; i < MAX_X; i++)
        {
            for(int j = 0; j < MAX_Y; j++)
            {
                // Adds the border for the square

                image.setColor(new Color(255,255,255));
                image.drawRect(
                    i*BLOCK_PIXEL_SIZE,
                    j*BLOCK_PIXEL_SIZE,
                    BLOCK_PIXEL_SIZE,
                    BLOCK_PIXEL_SIZE
                );
            }
        }

        image.setColor(new Color(0,0,0));
        image.drawRect(
            0,0,
            MAX_X*BLOCK_PIXEL_SIZE,
            MAX_Y*BLOCK_PIXEL_SIZE
        );
        image.setTransparency(105);
        setImage(image);
    }

    public void act()
    {
        checkKeys();
        if (++ticks % blockFallSpeed == 0) {
            simulateSingle();
        }
        // if (activePiece != null) {
        // activePiece.reshape(TetrisBlock.shapeMapO);
        // Mario mario = getObjectsInRange(9999, Mario.class).get(0);
        // activePiece.boardX = getBoardX(mario.getX());
        // activePiece.boardY = getBoardY(mario.getY());
        // } else {
        // spawnNewActivePiece();
        // }
    }

    /**
     * Repositions the blocks to corresponding places in the world.
     */
    public void repositionSquares(List<TetrisSquare> board)
    {
        // This is an adjustment for even numbers of the world
        int adjustmentX = 0, adjustmentY = 0;
        if (MAX_X%2 == 0)
            adjustmentX = BLOCK_PIXEL_SIZE/2 + BLOCK_PIXEL_SIZE%2;
        if (MAX_Y%2 == 0)
            adjustmentY = BLOCK_PIXEL_SIZE/2 + BLOCK_PIXEL_SIZE%2;

        for(TetrisSquare sq: board)
        {
            if (sq != null)
            {
                // 0 for X is the left
                int worldX = getX() + (BLOCK_PIXEL_SIZE * (sq.boardX - (MAX_X/2))) + adjustmentX;
                // 0 for Y is the bottom
                int worldY = getY() - (BLOCK_PIXEL_SIZE * (sq.boardY - (MAX_Y/2))) - adjustmentY;
                sq.setLocation(worldX, worldY);
            }
        }
    }

    public void checkGameOver()
    {
        if (activePiece != null) return;

        for(TetrisSquare sq: board)
        {
            if (sq != null)
            {
                if (sq.boardY >= MAX_Y)
                    getWorldOfType(TetrisWorld.class).gameOver("Player 2 wins!\n\nPlayer 1 hit\nthe top of the\nworld.");
            }
        }
    }

    /**
     * Returns the boardX at a certain world coordinate.
     */
    public int getBoardX(int worldX)
    {
        int leftMargin = getX() - MAX_X*BLOCK_PIXEL_SIZE/2;
        int boardX = (worldX - leftMargin) / BLOCK_PIXEL_SIZE;

        return boardX;
    }

    /**
     * Returns the boardY at a certain world coordinate.
     */
    public int getBoardY(int worldY)
    {
        // Mirror the coordinates
        // because in greenfoot a y of 0 is the top, but in our world a y of 0 is the bottom
        worldY = getWorld().getHeight() - worldY;

        int topMargin = getY() - MAX_Y*BLOCK_PIXEL_SIZE/2;
        int boardY = (worldY - topMargin) / BLOCK_PIXEL_SIZE;

        return boardY;
    }

    /**
     * Check input from the user and act accordingly.
     */
    private void checkKeys()
    {
        // No user input can do anything if there is no active piece
        if (activePiece == null) return;
        if (keyTriggered("up", keyDelay*2))
        {
            // rotate the piece clockwise
            tryRotateActivePiece();
        }
        if (keyTriggered("down", keyDelay*2)) {
            // move down faster
            if (!isTouchingBottom()) activePiece.down();
        }
        if (keyTriggered("left", keyDelay))
        {
            // move the piece right
            if (!isTouchingLeft()) activePiece.left();
        }
        if (keyTriggered("right", keyDelay) && !Greenfoot.isKeyDown("left"))
        {
            // move the piece left
            if (!isTouchingRight()) activePiece.right();
        }
        repositionSquares(board);
    }

    /**
     * Tries to rotate the active piece clockwise.
     */
    private void tryRotateActivePiece()
    {
        // https://doehoonlee.github.io/algorithm/Rotate-2D-Array/
        boolean[][] rotatedMap = new boolean[4][4];
        boolean[][] originalMap = activePiece.getMap();
        int row = originalMap.length;
        int col = originalMap[0].length;

        for (int i = 0; i < col; i++)
        {
            for(int j = 0; j < row; j++)
            {
                rotatedMap[i][j] = originalMap[row-1-j][i];
            }
        }

        for (int i = 0; i < 4; i++)
            if ((
                isOpenSpace(
                    TetrisBlock.repositionMapValues(rotatedMap),
                    activePiece.boardX-i,
                    activePiece.boardY
                )
            )) {
                activePiece.boardX -= i;
                activePiece.reshape(rotatedMap);
                break;
            }
    }

    /**
     * Checks if an area has only open squares, ignoring the active piece.
     */
    private boolean isOpenSpace(boolean[][] map, int boardX, int boardY)
    {
        TetrisSquare[] sqs = activePiece.getSquares();
        for (int x = 0; x < map.length; x++)
        {
            for (int y = 0; y < map[x].length; y++)
            {
                if (!map[x][y]) continue;

                // time complexity be like
                for(TetrisSquare sq: board)
                {
                    if (x+boardX >= MAX_X) return false;
                    if (sq.boardX == x+boardX && sq.boardY == y+boardY)
                    {
                        boolean shouldContinue = false;
                        for (TetrisSquare sq2: sqs) {
                            if (sq == sq2) {
                                shouldContinue = true;
                                break;
                            };
                        }

                        if (shouldContinue)
                        {
                            continue;
                        }

                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Checks if a key is triggered.
     * A key does not count as triggered if the last trigger was within the key delay.
     */
    private boolean keyTriggered(String key, int keyDelay)
    {
        if (Greenfoot.isKeyDown(key) && (
            lastKeyPress.get(key) == null ||
            lastKeyPress.get(key)+keyDelay <= ticks
        ))
        {
            lastKeyPress.put(key, ticks);
            return true;
        }

        return false;
    }

    /**
     * Simulate one "movement" of the pieces. Does not include user-input.
     */
    private void simulateSingle()
    {
        checkForCollisions();
        if (activePiece != null) moveActivePieceDown();
        if (activePiece == null) {
            checkGameOver();
            checkForLines();
            spawnNewActivePiece();
        }
        repositionSquares(board);
    }

    /**
     * Moves the current active piece down by one.
     */
    private void moveActivePieceDown()
    {
        if (activePiece != null)
            activePiece.down();
    }

    /**
     * Check for lines in the world, and remove them in tetris-style when there is one.
     */
    public void checkForLines()
    {
        // count how many blocks are in each row, and if it is the max ammount (MAX_X)
        // then that row is dead.
        int[] blockCountAt = new int[MAX_Y];
        for (TetrisSquare sq: board)
        {
            if (sq.boardY >= MAX_Y || sq.boardY < 0) continue;
            blockCountAt[sq.boardY]++;
        }

        // Check if rows are full
        for(int y = 0; y < blockCountAt.length; y++)
        {
            if (blockCountAt[y] >= MAX_X)
            {
                List<TetrisSquare> toRemove = new ArrayList<TetrisSquare>();

                for (TetrisSquare sq: board)
                {
                    if (sq.boardY == y)
                    {
                        toRemove.add(sq);
                    }
                    else if (sq.boardY > y) {
                        sq.boardY--;
                    }
                }

                // shift the values down
                for (int i = y; i < blockCountAt.length; i++)
                {
                    if (i >= MAX_Y-1) blockCountAt[i] = 0;
                    else blockCountAt[i] = blockCountAt[i+1];
                }

                // Remove the items from the board
                for (TetrisSquare sq: toRemove)
                {
                    getWorld().removeObject(sq);
                    board.remove(sq);
                }

                // This row was removed and the blocks shifted down,
                // so next time this will do the "same" row but it will have different
                // blocks
                y--;
            }
        }
    }

    /**
     * Spawns a new active piece, does nothing if there is already an active piece.
     */
    public void spawnNewActivePiece()
    {
        if (activePiece != null) return;
        String type = TetrisBlock.validTypes[
                Greenfoot.getRandomNumber(TetrisBlock.validTypes.length)
            ];
        activePiece = new TetrisBlock(type, MAX_X/2, MAX_Y);
        addBlock(activePiece);
    }

    /**
     * Checks for collisions with the active piece with the already placed pieces.
     */
    private void checkForCollisions()
    {
        // If there is no active piece, there will be no collisions
        if (activePiece == null)
            return;
        // If the piece is touching (or below) the ground, it has "collided" with it.
        if (activePiece.boardY <= 0)
        {
            // collision
            activePiece = null;
            return;
        }

        if (isTouchingBottom()) {
            // collision
            activePiece = null;
            return;
        }
    }

    /**
     * Checks if the active piece is touching another piece on its bottom side.
     */
    private boolean isTouchingBottom()
    {
        if (activePiece == null) return false;
        if (activePiece.boardY <= 0) return true;

        // Calculate the bottom part of the active piece, because
        // those are what would be touching another piece.
        Integer[] bottoms = new Integer[]{null,null,null,null};
        for (TetrisSquare sq: activePiece.getSquares())
        {
            int cur = sq.boardX-activePiece.boardX;
            if (cur > 4 || cur < 0) continue;
            if (bottoms[cur] == null || bottoms[cur] != null && sq.boardY < bottoms[cur]) {
                bottoms[cur] = sq.boardY;
            }
        }

        // Go over each bottom part of the piece and check if there is one below it.
        // If there is, it is touching.
        for (int i = 0; i < 4; i++)
        {
            if (bottoms[i] == null) continue;
            int x = i + activePiece.boardX;

            // this works???
            for (TetrisSquare sq: board)
            {
                if (sq.boardX == x && sq.boardY == bottoms[i]-1)
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks if the active piece is touching another piece on its left side.
     */
    private boolean isTouchingLeft()
    {
        if (activePiece == null) return false;
        if (activePiece.boardX <= 0) return true;

        // Calculate the left part of the active piece, because
        // those are what would be touching another piece.
        Integer[] lefts = new Integer[]{null,null,null,null};
        for (TetrisSquare sq: activePiece.getSquares())
        {
            int cur = sq.boardY-activePiece.boardY;
            if (cur > 4 || cur < 0) continue;
            if (lefts[cur] == null || lefts[cur] != null && sq.boardX < lefts[cur]) {
                lefts[cur] = sq.boardX;
            }
        }

        // Go over each bottom part of the piece and check if there is to the left of it.
        // If there is, it is touching.
        for (int i = 0; i < 4; i++)
        {
            if (lefts[i] == null) continue;
            int y = i + activePiece.boardY;

            // this works???
            for (TetrisSquare sq: board)
            {
                if (sq.boardY == y && sq.boardX == lefts[i]-1)
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks if the active piece is touching another piece on its right side.
     */
    private boolean isTouchingRight()
    {
        if (activePiece == null) return false;

        // Check if the piece is touching the border of the world
        for (TetrisSquare sq: activePiece.getSquares())
        {
            if (sq.boardX >= MAX_X-1) return true;
        }

        // Calculate the right part of the active piece, because
        // those are what would be touching another piece.
        Integer[] rights = new Integer[]{null,null,null,null};
        for (TetrisSquare sq: activePiece.getSquares())
        {
            int cur = sq.boardY-activePiece.boardY;
            if (cur > 4 || cur < 0) continue;
            if (rights[cur] == null || rights[cur] != null && sq.boardX > rights[cur]) {
                rights[cur] = sq.boardX;
            }
        }

        // Go over each right part of the piece and check if there is to the right of it.
        // If there is, it is touching
        for (int i = 0; i < 4; i++)
        {
            if (rights[i] == null) continue;
            int y = i + activePiece.boardY;

            // this works???
            for (TetrisSquare sq: board)
            {
                if (sq.boardY == y && sq.boardX == rights[i]+1)
                {
                    return true;
                }
            }
        }

        return false;
    }
}
