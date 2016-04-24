package org.aqua.struct.galaxy.hexgon;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aqua.struct.galaxy.Channel;
import org.aqua.struct.galaxy.matrix.Element;
import org.aqua.struct.galaxy.matrix.Matrix;

public class Hexgon extends Matrix {
    protected final static Logger logger   = LogManager.getLogger(Hexgon.class);
    public final static Integer   FLAG_HEX = FLAG_MATRIX - 1;
    public Hexgon() {
        super(2);//[[-2,-2],[2,2]]
        //[[-1,-1],[1,1]]
        //[[0,0], [2,0]]
    }
    /*
     * ..3
     * 2.o.0
     * ..1
     */
    @Override
    protected List<Element> attachSurface(int surface, int normal) {
        List<Element> novaCollection = super.attachSurface(surface, normal);
        System.out.println(surface + ":" + normal);
        for (Element nova : novaCollection) {
            System.out.println("Nova:" + nova);
            for (Channel channel : nova.channels) {
                System.out.println(channel.getWeight(nova, FLAG_MATRIX));
            }
        }
        return novaCollection;
    }
}
