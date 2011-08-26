/**
 * 
 */
package sk.seges.acris.core.client.component.semaphore;

import java.util.Arrays;

public class SemaphoreEvent {
	private byte count;
	private byte[] states;
	private boolean removeListener = false;
	
	public SemaphoreEvent(byte count, byte[] states) {
		super();
		this.count = count;
		this.states = states;
	}
	
	public byte getCount() {
		return count;
	}
	
	public byte[] getStates() {
		return states;
	}
	
	public void removeListener() {
		this.removeListener = true;
	}
	
	public boolean isRemoveListener() {
		return removeListener;
	}
	
	@Override
	public String toString() {
		return "SemaphoreEvent [count=" + count + ", states=" + Arrays.toString(states) + "]";
	}
}