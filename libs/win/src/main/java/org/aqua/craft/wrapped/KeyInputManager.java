package org.aqua.craft.wrapped;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import org.aqua.framework.event.EventManager;
import org.aqua.framework.ui.input.Constants;
import org.aqua.framework.ui.input.InputEvent;

public class KeyInputManager extends KeyAdapter implements Constants {
    protected static EventManager eventManager = EventManager.getInstance();
    @Override
    public void keyPressed(KeyEvent e) {
        eventManager.handleEvent(new InputEvent(FLAG_KEY_PRESSED, e.getKeyCode()));
    }
    @Override
    public void keyReleased(KeyEvent e) {
        eventManager.handleEvent(new InputEvent(FLAG_KEY_RELEASED, e.getKeyCode()));
    }
}
