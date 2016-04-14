package org.aqua.craft.component.craft;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aqua.craft.component.KeyInputManager;
import org.aqua.craft.component.MouseInputManager;
import org.aqua.craft.component.wrapped.PaintableImage;
import org.aqua.framework.event.EventManager;
import org.aqua.framework.ui.Constants;
import org.aqua.framework.ui.event.ContainerEvent;
import org.aqua.framework.ui.wrapper.IContainer;
import org.aqua.framework.ui.wrapper.IPaintable;

public class SwingContainer implements IContainer, Constants {
    protected static final Logger logger       = LogManager.getLogger(SwingContainer.class);
    protected static EventManager eventManager = EventManager.getInstance();
    protected static Rectangle    windowbounds;
    protected static Insets       insets;
    protected JFrame              frame;
    protected Container           panel;
    protected PaintableImage      paintable;
    protected KeyInputManager     keyManager;
    protected MouseInputManager   mouseManager;

    public SwingContainer() {
        windowbounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        frame = new JFrame("AquaCraft");
        // TODO 这个要读配置的
        //        frame.setUndecorated(undecorated);
        //        if (!decor) {
        //            AWTUtilities.setWindowOpaque(frame, false);
        //        } else {
        //            AWTUtilities.setWindowOpacity(frame, Option.Screen_Alpha);
        //        }
        frame.setContentPane(panel = buildContentPanel());
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();    // TODO collect some data
            }
            @Override
            public void windowClosed(WindowEvent e) {
                eventManager.handleEvent(new ContainerEvent(ContainerEvent.CONTAINER_CLOSED));
            }
        });
        keyManager = new KeyInputManager();
        mouseManager = new MouseInputManager();

        panel.addKeyListener(keyManager);
        panel.addMouseListener(mouseManager);
        panel.addMouseWheelListener(mouseManager);
        panel.addMouseMotionListener(mouseManager);
    }

    protected Container buildContentPanel() {
        return new JPanel() {
            private static final long serialVersionUID = 1L;
            @Override
            public void paint(Graphics g) {
                if (null != paintable && null != paintable.image) {
                    g.drawImage(paintable.image, 0, 0, null);
                } else {
                    super.paint(g);
                }
            }
        };
    }
    @Override
    public void show() {
        frame.setVisible(true);
        panel.requestFocus();
    }
    @Override
    public void hide() {
        frame.setVisible(false);
    }
    @Override
    public void repaint() {
        panel.repaint();
    }
    @Override
    public void resieze(int width, int height) {
        if (null == insets) {
            if (!frame.isVisible()) {
                frame.setVisible(true);
                insets = frame.getInsets();
                frame.setVisible(false);
            } else {
                insets = frame.getInsets();
            }
        }
        width += insets.left + insets.right;
        height += insets.top + insets.bottom;
        int left = (windowbounds.width - width) / 2;
        int top = (windowbounds.height - height) / 2;
        frame.setBounds(left, top, width, height);
    }

    @Override
    public void add(IPaintable paintable, Object... params) {
        // TODO Auto-generated method stub

    }

    @Override
    public void remove(IPaintable paintable, Object... params) {
        // TODO Auto-generated method stub

    }

    @Override
    public void handleCommand(Object... commands) {
        // TODO Auto-generated method stub

    }
}