import javazoom.jl.decoder.JavaLayerException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eidan on 1/31/2015.
 */
public class Start extends Game {

    private static final int SCREEN_WIDTH = 600;
    private static final int SCREEN_HEIGHT = 600;

    private static final int PLAYER_VELOCITY = 8;
    public static final int PLAYER_WIDTH = 80;
    public static final int PLAYER_HEIGHT = 20;

    public static final int BALL_WIDTH = 5;
    public static final int BALL_HEIGHT = 5;

    public static final int PLAYER_RIGHTMOST_X = SCREEN_WIDTH - PLAYER_WIDTH - 8;
    public static final int PLAYER_LEFTMOST_X = 0;
    public static final int BALL_RIGHTMOST_X = SCREEN_WIDTH - BALL_WIDTH;
    public static final int BALL_LEFTMOST_X = BALL_WIDTH;
    public static final int BALL_TOPMOST_Y = SCREEN_HEIGHT - BALL_HEIGHT;
    public static final int BALL_LOWEST_Y = BALL_HEIGHT;

    private static final Rectangle BACKGROUND = new Rectangle(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    public static final Color BACKGROUND_COLOR = new Color(32, 156, 200);
    public static final int INITIAL_BALL_VELOCITY = 3;
    public static final Font FONT_LIVES = new Font(Font.SANS_SERIF, Font.BOLD, 16);
    public static final Font FONT_GAME_OVER = new Font(Font.SANS_SERIF, Font.BOLD, 60);

    private int lives = 3;
    private boolean didMove;
    private Ball ball;
    private Player player;
    private List<Rectangle> objectives;
    private static GameControls controls;
    private Rectangle oldBallBounds;
    private final AssetManager assetManager;

    public static void main(String[] args) {
        try {
            InputStream is = Start.class.getResourceAsStream("images/icons/icon.png");
            BufferedImage bufferedImage = ImageIO.read(is);

            Start start = new Start("My game", SCREEN_WIDTH, SCREEN_HEIGHT, 3, bufferedImage);
            controls = new GameControls();
            new GameThread(start, controls).start();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public Start(String title, int width, int height, int scale, BufferedImage iconImage) {
        super(title, width, height, scale, iconImage);

        player = new Player(getInitialPlayerBounds());
        ball = new Ball(getInitialBallBounds());
        assetManager = AssetManager.getInstance();
        objectives = new ArrayList<>();

        int w = 50;
        int h = 20;
        int marginX = 10;
        int marginY = 5;
        int objsPerRow = SCREEN_WIDTH / (w + marginX);

        for(int i = 0; i < objsPerRow * 2; i++) {
            Rectangle r = new Rectangle((i * (w + marginX)) % SCREEN_WIDTH, marginY + (h + marginY) * (i / objsPerRow), w, h);
            objectives.add(r);
        }

        ball.v.x = 0;
        ball.v.y = 0;
    }

    @Override
    public void update() {
        if(gameOver) {
            return;
        }

        // Player controls
        if(controls.leftPressed) {
            player.v.x = -PLAYER_VELOCITY;
            if(!didMove) {
                playerMoved();
            }
        } else if(controls.rightPressed) {
            player.v.x = PLAYER_VELOCITY;
            if(!didMove) {
                playerMoved();
            }
        } else {
            player.v.x = 0;
        }

        player.bounds.x += player.v.x;
        clipPlayerBounds();

        oldBallBounds = ball.bounds.copy();
        ball.bounds.x += ball.v.x;
        ball.bounds.y += ball.v.y;
        //System.out.println("ball bounds = " + ball.bounds);
        clipBallBounds();
        checkBallCollision();
    }

    private void playerMoved() {
        if(ball.v.x == 0 && ball.v.y == 0) {
            ball.v = new Velocity(INITIAL_BALL_VELOCITY, INITIAL_BALL_VELOCITY);
            didMove = true;
        }
    }

    private void checkBallCollision() {
        Rectangle ballBounds = ball.bounds;

        // Left and Right side collisions
        if(ballBounds.x >= BALL_RIGHTMOST_X || ballBounds.x <= BALL_LEFTMOST_X) {
            ball.v.x *= -1;
        }

        // Top collision
        if(ballBounds.y <= BALL_LOWEST_Y) {
            ball.v.y *= -1;
        }

        if(ballBounds.y >= BALL_TOPMOST_Y) {
            lostLife();
            return;
        }

        // Player collision
        if(ballBounds.x + ballBounds.width >= player.bounds.x && ballBounds.x <= player.bounds.x + player.bounds.width &&
                ballBounds.y + ballBounds.height >= player.bounds.y && ballBounds.y <= player.bounds.y + player.bounds.height) {
            // check direction of collision
            if(oldBallBounds.x >= player.bounds.x + player.bounds.width || oldBallBounds.x + oldBallBounds.width <= player.bounds.x) {
                // horizontal
                ball.v.x *= -1;
            } else {
                // vertical
                ball.v.y *= -1;

                // check which part of the player we collided with
                if(ballBounds.x >= player.bounds.x && ballBounds.x <= player.bounds.x + player.bounds.width / 3) {
                    // left part
                    ball.v.x = -INITIAL_BALL_VELOCITY;
                } else if(ballBounds.x <= player.bounds.x +player.bounds.width && ballBounds.x >= player.bounds.x + player.bounds.width * 2 / 3) {
                    // right part
                    ball.v.x = INITIAL_BALL_VELOCITY;
                } else {
                    // middle
                    ball.v.y = clip(ball.v.y - Math.abs(ball.v.x), -2*INITIAL_BALL_VELOCITY, 2*INITIAL_BALL_VELOCITY);
                    ball.v.x = 0;
                }
            }
            ball.bounds = oldBallBounds;
        }

        // Check objective collision
        for(Rectangle obj : objectives) {
            if(ballBounds.x + ballBounds.width >= obj.x && ballBounds.x <= obj.x + obj.width &&
                    ballBounds.y <= obj.y + obj.height && ballBounds.y + ballBounds.height <= obj.y + obj.height) {
                objectiveHit(obj);

                // check direction of collision
                if(oldBallBounds.x >= obj.x + obj.width || oldBallBounds.x + oldBallBounds.width <= obj.x) {
                    // horizontal
                    ball.v.x *= -1;
                } else {
                    // vertical
                    ball.v.y *= -1;
                }
                ball.bounds = oldBallBounds;
                break;
            }
        }
    }

    private void lostLife() {
        lives--;

        if(lives > 0) {
            didMove = false;
            ball.bounds = getInitialBallBounds();
            player.bounds = getInitialPlayerBounds();
            ball.v = new Velocity(0,0);
            player.v = new Velocity(0, 0);
        } else {
            gameOver = true;

            // GAME OVER
        }
    }

    private void objectiveHit(Rectangle obj) {
        objectives.remove(obj);

        javazoom.jl.player.Player player = assetManager.getClip("ding");
        if(player != null) {
            new Thread(() -> {
                try {
                    player.play();
                    if(player.isComplete()) {
                        assetManager.finishedClip("ding");
                    }
                } catch(JavaLayerException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private void clipBallBounds() {
        if(ball.bounds.x < BALL_LEFTMOST_X) {
            ball.bounds.x = BALL_LEFTMOST_X;
        } else if(ball.bounds.x > BALL_RIGHTMOST_X) {
            ball.bounds.x = BALL_RIGHTMOST_X;
        }
    }

    private void clipPlayerBounds() {
        if(player.bounds.x < PLAYER_LEFTMOST_X) {
            player.bounds.x = PLAYER_LEFTMOST_X;
        } else if(player.bounds.x > PLAYER_RIGHTMOST_X) {
            player.bounds.x = PLAYER_RIGHTMOST_X;
        }
    }

    private Rectangle getInitialPlayerBounds() {
        return new Rectangle(SCREEN_WIDTH / 2, SCREEN_HEIGHT * 9 / 10, PLAYER_WIDTH, PLAYER_HEIGHT);
    }

    private Rectangle getInitialBallBounds() {
        return new Rectangle(SCREEN_WIDTH / 8, 2 * SCREEN_HEIGHT / 7, BALL_WIDTH, BALL_HEIGHT);
    }

    @Override
    public void draw(Graphics2D g) {
        // draw background
        drawRectangle(g, BACKGROUND, BACKGROUND_COLOR);

        // draw objects
        objectives.forEach(r -> drawRectangle(g, r, Color.BLACK));
        drawRectangle(g, player.bounds, Color.RED);
        drawOval(g, ball.bounds, Color.ORANGE);

        // draw lives
        g.setFont(FONT_LIVES);
        g.setColor(Color.WHITE);
        String str = "Lives: " + lives;
        g.drawString(str, 4, 80);

        if(gameOver) {
            g.setColor(Color.WHITE);
            g.setFont(FONT_GAME_OVER);
            g.drawString("GAME OVER", SCREEN_WIDTH / 3, SCREEN_HEIGHT / 3);
        }
    }

    private void drawRectangle(Graphics2D g, Rectangle r, Color color) {
        g.setColor(color);
        g.fillRect(r.x, r.y, r.width, r.height);
    }

    private void drawOval(Graphics2D g, Rectangle r, Color color) {
        g.setColor(color);
        g.fillOval(r.x, r.y, r.width, r.height);
    }
}