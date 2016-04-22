package org.aqua.struct.galaxy.matrix;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Matrix2DTest {
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
    public void init() {
        Matrix matrix = new Matrix(2);
        assertEquals(2, matrix.dimens);
        assertArrayEquals(new int[] { 0, 0 }, matrix.lower);
        assertArrayEquals(new int[] { 0, 0 }, matrix.upper);
    }
    @Test
    public void collapse() {

    }
    @Test
    public void realloc() {
        Matrix matrix;

        matrix = new Matrix(2);
        matrix.realloc(new int[] { 0, 0 }, new int[] { 0, 1 });
        assertArrayEquals(new int[] { 0, 0 }, matrix.lower);
        assertArrayEquals(new int[] { 0, 1 }, matrix.upper);
        matrix.realloc(new int[] { 0, 0 }, new int[] { 0, 2 });
        assertArrayEquals(new int[] { 0, 0 }, matrix.lower);
        assertArrayEquals(new int[] { 0, 2 }, matrix.upper);
        matrix.realloc(new int[] { 0, -1 }, new int[] { 0, 2 });
        assertArrayEquals(new int[] { 0, -1 }, matrix.lower);
        assertArrayEquals(new int[] { 0, 2 }, matrix.upper);
        matrix.realloc(new int[] { 0, -2 }, new int[] { 0, 2 });
        assertArrayEquals(new int[] { 0, -2 }, matrix.lower);
        assertArrayEquals(new int[] { 0, 2 }, matrix.upper);

        matrix = new Matrix(2);
        matrix.realloc(new int[] { -1, -1 }, new int[] { 1, 1 });
    }
}
