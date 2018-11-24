package bgu.spl.mics;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A Future object represents a promised result - an object that will
 * eventually be resolved to hold a result of some operation. The class allows
 * Retrieving the result once it is available.
 * 
 * Only private methods may be added to this class.
 * No public constructor is allowed except for the empty constructor.
 */
public class Future<T> {
	private final CountDownLatch latch = new CountDownLatch(1);
	private T value;
	private final AtomicBoolean done = new AtomicBoolean(false);


	/**
	 * This should be the the only public constructor in this class.
	 */
	public Future() {

	}
	
	/**
     * retrieves the result the Future object holds if it has been resolved.
     * This is a blocking method! It waits for the computation in case it has
     * not been completed.
     * <p>
     * @return return the result of type T if it is available, if not wait until it is available.
     * 	       
     */
	public T get() {
		try {
			latch.await();
		}catch (InterruptedException e){}

		return value;
	}
	
	/**
     * Resolves the result of this Future object.
     */
	public void resolve (T result) {
		value = result;
		done.compareAndSet(false,true);
		latch.countDown();
	}
	
	/**
     * @return true if this object has been resolved, false otherwise
     */
	public boolean isDone() {
		return done.get();
	}
	
	/**
     * retrieves the result the Future object holds if it has been resolved,
     * This method is non-blocking, it has a limited amount of time determined
     * by {@code timeout}
     * <p>
     * @param timout 	the maximal amount of time units to wait for the result.
     * @param unit		the {@link TimeUnit} time units to wait.
     * @return return the result of type T if it is available, if not, 
     * 	       wait for {@code timeout} TimeUnits {@code unit}. If time has
     *         elapsed, return null.
     */
	public T get(long timeout, TimeUnit unit) {
		try {
			if (latch.await(timeout, unit)) {
				return value;
			}else{
				throw new TimeoutException();
			}
		} catch (InterruptedException e) {

		} catch (TimeoutException e) {

		}

		return null;
	}

}
