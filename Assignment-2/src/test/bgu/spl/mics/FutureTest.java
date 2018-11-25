package bgu.spl.mics;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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

    }

    @Test
    public void testResolve() {
        int test = 5;
        future.resolve(test);
        int get = future.get();
        assertEquals(test, get);

    }

    @Test
    public void testResolve2(){
        AtomicInteger test = new AtomicInteger(0);
        Thread t1= new Thread(()->{
            test.set(future.get());
        });
        Thread t = new Thread(()->{
            future.resolve(5);
        });
        t.start();
        t1.start();
        try {
            t.join();
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(5, test.get());
    }


    @Test
    public void isDoneBeforeResolve() {
        assertEquals(false, future.isDone());
    }

    @Test
    public void isDoneAfterResolve() {
        int test = 5;
        future.resolve(test);
        assertEquals(true, future.isDone());
    }

    @Test
    public void testTestResolve2(){
        for (int i = 0; i < 100; i++) {
            testResolve2();
        }
    }

}