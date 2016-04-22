package org.aqua.craft.component;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aqua.craft.wrapper.IComponent;
import org.aqua.framework.event.EventManager;
import org.aqua.framework.ui.event.ContainerEvent;
import org.aqua.framework.ui.wrapper.IPaintable;
import org.aqua.struct.IContent;
import org.aqua.struct.Operator;
import org.aqua.struct.Subject;

public class ControlPanel extends JPanel implements IComponent, ActionListener {
    protected static final Logger logger           = LogManager.getLogger(ControlPanel.class);
    private static final long     serialVersionUID = 1L;
    private Map<String, Method>   methodMap        = new HashMap<String, Method>();
    private Object                subject;
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
                Operator operator = method.getAnnotation(Operator.class);
                if (null != operator && Operator.Type.OTHERS == operator.type()) {
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
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Method method = methodMap.get(e.getActionCommand());
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object params = null;
        String message = Arrays.toString(parameterTypes);
        logger.debug("requst:" + message);
        if (0 != parameterTypes.length) {
            String inputparams = JOptionPane.showInputDialog(message);
            logger.debug("response:" + inputparams);
            try {
                List<?> inputs = new com.google.gson.Gson().fromJson(inputparams, List.class);
                logger.debug("unparsed:" + inputs);
                params = parse(inputs, parameterTypes, Object.class);
                logger.debug("parsed:" + new com.google.gson.Gson().toJson(params) + "," + params);
            } catch (Exception e1) {
                e1.printStackTrace();
                return;
            }
        }
        try {
            method.invoke(subject, (Object[]) params);
            EventManager.getInstance().handleEvent(new ContainerEvent(ContainerEvent.REFRESH));
        } catch (IllegalArgumentException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();
        }
    }

    private Object parse(List<?> inputs, Class<?>[] parameterTypes, Class<?> specific) {
        Object outputs = Array.newInstance(specific, parameterTypes.length);
        for (int i = 0, count = parameterTypes.length; i < count; i++) {
            Class<?> type = parameterTypes[i];
            Object input = inputs.get(i);
            Object output = input;
            if (type.isArray()) {
                Class<?>[] pTypes = new Class[((List<?>) input).size()];
                Arrays.fill(pTypes, type.getComponentType());
                output = parse((List<?>) input, pTypes, type.getComponentType());
            } else if (type.isPrimitive()) {
                if (boolean.class == type) {
                } else if (char.class == type) {
                } else if (byte.class == type) {
                } else if (short.class == type) {
                    output = Number.class.cast(input).shortValue();
                } else if (int.class == type) {
                    output = Number.class.cast(input).intValue();
                } else if (long.class == type) {
                    output = Number.class.cast(input).longValue();
                } else if (float.class == type) {
                    output = Number.class.cast(input).floatValue();
                } else if (double.class == type) {
                    output = Number.class.cast(input).doubleValue();
                }
            } else if (String.class == type) {
            } else if (IContent.class.isAssignableFrom(type)) {
                try {
                    type.getConstructor();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
            Array.set(outputs, i, output);
        }
        return outputs;
    }
}
