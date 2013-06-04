package sk.seges.acris.binding.rebind.binding.support;

import java.beans.IntrospectionException;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.gwt.beansbinding.core.client.BeanProperty;
import org.gwt.beansbinding.core.client.BindingGroup;

import sk.seges.acris.binding.client.annotations.BindingField;
import sk.seges.acris.binding.client.annotations.BindingSpecLoader;
import sk.seges.acris.binding.client.providers.annotations.OneToMany;
import sk.seges.acris.binding.client.providers.support.BoundListAsyncCallback;
import sk.seges.acris.binding.client.providers.support.LoaderInitializationHandler;
import sk.seges.acris.binding.rebind.GeneratorException;
import sk.seges.acris.binding.rebind.bean.PropertyResolver;
import sk.seges.acris.binding.rebind.binding.BeanBindingCreator;
import sk.seges.acris.binding.rebind.loader.DefaultLoaderCreatorFactory;
import sk.seges.acris.binding.rebind.loader.FieldSpecLoaderCreator;
import sk.seges.acris.binding.rebind.loader.ILoaderCreator;
import sk.seges.acris.binding.rebind.loader.StaticListLoaderCreator;
import sk.seges.sesam.dao.ICallback;
import sk.seges.sesam.dao.PagedResult;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;

/**
 * One-To-Many binding creator using GWT BeansBinding's ListBoxBinding.
 * 
 * @author eldzi
 */
class OneToManyBeansBindingCreator extends AbstractBindingCreator<OneToMany> implements IBindingCreator<OneToMany> {

	@Override
	public Class<OneToMany> getSupportedClass() {
		return OneToMany.class;
	}

	@Override
	public String getPropertyValue(OneToMany annotation) {
		return annotation.value();
	}
	
	@Override
	protected String[] generateImports(JField field, BindingField bindingFieldAnnotation) throws IntrospectionException {

		PropertyResolver propertyResolver = new PropertyResolver(bindingFieldAnnotation.value());
		JClassType classType = propertyResolver.resolveBeanPropertyClassType(parentBeanClassType);
		
		ILoaderCreator loaderCreator = getLoaderCreator(field, null);

		String[] loaderImports = loaderCreator.getImports();
		
		return (String[])ArrayUtils.addAll(loaderImports, new String[] {
				classType.getQualifiedSourceName(),
				BeanProperty.class.getCanonicalName(),
				BindingGroup.class.getCanonicalName(),
				PagedResult.class.getCanonicalName(),
				List.class.getCanonicalName()
				
		});
	}
	
	@Override
	public boolean generateBinding(JField field, BindingField bindingFieldAnnotation)
			throws UnableToCompleteException {

		JClassType classType;
		PropertyResolver propertyResolver;

		JClassType beanReferenceType;
		
		String itemText = null;
		String itemValue = null;
		
		String targetBeanProperty;
		
		try {
			propertyResolver = new PropertyResolver(bindingFieldAnnotation.value());
			beanReferenceType = propertyResolver.resolveBeanReferencePropertyClassType(parentBeanClassType);
		} catch (IntrospectionException e1) {
			logger.log(Type.ERROR, "Unable to resolve bean reference type = " + parentBeanClassType, e1);
			throw new UnableToCompleteException();
		}
		if(beanReferenceType.isEnum() != null) {
			classType = beanReferenceType;
			targetBeanProperty = propertyResolver.getBeanProperty() + "." + propertyResolver.getBeanProperty();
		} else {
			try {
				classType = propertyResolver.resolveBeanPropertyClassType(parentBeanClassType);
			} catch (IntrospectionException e) {
				logger.log(Type.ERROR, "Unable to resolve = " + parentBeanClassType, e);
				throw new UnableToCompleteException();
			}
			
			targetBeanProperty = propertyResolver.getBeanProperty();
			itemText = "\"" + propertyResolver.getBeanPropertyReference() +"\"";
			itemValue = "\"" + propertyResolver.getBeanPropertyReference() +"\"";
		}

		String classTypeName = classType.getQualifiedSourceName();
		
		String loaderName;
		try {
			ILoaderCreator loaderCreator = getLoaderCreator(field, classType);
			loaderName = loaderCreator.generateLoader(sourceWriter, classType, propertyResolver.getBeanPropertyReference(), 
							field.getName(), null, "bg", field);
		} catch (Exception e) {
			logger.log(Type.ERROR, "Error generating loader", e);
			throw new UnableToCompleteException();
		}
		
		if(loaderName != null) {
			String callbackName = loaderName + "Callback";
			
			sourceWriter.println("final " + ICallback.class.getName() + "<PagedResult<List<" + classTypeName + ">>> " + callbackName + " = new " + BoundListAsyncCallback.class.getName() + "<" + classTypeName + ">(" + bindingHolder + ", \"" + targetBeanProperty + "\", "
					+ field.getName() + ", " + itemText + ", " + itemValue + ") {");
			sourceWriter.indent();
			sourceWriter.println("protected BeanWrapper<" + classTypeName + "> createWrapper() {");
			sourceWriter.indent();
			sourceWriter.println("return GWT.create(" + classTypeName + BeanBindingCreator.WRAPPER_SUFFIX + ".class);");
			sourceWriter.outdent();
			sourceWriter.println("}");
			sourceWriter.outdent();
			sourceWriter.println("};");
			sourceWriter.println(bindingHolder + ".addListener(new " + LoaderInitializationHandler.class.getName() + "<List<" + classTypeName + ">>(" + loaderName +", " + callbackName + ", " + field.getName() +"));");
		} else {
			logger.log(Type.WARN, "No loader name returned for field = " + field);
		}

		return true;
	}

	private ILoaderCreator getLoaderCreator(JField field, JClassType classType) throws IntrospectionException {
		BindingSpecLoader fieldSpecLoader = field.getAnnotation(BindingSpecLoader.class);
		
		if (fieldSpecLoader == null || fieldSpecLoader.value() == null) {
			if(classType != null && classType.isEnum() != null) {
				// handle enums automatically
				StaticListLoaderCreator creator = new StaticListLoaderCreator();
				creator.setStaticList(classType.getQualifiedSourceName() + ".values()");
				return creator;
			} else {
				try {
					return DefaultLoaderCreatorFactory.getLoaderCreator();
				} catch (GeneratorException e) {
					throw new IntrospectionException(e.getMessage());
				}
			}
		}

		FieldSpecLoaderCreator fieldSpecLoaderCreator = new FieldSpecLoaderCreator();
		fieldSpecLoaderCreator.setDataLoaderCreatorClass(fieldSpecLoader.value());
		
		return fieldSpecLoaderCreator;

	}
	
	@Override
	public boolean generateFields(JField field, BindingField bindingFieldAnnotation)
			throws UnableToCompleteException {

		String defaultValue = bindingFieldAnnotation.defaultValue();
		
		if (defaultValue.equals("null")) {
			defaultValue = null;
		}

		return false;
	}
}