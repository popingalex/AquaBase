package org.aqua.framework.ui.input;

public interface Constants {
    int FLAG_MOUSE_FIRST    = 0;
    int FLAG_MOUSE_CLICKED  = FLAG_MOUSE_FIRST;
    int FLAG_MOUSE_PRESSED  = FLAG_MOUSE_FIRST + 1;
    int FLAG_MOUSE_RELEASED = FLAG_MOUSE_FIRST + 2;
    int FLAG_MOUSE_MOVED    = FLAG_MOUSE_FIRST + 3;
    int FLAG_MOUSE_ENTERED  = FLAG_MOUSE_FIRST + 4;
    int FLAG_MOUSE_EXITED   = FLAG_MOUSE_FIRST + 5;
    int FLAG_MOUSE_DRAGGED  = FLAG_MOUSE_FIRST + 6;
    int FLAG_MOUSE_WHEEL    = FLAG_MOUSE_FIRST + 7;
    int FLAG_MOUSE_LAST     = 49;

    int MOUSE_BUT0       = 0;
    int MOUSE_BUT1       = 1;
    int MOUSE_BUT2       = 2;
    int MOUSE_BUT3       = 4;

    int FLAG_KEY_FIRST      = 50;
    int FLAG_KEY_TYPED      = FLAG_KEY_FIRST;
    int FLAG_KEY_PRESSED    = FLAG_KEY_FIRST + 1;
    int FLAG_KEY_RELEASED   = FLAG_KEY_FIRST + 2;
    int FLAG_KEY_LAST       = 99;

    int FLAG_MOTION_FIRST   = 100;
    int FLAG_MOTION_TAP     = FLAG_MOTION_FIRST;
    int FLAG_MOTION_DOWN    = FLAG_MOTION_FIRST + 1;
    int FLAG_MOTION_UP      = FLAG_MOTION_FIRST + 2;
    int FLAG_MOTION_FLIP    = FLAG_MOTION_FIRST + 3;
    int FLAG_MOTION_DRAG    = FLAG_MOTION_FIRST + 4;
    int FLAG_MOTION_LAST    = 150;
}
