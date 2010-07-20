/**
 * 
 */
package sk.seges.acris.binding.rebind.bean;

import sk.seges.acris.binding.client.wrappers.BeanWrapper;
import sk.seges.acris.binding.jsr269.BeanWrapperProcessor;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.validation.rebind.InterfaceTypeNameStrategy;
import com.google.gwt.validation.rebind.TypeStrategy;

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
		TypeStrategy typeStrategy = new InterfaceTypeNameStrategy(BeanWrapper.class, BeanWrapperProcessor.BEAN_WRAPPER_SUFFIX);
		BeanWrapperCreator binder = new BeanWrapperCreator(logger, context, typeClass, typeStrategy);
		String className = binder.createWrapper();
		return className;
	}
}
