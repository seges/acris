/**
 * 
 */
package sk.seges.acris.core.client.component.semaphore;

import java.util.ArrayList;
import java.util.List;

/**
 * Semaphore is used to synchronize several asynchronous executions into one
 * stream. Listens for execution finalization and when expected count is
 * fulfilled, signals all observing parties.
 * <p>
 * Supports monitoring of multiple states (e.g. a state for failure and state
 * for success RPC method invocation). By signaling the state (using
 * {@link #signal(int)} method) the counter is increased and
 * {@link SemaphoreEvent} is fired to all observers. By calling
 * {@link #raise(byte)} or {@link #raiseOne()} you define how many signals you
 * expect.
 * </p>
 * <p>
 * Example: <br/>
 * <p>
 * Let's have two GWT-RPC async services where each one returns either success
 * or failure. We want to be notified after both of the are finished to execute
 * further processing.
 * </p>
 * <p>
 * We have to initialize a semaphore with two states (one for success signal and
 * one for failure signal). A {@link SemaphoreListener} will notify us about a
 * change in the state. We raise the semaphore by 2 (because we have 2 services
 * and we expect each of them will return either success or failure). The
 * listener will compare the overall (raised) count with number of signals for
 * each state and when the amount of signals for all states reaches raised count
 * we can continue with processing. It is also possible to evaluate whether
 * execution of both services was successful or not.
 * </p>
 * </p>
 * 
 * @author ladislav.gazo
 */
public class Semaphore {
	private List<SemaphoreListener> listeners;

	private byte count = 0;
	private byte[] states;

	/**
	 * Initializes the semaphore with specified number of states to monitor.
	 * 
	 * @param numberOfStates
	 */
	public Semaphore(int numberOfStates) {
		states = new byte[numberOfStates];
	}

	private List<SemaphoreListener> getListeners() {
		if (listeners == null) {
			listeners = new ArrayList<SemaphoreListener>();
		}
		return listeners;
	}

	public void addListener(SemaphoreListener listener) {
		getListeners().add(listener);
	}

	public void removeListener(SemaphoreListener listener) {
		getListeners().remove(listener);
	}

	private void fire() {
		List<SemaphoreListener> removeList = new ArrayList<SemaphoreListener>();
		for (SemaphoreListener listener : getListeners()) {
			SemaphoreEvent event = new SemaphoreEvent(count, states);
			listener.change(event);
			if (event.isRemoveListener()) {
				removeList.add(listener);
			}
		}

		for (SemaphoreListener listener : removeList) {
			removeListener(listener);
		}
		removeList.clear();
	}

	/**
	 * Raise expected count by the specified number.
	 * 
	 * @param count
	 */
	public void raise(byte count) {
		this.count += count;
	}

	public void raise(int count) {
		this.count += count;
	}

	/**
	 * Raise expected count by one.
	 */
	public void raiseOne() {
		count++;
	}

	/**
	 * Signal a state. Increases its value by one.
	 * 
	 * @param index
	 */
	public void signal(int stateIndex) {
		states[stateIndex]++;
		fire();
	}

	/**
	 * Clear the state of the semaphore. Zero the raised count and states.
	 */
	public void clear() {
		count = 0;
		for (int i = 0; i < states.length; i++) {
			states[i] = 0;
		}
	}

	public byte getCount() {
		return count;
	}

	public byte getState(int index) {
		return states[index];
	}
}
