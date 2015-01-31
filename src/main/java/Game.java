import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Eidan on 1/31/2015.
 */
public abstract class Game {
    protected String title;
    protected int width;
    protected int height;
    protected int scale;
    protected BufferedImage iconImage;
    protected boolean gameOver;
    protected boolean paused;

    public Game(String title, int width, int height, int scale, BufferedImage iconImage) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.scale = scale;
        this.iconImage = iconImage;
    }

    public abstract void update();

    public abstract void draw(Graphics2D g);

    public int clip(int num, int min, int max) {
        if(num < min) {
            return min;
        } if(num > max) {
            return max;
        } else {
            return num;
        }
    }
}
