package sk.seges.sesam.core.pap.model;

import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.ParameterElement.ParameterUsageContext;
import sk.seges.sesam.core.pap.model.ParameterElement.ParameterUsageProvider;
import sk.seges.sesam.core.pap.model.mutable.api.MutableType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public class ConverterParameter {

	private boolean isPropagated = true;
	private String name;
	private final MutableTypeMirror type;
	private final MutableType usage; //rename to element later;
	private ConverterParameter sameParameter;
	private ParameterUsageProvider usageProvider;

	public ConverterParameter(MutableTypeMirror type, String name, MutableType usage, boolean isPropagated, MutableProcessingEnvironment processingEnv) {
		this.type = type;
		this.name = name;
		this.isPropagated = isPropagated;
		
		if (usage != null) {
			this.usage = usage;
		} else {
			this.usage = processingEnv.getTypeUtils().getReference(null, name);
		}
		
		this.usageProvider = null;
	}

	public ConverterParameter(MutableTypeMirror type, String name, ParameterUsageProvider usageProvider, boolean isPropagated) {
		this.type = type;
		this.name = name;
		this.isPropagated = isPropagated;
		this.usage = null;
		this.usageProvider = usageProvider;
	}

	public ConverterParameter(ParameterElement parameter, MutableProcessingEnvironment processingEnvironment) {
		this(parameter.type, parameter.name, parameter.usage, parameter.propagated, processingEnvironment);
		
		if (parameter.usage == null) {
			this.usageProvider = parameter.usageProvider;
		}
	}
	
	public ParameterElement toParameterElement() {
		if (usage != null) {
			return new ParameterElement(type, name, usage, isPropagated, null);
		}
		
		return new ParameterElement(type, name, usageProvider, isPropagated);
	}
	
	public String getName() {
		return name;
	}

	public ConverterParameter setName(String name) {
		this.name = name;
		return this;
	}
	
	public MutableType getUsage(ParameterUsageContext context) {
		if (usage != null) {
			return usage;
		}
		
		return usageProvider.getUsage(context);
	}
	
	public MutableTypeMirror getType() {
		return type;
	}

	public ConverterParameter getSameParameter() {
		return sameParameter;
	}

	public ConverterParameter setSameParameter(ConverterParameter sameParameter) {
		this.sameParameter = sameParameter;
		return this;
	}
	
	public boolean isPropagated() {
		return isPropagated;
	}
	
	public ConverterParameter setPropagated(boolean isPropagated) {
		this.isPropagated = isPropagated;
		return this;
	}
}