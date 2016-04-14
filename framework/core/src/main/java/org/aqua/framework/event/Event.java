package org.aqua.framework.event;

public class Event {
    public Object[] params;
    public Event(Object... params) {
        this.params = params;
    }
}
