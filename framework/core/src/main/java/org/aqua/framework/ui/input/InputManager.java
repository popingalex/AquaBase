package org.aqua.framework.ui.input;

import org.aqua.framework.event.ActionEvent;
import org.aqua.framework.event.EventHandler;
import org.aqua.framework.event.EventManager;
import org.aqua.framework.event.IEventListener;

public class InputManager implements Constants, IEventListener {
    private static EventManager eventManager = EventManager.getInstance();
    private static InputManager instance;
    public static InputManager getInstance() {
        return (null == instance) ? (instance = new InputManager()) : instance;
    }
    private InputManager() {
    }
    @EventHandler
    public void handleInput(InputEvent event) {
        eventManager.handleEvent(new ActionEvent());
    }
}
