package com.pong.view;

import com.pong.controller.Game;

import javax.swing.*;
import java.awt.event.KeyListener;

/**
 * Created by Eidan on 1/31/2015.
 */
public class GameWindow {

    private JFrame window;
    private GameComponents gameComponent;

    public GameWindow(Game game, KeyListener controls) {
        gameComponent = new GameComponents(game);
        window = new JFrame();

        window.setTitle(game.getTitle());
        window.setSize(game.getWidth(), game.getHeight());

        window.setLocale(null);
        window.setResizable(false);
        window.setIconImage(game.getIconImage());

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        window.add(gameComponent);
        gameComponent.requestFocus();

        window.addKeyListener(controls);
        window.setVisible(true);
    }

    public GameComponents getGameComponent() {
        return gameComponent;
    }
}
