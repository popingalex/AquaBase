package org.aqua.framework.ui.input;

import org.aqua.framework.ui.event.UIEvent;

public class InputEvent extends UIEvent implements Constants {
    public int type;
    public InputEvent(int type, Object... params) {
        this.type = type;
        this.params = params;
    }
}
