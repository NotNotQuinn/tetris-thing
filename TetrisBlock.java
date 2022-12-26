import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class TetrisBlock here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TetrisBlock extends Actor
{
    public int color;
    // Valid types are "S", "Z", "L", "J", "O", "I", and "T"
    public String type;
    public static final String[] validTypes = new String[]{"S", "Z", "L", "J", "O", "I", "T"};
    // The coordinates are from the top left of the map, so 0,0 is top left
    public int boardX = 0;
    public int boardY = 0;

    // Shapes will be flipped from this view when in game
    public static final boolean[][] shapeMapS = new boolean[][]{
            {true ,false,false,false},
            {true ,true ,false,false},
            {false,true ,false,false},
            {false,false,false,false},
        };
    public static boolean[][] shapeMapZ = new boolean[][]{
            {false,true ,false,false},
            {true ,true ,false,false},
            {true ,false,false,false},
            {false,false,false,false},
        };
    public static boolean[][] shapeMapL = new boolean[][]{
            {true ,true ,true ,false},
            {true ,false,false,false},
            {false,false,false,false},
            {false,false,false,false},
        };
    public static boolean[][] shapeMapJ = new boolean[][]{
            {true ,false,false,false},
            {true ,true ,true ,false},
            {false,false,false,false},
            {false,false,false,false},
        };
    public static boolean[][] shapeMapO = new boolean[][]{
            {true ,true ,false,false},
            {true ,true ,false,false},
            {false,false,false,false},
            {false,false,false,false},
        };
    public static boolean[][] shapeMapI = new boolean[][]{
            {true ,true ,true ,true },
            {false,false,false,false},
            {false,false,false,false},
            {false,false,false,false},
        };
    public static boolean[][] shapeMapT = new boolean[][]{
            {true ,false,false,false},
            {true ,true ,false,false},
            {true ,false,false,false},
            {false,false,false,false},
        };

    private boolean[][] modifiedMap;

    // This is all 4 squares used in this tetromino.
    private TetrisSquare[] squares = new TetrisSquare[4];

    public boolean[][] getMap() {
        if (modifiedMap != null) return modifiedMap;
        switch (type) {
            case "S": return shapeMapS;
            case "Z": return shapeMapZ;
            case "L": return shapeMapL;
            case "J": return shapeMapJ;
            case "O": return shapeMapO;
            case "I": return shapeMapI;
            case "T": return shapeMapT;
            default: return null;
        }
    }

    public TetrisBlock(String type, int boardX, int boardY) {
        this.type = type;
        this.boardX = boardX;
        this.boardY = boardY;

        for (int i = 0; i < squares.length; i++)
            squares[i] = new TetrisSquare(type + ".png", boardX, boardY);

        boolean[][] map = getMap();

        if (map == null) {
            System.out.println("Invalid tetris block type \""+type+"\"!");
            return;
        }

        reshape(map);
    }

    /**
     * Repositions (moves) the values in the map to have the lowest x and y be 0.
     */
    public static boolean[][] repositionMapValues(boolean[][] map)
    {
        // Find lowest x and y
        int lowestX = 9999999, lowestY = 9999999;
        for(int x = 0; x < map.length; x++)
        {
            for(int y = 0; y < map[x].length; y++)
            {
                if (map[x][y])
                {
                    if (x < lowestX)
                    {
                        lowestX = x;
                    }
                    if (y < lowestY)
                    {
                        lowestY = y;
                    }
                }
            }
        }
        if (lowestX == 0 && lowestY == 0) return map;

        // Copy the values of the first map offset by the lowest value to get
        // them to be 0 in the new map.
        boolean[][] newMap = new boolean[4][4];
        for(int x = 0; x < map.length; x++)
        {
            for(int y = 0; y < map[x].length; y++)
            {
                if (map[x][y]) 
                    newMap[x-lowestX][y-lowestY] = true;
            }
        }

        return newMap;
    }

    /**
     * Reshapes the block to the map. The map must have exactly 4 true items.
     */
    public void reshape(boolean[][] map)
    {
        modifiedMap = repositionMapValues(map);

        int squareCount = 0;
        for (int x = 0; x < modifiedMap.length; x++)
        {
            for (int y = 0; y < modifiedMap[x].length; y++)
            {
                if (!modifiedMap[x][y]) continue;

                squares[squareCount].boardX = x+boardX;
                squares[squareCount].boardY = y+boardY;
                squareCount++;
            }
        }
    }

    /**
     * Returns the squares that this block is a part of
     */
    public TetrisSquare[] getSquares() {
        return squares;
    }

    /**
     * Moves all the squares down by one. Does not reposition them in the world.
     */
    public void down() {
        boardY--;
        for (TetrisSquare sq: squares) {
            sq.boardY--;
        }
    }

    /**
     * Moves all the squares to the right by one. Does not reposition them in the world.
     */
    public void right() {
        boardX++;
        for (TetrisSquare sq: squares) {
            sq.boardX++;
        }
    }

    /**
     * Moves all the squares to the left by one. Does not reposition them in the world.
     */
    public void left() {
        boardX--;
        for (TetrisSquare sq: squares) {
            sq.boardX--;
        }
    }
}
