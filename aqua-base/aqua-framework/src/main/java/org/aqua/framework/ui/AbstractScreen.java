package org.aqua.framework.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aqua.framework.event.EventHandler;
import org.aqua.framework.event.EventManager;
import org.aqua.framework.event.IEventListener;
import org.aqua.framework.ui.event.ContainerEvent;
import org.aqua.framework.ui.wrapper.IContainer;

public abstract class AbstractScreen implements IEventListener, Constants {
    protected static final Logger logger       = LogManager.getLogger(AbstractScreen.class);
    protected static EventManager eventManager = EventManager.getInstance();
    protected IContainer          container;

    public AbstractScreen(IContainer container) {
        this.container = container;
        eventManager.registerListener(this);
        container.resieze(800, 600);
    }

    public void load(AbstractDrawable[] drawables) {

    }

    public void show() {
        container.show();
    }
    @EventHandler
    public void handleContainerEvent(ContainerEvent event) {
        if (ContainerEvent.CONTAINER_CLOSED == event.type) {
            logger.debug("contianer closed");
            System.exit(0);
        }
    }
}
