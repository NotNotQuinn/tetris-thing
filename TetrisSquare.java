import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class TetrisSquare here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TetrisSquare extends Actor
{
    public int boardX = 0, boardY = 0;
    public TetrisSquare(String imageName,int boardX, int boardY) {
        this.boardX = boardX;
        this.boardY = boardY;
        setImage(imageName);
        // // adds white borders to the images... not sure if we want this
        // GreenfootImage img = getImage();
        // img.setColor(new Color(255,255,255));
        // img.drawRect(0, 0, img.getWidth()-1, img.getHeight()-1);
    }

    public TetrisSquare() {
        this("O.png", 0, 0);
    }
}
