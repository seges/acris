package sk.seges.acris.security.shared.session;

import java.io.Serializable;

public class PropertyHolder implements Serializable{

	private static final long serialVersionUID = 3632843192430935592L;
	
	private ValueType valueType;
	private Boolean booleanValue;
	private String stringValue;
	private SessionArrayHolder arrayValue;
	private Enum<?> enumValue;	

	protected  PropertyHolder() {};

	public PropertyHolder(Object value) {
		setValue(value);
	}
	
	public ValueType getValueType() {
		return valueType;
	}

	public void setValueType(ValueType valueType) {
		this.valueType = valueType;
	}

	public Boolean getBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(Boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public SessionArrayHolder getArrayValue() {
		return arrayValue;
	}

	public void setArrayValue(SessionArrayHolder arrayValue) {
		this.arrayValue = arrayValue;
	}

	public Enum<?> getEnumValue() {
		return enumValue;
	}

	public void setEnumValue(Enum<?> enumValue) {
		this.enumValue = enumValue;
	}

	public PropertyHolder(Object value, ValueType valueType) {
		this.valueType = valueType;
		valueType.setValue(this, value);
	}

	public void setValue(Object value) {
		this.valueType = ValueType.valueFor(value).setValue(this, value);
	}

	public Object getValue() {
		return valueType.getValue(this);
	}
}