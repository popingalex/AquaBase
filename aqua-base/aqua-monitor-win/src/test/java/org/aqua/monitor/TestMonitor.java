package org.aqua.monitor;

import org.aqua.craft.struct.galaxy.matrix.Matrix;
import org.aqua.monitor.craft.Matrix3DMonitor;

public class TestMonitor {
    public static void main(String[] args) {
        MonitorManager manager = MonitorManager.getInstance();
        manager.registerSubject(new Matrix3DMonitor(new Matrix(2)));
        manager.registerSubject(new Matrix(3));
        manager.show();
    }
}
