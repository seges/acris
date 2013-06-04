package sk.seges.sesam.pap.model;

import sk.seges.sesam.model.metadata.annotation.MetaModel;


@MetaModel
public class SecondDomainObject {
	
	private Long id;
	private String field1;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getField1() {
		return field1;
	}
	
	public void setField1(String field1) {
		this.field1 = field1;
	}
}
