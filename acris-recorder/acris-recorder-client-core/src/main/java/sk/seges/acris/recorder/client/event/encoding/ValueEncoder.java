package sk.seges.acris.recorder.client.event.encoding;


public class ValueEncoder {

	public static long writeValueOnPosition(long value, int position, int length, long encodedEvent) {
        int andValue = 0;
        for (int i =0; i < length; i++) andValue += 1 << i;
        value &= andValue;
		return encodedEvent | (value << (position % 64));
	}
}
