package org.aqua.monitor.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aqua.framework.ui.AbstractScreen;
import org.aqua.framework.ui.wrapper.IContainer;
import org.aqua.monitor.AbstractMonitor;

public class MonitorScreen extends AbstractScreen {
    protected static final Logger logger = LogManager.getLogger(MonitorScreen.class);
    public MonitorScreen(IContainer container) {
        super(container);
    }

    public <Subject> void registerSubject(AbstractMonitor<?, ?> monitor) {
        container.add(monitor.getPaintable(), monitor);
    }
}
