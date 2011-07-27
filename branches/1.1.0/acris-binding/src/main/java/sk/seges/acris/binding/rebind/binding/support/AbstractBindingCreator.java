package sk.seges.acris.binding.rebind.binding.support;

import java.beans.IntrospectionException;
import java.lang.annotation.Annotation;

import sk.seges.acris.binding.client.annotations.BindingField;
import sk.seges.acris.binding.rebind.configuration.BindingNamingStrategy;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.rebind.SourceWriter;

abstract class AbstractBindingCreator<T extends Annotation> implements IBindingCreator<T> {

	/**
	 * GWT generator related instances
	 */
	protected GeneratorContext context;
	protected TypeOracle typeOracle;
	protected SourceWriter sourceWriter;
	protected TreeLogger logger;
	
	/**
	 * Binding generator related instances
	 */
	protected String packageName;
	protected JClassType parentBeanClassType;
	protected String bindingHolder;

	/**
	 * Naming strategy.
	 */
	protected BindingNamingStrategy namingStrategy;

	protected AbstractBindingCreator() {
	}
	
	public boolean isSupported(Class<? extends Annotation> clazz) {
		return clazz.getName().equals(getSupportedClass().getName());
	}
	
	abstract protected Class<? extends Annotation> getSupportedClass();

	protected String[] generateImports(JField field, BindingField bindingFieldAnnotation) throws IntrospectionException {
		return new String[0];
	}
	
	final public String[] getImports(JField field, BindingField bindingFieldAnnotation) throws UnableToCompleteException {
		try {
			return generateImports(field, bindingFieldAnnotation);
		} catch (IntrospectionException e) {
			logger.log(Type.ERROR, "Unable to generate imports, field = " + field + ", annotation = " + bindingFieldAnnotation, e);
			throw new UnableToCompleteException();
		}
	}

	void setGeneratorContext(GeneratorContext context, SourceWriter sourceWriter, TreeLogger logger) {
		this.context = context;
		typeOracle = context.getTypeOracle();
		this.sourceWriter = sourceWriter;
		this.logger = logger;
	}
	
	void setBindingContext(JClassType parentBeanClassType, String packageName, BindingNamingStrategy namingStrategy) {
		this.namingStrategy = namingStrategy;
		this.packageName = packageName;
		this.parentBeanClassType = parentBeanClassType;
	}
	
	void setBindingHolder(String bindingHolderField) {
		this.bindingHolder = bindingHolderField;
	}
	
	boolean isSupported(JField field) throws UnableToCompleteException {
		AbstractBindingCreator<? extends Annotation> bindingCreator = getBindingCreator(field);
		return (bindingCreator != null);
	}
	
	private AbstractBindingCreator<? extends Annotation> getBindingCreator(JField field) throws UnableToCompleteException {
		Class<? extends Annotation> bindingType = getFieldBindingType(field);
		if (this.isSupported(bindingType)) {
			return this;
		}
		
		return null;
	}

	/**
	 * Method is used to determine is mapping/binding is one to one
	 * e.g. One value to single value component - String <-> TextBox
	 * @return true if binding is one to one
	 * 		   false otherwise
	 * @throws UnableToCompleteException 
	 */
	private Class<? extends Annotation> getFieldBindingType(JField field) throws UnableToCompleteException {
		if (!((field.getType()) instanceof JClassType)) {
			logger.log(Type.ERROR, "Only class type bindings are allowed. Cannot provide binding to " + field.getType() + " type");
			throw new UnableToCompleteException();
		}
		
		JClassType classType = (JClassType)field.getType();
		
		BindingComponent bindingComponent = getBindingComponent(classType);
		
		if (bindingComponent == null) {
			logger.log(Type.ERROR, "Unable to find binding component for " + (classType == null ? "null" : classType.getQualifiedSourceName()) + ". Did you forgot to create an adapter provider?");
			throw new UnableToCompleteException();
		}
		
		return bindingComponent.bindingType;
	}

		
	private BindingComponent getBindingComponent(JClassType classType) throws UnableToCompleteException {
		
		BindingComponent bindingComponent = BindingCreatorFactory.getBindingComponent(classType);
		
		if (bindingComponent != null) {
			return bindingComponent;
		}
		
		classType = classType.getSuperclass();
		
		if (classType != null) {
			return getBindingComponent(classType);
		}
		
		return null;
	}
	
	protected String getWidgetBindingAdapterProperty(JField field) throws UnableToCompleteException {
		JType type = field.getType();

		if (!(type instanceof JClassType)) {
			logger.log(Type.ERROR, "Only class type bindings are allowed. Cannot provide binding to " + type + " type");
			throw new UnableToCompleteException();
		}

		JClassType classType = (JClassType)type;
		
		try {
			if (classType.isAssignableTo(typeOracle.getType(TextBoxBase.class.getName()))) {
				return "value";
			}
			if (classType.isAssignableTo(typeOracle.getType(DateBox.class.getName()))) {
				return "value";
			}
			//TODO
//			if (classType.isAssignableTo(typeOracle.getType(DateComboBox.class.getName()))) {
//				return "value";
//			}
			if (classType.isAssignableTo(typeOracle.getType(CheckBox.class.getName()))) {
				return "value";
			}

		} catch (NotFoundException e) {
			return "value";
		}
		return "value";
	}
}