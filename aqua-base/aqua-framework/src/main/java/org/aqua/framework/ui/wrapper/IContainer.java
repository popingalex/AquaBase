package org.aqua.framework.ui.wrapper;

import org.aqua.framework.command.ICommandHandler;

public interface IContainer extends ICommandHandler{
    void show();
    void hide();
    void add(IPaintable paintable, Object... params);
    void remove(IPaintable paintable, Object... params);
    void repaint();
    void resieze(int width, int height);
}
