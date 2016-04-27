package org.aqua.struct.galaxy;

import java.util.HashMap;
import java.util.Map;

import org.aqua.Caster;

public final class Channel {
    private Planet[]    ports   = new Planet[2];
    private Map<?, ?>[] weights = new Map<?, ?>[2];
    public static Channel channel(Planet one, Planet another) {
        for (Channel channel : one.channels) {
            if (another == channel.getAnother(one)) {
                return channel;
            }
        }
        return new Channel(one, another);
    }

    private Channel(Planet one, Planet another) {
        ports[0] = one;
        ports[1] = another;
        weights[0] = new HashMap<Object, Object>();
        weights[1] = new HashMap<Object, Object>();
        one.channels.add(this);
        another.channels.add(this);
    }

    public boolean above(Planet planet) {
        return (planet == ports[0]) || (planet == ports[1]);
    }

    public Planet getOne(int index) {
        return ports[index];
    }

    public Planet getAnother(Planet one) {
        if (above(one)) {
            return (one == ports[0]) ? ports[1] : ports[0];
        } else {
            throw new NullPointerException("Not above us");
        }
    }

    public Object getWeight(Planet origin, Object flag) {
        if (above(origin)) {
            return ((origin == ports[0]) ? weights[0] : weights[1]).get(flag);
        } else {
            throw new NullPointerException("Not above us");
        }
    }

    public void dropWeight(Planet origin, Object flag) {
        if (above(origin)) {
            ((origin == ports[0]) ? weights[0] : weights[1]).remove(flag);
        } else {
            throw new NullPointerException("Not above us");
        }
    }

    public void setWeight(Planet origin, Object weight, Object flag) {
        if (above(origin)) {
            Map<Object, Object> weightmap;
            weightmap = Caster.cast(((origin == ports[0]) ? weights[0] : weights[1]));
            weightmap.put(flag, weight);
        } else {
            throw new NullPointerException("Not above us");
        }
    }
}