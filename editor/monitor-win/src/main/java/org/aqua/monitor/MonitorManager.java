package org.aqua.monitor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import org.aqua.monitor.ui.MonitorContainer;
import org.aqua.monitor.ui.MonitorScreen;

public class MonitorManager {
    private static MonitorManager instance;
    public static MonitorManager getInstance() {
        return (null == instance) ? (instance = new MonitorManager()) : instance;
    }

    private MonitorScreen screen;
    private MonitorManager() {
        screen = new MonitorScreen(new MonitorContainer());
    }

    public <Subject> void registerSubject(Subject subject) {
        Properties properties = new Properties();
        try {
            properties.load(ClassLoader.getSystemResourceAsStream("monitor.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String subjectName : properties.stringPropertyNames()) {
            if (subjectName.equals(subject.getClass().getName())) {
                Class<?> monitorClass;
                try {
                    monitorClass = Class.forName(properties.getProperty(subjectName));
                    Constructor<?> constructor = monitorClass.getConstructor(subject.getClass());
                    registerSubject(AbstractMonitor.class.cast(constructor.newInstance(subject)));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
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

    public <Subject> void registerSubject(AbstractMonitor<?, ?> monitor) {
        screen.registerSubject(monitor);
    }

    public void show() {
        screen.show();
    }
}
