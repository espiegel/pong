/**
 * Created by Eidan on 1/31/2015.
 */
public class Velocity {
    protected int x;
    protected int y;

    public Velocity(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Velocity copy() {
        return new Velocity(x, y);
    }
}
