package sk.seges.acris.crypto;

import sk.seges.acris.crypto.util.Hasher;

/**
 * Standalone commandline starter of crypto functions.
 * 
 * NOTE: excluded from GWT build
 * 
 * @author ladislav.gazo
 */
public class CryptoStarter {
	public static void main(String[] args) {
		if(args == null || args.length != 1) {
			System.err.println("Provide text to hash");
			System.exit(42);
		}
		System.out.print(Hasher.getSHAHexDigest(args[0]));
	}
}
