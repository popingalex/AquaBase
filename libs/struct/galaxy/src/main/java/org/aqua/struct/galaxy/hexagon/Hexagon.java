package org.aqua.struct.galaxy.hexagon;

import java.util.HashSet;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aqua.struct.galaxy.Channel;
import org.aqua.struct.galaxy.matrix.Element;
import org.aqua.struct.galaxy.matrix.Matrix;

public class Hexagon extends Matrix {
    protected static final Logger logger       = LogManager.getLogger(Hexagon.class);
    public static final Integer   FLAG_HEXAGON = FLAG_MATRIX - 1;
    public static final Integer   OFF_ZERO     = 0;
    public static final Integer   OFF_X_POSI   = OFF_ZERO;
    public static final Integer   OFF_Y_POSI   = OFF_ZERO + 1;
    public static final Integer   OFF_Z_POSI   = OFF_ZERO + 2;
    public static final Integer   OFF_X_NEGA   = OFF_ZERO + 3;
    public static final Integer   OFF_Y_NEGA   = OFF_ZERO + 4;
    public static final Integer   OFF_Z_NEGA   = OFF_ZERO + 5;
    public Hexagon() {
        super(2);//[[-2,-2],[2,2]]
        //[[-1,-1],[1,1]]
        //[[0,0], [2,0]]
    }
    /*
     * ..3...|..4.5
     * 2.o.0.|.3.o.0
     * ..1...|..2.1
     */
    @Override
    protected List<Element> attachSurface(int surface, int normal) {
        List<Element> novaCollection = super.attachSurface(surface, normal);
        for (Element nova : novaCollection) {
            for (Channel channel : new HashSet<Channel>(nova.channels)) {
                Element round = (Element) channel.getAnother(nova);
                switch ((Integer) channel.getWeight(nova, FLAG_MATRIX)) {
                case 0 + 0: // x positive
                    channel.setWeight(nova, OFF_X_POSI, FLAG_HEXAGON);
                    channel.setWeight(round, OFF_X_NEGA, FLAG_HEXAGON);
                    break;
                case 0 + 1: // y positive
                    channel.setWeight(nova, OFF_Y_POSI, FLAG_HEXAGON);
                    channel.setWeight(round, OFF_Y_NEGA, FLAG_HEXAGON);
                    if (null != (round = round.rounds[2])) {
                        channel = Channel.channel(nova, round);
                        channel.setWeight(nova, OFF_Z_POSI, FLAG_HEXAGON);
                        channel.setWeight(round, OFF_Z_NEGA, FLAG_HEXAGON);
                    }
                    break;
                case 2 + 0: // x negative
                    channel.setWeight(nova, OFF_X_NEGA, FLAG_HEXAGON);
                    channel.setWeight(round, OFF_X_POSI, FLAG_HEXAGON);
                    break;
                case 2 + 1: // y negative
                    channel.setWeight(nova, OFF_Y_NEGA, FLAG_HEXAGON);
                    channel.setWeight(round, OFF_Y_POSI, FLAG_HEXAGON);
                    if (null != (round = round.rounds[0])) {
                        channel = Channel.channel(nova, round);
                        channel.setWeight(nova, OFF_Z_NEGA, FLAG_HEXAGON);
                        channel.setWeight(round, OFF_Z_POSI, FLAG_HEXAGON);
                    }
                    break;
                }
            }
        }
        return novaCollection;
    }
}
