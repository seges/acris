package sk.seges.sesam.pap.model.model;

import sk.seges.sesam.core.pap.model.api.NamedType;

public class ConverterParameter {

	private boolean isConverter;
	private String name;
	private NamedType type;
	private ConverterTypeElement converter;
	private ConverterParameter sameParameter;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public NamedType getType() {
		return type;
	}

	public void setType(NamedType typeMirror) {
		this.type = typeMirror;
	}

	public ConverterTypeElement getConverter() {
		return converter;
	}

	public void setConverter(ConverterTypeElement converter) {
		this.converter = converter;
	}

	public ConverterParameter getSameParameter() {
		return sameParameter;
	}

	public void setSameParameter(ConverterParameter sameParameter) {
		this.sameParameter = sameParameter;
	}
	
	public boolean isConverter() {
		return isConverter;
	}

	public void setConverter(boolean converter) {
		this.isConverter = converter;
	}
}