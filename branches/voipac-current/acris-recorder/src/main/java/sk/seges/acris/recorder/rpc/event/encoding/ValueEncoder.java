package sk.seges.acris.recorder.rpc.event.encoding;


public class ValueEncoder {
	private static final boolean LOG_ENABLED = false;
	
	public static int[] writeValueOnPosition(int value, int position, int[] encodedEvent) {
		int resultPosition = position / 32;
		
		String debug = "";
		
		if (LOG_ENABLED) {
			debug = "Writing value (" + value + ") on position (" + position + "). Previous encoded value = " + encodedEvent[0];
		}
		
		encodedEvent[resultPosition] = encodedEvent[resultPosition] + (value << (position % 32)); 

		if (LOG_ENABLED) {
			debug += ". New encoded value = " + encodedEvent[0] + " (" + Integer.toBinaryString(encodedEvent[0]) + ")";
			System.out.println(debug);
		}
		
		return encodedEvent;
	}
}
