import greenfoot.*;



/**
 * The ScoreBoard is used to display results on the screen. It can display some
 * text and a score.
 * 
 * @author M Kölling
 * @version 1.0
 */
public class ScoreBoard extends Actor
{
    public static final float FONT_SIZE = 32f;
    public static final int WIDTH = 300;
    public static final int HEIGHT = 400;

    /**
     * Create a score board for the final result.
     */
    public ScoreBoard(String message)
    {
        makeImage(message, FONT_SIZE,WIDTH,HEIGHT);
    }
    /**
     * Create a score board for the final result.
     */
    public ScoreBoard(String message, float fontSize)
    {
        makeImage(message, fontSize,WIDTH,HEIGHT);
    }

    /**
     * Make the score board image.
     */
    private void makeImage(String message, float fontSize, int width, int height)
    {
        GreenfootImage image = new GreenfootImage(width, height);

        image.setColor(new Color(255,255,255, 128));
        image.fillRect(0, 0, width, height);
        image.setColor(new Color(0, 0, 0, 128));
        image.fillRect(5, 5, width-10, height-10);
        Font font = image.getFont();
        font = font.deriveFont(fontSize);
        image.setFont(font);
        image.setColor(Color.WHITE);
        image.drawString(message, 30, 50);
        setImage(image);
    }
}
