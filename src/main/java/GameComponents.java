import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * Created by Eidan on 1/31/2015.
 */
public class GameComponents extends JComponent implements ComponentListener {

    private final Game game;

    public GameComponents(Game game) {
        this.game = game;
        addComponentListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        game.draw((Graphics2D)g);
    }

    @Override
    public void componentResized(ComponentEvent e) {

    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}
