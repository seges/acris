package sk.seges.acris.recorder.client.event.encoding;


public class ValueEncoder {

	public static long writeValueOnPosition(long value, int position, long encodedEvent) {
		return encodedEvent | (value << (position % 64));
	}
}
