package org.aqua.craft.wrapped;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import org.aqua.framework.event.EventManager;
import org.aqua.framework.ui.input.Constants;
import org.aqua.framework.ui.input.InputEvent;

public class MouseInputManager extends MouseAdapter implements Constants {
    protected static EventManager eventManager = EventManager.getInstance();
    private static int[]          BUTTONS      = { MOUSE_BUT0, MOUSE_BUT1, MOUSE_BUT2, MOUSE_BUT3 };
    private void handle(int type, Object... params) {
        eventManager.handleEvent(new InputEvent(type, params));
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        handle(FLAG_MOUSE_CLICKED, BUTTONS[e.getButton()], e.getX(), e.getY());
    }
    @Override
    public void mousePressed(MouseEvent e) {
        handle(FLAG_MOUSE_PRESSED, BUTTONS[e.getButton()], e.getX(), e.getY());
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        handle(FLAG_MOUSE_PRESSED, BUTTONS[e.getButton()], e.getX(), e.getY());
    }
    @Override
    public void mouseEntered(MouseEvent e) {
        handle(FLAG_MOUSE_ENTERED, BUTTONS[e.getButton()], e.getX(), e.getY());
    }
    @Override
    public void mouseExited(MouseEvent e) {
        handle(FLAG_MOUSE_EXITED, BUTTONS[e.getButton()], e.getX(), e.getY());
    }
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        handle(FLAG_MOUSE_WHEEL, BUTTONS[e.getButton()], e.getUnitsToScroll(), e.getX(), e.getY());
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        handle(FLAG_MOUSE_DRAGGED, BUTTONS[e.getButton()], e.getX(), e.getY());
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        handle(FLAG_MOUSE_MOVED, BUTTONS[e.getButton()], e.getX(), e.getY());
    }
}
