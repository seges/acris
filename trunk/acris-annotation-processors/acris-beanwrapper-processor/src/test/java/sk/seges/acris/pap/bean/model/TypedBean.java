package sk.seges.acris.pap.bean.model;

import sk.seges.acris.core.client.annotation.BeanWrapper;

@BeanWrapper
public class TypedBean<T> implements Comparable<T> {

	private T t;

	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}

	@Override
	public int compareTo(T o) {
		return 0;
	}
}