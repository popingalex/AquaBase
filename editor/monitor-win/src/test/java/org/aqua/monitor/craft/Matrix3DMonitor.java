package org.aqua.monitor.craft;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.aqua.craft.component.j3d.BoxNode;
import org.aqua.craft.component.j3d.CursorNode;
import org.aqua.craft.component.j3d.J3DUniverse;
import org.aqua.craft.wrapper.IComponent;
import org.aqua.framework.event.EventHandler;
import org.aqua.framework.event.EventManager;
import org.aqua.framework.event.IEventListener;
import org.aqua.framework.ui.event.ContainerEvent;
import org.aqua.monitor.AbstractMonitor;
import org.aqua.struct.IContent;
import org.aqua.struct.galaxy.Channel;
import org.aqua.struct.galaxy.Galaxy;
import org.aqua.struct.galaxy.Galaxy.DepthCruiser;
import org.aqua.struct.galaxy.Planet;
import org.aqua.struct.galaxy.hexgon.Hexgon;
import org.aqua.struct.galaxy.matrix.Element;
import org.aqua.struct.galaxy.matrix.Matrix;

public class Matrix3DMonitor extends AbstractMonitor<Matrix, Planet> implements IEventListener {
    private J3DUniverse  universe;
    private DepthCruiser cruiser;
    private Planet       cursor;
    public Matrix3DMonitor(Matrix subject) {
        super(subject);
        if (3 < subject.dimens) {
            throw new IllegalArgumentException("dimention > 3");
        }
        cruiser = subject.new DepthCruiser();
        cursor = subject.getCenter();
        universe = new J3DUniverse();
        universe.compile();
        universe.getComponent().addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                universe.scale(-e.getWheelRotation());
            }
        });
        EventManager.getInstance().registerListener(this);
    }
    @Override
    public Planet getRoot() {
        return subject.getCenter();
    }
    @Override
    public Planet getCursor() {
        return cursor;
    }
    @Override
    public IContent getContent(Object node) {
        return ((Planet) node).content;
    }
    @Override
    public List<Planet> getBranch(Object node) {
        List<Planet> branches = new LinkedList<Planet>();
        for (Channel channel : ((Planet) node).channels) {
            branches.add(channel.getAnother((Planet) node));
        }
        return branches;
    }
    @Override
    public IComponent getPaintable() {
        return universe;
    }
    @EventHandler
    public void refresh(ContainerEvent event) {
        if (ContainerEvent.REFRESH == event.type) {
            cruiser.cruise(cursor);
            universe.display("model", null);
            int[] coord3 = new int[3];
            Set<Channel> channels = new HashSet<Channel>();
            for (Planet planet : cruiser.getCollection()) {
                channels.addAll(planet.channels);
                BoxNode node = new BoxNode();
                int[] pos = ((Element) planet).coords;
                System.arraycopy(pos, 0, coord3, 0, pos.length);
                node.setCoord3(coord3);
                if (null == planet.content) {
                    node.deserialize(0);
                } else {
                    node.deserialize(1);
                }
                universe.display("model", node);
            }
            universe.display("channel", null);
            int[] coord32 = new int[3];
            for (Channel channel : channels) {
                CursorNode node = new CursorNode();
                Element one = ((Element) channel.getOne(0));
                int flag = Galaxy.FLAG_TREE;
                flag = Matrix.FLAG_MATRIX;
                flag = Hexgon.FLAG_HEX;
                Integer weight = (Integer) channel.getWeight(one, flag);
                if (null == weight) {
                    continue;
                    //                } else if (weight < 2) {
                    //                    continue;
                }
                System.arraycopy(one.coords, 0, coord3, 0, one.coords.length);
                one = ((Element) channel.getOne(1));
                System.arraycopy(one.coords, 0, coord32, 0, one.coords.length);
                node.setCoord3(coord3, coord32);
                universe.display("channel", node);
            }
        }
    }
}
