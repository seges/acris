package sk.seges.acris.player.client.event.decoding;

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

		if (result < 0) {
			result *= -1;
		}
		
		return result;
	}

}
