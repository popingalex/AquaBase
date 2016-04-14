package org.aqua.framework.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EventManager {
    protected static final Logger log = LogManager.getLogger(EventManager.class);
    private static EventManager   instance;
    public static EventManager getInstance() {
        return (null == instance) ? (instance = new EventManager()) : instance;
    }
    private Map<Class<? extends Event>, List<RegistedListener>> listenerMap;
    private List<IEventListener>                                listenerList;
    private EventManager() {
        listenerMap = new HashMap<Class<? extends Event>, List<EventManager.RegistedListener>>();
        listenerList = new LinkedList<IEventListener>();
    }

    public void handleEvent(Event event) {
        for (Entry<Class<? extends Event>, List<RegistedListener>> entry : listenerMap.entrySet()) {
            if (entry.getKey().isInstance(event)) {
                for (RegistedListener listener : entry.getValue()) {
                    try {
                        listener.handlerMap.get(entry.getKey()).invoke(listener.listener, event);
                        log.debug(listener.listener.getClass().getName() + "."
                                + listener.handlerMap.get(entry.getKey()).getName() + " handled "
                                + event.getClass().getSimpleName() + " as "
                                + entry.getKey().getSimpleName());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void registerHandler(Class<? extends Event> eventClass, RegistedListener listener) {
        if (!listenerMap.containsKey(eventClass)) {
            listenerMap.put(eventClass, new LinkedList<RegistedListener>());
        }
        listenerMap.get(eventClass).add(listener);
        for (Class<?> clazz = eventClass; Event.class.isAssignableFrom(clazz);) {
            clazz = clazz.getSuperclass();
        }
    }

    public void registerListener(IEventListener listener) {
        if (!listenerList.contains(listener)) {
            listenerList.add(listener);
            new RegistedListener(listener);
        }
    }

    private class RegistedListener {
        IEventListener                      listener;
        Map<Class<? extends Event>, Method> handlerMap;
        public RegistedListener(IEventListener listener) {
            this.listener = listener;
            this.handlerMap = new HashMap<Class<? extends Event>, Method>();
            for (Method method : listener.getClass().getMethods()) {
                if (method.isAnnotationPresent(EventHandler.class)) {
                    Class<?>[] types = method.getParameterTypes();
                    if (1 == types.length && Event.class.isAssignableFrom(types[0])) {
                        Class<? extends Event> eventClass = types[0].asSubclass(Event.class);
                        handlerMap.put(eventClass, method);
                        registerHandler(eventClass, this);
                        log.debug(listener.getClass().getName() + "." + method.getName()
                                + " catching " + eventClass.getSimpleName());
                    }
                }
            }
        }
    }
}
