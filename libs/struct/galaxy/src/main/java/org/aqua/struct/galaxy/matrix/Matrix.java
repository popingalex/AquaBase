package org.aqua.struct.galaxy.matrix;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aqua.struct.IContent;
import org.aqua.struct.Operator;
import org.aqua.struct.galaxy.Channel;
import org.aqua.struct.galaxy.Galaxy;
import org.aqua.struct.galaxy.Planet;

public class Matrix extends Galaxy implements Constants {
    protected final static Logger    log = LogManager.getLogger(Matrix.class);
    public final int                 dimens;
    public final int[]               lower;
    public final int[]               upper;

    protected MatrixDimensionCruiser matrixCruiser;

    public Matrix(int dimens) {
        this(dimens, new Element(new int[dimens]));
    }

    protected Matrix(int dimens, Element center) {
        super(center);
        this.dimens = dimens;
        this.lower = new int[dimens];
        this.upper = new int[dimens];
        this.matrixCruiser = new MatrixDimensionCruiser();
    }
    @Operator(type = Operator.Type.OTHERS)
    public final Element attachElement(int... coords) {
        if (0 > covers(lower, coords, upper)) {
            int[] lower = this.lower.clone();
            int[] upper = this.upper.clone();
            cover(lower, coords, upper);
            realloc(lower, upper);
        }
        return getElement(coords);
    }

    public final Element getElement(int... coords) {
        matrixCruiser.cruise(coords, coords);
        return (Element) matrixCruiser.getCollection().peek();
    }
    @Operator(type = Operator.Type.OTHERS)
    public final void collapse() {
        Object[] output = matrixCruiser.cruise(lower, upper);
        realloc((int[]) output[0], (int[]) output[1]);
    }
    @Operator(type = Operator.Type.OTHERS)
    public final void realloc(int[] lower, int[] upper) {
        for (int i = 0; i < dimens; i++) {
            lower[i] = Math.min(lower[i], 0);
            upper[i] = Math.max(upper[i], 0);
            for (int j = this.lower[i]; j < lower[i]; j++) {    // neg remove
                removeSurface(j + 1, i + dimens);
            }
            for (int j = this.upper[i]; j > upper[i]; j--) {    // pos remove
                removeSurface(j - 1, i);
            }
            for (int j = this.lower[i]; j > lower[i]; j--) {    // neg attach
                attachSurface(j, i + dimens);
            }
            for (int j = this.upper[i]; j < upper[i]; j++) {    // pos attach
                attachSurface(j, i);
            }
            this.lower[i] = lower[i];
            this.upper[i] = upper[i];
        }
    }

    private void removeSurface(int surface, int normal) {
        int[] lower = this.lower.clone();
        int[] upper = this.upper.clone();
        lower[normal] = upper[normal] = surface;
        matrixCruiser.cruise(lower, upper);
        for (Planet planet : matrixCruiser.getCollection()) {
            Element parent = (Element) planet;
            Element cursor = parent.rounds[normal];
            removingPlanet(parent, cursor, cursor.content);
            parent.rounds[normal] = null;
        }
    }

    @Override
    protected void removingPlanet(Planet parent, Planet planet, IContent content) {
        super.removingPlanet(parent, planet, content);
        Arrays.fill(((Element) planet).rounds, null);
    }

    private void attachSurface(int surface, int normal) {
        int[] lower = this.lower.clone();
        int[] upper = this.upper.clone();
        lower[normal] = upper[normal] = surface;
        matrixCruiser.cruise(lower, upper);
        int inverseNormal = (normal + dimens) % (2 * dimens);
        for (Planet planet : matrixCruiser.getCollection()) {
            Element parent = (Element) planet;
            int[] offset = parent.coords.clone();
            offset[normal % dimens] += surface > 0 ? 1 : -1;
            Element nova = (parent.rounds[normal] = new Element(offset));
            nova.rounds[inverseNormal] = parent;
            Channel channel = attachingPlanet(nova, parent);
            channel.setWeight(parent, normal, FLAG_MATRIX);
            channel.setWeight(nova, inverseNormal, FLAG_MATRIX);
        }
        for (Planet planet : matrixCruiser.getCollection()) {
            Element parent = (Element) planet;
            for (Channel channel : planet.channels) {
                int weight = (Integer) channel.getWeight(planet, FLAG_MATRIX);
                if (weight != normal && weight != inverseNormal) {
                    Element nova = parent.rounds[normal];
                    Element round = parent.rounds[weight].rounds[normal];
                    nova.rounds[weight] = round;
                    // 建立两点之间的channel, 不应重复!
                    if (dimens > weight) {  // pos normal only prevent from duplicated.
                        Channel c = new Channel(nova, round);
                        c.setWeight(nova, weight, FLAG_MATRIX);
                        c.setWeight(round, weight + dimens, FLAG_MATRIX);
                    }
                }
            }
        }
    }
    /** @return -1 for out ,0 on, 1 in */
    public static int covers(int[] lower, int[] coords, int[] upper) {
        boolean edge = false;
        for (int i = 0, dimens = coords.length; i < dimens; i++) {
            if (lower[i] > coords[i] || upper[i] < coords[i]) {
                return -1;
            } else if (lower[i] == coords[i] || upper[i] == coords[i]) {
                edge = true;
            }
        }
        return edge ? 0 : 1;
    }

    public static void cover(int[] lower, int[] coords, int[] upper) {
        for (int i = 0, dimens = coords.length; i < dimens; i++) {
            lower[i] = Math.min(lower[i], coords[i]);
            upper[i] = Math.max(upper[i], coords[i]);
        }
    }

    protected class MatrixDimensionCruiser extends MatrixCruiser {
        protected int[] targetLower;
        protected int[] targetUpper;
        protected int[] loadedLower = new int[dimens];
        protected int[] loadedUpper = new int[dimens];

        public Object[] cruise(int[] lower, int[] upper) {
            return super.cruise(center, lower, upper);
        }

        @Override
        public Cruiser prepare(Object... input) {
            targetLower = (int[]) input[0];
            targetUpper = (int[]) input[1];
            Arrays.fill(loadedLower, 0);
            Arrays.fill(loadedUpper, 0);
            return super.prepare(input);
        }
        @Override
        protected Object[] cleanup() {
            output.add(loadedLower);
            output.add(loadedUpper);
            return super.cleanup();
        }
        @Override
        protected boolean visit(Planet cursor) {
            int[] coords = ((Element) cursor).coords;
            for (int i = 0; i < dimens; i++) {
                if (targetLower[i] > coords[i] || targetUpper[i] < coords[i]) {
                    for (int zero = i + 1; zero < dimens; zero++) {
                        if (0 != coords[i]) {// != origin[i]
                            return false;
                        }
                    }
                    break;
                }
            }
            return true;
        }
        @Override
        protected boolean accept(Planet cursor) {
            int[] coords = ((Element) cursor).coords;
            if (0 > covers(targetLower, coords, targetUpper)) {
                return false;
            }
            if (null != cursor.content) {
                cover(loadedLower, coords, loadedUpper);
            }
            return true;
        }
    }

    protected class MatrixCruiser extends Cruiser {
        {
            flag = FLAG_MATRIX;
        }
        /**
         * TODO 如果origin不为center
         * 应该使坐标顺次, 当一个坐标不在lower, upper之间, 则其他点和原点坐标相同？
         */
        @Override
        protected boolean routable(Planet cursor, Planet next, Object weight) {
            Element element = (Element) cursor;
            int zero = dimens - 1;
            for (; zero >= 0 && element.coords[zero] == 0;) {
                zero--;    // get the last none-zero index
            }
            int direct = (Integer) weight;
            if (zero >= 0 && direct == (element.coords[zero] > 0 ? zero : zero + dimens)) {
                return true;
            } else if (direct > zero && direct < dimens || direct > zero + dimens) {
                return true;
            } else {
                return false;
            }
        }
    }
}