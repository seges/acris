package sk.seges.sesam.pap.model.model;

import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;

public class ConverterParameter {

//	private boolean isConverter = false;
	private boolean isPropagated = true;
	private String name;
	private MutableTypeMirror type;
//	private ConverterTypeElement converter;
	private ConverterParameter sameParameter;

	public ConverterParameter() {}
	
	public ConverterParameter(MutableTypeMirror type, String name, boolean isPropagated) {
		this.type = type;
		this.name = name;
		this.isPropagated = isPropagated;
	}
	
	public String getName() {
		return name;
	}

	public ConverterParameter setName(String name) {
		this.name = name;
		return this;
	}

	public MutableTypeMirror getType() {
		return type;
	}

	public ConverterParameter setType(MutableTypeMirror typeMirror) {
		this.type = typeMirror;
		return this;
	}

//	public ConverterTypeElement getConverter() {
//		return converter;
//	}
//
//	public ConverterParameter setConverter(ConverterTypeElement converter) {
//		this.converter = converter;
//		return this;
//	}

	public ConverterParameter getSameParameter() {
		return sameParameter;
	}

	public ConverterParameter setSameParameter(ConverterParameter sameParameter) {
		this.sameParameter = sameParameter;
		return this;
	}
	
//	public boolean isConverter() {
//		return isConverter;
//	}

//	public ConverterParameter setConverter(boolean converter) {
//		this.isConverter = converter;
//		return this;
//	}
	
	public void setPropagated(boolean isPropagated) {
		this.isPropagated = isPropagated;
	}
	
	public boolean isPropagated() {
		return isPropagated;
	}
}