/**
 * 
 */
package sk.seges.acris.binding.rebind.bean;

import sk.seges.acris.binding.client.wrappers.BeanWrapper;
import sk.seges.acris.binding.pap.model.BeanWrapperType;
import sk.seges.acris.binding.rebind.configuration.DefaultBindingNamingStrategy;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.validation.rebind.InterfaceTypeNameStrategy;

/**
 * @author eldzi
 */
public class BeanWrapperGenerator extends Generator {

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.core.ext.Generator#generate(com.google.gwt.core.ext.TreeLogger,
	 * com.google.gwt.core.ext.GeneratorContext, java.lang.String)
	 */
	@Override
	public String generate(TreeLogger logger, GeneratorContext context, String typeClass) throws UnableToCompleteException {
		DefaultBindingNamingStrategy typeStrategy = new DefaultBindingNamingStrategy(new InterfaceTypeNameStrategy(BeanWrapper.class,
				BeanWrapperType.BEAN_WRAPPER_SUFFIX));
		BeanWrapperCreator binder = new BeanWrapperCreator(typeStrategy);
		String className = binder.generate(logger, context, typeClass);
		return className;
	}
}