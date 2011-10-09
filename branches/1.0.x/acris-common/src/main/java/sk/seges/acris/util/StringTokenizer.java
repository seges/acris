/**
 * 
 */
package sk.seges.acris.util;

import java.util.NoSuchElementException;

public class StringTokenizer {
	private final char[] delimiters;
	private final String string;
	private int currentPosition = 0;
	
	public StringTokenizer(String string, String delimiters) {
		this.delimiters = new char[delimiters.length()];
	
		for(int i = 0; i < delimiters.length(); i++) {
			this.delimiters[i] = delimiters.charAt(i);
		}
	
		this.string = string;
	}
	
	public boolean hasMoreTokens() {
		int index = delimiterIndex();
		return hasMoreTokens(index);
	}
	
	private boolean hasMoreTokens(int delimiterIndex) {
		return (delimiterIndex == -1 && isRestOfStringWithoutDelimiter()) || delimiterIndex > -1;
	}
	
	private boolean isRestOfStringWithoutDelimiter() {
		return currentPosition > 0 && string.length() - currentPosition - 1 > 0;
	}
	
	public String nextToken() {
		int index = delimiterIndex();
		if(!hasMoreTokens(index)) {
			throw new NoSuchElementException();
		}
		
		if(index == -1) {
			// we need the rest of the string
			index = string.length();
		}
		String result = string.substring(currentPosition, index);
		currentPosition += result.length()  + 1;
		return result;
	}
	
	private int delimiterIndex() {
		for(char delimiter : delimiters) {
			int index = string.indexOf(delimiter, currentPosition);
			if(index > -1) {
				return index;
			}
		}
		return -1;
	}
}