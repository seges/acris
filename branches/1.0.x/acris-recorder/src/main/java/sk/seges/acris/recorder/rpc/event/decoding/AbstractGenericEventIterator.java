package sk.seges.acris.recorder.rpc.event.decoding;

import java.util.Iterator;

import sk.seges.acris.recorder.rpc.event.generic.AbstractGenericEvent;
import sk.seges.acris.recorder.rpc.transfer.StringMapper;

public class AbstractGenericEventIterator implements Iterator<AbstractGenericEvent> {
	private final EventIterator eventIterator;
	private int[] deltaTimes;
	private final StringMapper stringMapper;

	private int deltaTimesIndex = 0;
	private int[] currentEvent;

	public AbstractGenericEventIterator(int[] event, int[] deltaTimes, String[] targets) {
		eventIterator = new EventIterator(event);
		stringMapper = new StringMapper();
		
		if (targets != null && targets.length > 0) {
			for (String target : targets) {
				stringMapper.add(target);
			}
		}
		
		this.deltaTimes = deltaTimes;
		
		if (deltaTimes.length == 0) {
			this.deltaTimes = null;
		}
	}
	
	public boolean hasNext() {
		return eventIterator.hasNext();
	}

	@Override
	public AbstractGenericEvent next() {
		if (!hasNext()) {
			return null;
		}
		
		currentEvent = eventIterator.next();
		AbstractGenericEvent decodedEvent = EventDecoder.decodeEvent(currentEvent, stringMapper);
		
		if (deltaTimes != null) {
			if (deltaTimesIndex >= deltaTimes.length) {
				throw new IllegalStateException("Inconsistent state of the delta times. Not a same length as event list.");
			}

			decodedEvent.setDeltaTime(deltaTimes[deltaTimesIndex++]);
		}
		
		return decodedEvent;
	}

	public int[] getDecodedEvent() {
		return currentEvent;
	}
	
	@Override
	public void remove() {
		throw new UnsupportedOperationException("Remove method not supported.");
	}
}
