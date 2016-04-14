package org.aqua.framework.ui;

public interface Constants {
    /** @see org.aqua.framework.ui.input.Constants */
    int FLAG_INPUT_FIRST   = 0;
    int FLAG_INPUT_LAST    = 199;

    int FLAG_SPRITE_FIRST  = 200;
    int FLAG_SPRITE_ACTION = FLAG_SPRITE_FIRST;
    int FLAG_SPRITE_LAST   = 249;

    int FLAG_SCREEN_FIRST  = 250;
    int FLAG_SCREEN_OPENED = FLAG_SCREEN_FIRST;
    int FLAG_SCREEN_CLOSED = FLAG_SCREEN_FIRST + 1;
    int FLAG_SCREEN_LAST   = 299;

    int STATUS_HOVER       = 1 << 0;
    int STATUS_PRESS       = 1 << 1;
    int STATUS_FREE        = 0;
}
