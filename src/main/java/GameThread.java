import java.awt.event.KeyListener;

/**
 * Created by Eidan on 1/31/2015.
 */
public class GameThread extends Thread {
    private final GameLoop loop;
    private final Thread gameLoopThread;

    public GameThread(Game game, KeyListener controls) {
        loop = new GameLoop(game, controls);
        gameLoopThread = new Thread(loop, game.title + " loop thread");
    }

    @Override
    public synchronized void start() {
        gameLoopThread.start();
    }
}
