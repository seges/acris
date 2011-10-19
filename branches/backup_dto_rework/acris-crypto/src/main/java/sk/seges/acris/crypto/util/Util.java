package sk.seges.acris.crypto.util;


public class Util {
	
	private static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
		'A', 'B', 'C', 'D', 'E', 'F' };
	
	public static String toHexString(byte[] b) {
		StringBuffer sb = new StringBuffer(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			// look up high nibble char
			sb.append(hexChar[(b[i] & 0xf0) >>> 4]); // fill left with zero
			// bits

			// look up low nibble char
			sb.append(hexChar[b[i] & 0x0f]);
		}
		return sb.toString();
	}
	
	public static byte[] toBinArray( String hexStr ){
	    byte bArray[] = new byte[hexStr.length()/2];  
	    for(int i=0; i<(hexStr.length()/2); i++){
	    	byte firstNibble  = Byte.parseByte(hexStr.substring(2*i,2*i+1),16); // [x,y)
	    	byte secondNibble = Byte.parseByte(hexStr.substring(2*i+1,2*i+2),16);
	    	int finalByte = (secondNibble) | (firstNibble << 4 ); // bit-operations only with numbers, not bytes.
	    	bArray[i] = (byte) finalByte;
	    }
	    return bArray;
	}
	
	public static void arraycopyBytes(byte[] src, int srcPos, byte[] dest,
			int destPos, int length) {
		checkJdkCompatabilityExceptions(src, srcPos, dest, destPos, length,
				src.length, dest.length);

		int destIdx = destPos;
		for (int s = srcPos; s < length; s++) {
			dest[destIdx++] = src[s];
		}

	}

	public static void arraycopyInt(int[] src, int srcPos, int[] dest,
			int destPos, int length) {
		int destIdx = destPos;
		for (int s = srcPos; s < length; s++) {
			dest[destIdx++] = src[s];
		}

	}

	private static void checkJdkCompatabilityExceptions(Object src, int srcPos,
			Object dest, int destPos, int length, int srcLen, int destLen) {
		if (src == null || dest == null) {
			// jdk compatability
			throw new NullPointerException("src and dest must not be null");
		}
		if (srcPos < 0 || destPos < 0 || length < 0) {
			// jdk compatability
			throw new IndexOutOfBoundsException(
					"srcPos, destPos and length must not be negative");
		}
		if (srcPos + length > srcLen) {
			throw new IndexOutOfBoundsException(
					"srcPos + length must not be greater than src.length");
		}
		if (destPos + length > destLen) {
			throw new IndexOutOfBoundsException(
					"destPos + length must not be greater than dest.length");
		}
	}
}