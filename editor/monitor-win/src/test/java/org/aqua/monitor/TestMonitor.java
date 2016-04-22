package org.aqua.monitor;

import org.aqua.monitor.craft.Matrix3DMonitor;
import org.aqua.struct.galaxy.matrix.Matrix;

public class TestMonitor {
    public static void main(String[] args) throws Exception {
        MonitorManager manager = MonitorManager.getInstance();
        manager.registerSubject(new Matrix3DMonitor(new Matrix(3)));
//        manager.registerSubject(new Matrix(3));
        manager.show();
    
        //[[-1,-1,-1],[1,1,1]]
        //[[-1,-1],[1,1]]
        //[[0,0],[0,1]]
        //[[0,0],[1,1]]
//        Object[] a = new Object[]{new int[]{1,2}, new int[]{1,2}};
//        Object b = a;
//        Method m = TestMonitor.class.getMethod("test", int[].class, int[].class);
//        System.out.println(Arrays.toString(TestMonitor.class.getDeclaredMethods()));
//        m.invoke(null, a);
//        Object b= Array.newInstance(int.class, 2);
//        System.out.println(b);
    }
    
    public static void test(int[] a, int[] b) {
        System.out.println(a);
    }
}
