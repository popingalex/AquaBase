package org.aqua.monitor;

import org.aqua.monitor.craft.Matrix3DMonitor;
import org.aqua.struct.galaxy.matrix.Matrix;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MonitorTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        MonitorManager manager = MonitorManager.getInstance();
        manager.registerSubject(new Matrix3DMonitor(new Matrix(2)));
        manager.registerSubject(new Matrix(3));
        manager.show();
    }

}
