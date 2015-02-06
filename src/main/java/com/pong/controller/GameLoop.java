package com.pong.controller;

import com.pong.controller.Game;
import com.pong.view.GameComponents;
import com.pong.view.GameWindow;

import java.awt.event.KeyListener;

/**
 * Created by Eidan on 1/31/2015.
 */
public class GameLoop implements Runnable {

    private final Game game;
    private final GameWindow window;
    private final GameComponents component;

    private final double ONE_SIXTIETH_OF_A_SECOND = 1000000000.0 / 60.0;

    public GameLoop(Game game, KeyListener controls) {
        this.game = game;
        window = new GameWindow(game, controls);
        component = window.getGameComponent();
    }

    @Override
    public void run() {
        long timer = System.currentTimeMillis();
        int ups = 0;
        int fps = 0;

        long lastTime = System.nanoTime();
        long currentTime;
        double nextUpdate = 0;

        while(!game.gameOver) {
            while(!game.paused) {
                currentTime = System.nanoTime();
                nextUpdate += (currentTime - lastTime) / ONE_SIXTIETH_OF_A_SECOND;
                lastTime = currentTime;

                if(nextUpdate >= 1) {
                    ups++;
                    fps++;
                    nextUpdate--;

                    game.update();
                    component.repaint();
                }

                if(System.currentTimeMillis() - timer >= 1000) {
                    timer = System.currentTimeMillis();
                    System.out.println("ups = "+ups+", fps = "+fps);
                    ups = 0;
                    fps = 0;
                }
            }
        }
    }
}
