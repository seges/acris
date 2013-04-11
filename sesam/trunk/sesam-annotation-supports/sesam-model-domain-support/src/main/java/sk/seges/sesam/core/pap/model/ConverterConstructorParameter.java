package sk.seges.sesam.core.pap.model;

import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.ParameterElement.ParameterUsageContext;
import sk.seges.sesam.core.pap.model.ParameterElement.ParameterUsageProvider;
import sk.seges.sesam.core.pap.model.mutable.api.MutableType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public class ConverterConstructorParameter extends ConstructorParameter {

	private boolean isPropagated = true;
	
	private ConverterConstructorParameter sameParameter;
	
	private MutableType usage; //rename to element later;
	private ParameterUsageProvider usageProvider;

	public ConverterConstructorParameter(MutableTypeMirror type, String name, MutableType usage, boolean isPropagated, MutableProcessingEnvironment processingEnv) {
		super(type, name);
		this.isPropagated = isPropagated;
		
		if (usage != null) {
			this.usage = usage;
		} else {
			this.usage = processingEnv.getTypeUtils().getReference(null, name);
		}
		
		this.usageProvider = null;
	}

	public ConverterConstructorParameter(MutableTypeMirror type, String name, ParameterUsageProvider usageProvider, boolean isPropagated) {
		super(type, name);
		this.isPropagated = isPropagated;
		this.usage = null;
		this.usageProvider = usageProvider;
	}

	public ConverterConstructorParameter(ParameterElement parameter, MutableProcessingEnvironment processingEnvironment) {
		this(parameter.type, parameter.name, parameter.usage, parameter.propagated, processingEnvironment);
		
		if (parameter.usage == null) {
			this.usageProvider = parameter.usageProvider;
		}
	}

	public void merge(ParameterElement parameter) {
		if (parameter.usage != null) {
			this.usage = parameter.usage;
		} else {
			this.usageProvider = parameter.usageProvider;
		}
		
		setName(parameter.name);
	}
	
	public ParameterElement toParameterElement() {
		if (usage != null) {
			return new ParameterElement(getType(), getName(), usage, isPropagated, null);
		}
		
		return new ParameterElement(getType(), getName(), usageProvider, isPropagated);
	}
	
	public MutableType getUsage(ParameterUsageContext context) {
		if (usage != null) {
			return usage;
		}
		
		return usageProvider.getUsage(context);
	}
	
	public ConverterConstructorParameter getSameParameter() {
		return sameParameter;
	}

	public ConverterConstructorParameter setSameParameter(ConverterConstructorParameter sameParameter) {
		this.sameParameter = sameParameter;
		return this;
	}
	
	public boolean isPropagated() {
		return isPropagated;
	}
	
	public ConverterConstructorParameter setPropagated(boolean isPropagated) {
		this.isPropagated = isPropagated;
		return this;
	}
}