package sk.seges.acris.pap.bean.model;

import java.util.List;

import sk.seges.acris.core.client.annotation.BeanWrapper;

@BeanWrapper
public class DummyBean {

	private int intField;
	private boolean booleanField;
	private String stringField;
	private Double doubleField;
	private ReferencedBean referencedField;
	private List<String> listField;
	private String[] arrayField;

	public int getIntField() {
		return intField;
	}

	public void setIntField(int intField) {
		this.intField = intField;
	}

	public boolean isBooleanField() {
		return booleanField;
	}

	public void setBooleanField(boolean booleanField) {
		this.booleanField = booleanField;
	}

	public String getStringField() {
		return stringField;
	}

	public void setStringField(String stringField) {
		this.stringField = stringField;
	}

	public Double getDoubleField() {
		return doubleField;
	}

	public void setDoubleField(Double doubleField) {
		this.doubleField = doubleField;
	}

	public ReferencedBean getReferencedField() {
		return referencedField;
	}

	public void setReferencedField(ReferencedBean referencedField) {
		this.referencedField = referencedField;
	}

	public List<String> getListField() {
		return listField;
	}

	public void setListField(List<String> listField) {
		this.listField = listField;
	}

	public String[] getArrayField() {
		return arrayField;
	}

	public void setArrayField(String[] arrayField) {
		this.arrayField = arrayField;
	}
}