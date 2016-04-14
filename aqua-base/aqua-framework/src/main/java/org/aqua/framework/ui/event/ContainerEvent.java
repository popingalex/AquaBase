package org.aqua.framework.ui.event;

public class ContainerEvent extends UIEvent {
    public static final int CONTAINER_FIRST   = 0;
    public static final int CONTAINER_CLOSED  = CONTAINER_FIRST;
    public static final int CONTAINER_REFRESH = CONTAINER_FIRST + 1;

    public int              type;
    public ContainerEvent(int type) {
        this.type = type;
    }
}
