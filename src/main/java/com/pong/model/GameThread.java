package com.pong.model;

import com.pong.controller.Game;
import com.pong.controller.GameLoop;

import java.awt.event.KeyListener;

/**
 * Created by Eidan on 1/31/2015.
 */
public class GameThread extends Thread {
    private final GameLoop loop;
    private final Thread gameLoopThread;

    public GameThread(Game game, KeyListener controls) {
        loop = new GameLoop(game, controls);
        gameLoopThread = new Thread(loop, game.getTitle() + " loop thread");
    }

    @Override
    public synchronized void start() {
        gameLoopThread.start();
    }
}
