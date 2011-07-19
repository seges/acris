package sk.seges.sesam.pap.model;

import sk.seges.sesam.model.metadata.annotation.MetaModel;

@MetaModel
public class DomainObject {

	private Long id;
	private int field1;
	private String field2;
	private SecondDomainObject reference;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setField1(int field1) {
		this.field1 = field1;
	}
	
	public void setField2(String field2) {
		this.field2 = field2;
	}
	
	public void setReference(SecondDomainObject reference) {
		this.reference = reference;
	}
	
	public int getField1() {
		return field1;
	}
	
	public String getField2() {
		return field2;
	}
	
	public SecondDomainObject getReference() {
		return reference;
	}
}
