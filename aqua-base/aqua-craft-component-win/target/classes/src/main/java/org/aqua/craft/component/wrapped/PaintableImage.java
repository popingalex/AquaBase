package org.aqua.craft.component.wrapped;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.aqua.framework.ui.wrapper.IPaintable;

public class PaintableImage implements IPaintable {
    public BufferedImage image;
    public Graphics2D    graph;
    public PaintableImage(BufferedImage image, Graphics2D graph) {
        this.image = image;
        this.graph = graph;
    }
    @Override
    public IPaintable getPaint() {
        return this;
    }
    @Override
    public void paint(IPaintable paint, Object... params) {
    }
}
