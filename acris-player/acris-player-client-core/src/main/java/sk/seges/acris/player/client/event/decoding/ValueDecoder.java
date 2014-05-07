package sk.seges.acris.player.client.event.decoding;

import sk.seges.acris.recorder.client.event.fields.EMouseEventFields;

public class ValueDecoder {

	public static int readValueFromPosition(int position, long encodedEvent, int length) {
		long intPosition = position % 64;

		long tempPosition = intPosition + length;

		long clearValue = ((encodedEvent >> (intPosition + length)) << (intPosition + length));
		if (tempPosition >= 64) {
			clearValue = 0;
		}
		
		long decalculatedValue = encodedEvent - clearValue;
		int result = (int) (decalculatedValue >> intPosition);

		return result;
	}

}
