package sk.seges.acris.recorder.rpc.event.decoding;

public class ValueDecoder {
	private static final boolean LOG_ENABLED = false;

	public static int readValueFromPosition(int position, int[] encodedEvent, int length) {
		int resultPosition = position / 32;
		int intPosition = position % 32;

		int tempPosition = intPosition + length;

		if (LOG_ENABLED) {
			String debug = "Reading value from " + encodedEvent[resultPosition] + "(" + Integer.toBinaryString(encodedEvent[resultPosition]) + ")\n";
			debug += " on position " + intPosition + " with length: " + length + "\n";
			System.out.println(debug);
		}
		
		int clearValue = ((encodedEvent[resultPosition] >> (intPosition + length)) << (intPosition + length));
		if (tempPosition >= 32) {
			clearValue = 0;
		}
		
		int decalculatedValue = encodedEvent[resultPosition] - clearValue;
		int result = decalculatedValue >> intPosition;

		if (result < 0) {
			result *= -1;
		}
		
		if (LOG_ENABLED) {
			String debug = " Decalculated value: " + Integer.toBinaryString(decalculatedValue) + ", value representaton: " + decalculatedValue + "\n";
			debug += " Shifted value back to original position: " + Integer.toBinaryString(result) + "\n";
			debug += " Result: " + result + " (" + Integer.toBinaryString(result) + ")\n";
			System.out.println(debug);
		}
		
		return result;
	}

}
