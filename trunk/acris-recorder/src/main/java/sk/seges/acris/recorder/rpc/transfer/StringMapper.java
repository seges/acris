package sk.seges.acris.recorder.rpc.transfer;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

public class StringMapper extends LinkedList<String> implements Serializable {

	private static final long serialVersionUID = 3647407821823998887L;

	public StringMapper() {
		add(""); //for null strings
	}
	
	public void clear() {
		super.clear();
		add("");
	}
	
	public int getStringPosition(String string) {
		if (string == null) {
			return 0;
		}

		for (int index = 0; index < size(); index++) {
			if (string.equals(get(index))) {
				return index;
			}
		}

		return -1;
	}

	public boolean add(String e) {
		if (this.contains(e)) {
			return false;
		}
		return super.add(e);
	}

	public void addLast(String e) {
		if (this.contains(e)) {
			return;
		}
		super.addLast(e);
	}
	
	public void addFirst(String e) {
		if (this.contains(e)) {
			return;
		}
		super.addFirst(e);
	}
	
	public void add(int index, String element) {
		if (this.contains(element)) {
			return;
		}
		super.add(index, element);
	}

	public boolean addAll(Collection<? extends String> c) {
		boolean result = true;
		for (String string : c) {
			result |= add(string);
		}
		
		return result;
	}
	
	public boolean addAll(int index, Collection<? extends String> c) {
		boolean result = true;
		for (String string : c) {
			add(index, string);
		}
		
		return result;
	}
}