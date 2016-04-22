package org.aqua.craft.wrapper;

import java.awt.Component;

import org.aqua.framework.command.ICommandHandler;
import org.aqua.framework.ui.wrapper.IPaintable;

public interface IComponent extends IPaintable, ICommandHandler {
    Component getComponent();
}
