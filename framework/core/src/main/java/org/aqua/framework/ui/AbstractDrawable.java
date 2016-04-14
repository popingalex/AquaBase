package org.aqua.framework.ui;

import org.aqua.framework.ui.wrapper.IDrawable;

public abstract class AbstractDrawable implements IDrawable, Comparable<AbstractDrawable> {
    protected static int counter   = 0;
    public int           x;
    public int           y;
    public int           id        = (counter = counter + 1);
    public String        name      = getClass().getSimpleName() + ":" + id;
    public int           layer;
    public double        angle;
    public int           width;
    public int           height;
    public boolean       valid     = true;
    public boolean       visible   = true;
    public boolean       touchable = true;

    @Override
    public boolean capture(int x, int y) {
        double px = x * Math.cos(-angle) + y * Math.sin(-angle);
        double py = y * Math.cos(-angle) - x * Math.sin(-angle);
        return 0 < px && 0 < py && px < width && py < height;
    }

    @Override
    public int compareTo(AbstractDrawable another) {
        return another.layer - layer;
    }

    @Override
    public String toString() {
        return name;
    }
}
