package org.aqua.framework.ui.event;

public class ContainerEvent extends UIEvent {
    public static final int CONTAINER_FIRST = 0;
    public static final int CLOSED          = CONTAINER_FIRST;
    public static final int REFRESH         = CONTAINER_FIRST + 1;

    public int              type;
    public ContainerEvent(int type) {
        this.type = type;
    }
}
