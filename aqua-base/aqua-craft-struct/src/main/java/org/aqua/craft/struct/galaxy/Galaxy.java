package org.aqua.craft.struct.galaxy;

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aqua.craft.struct.IContent;
import org.aqua.craft.struct.Subject;
import org.aqua.craft.struct.Subject.SubjectType;
import org.aqua.craft.struct.Subject.Operator.OperatorType;

@Subject(type = SubjectType.STRUCT)
public class Galaxy implements Constants {
    protected final static Logger log = LogManager.getLogger(Galaxy.class);
    protected Planet              center;
    protected Planet              cursor;

    public Galaxy() {
        this(new Planet());
    }

    protected Galaxy(Planet center) {
        setCursor(this.center = center);
        attachingPlanet(center, null);
    }
    @Subject.Operator(type = OperatorType.CURSOR)
    public final Planet getCursor() {
        return cursor;
    }
    @Subject.Operator(type = OperatorType.CENTER)
    public final Planet getCenter() {
        return center;
    }

    public final void setCursor(Planet cursor) {
        this.cursor = (null == cursor ? center : cursor);
    }

    public Planet attachPlanet(Planet parent) {
        return null;
    }

    protected Channel attachingPlanet(Planet nova, Planet parent) {
        nova.content = attachingContent(nova);
        if (null != parent) {
            log.debug("attach nova " + nova + "->" + parent);
            Channel channel = new Channel(parent, nova);
            channel.setWeight(parent, 1, FLAG_TREE);
            channel.setWeight(nova, -1, FLAG_TREE);
            return channel;
        } else if (center != nova) {
            throw new NullPointerException("no parent");
        } else {
            return null;
        }
    }

    protected void removingPlanet(Planet parent, Planet planet, IContent content) {
        log.debug("remove planet " + planet + "-//-" + parent);
        for (Channel channel : planet.channels) {
            log.debug("discon planet " + planet + "-//-" + channel.getAnother(planet));
            planet.channels.remove(channel);
            channel.getAnother(planet).channels.remove(channel);
        }
    }

    protected IContent attachingContent(Planet planet) {
        return null;
    }

    public class DepthCruiser extends Cruiser {
        {
            flag = FLAG_TREE;
        }
        @Override
        protected boolean routable(Planet cursor, Planet next, Object weight) {
            // TODO
            return (Integer) weight > 0;
        }
    }

    protected class Cruiser {
        /*
         * 可以选择深度/广度
         * debug模式中使用tracker记录
         */
        //        protected Galaxy tracker;

        //        protected Planet       origin;
        protected Object       flag;
        protected List<Object> output     = new LinkedList<Object>();
        protected List<Planet> collection = new LinkedList<Planet>();

        public LinkedList<Planet> getCollection() {
            return new LinkedList<Planet>(collection);
        }

        public final synchronized Object[] cruise(Planet origin, Object... input) {
            prepare(input);
            cruise(origin);
            return cleanup();
        }

        public Cruiser prepare(Object... input) {
            return this;
        }

        protected final synchronized void cruise(Planet origin) {
            output.clear();
            collection.clear();
            tour(origin/* , tracker */);
            /*
             * collect the planet in tracker.
             */
        }

        protected Object[] cleanup() {
            return output.toArray();
        }

        protected void tour(Planet cursor) {
            /* track everything */
            if (visit(cursor)) {
                if (accept(cursor)) {
                    log.debug("accepted:" + cursor);
                    collection.add(cursor);
                } else {
                    log.debug("visited:" + cursor);
                }
            } else {
                log.debug("reject:" + cursor);
            }
            if (interrupt(cursor)) {
                log.debug("blocked:" + cursor);
            } else {
                log.debug("passed:" + cursor);
                route(cursor/* , tracker */);
            }
        }

        protected boolean visit(Planet cursor) {
            return true;
        }

        protected boolean accept(Planet cursor) {
            return true;
        }

        protected boolean interrupt(Planet cursor) {
            return false;
        }

        protected void route(Planet cursor) {
            for (Channel channel : cursor.channels) {
                Planet next = channel.getAnother(cursor);
                Object weight = channel.getWeight(cursor, flag);
                if (null == weight && null != flag) {
                    log.debug("channel:" + cursor + "//" + next + " blocked flag");
                } else if (routable(cursor, next, weight)) {    // flag could be null
                    log.debug("channel:" + cursor + "->" + next + " cost " + weight);
                    tour(next);
                } else {
                    log.debug("channel:" + cursor + "//" + next + " cost " + weight);
                }
            }
        }

        protected boolean routable(Planet cursor, Planet next, Object weight) {
            return true;
        }
    }
}
