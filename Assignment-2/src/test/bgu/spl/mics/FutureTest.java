package bgu.spl.mics;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FutureTest {

    private Future<Integer> future;

    /**
     * Set up for a test.  Note the @Before annotation.  It indicate this method is executed before the tests of
     * this test case are executed.
     */
    @Before
    public void setUp() throws Exception {
        future = new Future<>();
    }

    @After
    public void tearDown() throws Exception {
        future = null;
    }

    @Test
    public void testGet() {
        fail();
    }

    @Test
    public void testResolve() {

    }

    @Test
    public void isDone() {
    }

    @Test
    public void get1() {
    }
}