package sk.seges.acris.recorder.rpc.event.decoding;

import java.util.Iterator;

class EventIterator implements Iterator<int[]> {
	private int[] event;
	
	public EventIterator(int[] event) {
		this.event = event;
	}

	@Override
	public boolean hasNext() {
		if (event == null) {
			return false;
		}
		
		return event.length > 0;
	}

	@Override
	public int[] next() {
		if (!hasNext()) {
			return null;
		}
		
		int length = EventDecoder.getEventLength(event[0]);
		
		if (length > event.length) {
			throw new IllegalStateException("Invalid event list. Unable to process event");
		}
		
		int[] resultEvent = new int[length];
		
		for (int i = 0; i < length; i++) {
			resultEvent[i] = event[i];
		}
		
		int newLength = event.length - length;

		if (newLength == 0) {
			event = null;
		}
				
		int[] newEvent = new int[newLength];

		for (int i = 0; i < newLength; i++) {
			newEvent[i] = event[i + length];
		}
		
		event = newEvent;
		
		return resultEvent;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Remove method not supported.");
	}
}
