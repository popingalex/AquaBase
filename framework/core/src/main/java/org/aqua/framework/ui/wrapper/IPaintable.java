package org.aqua.framework.ui.wrapper;

public interface IPaintable {
    IPaintable getPaint();
    void paint(IPaintable paint, Object... params);
}
