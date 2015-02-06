package com.pong.controller;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public BufferedImage getIconImage() {
        return iconImage;
    }

    public void setIconImage(BufferedImage iconImage) {
        this.iconImage = iconImage;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}
