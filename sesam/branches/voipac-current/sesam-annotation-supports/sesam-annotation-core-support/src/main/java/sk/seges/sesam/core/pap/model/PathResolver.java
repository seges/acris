package sk.seges.sesam.core.pap.model;

import java.util.Iterator;

public class PathResolver implements Iterator<String> {
	
	private String[] fields;
	private int index = 0;
	private String path;
	
	public PathResolver(String path) {
		this.path = path;
		fields = path.split("\\.");
		if (fields == null) {
			fields = new String[0];
		}
	}

	public boolean isNested() {
		return fields.length > 1;
	}

	@Override
	public boolean hasNext() {
		return index < fields.length;
	}

	@Override
	public String next() {
		return fields[index++];
	}

	@Override
	public void remove() {
		throw new RuntimeException("Remove operation is not allowed in the " + getClass().getSimpleName());
	}

	public void reset() {
		index = 0;
	}
	
	public String getPath() {
		return path;
	}
	
	public String getCurrent() {
		return fields[index - 1];
	}
}