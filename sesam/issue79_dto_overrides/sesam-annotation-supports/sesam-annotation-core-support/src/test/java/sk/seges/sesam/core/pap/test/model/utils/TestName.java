package sk.seges.sesam.core.pap.test.model.utils;

import javax.lang.model.element.Name;

public class TestName implements Name {

	private final String name;
	
	public TestName(String name) {
		this.name = name;
	}
	
	@Override
	public int length() {
		return name.length();
	}

	@Override
	public char charAt(int index) {
		return name.charAt(index);
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return name.subSequence(start, end);
	}

	@Override
	public boolean contentEquals(CharSequence cs) {
		return name.contentEquals(cs);
	}

	@Override
	public String toString() {
		return name;
	}
}