/**
 * 
 */
package sk.seges.sesam.mock;

import java.util.LinkedList;
import java.util.List;

/**
 * @author eldzi
 * 
 */
public class Storage {
	private List<Integer> integers;

	public Storage(int size) {
		integers = new LinkedList<Integer>();

		for (int i = 0; i < size; i++) {
			integers.add(i);
		}
	}
	
	public Storage(boolean empty) {
		integers = new LinkedList<Integer>();
	}

	public List<Integer> getIntegers() {
		return integers;
	}
}
