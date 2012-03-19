package sk.seges.sesam.core.pap.model.mutable.utils;

import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeValue;

abstract class MutableValue implements MutableTypeValue {

	protected Object value;
	
	protected MutableValue(Object value) {
		this.value = value;
	}
	
	@Override
	public Object getValue() {
		return value;
	}	
}