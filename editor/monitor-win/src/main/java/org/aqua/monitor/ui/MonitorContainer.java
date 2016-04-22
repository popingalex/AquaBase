package org.aqua.monitor.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.aqua.craft.component.ControlPanel;
import org.aqua.craft.component.StructTree;
import org.aqua.craft.component.SwingContainer;
import org.aqua.craft.wrapper.IComponent;
import org.aqua.framework.ui.event.ContainerEvent;
import org.aqua.framework.ui.wrapper.IPaintable;
import org.aqua.monitor.AbstractMonitor;

public class MonitorContainer extends SwingContainer implements ChangeListener, ActionListener {
    private StructTree                         structTree;
    private JTabbedPane                        monitorPanel;
    private ControlPanel                       structPanel;
    private ControlPanel                       nodePanel;
    private ControlPanel                       contentPanel;
    private Map<String, AbstractMonitor<?, ?>> monitorMap;
    public MonitorContainer() {
        super();
        structTree = new StructTree();
        monitorPanel = new JTabbedPane();
        structPanel = new ControlPanel();
        nodePanel = new ControlPanel();
        contentPanel = new ControlPanel();
        monitorMap = new HashMap<String, AbstractMonitor<?, ?>>();

        structPanel.setBorder(BorderFactory.createTitledBorder("Struct"));
        nodePanel.setBorder(BorderFactory.createTitledBorder("Node"));
        contentPanel.setBorder(BorderFactory.createTitledBorder("Content"));
        monitorPanel.addChangeListener(this);
        monitorPanel.setBorder(BorderFactory.createEtchedBorder());

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridy = 0;
        constraints.weighty = 1;
        //        constraints.anchor = GridBagConstraints.NORTH;
        gridbag.setConstraints(structPanel, constraints);
        constraints.gridy = 1;
        gridbag.setConstraints(nodePanel, constraints);
        constraints.gridy = 2;
        gridbag.setConstraints(contentPanel, constraints);
        JButton button = new JButton("refresh");
        button.setFocusable(false);
        button.setActionCommand("refresh");
        button.addActionListener(this);
        constraints.gridy = 3;
        gridbag.setConstraints(button, constraints);

        JPanel controlPanel = new JPanel(gridbag);
        controlPanel.add(structPanel);
        controlPanel.add(nodePanel);
        controlPanel.add(contentPanel);
        controlPanel.add(button);

        int vsb = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
        int hsb = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER;

        panel.setLayout(new BorderLayout());
        panel.add(monitorPanel);
        panel.add(new JScrollPane(structTree, vsb, hsb), BorderLayout.WEST);
        panel.add(monitorPanel, BorderLayout.CENTER);
        panel.add(new JScrollPane(controlPanel, vsb, hsb), BorderLayout.EAST);
    }
    @Override
    public void handleCommand(Object... commands) {
        super.handleCommand(commands);
    }
    @Override
    public void add(IPaintable paintable, Object... params) {
        AbstractMonitor<?, ?> monitor = (AbstractMonitor<?, ?>) params[0];
        String tag = monitor.getSubjectTag();
        monitorMap.put(tag, monitor);
        monitorPanel.add(tag, ((IComponent) paintable).getComponent());
    }
    @Override
    public void stateChanged(ChangeEvent e) {
        String tag = monitorPanel.getTitleAt(monitorPanel.getSelectedIndex());
        logger.debug("monitoring " + tag);
        AbstractMonitor<?, ?> monitor = monitorMap.get(tag);
        structTree.handleCommand(monitor);
        logger.debug("handle struct");
        structPanel.handleCommand(monitor.getSubject());
        logger.debug("handle node");
        nodePanel.handleCommand(monitor.getContent(monitor.getCursor()));
        logger.debug("handle content");
        contentPanel.handleCommand(monitor.getContent(monitor.getCursor()));
        monitorPanel.getSelectedComponent().requestFocus();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        eventManager.handleEvent(new ContainerEvent(ContainerEvent.REFRESH));
    }
}
