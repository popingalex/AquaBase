package org.aqua.monitor.craft;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.LinkedList;
import java.util.List;

import org.aqua.craft.component.craft.j3d.BoxNode;
import org.aqua.craft.component.craft.j3d.J3DUniverse;
import org.aqua.craft.component.wrapper.IComponent;
import org.aqua.craft.struct.IContent;
import org.aqua.craft.struct.galaxy.Channel;
import org.aqua.craft.struct.galaxy.Galaxy.DepthCruiser;
import org.aqua.craft.struct.galaxy.Planet;
import org.aqua.craft.struct.galaxy.matrix.Matrix;
import org.aqua.framework.event.EventHandler;
import org.aqua.framework.event.EventManager;
import org.aqua.framework.event.IEventListener;
import org.aqua.framework.ui.event.ContainerEvent;
import org.aqua.monitor.AbstractMonitor;

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
        if (ContainerEvent.CONTAINER_REFRESH == event.type) {
            System.out.println("fresh");
            cruiser.cruise(cursor);
            System.out.println(cruiser.getCollection());
            BoxNode node = new BoxNode();
            node.setCoord3(new int[3]);
            node.deserialize(0);
            universe.display("model", null);
            universe.display("model", node);
        }
    }
}
