package org.aqua.craft.struct.galaxy.matrix;

import java.util.Arrays;

import org.aqua.craft.struct.galaxy.Planet;

public class Element extends Planet {
    public final int       dimens;
    public final int[]     coords;
    public final Element[] rounds;
    public Element(int[] coords) {
        this.dimens = coords.length;
        this.coords = coords;
        this.rounds = new Element[dimens * 2];
    }
    @Override
    public String toString() {
        return super.toString() + Arrays.toString(coords);
    }
}