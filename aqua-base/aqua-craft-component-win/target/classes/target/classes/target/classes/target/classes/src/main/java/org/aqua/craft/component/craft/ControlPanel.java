package org.aqua.craft.component.craft;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.aqua.craft.component.wrapper.IComponent;
import org.aqua.craft.struct.Subject;
import org.aqua.craft.struct.Subject.Operator.OperatorType;
import org.aqua.framework.ui.wrapper.IPaintable;

public class ControlPanel extends JPanel implements IComponent, ActionListener {
    private static final long   serialVersionUID = 1L;
    private Map<String, Method> methodMap        = new HashMap<String, Method>();
    private Object              subject;
    @Override
    public IPaintable getPaint() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void paint(IPaintable paint, Object... params) {
        // TODO Auto-generated method stub

    }

    @Override
    public void handleCommand(Object... commands) {
        if (null == commands || 0 == commands.length || null == commands[0]) {
            return;
        } else if (commands[0].getClass().isAnnotationPresent(Subject.class)) {
            subject = commands[0];
            Class<?> subjectClass = subject.getClass();
            //            subject.getAnnotations()
            //            subject.getDeclaredAnnotations()
            removeAll();
            methodMap.clear();
            for (Method method : subjectClass.getMethods()) {
                Subject.Operator operator = method.getAnnotation(Subject.Operator.class);
                if (null != operator && OperatorType.OTHERS == operator.type()) {
                    methodMap.put(method.getName(), method);
                    JButton button = new JButton(method.getName());
                    button.setFocusable(false);
                    button.setActionCommand(method.getName());
                    button.addActionListener(this);
                    add(button);
                }
            }
        }
    }

    @Override
    public Component getComponent() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
    }

}
