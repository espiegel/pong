/**
 * Created by Eidan on 1/31/2015.
 */
public abstract class Entity {
    protected Rectangle bounds;
    protected Velocity v = new Velocity(0,0);

    public Entity(Rectangle bounds) {
        this.bounds = bounds;
    }
}
