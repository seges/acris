package sk.seges.acris.security.shared.session;

import java.io.Serializable;

public class SessionArrayHolder implements Serializable, Comparable<String[]>{
	private static final long serialVersionUID = 1617109303404115704L;
	
	private String[] values;
	
	public SessionArrayHolder() {}
	
	public SessionArrayHolder(String[] values) {
		this.values = values;
	}
	
	public void setValues(String[] values) {
		this.values = values;
	}
	
	public String[] getValues() {
		return values;
	}
	
	@Override
	public int compareTo(String[] other) {
		if (values == null && other == null) {
			return 0;
		} else if ((values == null && other != null)) {
			return -1;
		} else if (values.length != other.length) {
			return -1;
		} else {
			for (int i = 0; i < values.length; i++) {
				if (values[i].compareTo(other[i]) != 0) {
					return values[i].compareTo(other[i]);
				}
			}
		}
		return 0;
	}
}
